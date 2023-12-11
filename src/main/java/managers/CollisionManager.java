package managers;

import game.Game;
import ui.Sound;
import ui.UI;
import entities.Ball;
import entities.Block;
import entities.Bonus;
import entities.Breaker;
import enums.BounceDirection;
import javafx.application.Platform;
import javafx.scene.image.Image;

/**
 * The CollisionManager class is responsible for managing the collisions in the game.
 * It handles the collision detection between the ball and the walls, the ball and the blocks, the ball and the bonuses, and the ball and the paddle.
 */
public class CollisionManager {
    private final Game game;
    private final BlockManager manager;
    private final Ball ball;
    private final Breaker breaker;
    private final UI ui;
    private final Sound sound;
    private BonusManager bonuses;

    /**
     * Constructs a new CollisionManager object.
     *
     * @param game         the game instance.
     * @param ball         the ball instance.
     * @param breaker      the breaker instance.
     * @param blockManager the block manager instance.
     * @param ui           the UI instance.
     * @param sound        the sound instance.
     */
    public CollisionManager(Game game, Ball ball, Breaker breaker, BlockManager blockManager, UI ui, Sound sound) {
        this.game = game;
        this.ball = ball;
        this.breaker = breaker;
        this.manager = blockManager;
        this.ui = ui;
        this.sound = sound;
    }

    /**
     * Checks if the ball collides with the top wall.
     *
     * @return true if the ball collides with the top wall, false otherwise.
     */
    private boolean BallCollideTopWall() {
        return ball.yBall <= Ball.BALL_RADIUS;
    }

    /**
     * Checks if the ball collides with the bottom wall.
     *
     * @return true if the ball collides with the bottom wall, false otherwise.
     */
    private boolean BallCollideBottomWall() {
        return ball.yBall >= UI.SCENE_HEIGHT - Ball.BALL_RADIUS;
    }

    /**
     * Checks if the ball collides with the left wall.
     *
     * @return true if the ball collides with the left wall, false otherwise.
     */
    private boolean BallCollideLeftWall() {
        return ball.xBall <= Ball.BALL_RADIUS;
    }

    /**
     * Checks if the ball collides with the right wall.
     *
     * @return true if the ball collides with the right wall, false otherwise.
     */
    private boolean BallCollideRightWall() {
        return ball.xBall >= UI.SCENE_WIDTH - Ball.BALL_RADIUS;
    }

    /**
     * Checks if the ball collides with a bonus drop.
     *
     * @param choco the bonus to check for collision.
     * @return true if the ball collides with the bonus drop, false otherwise.
     */
    private boolean BallCollideBonusDrop(Bonus choco) {
        return choco.y >= breaker.yBreak && choco.y <= breaker.yBreak + breaker.BREAK_HEIGHT && choco.x >= breaker.xBreak && choco.x <= breaker.xBreak + breaker.BREAK_WIDTH;
    }

    /**
     * Checks if the ball collides with a block.
     *
     * @return true if the ball collides with a block, false otherwise.
     */
    private boolean BallCollideBlock() {
        return ball.yBall >= Block.getPaddingTop() - Ball.BALL_RADIUS && ball.yBall <= (Block.getHeight() * (game.level + 1)) + Block.getPaddingTop() - Ball.BALL_RADIUS;
    }

    /**
     * Checks if the ball is in the breaker zone.
     *
     * @return true if the ball is in the breaker zone, false otherwise.
     */
    private boolean BallInBreakerZone() {
        return ball.yBall >= breaker.yBreak - Ball.BALL_RADIUS;
    }

    /**
     * Checks if the ball collides in the right zone of the breaker.
     *
     * @param rightZone the right zone of the breaker.
     * @return true if the ball collides in the right zone of the breaker, false otherwise.
     */
    private boolean BreakerRightZone(double rightZone) {
        return ball.xBall >= rightZone && ball.xBall <= breaker.xBreak + breaker.BREAK_WIDTH;
    }

    /**
     * Checks if the ball collides in the middle zone of the breaker.
     *
     * @param leftZone  the left zone of the breaker.
     * @param rightZone the right zone of the breaker.
     * @return true if the ball collides in the middle zone of the breaker, false otherwise.
     */
    private boolean BreakerMiddleZone(double leftZone, double rightZone) {
        return ball.xBall > leftZone && ball.xBall < rightZone;
    }

    /**
     * Checks if the ball collides in the left zone of the breaker.
     *
     * @param leftZone the left zone of the breaker.
     * @return true if the ball collides in the left zone of the breaker, false otherwise.
     */
    private boolean BreakerLeftZone(double leftZone) {
        return ball.xBall >= breaker.xBreak && ball.xBall <= leftZone;
    }

    /**
     * Sets the bonus manager.
     *
     * @param bonuses the bonus manager instance.
     */
    public void setBonuses(BonusManager bonuses) {
        this.bonuses = bonuses;
    }

    /**
     * Checks for collisions between the ball and the breaker, and the ball and the walls.
     */
    public void checkCollisions() {
        checkBreakerCollision();
        checkWallCollision();
    }

    /**
     * Checks for collisions between the ball and the blocks.
     * If a collision is detected, it handles the collision based on the block type and the hit direction.
     */
    public void checkBlockCollisions() {
        if (BallCollideBlock()) {
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

    /**
     * Checks for collisions between the ball and the bonuses.
     * If a collision is detected, it handles the collision.
     *
     * @param choco the bonus to check for collision.
     */
    public void checkBonusCollisions(Bonus choco) {
        if (BallCollideBonusDrop(choco)) {
            sound.playSound("Bonus Hit.mp3");
            System.out.println("Bonus Caught");
            bonuses.caught(choco);
        }
    }

    /**
     * Checks for collisions between the ball and the breaker.
     * If a collision is detected, it handles the collision based on the breaker zone.
     */
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

    /**
     * Checks for collisions between the ball and the walls.
     * If a collision is detected, it handles the collision based on which wall.
     */
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
        if (BallCollideRightWall()) {
            ball.ballBounce(BounceDirection.LEFT);
        }

        if (BallCollideLeftWall()) {
            ball.ballBounce(BounceDirection.RIGHT);
        }
    }
}