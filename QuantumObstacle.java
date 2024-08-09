import java.util.Random;

public class QuantumObstacle extends ObstacleModel {
    private Random random;
    private double teleportInterval;
    private double timeSinceLastTeleport;

    /**
     * Constructeur pour initialiser un obstacle quantique avec ses coordonnées et
     * son rayon.
     * L'obstacle se téléporte aléatoirement à des intervalles de temps fixes.
     * 
     * @param x      Coordonnée x de l'obstacle.
     * @param y      Coordonnée y de l'obstacle.
     * @param radius Rayon de l'obstacle.
     */
    public QuantumObstacle(double x, double y, double radius) {
        super(x, y, radius);
        this.random = new Random();
        this.teleportInterval = 0.2; // Intervalle de téléportation en secondes.
        this.timeSinceLastTeleport = 0;
    }

    /**
     * Met à jour la position de l'obstacle quantique en fonction du temps écoulé et
     * de la vitesse de défilement.
     * L'obstacle se déplace horizontalement et se téléporte à des intervalles
     * réguliers.
     * 
     * @param deltaTime      Temps écoulé depuis la dernière mise à jour.
     * @param scrollingSpeed Vitesse de défilement de l'écran.
     */
    @Override
    public void update(double deltaTime, double scrollingSpeed) {
        // Met à jour la position x de l'obstacle pour simuler le défilement horizontal.
        setX(getX() - scrollingSpeed * deltaTime);

        // Met à jour le temps écoulé depuis la dernière téléportation.
        timeSinceLastTeleport += deltaTime;
        // Téléporte l'obstacle à une nouvelle position aléatoire si l'intervalle de
        // téléportation est écoulé.
        if (timeSinceLastTeleport >= teleportInterval) {
            double newX = getX() + (random.nextDouble() * 60 - 30);
            double newY = getY() + (random.nextDouble() * 60 - 30);
            setX(newX);
            setY(newY);
            timeSinceLastTeleport = 0;
        }
    }
}
