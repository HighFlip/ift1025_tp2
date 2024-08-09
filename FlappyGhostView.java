import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Vue pour gérer l'affichage du jeu Flappy Ghost.
 */
public class FlappyGhostView {
    private Image bgImage;
    private Image flappyGhostImage;
    private List<Image> obstacleImages;
    private GraphicsContext gc;
    private boolean debugMode = false;

    /**
     * Crée une vue pour gérer l'affichage du jeu.
     * 
     * @param gc Le contexte graphique pour dessiner sur le canevas.
     */
    public FlappyGhostView(GraphicsContext gc) {
        this.gc = gc;
        this.bgImage = new Image("file:fichiersFH/bg.png");
        this.flappyGhostImage = new Image("file:fichiersFH/ghost.png");
        this.obstacleImages = loadObstacleImages();
    }

    /**
     * Charge les images des obstacles.
     * 
     * @return La liste des images des obstacles.
     */
    private List<Image> loadObstacleImages() {
        List<Image> images = new ArrayList<>();
        for (int i = 0; i <= 26; i++) {
            String imagePath = "file:fichiersFH/obstacles/" + i + ".png";
            images.add(new Image(imagePath));
        }
        return images;
    }

    /**
     * Active ou désactive le mode debug.
     * 
     * @param debugMode True pour activer le mode debug, false pour le désactiver.
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Dessine l'arrière-plan du jeu.
     * 
     * @param backgroundX La position en X de l'arrière-plan.
     */
    public void drawBackground(double backgroundX) {
        gc.clearRect(0, 0, FlappyGhost.BACKGROUND_WIDTH, FlappyGhost.BACKGROUND_HEIGHT);
        gc.drawImage(bgImage, backgroundX, 0, FlappyGhost.BACKGROUND_WIDTH, FlappyGhost.BACKGROUND_HEIGHT);
        gc.drawImage(bgImage, backgroundX + FlappyGhost.BACKGROUND_WIDTH, 0, FlappyGhost.BACKGROUND_WIDTH,
                FlappyGhost.BACKGROUND_HEIGHT);
    }

    /**
     * Dessine le personnage principal (Flappy Ghost).
     * 
     * @param characterModel Le modèle du personnage à dessiner.
     */
    public void drawFlappyGhost(CharacterModel characterModel) {
        if (debugMode) {
            gc.setFill(Color.BLACK);
            gc.fillOval(characterModel.getX() - characterModel.getRadius(),
                    characterModel.getY() - characterModel.getRadius(), characterModel.getRadius() * 2,
                    characterModel.getRadius() * 2);
        } else {
            double flappyGhostX = characterModel.getX() - flappyGhostImage.getWidth() / 2;
            double flappyGhostY = characterModel.getY() - flappyGhostImage.getHeight() / 2;
            gc.drawImage(flappyGhostImage, flappyGhostX, flappyGhostY);
        }
    }

    /**
     * Dessine les obstacles du jeu.
     * 
     * @param obstacles La liste des obstacles à dessiner.
     */
    public void drawObstacles(List<ObstacleModel> obstacles) {
        for (ObstacleModel obstacle : obstacles) {
            double x = obstacle.getX();
            double y = obstacle.getY();
            double radius = obstacle.getRadius();

            if (debugMode) {
                if (obstacle.isHit()) {
                    gc.setFill(Color.RED); // Couleur de l'obstacle en cas de collision
                } else {
                    gc.setFill(Color.YELLOW); // Couleur de l'obstacle en mode debug
                }
                gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            } else {
                gc.drawImage(obstacleImages.get(obstacle.getObstacleIndex()), x - radius, y - radius, radius * 2,
                        radius * 2);
            }
        }
    }

    /**
     * Définit l'icône de la fenêtre.
     * 
     * @param primaryStage La fenêtre principale du jeu.
     */
    public void setWindowIcon(Stage primaryStage) {
        primaryStage.getIcons().add(flappyGhostImage);
    }
}
