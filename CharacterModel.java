public class CharacterModel extends ObjectModel {
    private static final double MAX_VELOCITY = 300; // Maximum velocity in pixels per second
    private static final double JUMP_STRENGTH = -300;
    private static final double RADIUS = 30;

    private double gravity; // Gravity in pixels per second squared
    private double velocity;

    public CharacterModel(double startX, double startY) {
        super(startX, startY);
        this.velocity = 0;
        this.gravity = 500;
    }

    public double getRadius() {
        return RADIUS;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void update(double deltaTime) {
        velocity += gravity * deltaTime;

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

    public void reset() {
        velocity = 0;
        gravity = 500;
        setY(Math.floor(FlappyGhost.BACKGROUND_HEIGHT / 2));
    }
}
