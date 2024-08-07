public class SimpleObstacle extends ObstacleModel {
    public SimpleObstacle(double x, double y, double radius) {
        super(x, y, radius);
    }

    @Override
    public void update(double deltaTime, double scrollingSpeed) {
        // No movement
        setX(getX() - scrollingSpeed * deltaTime);
    }
}
