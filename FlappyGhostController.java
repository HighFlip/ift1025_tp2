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

/**
 * Contrôleur pour gérer la logique du jeu Flappy Ghost.
 */
public class FlappyGhostController {
    private CharacterModel character;
    private FlappyGhostView view;
    private Timeline timeline;
    private double backgroundX = 0;
    private double scrollingSpeed = 120;
    private boolean isPaused = true;
    private boolean isDebugMode = false;
    private List<ObstacleModel> obstacles = new ArrayList<>();
    private double timeSinceLastObstacle = 0;
    private int obstaclesPassed = 0;
    private boolean collisionPause = false;
    private int score = 0;
    private Label scoreLabel;
    private Random random = new Random();

    /**
     * Crée un contrôleur pour gérer la logique du jeu.
     * 
     * @param character Le modèle du personnage du jeu.
     * @param view      La vue du jeu.
     * @param timeline  La timeline pour l'animation du jeu.
     */
    public FlappyGhostController(CharacterModel character, FlappyGhostView view, Timeline timeline) {
        this.character = character;
        this.view = view;
        this.timeline = timeline;
    }

    /**
     * Dessine la zone de jeu.
     */
    public void drawGameArea() {
        view.drawBackground(backgroundX);
        view.drawFlappyGhost(character);
        view.drawObstacles(obstacles);
    }

    /**
     * Gère le bouton de pause.
     * 
     * @param pauseButton Le bouton de pause.
     * @param gameArea    La zone de jeu.
     */
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

    /**
     * Gère le toggle du mode debug.
     * 
     * @param debugToggle La case à cocher pour le mode debug.
     * @param gameArea    La zone de jeu.
     */
    public void handleDebugToggle(CheckBox debugToggle, Canvas gameArea) {
        debugToggle.setOnAction(event -> {
            boolean debugModeState = debugToggle.isSelected();
            isDebugMode = debugModeState;
            view.setDebugMode(debugModeState);
            drawGameArea();
            Platform.runLater(() -> {
                gameArea.requestFocus();
            });
        });
    }

    /**
     * Associe le label de score.
     * 
     * @param scoreLabel Le label pour afficher le score.
     */
    public void handleScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    /**
     * Gère les pressions de touches.
     * 
     * @param scene    La scène du jeu.
     * @param gameArea La zone de jeu.
     */
    public void handleKeyPress(Scene scene, Canvas gameArea) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !isPaused) {
                character.jump();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit(); // Quitter l'application
            }
        });
    }

    /**
     * Gère les clics de souris.
     * 
     * @param scene  La scène du jeu.
     * @param canvas La zone de jeu.
     */
    public void handleMouseClick(Scene scene, Canvas canvas) {
        scene.setOnMouseClicked(event -> canvas.requestFocus());
    }

    /**
     * Initialise le jeu.
     * 
     * @param gameArea La zone de jeu.
     */
    public void initializeGame(Canvas gameArea) {
        Platform.runLater(() -> {
            gameArea.requestFocus();
            drawGameArea(); // Premier dessin
        });
    }

    /**
     * Met à jour l'état du jeu.
     * 
     * @param deltaTime Le temps écoulé depuis la dernière mise à jour.
     */
    public void update(double deltaTime) {
        if (!isPaused && !collisionPause) {
            character.update(deltaTime);

            // Défilement de l'arrière-plan
            backgroundX -= scrollingSpeed * deltaTime;
            if (backgroundX <= -FlappyGhost.BACKGROUND_WIDTH) {
                backgroundX = 0;
            }

            // Mise à jour des obstacles
            for (ObstacleModel obstacle : obstacles) {
                obstacle.update(deltaTime, scrollingSpeed);
            }

            checkCollisionsAndUpdateScore();

            drawGameArea();

            // Ajoute de nouveaux obstacles périodiquement
            timeSinceLastObstacle += deltaTime;
            if (timeSinceLastObstacle >= 3) {
                addNewObstacle();
                timeSinceLastObstacle = 0;
            }
        }
    }

    /**
     * Ajoute un nouvel obstacle au jeu.
     */
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

    /**
     * Vérifie les collisions et met à jour le score.
     */
    private void checkCollisionsAndUpdateScore() {
        List<ObstacleModel> passedObstacles = new ArrayList<>();

        for (ObstacleModel obstacle : obstacles) {
            double dx = character.getX() - obstacle.getX();
            double dy = character.getY() - obstacle.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Detection de collision
            if (distance < character.getRadius() + obstacle.getRadius()) {
                obstacle.setHit(true);

                if (!isDebugMode) {
                    collisionPause = true;
                    drawGameArea();
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(event -> {
                        collisionPause = false; // Réinitialise l'état de pause après la collision
                        resetGame();
                    });
                    pause.play();

                    return; // Retourner après collision
                }
            } else {
                if (obstacle.isHit()) { // Termine d'être en collision
                    obstacle.setHit(false);
                }
            }

            // Check if the obstacle is passed
            if (obstacle.getX() + obstacle.getRadius() < 0) {
                passedObstacles.add(obstacle);
            } else if (!obstacle.isPassed() && character.getX() > obstacle.getX() + 2 * obstacle.getRadius()) {
                score += 5;
                scoreLabel.setText("Score: " + score);
                obstaclesPassed++;
                obstacle.setPassed(true);

                // Augmente la vitesse et la gravité tous les deux obstacles
                if (obstaclesPassed % 2 == 0) {
                    scrollingSpeed += 15;
                    character.setGravity(character.getGravity() + 15);
                }
            }
        }

        // Enlever les obstacles sortant de l'écran
        obstacles.removeAll(passedObstacles);
    }

    /**
     * Réinitialise le jeu après une collision.
     */
    private void resetGame() {
        character.reset();
        scrollingSpeed = 120;
        obstacles.clear();
        score = 0;
        scoreLabel.setText("Score: " + score);
    }
}
