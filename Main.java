import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int BACKGROUND_WIDTH = 640;
    private static final int BACKGROUND_HEIGHT = 400;
    private static final int CONTROLBAR_HEIGHT = 40;
    private static final double GRAVITY = 500; // Gravity in pixels per second squared
    private static final double MAX_VELOCITY = 300; // Maximum velocity in pixels per second
    private static final double JUMP_STRENGTH = -300;
    private double velocity = 0;
    private ImageView flappyGhost;
    private boolean isPaused = true;
    private Canvas gameArea;
    private double backgroundX = 0;
    private Image bgImage;

    private long lastUpdateTime = 0;
    private static final double TIME_STEP = 1.0 / 60; // Target time step for 60 FPS

    @Override
    public void start(Stage primaryStage) {
        Canvas gameArea = setupGameArea();

        HBox controlBar = setupControlBar();

        // Arrange game area and control bar vertically
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(gameArea, controlBar);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, BACKGROUND_WIDTH, BACKGROUND_HEIGHT + CONTROLBAR_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Flappy Ghost");
        primaryStage.setResizable(false);
        primaryStage.show();

        setupAnimationTimer();
        setupKeyPress(scene);
        Platform.runLater(() -> {
            gameArea.requestFocus();
            drawGameArea(); // Initial draw call
        });
    }

    private Canvas setupGameArea() {
        // Create the game area
        Canvas gameArea = new Canvas(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        this.gameArea = gameArea;

        bgImage = new Image("file:fichiersFH/bg.png");

        // Create background image
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(BACKGROUND_WIDTH);
        bgImageView.setFitHeight(BACKGROUND_HEIGHT);

        // Create flappy ghost modeled by a circle
        Image ghostImage = new Image("file:fichiersFH/ghost.png");
        flappyGhost = new ImageView(ghostImage);
        double ghostImageWidth = ghostImage.getWidth();
        double ghostImageHeight = ghostImage.getHeight();
        flappyGhost.setFitWidth(ghostImageWidth); // Adjust to match your image size
        flappyGhost.setFitHeight(ghostImageHeight); // Adjust to match your image size
        flappyGhost.setX(Math.floor(BACKGROUND_WIDTH / 2) - Math.floor(ghostImageWidth / 2)); // Center horizontally
        flappyGhost.setY(Math.floor(BACKGROUND_HEIGHT / 2) - Math.floor(ghostImageHeight / 2)); // Center vertically

        // Make the game area occupy more space
        VBox.setVgrow(gameArea, Priority.ALWAYS);

        return gameArea;
    }

    private HBox setupControlBar() {
        // Create the control bar
        Button pauseButton = new Button("Jouer");
        pauseButton.setOnAction(event -> {
            isPaused = !isPaused;
            if (isPaused) {
                pauseButton.setText("Jouer");
            } else {
                pauseButton.setText("Pause");
            }
            // After the function, focus the canvas
            Platform.runLater(() -> {
                gameArea.requestFocus();
            });
        });

        CheckBox debugToggle = new CheckBox("Debut Mode");
        Label scoreLabel = new Label("Score: 0");

        Line line1 = new Line(0, 0, 0, Math.floor(CONTROLBAR_HEIGHT / 2));
        Line line2 = new Line(0, 0, 0, Math.floor(CONTROLBAR_HEIGHT / 2));
        line1.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
        line2.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");

        HBox controlBar = new HBox();
        controlBar.getChildren().addAll(pauseButton, line1, debugToggle, line2, scoreLabel);
        controlBar.setSpacing(10);
        controlBar.setPrefHeight(CONTROLBAR_HEIGHT);
        controlBar.setAlignment(Pos.CENTER);

        return controlBar;
    }

    private void setupAnimationTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    // Calculate elapsed time
                    double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
                    lastUpdateTime = now;

                    // Ensure we use the target time step
                    deltaTime = Math.min(deltaTime, TIME_STEP);

                    // Update velocity with gravity
                    velocity += GRAVITY * deltaTime;

                    // Clamp the velocity
                    if (velocity > MAX_VELOCITY) {
                        velocity = MAX_VELOCITY;
                    } else if (velocity < -MAX_VELOCITY) {
                        velocity = -MAX_VELOCITY;
                    }

                    // Update character's position
                    double newY = flappyGhost.getY() + velocity * deltaTime * 2; // Multiply by 60 to scale to target FPS

                    // Bounce off the top and bottom edges
                    if (newY < 0) {
                        newY = -newY; // Bounce from top
                        velocity = -velocity; // Invert velocity
                    } else if (newY > BACKGROUND_HEIGHT - flappyGhost.getFitHeight()) {
                        newY = 2 * (BACKGROUND_HEIGHT - flappyGhost.getFitHeight()) - newY; // Bounce from bottom
                        velocity = -velocity; // Invert velocity
                    }

                    flappyGhost.setY(newY);

                    // Update background scroll position
                    backgroundX -= 2 * deltaTime * 60; // Adjust speed as needed
                    if (backgroundX <= -BACKGROUND_WIDTH) {
                        backgroundX = 0;
                    }

                    // Draw the game area
                    drawGameArea();
                }
            }
        };
        timer.start();
    }

    private void drawGameArea() {
        GraphicsContext gc = gameArea.getGraphicsContext2D();

        // Clear the canvas
        gc.clearRect(0, 0, gameArea.getWidth(), gameArea.getHeight());

        // Draw the background image repeatedly
        gc.drawImage(bgImage, backgroundX, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        gc.drawImage(bgImage, backgroundX + BACKGROUND_WIDTH, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        gc.drawImage(flappyGhost.getImage(), flappyGhost.getX(), flappyGhost.getY(), flappyGhost.getFitWidth(), flappyGhost.getFitHeight());
    }

    private void setupKeyPress(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !isPaused) {
                if (flappyGhost.getY() >= 0 && flappyGhost.getY() <= BACKGROUND_HEIGHT - flappyGhost.getFitHeight()) {
                    velocity = JUMP_STRENGTH; // Apply jump strength
                }
            }
        });
        // When clicking elsewhere on the scene, focus returns to the canvas
        scene.setOnMouseClicked(event -> {
            gameArea.requestFocus();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
