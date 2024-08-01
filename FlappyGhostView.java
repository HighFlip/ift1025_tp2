import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class FlappyGhostView {
    private Image flappyGhostImage;
    private Image bgImage;
    private GraphicsContext gc;
    private boolean debugMode = false;

    public FlappyGhostView(GraphicsContext gc) {
        this.gc = gc;
        this.flappyGhostImage = new Image("file:fichiersFH/ghost.png");
        this.bgImage = new Image("file:fichiersFH/bg.png");
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void drawBackground(double backgroundX) {
        gc.clearRect(0, 0, FlappyGhost.BACKGROUND_WIDTH, FlappyGhost.BACKGROUND_HEIGHT);
        gc.drawImage(bgImage, backgroundX, 0, FlappyGhost.BACKGROUND_WIDTH, FlappyGhost.BACKGROUND_HEIGHT);
        gc.drawImage(bgImage, backgroundX + FlappyGhost.BACKGROUND_WIDTH, 0, FlappyGhost.BACKGROUND_WIDTH, FlappyGhost.BACKGROUND_HEIGHT);
    }

    public void drawFlappyGhost(CharacterModel model) {
        if (debugMode) {
            gc.setFill(Color.YELLOW);
            gc.fillOval(model.getX() - model.getRadius(), model.getY() - model.getRadius(), model.getRadius() * 2, model.getRadius() * 2);
        } else {
            double flappyGhostX = model.getX() - flappyGhostImage.getWidth() / 2;
            double flappyGhostY = model.getY() - flappyGhostImage.getHeight() / 2;
            gc.drawImage(flappyGhostImage, flappyGhostX, flappyGhostY);
        }
    }
}
