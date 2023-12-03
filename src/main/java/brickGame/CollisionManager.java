package brickGame;

import javafx.application.Platform;

public class CollisionManager {
    private BounceDirection bounce;
    private Game game;
    private GameEngine engine;
    private BlockManager manager;
    private Bonus bonus;
    private BonusManager bonuses;
    private Ball ball;
    private Score score;
    private Breaker breaker;
    private boolean BallCollideTopWall() {
        return ball.yBall <= Ball.BALL_RADIUS;
    }

    private boolean BallCollideBottomWall() {
        return ball.yBall >= Game.SCENE_HEIGHT - Ball.BALL_RADIUS;
    }

    private boolean BallCollideWithBreaker() {
        return ball.yBall >= breaker.yBreak - Ball.BALL_RADIUS;
    }

    private boolean BreakerRightZone(double rightZone) {
        return ball.xBall >= rightZone && ball.xBall <= breaker.xBreak + breaker.BREAK_WIDTH;
    }
    private boolean BreakerMiddleZone(double leftZone, double rightZone) { return ball.xBall > leftZone && ball.xBall < rightZone; }
    private boolean BreakerLeftZone(double leftZone) {
        return ball.xBall >= breaker.xBreak && ball.xBall <= leftZone;
    }

    public CollisionManager(Game game, Ball ball, Breaker breaker, BlockManager blockManager, Bonus bonus) {
        this.game = game;
        this.ball = ball;
        this.breaker = breaker;
        this.manager = blockManager;
        this.bonus = bonus;
    }

    public void setBonuses(BonusManager bonuses) {
        this.bonuses = bonuses;
    }

    public void checkCollisions() {
        checkBreakerCollision();
        checkWallCollision();
    }

    public void checkBlockCollisions() {
        if (ball.yBall >= Block.getPaddingTop() - Ball.BALL_RADIUS && ball.yBall <= (Block.getHeight() * (game.level + 1)) + Block.getPaddingTop() - Ball.BALL_RADIUS) {
            synchronized (manager.getBlocks()) {
                for (Block block : manager.getBlocks()) {
                    int hitCode = block.checkHitToBlock(ball.xBall, ball.yBall, Ball.BALL_RADIUS);
                    if (hitCode != Block.HitDirection.NO_HIT.ordinal()) {
                        game.score += 1;

                        Platform.runLater(() -> {
                            new Score().show(block.x, block.y, 1, game);
                            block.rect.setVisible(false);
                        });

                        block.isDestroyed = true;
                        manager.destroyedBlockCount++;
//                        ball.resetCollideFlags();

                        if (block.type == Block.Type.CHOCO.ordinal()) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = game.time;
                            Platform.runLater(() -> game.root.getChildren().add(choco.choco));
                            bonuses.chocos.add(choco);
                        }
                        if (block.type == Block.Type.STAR.ordinal()) {
                            bonuses.goldTime = game.time;
                            ball.setBallImagePattern("goldball.png");
                            System.out.println("gold ball");
                            Platform.runLater(() -> game.root.getStyleClass().add("goldRoot"));
                            bonuses.isGoldStatus = true;
                        }
                        if (block.type == Block.Type.HEART.ordinal()) {
                            game.heart++;
                        }

                        if (hitCode == Block.HitDirection.RIGHT.ordinal()) {
                            ball.ballBounce(BounceDirection.RIGHT);
                        } else if (hitCode == Block.HitDirection.BOTTOM.ordinal()) {
                            ball.ballBounce(BounceDirection.DOWN);
                        } else if (hitCode == Block.HitDirection.LEFT.ordinal()) {
                            ball.ballBounce(BounceDirection.LEFT);
                        } else if (hitCode == Block.HitDirection.TOP.ordinal()) {
                            ball.ballBounce(BounceDirection.UP);
                        }
                    }
                }
            }
        }
    }

    public void checkBonusCollisions(Bonus choco) {
        if (choco.y >= breaker.yBreak && choco.y <= breaker.yBreak + breaker.BREAK_HEIGHT && choco.x >= breaker.xBreak && choco.x <= breaker.xBreak + breaker.BREAK_WIDTH) {
            System.out.println("Bonus Caught");
            bonuses.caught(choco);
        }
    }



    private void checkBreakerCollision() {
        if (BallCollideWithBreaker()) {
            double zoneWidth = breaker.BREAK_WIDTH / 3.0;
            double leftZone = breaker.xBreak + zoneWidth;
            double rightZone = breaker.xBreak + 2 * zoneWidth;

            if (BreakerLeftZone(leftZone)) {
                ball.resetCollideFlags();
                ball.ballBounce(BounceDirection.UP);
                ball.ballBounce(BounceDirection.LEFT);
            } else if (BreakerMiddleZone(leftZone, rightZone)) {
                ball.resetCollideFlags();
                ball.ballBounce(BounceDirection.STRAIGHT_UP);
            } else if (BreakerRightZone(rightZone)) {
                ball.resetCollideFlags();
                ball.ballBounce(BounceDirection.UP);
                ball.ballBounce(BounceDirection.RIGHT);
            }
        }
    }

    private void checkWallCollision() {
        if (BallCollideTopWall()) {
//            ball.resetCollideFlags();
            ball.ballBounce(BounceDirection.DOWN);
            return;
        }
        if (BallCollideBottomWall()) {
//            ball.resetCollideFlags();
            ball.ballBounce(BounceDirection.UP);
            if (!bonuses.isGoldStatus) {
                //TODO game over
                game.heart--;
                new Score().show((double) Game.SCENE_WIDTH / 2, (double) Game.SCENE_HEIGHT / 2, -1, game);

                if (game.heart == 0) {
                    new Score().showGameOver(game);
                    engine.stop();
                }
            }
        }
        if (ball.xBall >= Game.SCENE_WIDTH - Ball.BALL_RADIUS) { // right wall
//            ball.resetCollideFlags();
            ball.ballBounce(BounceDirection.LEFT);
        }

        if (ball.xBall <= Ball.BALL_RADIUS) { // left wall
//            ball.resetCollideFlags();
            ball.ballBounce(BounceDirection.RIGHT);
        }
    }
}