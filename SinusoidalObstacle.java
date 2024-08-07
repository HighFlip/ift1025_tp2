public class SinusoidalObstacle extends ObstacleModel {
    private double initialY;
    private double amplitude;
    private double frequency;

    public SinusoidalObstacle(double x, double y, double radius, double amplitude, double frequency) {
        super(x, y, radius);
        this.initialY = y;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public void update(double deltaTime, double scrollingSpeed) {
        setX(getX() - scrollingSpeed * deltaTime);
        double newY = initialY + amplitude * Math.sin(System.currentTimeMillis() * frequency);
        setY(newY);
    }
}
