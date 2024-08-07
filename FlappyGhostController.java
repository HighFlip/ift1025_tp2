import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyGhostController {
    private CharacterModel character;
    private FlappyGhostView view;
    private Timeline timeline;
    private double backgroundX = 0;
    private double scrollingSpeed = 120;
    private boolean isPaused = true;
    private List<ObstacleModel> obstacles = new ArrayList<>();
    private double timeSinceLastObstacle = 0;
    private int obstaclesPassed = 0;
    private boolean collisionPause = false;
    private int score = 0;
    private Label scoreLabel;
    private Random random = new Random();

    public FlappyGhostController(CharacterModel character, FlappyGhostView view, Timeline timeline) {
        this.character = character;
        this.view = view;
        this.timeline = timeline;
    }

    public void drawGameArea() {
        view.drawBackground(backgroundX);
        view.drawFlappyGhost(character);
        view.drawObstacles(obstacles);
    }

    public void handlePauseButton(Button pauseButton, Canvas gameArea) {
        pauseButton.setOnAction(event -> {
            isPaused = !isPaused;
            if (isPaused) {
                pauseButton.setText("Jouer");
                timeline.pause();
            } else {
                pauseButton.setText("Pause");
                timeline.play();
            }
            Platform.runLater(() -> {
                gameArea.requestFocus();
            });
        });
    }

    public void handleDebugToggle(CheckBox debugToggle, Canvas gameArea) {
        debugToggle.setOnAction(event -> {
            view.setDebugMode(debugToggle.isSelected());
            drawGameArea();
            Platform.runLater(() -> {
                gameArea.requestFocus();
            });
        });
    }

    public void handleScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public void handleKeyPress(Scene scene, Canvas gameArea) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !isPaused) {
                character.jump();
            }
        });
    }

    public void handleMouseClick(Scene scene, Canvas canvas) {
        scene.setOnMouseClicked(event -> canvas.requestFocus());
    }

    public void initializeGame(Canvas gameArea) {
        Platform.runLater(() -> {
            gameArea.requestFocus();
            drawGameArea(); // Initial draw call
        });
    }

    public void update(double deltaTime) {
        if (!isPaused && !collisionPause) {
            character.update(deltaTime);

            backgroundX -= scrollingSpeed * deltaTime;
            if (backgroundX <= -FlappyGhost.BACKGROUND_WIDTH) {
                backgroundX = 0;
            }

            // Update obstacles
            for (ObstacleModel obstacle : obstacles) {
                obstacle.update(deltaTime, scrollingSpeed);
            }

            checkCollisionsAndUpdateScore();

            drawGameArea();

            // Add new obstacles periodically
            timeSinceLastObstacle += deltaTime;
            if (timeSinceLastObstacle >= 3) {
                addNewObstacle();
                timeSinceLastObstacle = 0;
            }
        }
    }

    private void addNewObstacle() {
        double x = FlappyGhost.BACKGROUND_WIDTH + 30;
        double y = random.nextDouble() * (FlappyGhost.BACKGROUND_HEIGHT - 45) + 45;
        double radius = random.nextDouble() * 35 + 10;

        int obstacleType = random.nextInt(3);
        ObstacleModel newObstacle;
        switch (obstacleType) {
            case 0:
                newObstacle = new SimpleObstacle(x, y, radius);
                break;
            case 1:
                newObstacle = new SinusoidalObstacle(x, y, radius, 50, 0.005);
                break;
            case 2:
                newObstacle = new QuantumObstacle(x, y, radius);
                break;
            default:
                newObstacle = new SimpleObstacle(x, y, radius);
                break;
        }

        obstacles.add(newObstacle);
    }

    private void checkCollisionsAndUpdateScore() {
        for (ObstacleModel obstacle : obstacles) {
            double dx = character.getX() - obstacle.getX();
            double dy = character.getY() - obstacle.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < character.getRadius() + obstacle.getRadius()) {
                score = 0;
                scoreLabel.setText("Score: " + score);
                obstacle.setHit(true);
                collisionPause = true;
                drawGameArea();

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    collisionPause = false; // Reset the collision pause state
                    resetGame();
                });
                pause.play();

                return;
            }
        }

        List<ObstacleModel> passedObstacles = new ArrayList<>();
        for (ObstacleModel obstacle : obstacles) {
            if (obstacle.getX() + obstacle.getRadius() < 0) {
                passedObstacles.add(obstacle);
            } else if (!obstacle.isPassed() && character.getX() > obstacle.getX() + obstacle.getRadius()) {
                score += 5;
                scoreLabel.setText("Score: " + score);
                obstaclesPassed++;
                obstacle.setPassed(true);

                // Increase speed and gravity every two obstacles
                if (obstaclesPassed % 2 == 0) {
                    scrollingSpeed += 15;
                    character.setGravity(character.getGravity() + 15);
                }
            }
        }

        obstacles.removeAll(passedObstacles);
    }

    private void resetGame() {
        character.reset();
        scrollingSpeed = 120;
        obstacles.clear();
    }
}
