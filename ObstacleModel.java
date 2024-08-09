import java.util.Random;

/**
 * Modèle abstrait pour les obstacles dans le jeu FlappyGhost.
 */
public abstract class ObstacleModel extends ObjectModel {
    private double radius; // Rayon de l'obstacle
    private int obstacleIndex; // Index de l'image associée à l'obstacle
    private boolean passed; // Indique si l'obstacle a été dépassé par le personnage
    private boolean isHit; // Indique si l'obstacle a été touché

    /**
     * Constructeur pour initialiser un obstacle avec ses coordonnées et son rayon.
     * 
     * @param x      La coordonnée X de l'obstacle.
     * @param y      La coordonnée Y de l'obstacle.
     * @param radius Le rayon de l'obstacle.
     */
    public ObstacleModel(double x, double y, double radius) {
        super(x, y);
        this.radius = radius;
        this.obstacleIndex = new Random().nextInt(27); // Sélectionne un index d'image aléatoire parmi 27 possibles
        this.passed = false;
        this.isHit = false;
    }

    /**
     * @return Le rayon de l'obstacle.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Définit un nouveau rayon pour l'obstacle.
     * 
     * @param radius Le nouveau rayon à définir.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return L'index de l'image associée à l'obstacle.
     */
    public int getObstacleIndex() {
        return obstacleIndex;
    }

    /**
     * @return Vrai si l'obstacle a été dépassé par le personnage, faux sinon.
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * Marque l'obstacle comme étant dépassé.
     * 
     * @param passed Vrai si l'obstacle est dépassé, faux sinon.
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * @return Vrai si l'obstacle a été touché, faux sinon.
     */
    public boolean isHit() {
        return isHit;
    }

    /**
     * Marque l'obstacle comme étant touché.
     * 
     * @param hit Vrai si l'obstacle est touché, faux sinon.
     */
    public void setHit(boolean hit) {
        isHit = hit;
    }

    /**
     * Méthode abstraite pour mettre à jour l'état de l'obstacle.
     * 
     * @param deltaTime      Temps écoulé depuis la dernière mise à jour.
     * @param scrollingSpeed Vitesse de défilement de l'écran.
     */
    public abstract void update(double deltaTime, double scrollingSpeed);
}
