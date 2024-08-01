import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

public class FlappyGhost extends Application {
    public static final int BACKGROUND_WIDTH = 640;
    public static final int BACKGROUND_HEIGHT = 400;
    public static final int CONTROLBAR_HEIGHT = 40;
    private static final double DELTA_TIME = 1.0 / 60;
    private boolean isPaused = true;

    private Image bgImage;
    private Canvas gameArea;
    private CharacterModel character;
    private FlappyGhostView view;
    private FlappyGhostController controller;
    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) {
        gameArea = setupGameArea();
        character = new CharacterModel(BACKGROUND_WIDTH / 2, BACKGROUND_HEIGHT / 2);
        view = new FlappyGhostView(gameArea.getGraphicsContext2D());
        timeline = setupTimeline();
        controller = new FlappyGhostController(character, view, timeline);

        HBox controlBar = setupControlBar();

        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(gameArea, controlBar);

        Scene scene = new Scene(mainLayout, BACKGROUND_WIDTH, BACKGROUND_HEIGHT + CONTROLBAR_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Flappy Ghost");
        primaryStage.setResizable(false);
        primaryStage.show();

        controller.handlePauseButton((Button) controlBar.getChildren().get(0), gameArea); // Pass pause button
        controller.handleDebugToggle((CheckBox) controlBar.getChildren().get(2), gameArea); // Pass debug checkbox
        controller.handleKeyPress(scene, gameArea);
        controller.handleMouseClick(scene, gameArea);
        controller.initializeGame(gameArea);
    }

    private Canvas setupGameArea() {
        Canvas gameArea = new Canvas(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        this.gameArea = gameArea;
        VBox.setVgrow(gameArea, Priority.ALWAYS);
        return gameArea;
    }

    private HBox setupControlBar() {
        Button pauseButton = new Button("Jouer");
        CheckBox debugToggle = new CheckBox("Debug Mode");
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

    private Timeline setupTimeline() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(DELTA_TIME), e -> updateGame()));
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    private void updateGame() {
        controller.update(DELTA_TIME, BACKGROUND_WIDTH);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
