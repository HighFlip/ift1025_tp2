public class SinusoidalObstacle extends ObstacleModel {
    private double initialY;
    private double amplitude;
    private double frequency;

    /**
     * Constructeur pour initialiser un obstacle sinusoïdal avec ses coordonnées,
     * son rayon, son amplitude et sa fréquence.
     * 
     * @param x         Coordonnée x de l'obstacle.
     * @param y         Coordonnée y initiale de l'obstacle.
     * @param radius    Rayon de l'obstacle.
     * @param amplitude Amplitude de l'oscillation sinusoïdale.
     * @param frequency Fréquence de l'oscillation sinusoïdale.
     */
    public SinusoidalObstacle(double x, double y, double radius, double amplitude, double frequency) {
        super(x, y, radius);
        this.initialY = y;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    /**
     * Met à jour la position de l'obstacle sinusoïdal en fonction du temps écoulé
     * et de la vitesse de défilement.
     * L'obstacle se déplace horizontalement et son mouvement vertical suit une
     * fonction sinusoïdale.
     * 
     * @param deltaTime      Temps écoulé depuis la dernière mise à jour.
     * @param scrollingSpeed Vitesse de défilement de l'écran.
     */
    @Override
    public void update(double deltaTime, double scrollingSpeed) {
        setX(getX() - scrollingSpeed * deltaTime);
        // Met à jour la position y de l'obstacle selon une fonction sinusoïdale.
        double newY = initialY + amplitude * Math.sin(System.currentTimeMillis() * frequency);
        setY(newY);
    }
}
