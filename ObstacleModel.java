import java.util.Random;

public abstract class ObstacleModel extends ObjectModel {
    private double radius;
    private int obstacleIndex;
    private boolean passed;
    private boolean isHit;

    public ObstacleModel(double x, double y, double radius) {
        super(x, y);
        this.radius = radius;
        this.obstacleIndex = new Random().nextInt(27);
        this.passed = false;
        this.isHit = false;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getObstacleIndex() {
        return obstacleIndex;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public abstract void update(double deltaTime, double scrollingSpeed);
}
