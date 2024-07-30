import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int BACKGROUND_WIDTH = 640;
    private static final int BACKGROUND_HEIGHT = 400;
    private static final int CONTROLBAR_HEIGHT = 40;
    private static final double GRAVITY = 0.5;
    private static final double JUMP_STRENGTH = -10;
    private double velocity = 0;
    private ImageView flappyGhost;

    @Override
    public void start(Stage primaryStage) {
        Pane gameArea = setupGameArea();

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
    }

    private Pane setupGameArea() {
        // Create the game area
        Pane gameArea = new Pane();

        // Create background image
        Image bgImage = new Image("file:fichiersFH/bg.png");
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
        flappyGhost.setX(BACKGROUND_WIDTH - Math.floor(ghostImageWidth / 2)); // Center horizontally
        flappyGhost.setY(BACKGROUND_HEIGHT - Math.floor(ghostImageHeight / 2)); // Center vertically

        gameArea.getChildren().addAll(bgImageView, flappyGhost);
        // Make the game area occupy more space
        VBox.setVgrow(gameArea, Priority.ALWAYS);

        return gameArea;
    }

    private HBox setupControlBar() {
        // Create the control bar
        Button pauseButton = new Button("Pause");
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
                // Update velocity with gravity
                velocity += GRAVITY;

                // Update character's position
                flappyGhost.setY(flappyGhost.getY() + velocity);

                // Prevent character from falling out of the game area
                if (flappyGhost.getY() > BACKGROUND_HEIGHT - flappyGhost.getFitHeight()) {
                    flappyGhost.setY(BACKGROUND_HEIGHT - flappyGhost.getFitHeight());
                    velocity = 0;
                }

                // Prevent character from moving above the top of the game area
                if (flappyGhost.getY() < 0) {
                    flappyGhost.setY(0);
                    velocity = 0;
                }
            }
        };
        timer.start();
    }

    private void setupKeyPress(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                velocity = JUMP_STRENGTH;
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
