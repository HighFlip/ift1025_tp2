public class CharacterModel {
    private static final double GRAVITY = 500; // Gravity in pixels per second squared
    private static final double MAX_VELOCITY = 300; // Maximum velocity in pixels per second
    private static final double JUMP_STRENGTH = -300;
    private static final double RADIUS = 30;
    
    private double x;
    private double y;
    private double velocity;

    public CharacterModel(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.velocity = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return RADIUS;
    }

    public void update(double deltaTime) {
        velocity += GRAVITY * deltaTime;

        if (velocity > MAX_VELOCITY) {
            velocity = MAX_VELOCITY;
        } else if (velocity < -MAX_VELOCITY) {
            velocity = -MAX_VELOCITY;
        }

        y += velocity * deltaTime;

        if (y < RADIUS) {
            y = RADIUS;
            velocity = -velocity;
        } else if (y > FlappyGhost.BACKGROUND_HEIGHT - RADIUS) {
            y = FlappyGhost.BACKGROUND_HEIGHT - RADIUS;
            velocity = -velocity;
        }
    }

    public void jump() {
        velocity = JUMP_STRENGTH;
    }
}
