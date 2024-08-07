import java.util.Random;

public class QuantumObstacle extends ObstacleModel {
    private Random random;
    private double teleportInterval;
    private double timeSinceLastTeleport;

    public QuantumObstacle(double x, double y, double radius) {
        super(x, y, radius);
        this.random = new Random();
        this.teleportInterval = 0.2; // 0.2 seconds
        this.timeSinceLastTeleport = 0;
    }

    @Override
    public void update(double deltaTime, double scrollingSpeed) {
        setX(getX() - scrollingSpeed * deltaTime);

        timeSinceLastTeleport += deltaTime;
        if (timeSinceLastTeleport >= teleportInterval) {
            double newX = getX() + (random.nextDouble() * 60 - 30);
            double newY = getY() + (random.nextDouble() * 60 - 30);
            setX(newX);
            setY(newY);
            timeSinceLastTeleport = 0;
        }
    }
}
