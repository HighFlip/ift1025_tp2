import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class FlappyGhostView {
    private Image bgImage;
    private Image flappyGhostImage;
    private List<Image> obstacleImages;
    private GraphicsContext gc;
    private boolean debugMode = false;

    public FlappyGhostView(GraphicsContext gc) {
        this.gc = gc;
        this.bgImage = new Image("file:fichiersFH/bg.png");
        this.flappyGhostImage = new Image("file:fichiersFH/ghost.png");
        this.obstacleImages = loadObstacleImages();
    }

    private List<Image> loadObstacleImages() {
        List<Image> images = new ArrayList<>();
        for (int i = 0; i <= 26; i++) {
            String imagePath = "file:fichiersFH/obstacles/" + i + ".png";
            images.add(new Image(imagePath));
        }
        return images;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void drawBackground(double backgroundX) {
        gc.clearRect(0, 0, FlappyGhost.BACKGROUND_WIDTH, FlappyGhost.BACKGROUND_HEIGHT);
        gc.drawImage(bgImage, backgroundX, 0, FlappyGhost.BACKGROUND_WIDTH, FlappyGhost.BACKGROUND_HEIGHT);
        gc.drawImage(bgImage, backgroundX + FlappyGhost.BACKGROUND_WIDTH, 0, FlappyGhost.BACKGROUND_WIDTH,
                FlappyGhost.BACKGROUND_HEIGHT);
    }

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

    public void drawObstacles(List<ObstacleModel> obstacles) {
        for (ObstacleModel obstacle : obstacles) {
            double x = obstacle.getX();
            double y = obstacle.getY();
            double radius = obstacle.getRadius();

            if (debugMode) {
                if (obstacle.isHit()) {
                    gc.setFill(Color.RED);
                } else {
                    gc.setFill(Color.YELLOW);
                }
                gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            } else {
                gc.drawImage(obstacleImages.get(obstacle.getObstacleIndex()), x - radius, y - radius, radius * 2,
                        radius * 2);
            }
        }
    }
}
