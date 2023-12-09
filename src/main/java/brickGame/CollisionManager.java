package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;

public class CollisionManager {
    private final Game game;
    private final BlockManager manager;
    private final Ball ball;
    private final Breaker breaker;
    private final UI ui;
    private final Sound sound;
    private BonusManager bonuses;

    public CollisionManager(Game game, Ball ball, Breaker breaker, BlockManager blockManager, UI ui, Sound sound) {
        this.game = game;
        this.ball = ball;
        this.breaker = breaker;
        this.manager = blockManager;
        this.ui = ui;
        this.sound = sound;
    }

    private boolean BallCollideTopWall() {
        return ball.yBall <= Ball.BALL_RADIUS;
    }

    private boolean BallCollideBottomWall() {
        return ball.yBall >= UI.SCENE_HEIGHT - Ball.BALL_RADIUS;
    }

    private boolean BallInBreakerZone() {
        return ball.yBall >= breaker.yBreak - Ball.BALL_RADIUS;
    }

    private boolean BreakerRightZone(double rightZone) {
        return ball.xBall >= rightZone && ball.xBall <= breaker.xBreak + breaker.BREAK_WIDTH;
    }

    private boolean BreakerMiddleZone(double leftZone, double rightZone) {
        return ball.xBall > leftZone && ball.xBall < rightZone;
    }

    private boolean BreakerLeftZone(double leftZone) {
        return ball.xBall >= breaker.xBreak && ball.xBall <= leftZone;
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

                        if (block.type == Block.Type.NORMAL.ordinal()) {
                            sound.playSound("BrickHit.mp3");
                            Platform.runLater(() -> {
                                ui.show(block.x, block.y, "+1");
                                block.rect.setVisible(false);
                            });
                        }

                        block.isDestroyed = true;
                        manager.destroyedBlockCount++;

                        if (block.type == Block.Type.CHOCO.ordinal()) {
                            sound.playSound("BrickHit.mp3");
                            Platform.runLater(() -> {
                                ui.show(block.x, block.y, "Bonus!");
                                block.rect.setVisible(false);
                            });
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = game.time;
                            Platform.runLater(() -> ui.root.getChildren().add(choco.choco));
                            bonuses.chocos.add(choco);
                        }
                        if (block.type == Block.Type.STAR.ordinal()) {
                            sound.playSound("Gold Ball.mp3");
                            Platform.runLater(() -> {
                                ui.show(block.x, block.y, "Gold Ball!");
                                block.rect.setVisible(false);
                            });
                            bonuses.goldTime = game.time;
                            ball.setBallImagePattern("goldball.png");
                            System.out.println("gold ball");
                            Platform.runLater(() -> ui.root.getStyleClass().add("goldRoot"));
                            bonuses.isGoldStatus = true;
                        }
                        if (block.type == Block.Type.HEART.ordinal()) {
                            game.heart++;
                            sound.playSound("Heart++.mp3");
                            Image image = new Image("heartplus.png");
                            Platform.runLater(() -> {
                                ui.showImg((double) UI.SCENE_WIDTH / 2, (double) UI.SCENE_HEIGHT / 2, image);
                                block.rect.setVisible(false);
                            });
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
            sound.playSound("Bonus Hit.mp3");
            System.out.println("Bonus Caught");
            bonuses.caught(choco);
        }
    }

    private void checkBreakerCollision() {
        if (BallInBreakerZone()) {
            double zoneWidth = breaker.BREAK_WIDTH / 3.0;
            double leftZone = breaker.xBreak + zoneWidth;
            double rightZone = breaker.xBreak + 2 * zoneWidth;

            if (BreakerLeftZone(leftZone)) {
                sound.playSound("Breaker Hit.mp3");
                ball.resetCollideFlags();
                ball.ballBounce(BounceDirection.UP);
                ball.ballBounce(BounceDirection.LEFT);
            } else if (BreakerMiddleZone(leftZone, rightZone)) {
                sound.playSound("Breaker Hit.mp3");
                ball.resetCollideFlags();
                ball.ballBounce(BounceDirection.STRAIGHT_UP);
            } else if (BreakerRightZone(rightZone)) {
                sound.playSound("Breaker Hit.mp3");
                ball.resetCollideFlags();
                ball.ballBounce(BounceDirection.UP);
                ball.ballBounce(BounceDirection.RIGHT);
            }
        }
    }

    private void checkWallCollision() {
        if (BallCollideTopWall()) {
            ball.ballBounce(BounceDirection.DOWN);
            return;
        }
        if (BallCollideBottomWall()) {
            ball.ballBounce(BounceDirection.UP);
            if (!bonuses.isGoldStatus) {
                sound.playSound("Heart--.mp3");
                game.heart--;
                Image image = new Image("heartstrike.png");
                ui.showImg((double) UI.SCENE_WIDTH / 2, (double) UI.SCENE_HEIGHT / 2, image);

                if (game.heart == 0) {
                    sound.musicOff();
                    sound.playSound("Game Over.mp3");
                    ui.restartMenu(game, "Game Over");
                    game.stopengine();
                }
            }
        }
        if (ball.xBall >= UI.SCENE_WIDTH - Ball.BALL_RADIUS) { // right wall
            ball.ballBounce(BounceDirection.LEFT);
        }

        if (ball.xBall <= Ball.BALL_RADIUS) { // left wall
            ball.ballBounce(BounceDirection.RIGHT);
        }
    }
}