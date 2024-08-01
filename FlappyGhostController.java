import javafx.application.Platform;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;

public class FlappyGhostController {
    private CharacterModel character;
    private FlappyGhostView view;
    private Timeline timeline;
    private double backgroundX = 0;
    private boolean isPaused = true;

    public FlappyGhostController(CharacterModel character, FlappyGhostView view, Timeline timeline) {
        this.character = character;
        this.view = view;
        this.timeline = timeline;
    }

    public void drawGameArea() {
        view.drawBackground(backgroundX);
        view.drawFlappyGhost(character);
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
            Platform.runLater(() -> {
                gameArea.requestFocus();
            });
        });
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

    public void update(double deltaTime, int bgWidth) {
        if (!isPaused) {
            character.update(deltaTime);
            backgroundX -= 2 * deltaTime * 60;
            if (backgroundX <= -bgWidth) {
                backgroundX = 0;
            }
            drawGameArea();
        }
    }
}
