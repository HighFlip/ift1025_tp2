import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Main extends Application {
    private int BACKGROUND_WIDTH = 640;
    private int BACKGROUND_HEIGHT = 400;
    private int CONTROLBAR_HEIGHT = 40;

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
    }

    private Pane setupGameArea() {
        // Create the game area with a background image
        Pane gameArea = new Pane();

        Image bgImage = new Image("file:fichiersFH\\bg.png"); // Make sure bg.png is in the correct path
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(BACKGROUND_WIDTH); // Set the width to match your window
        bgImageView.setFitHeight(BACKGROUND_HEIGHT); // Set the height to match your window
        gameArea.getChildren().add(bgImageView);

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

    public static void main(String[] args) {
        launch(args);
    }
}
