/**
 * Modèle pour un obstacle simple dans le jeu FlappyGhost.
 */
public class SimpleObstacle extends ObstacleModel {

    /**
     * Constructeur pour initialiser un obstacle simple avec ses coordonnées et son
     * rayon.
     * 
     * @param x      La coordonnée X de l'obstacle.
     * @param y      La coordonnée Y de l'obstacle.
     * @param radius Le rayon de l'obstacle.
     */
    public SimpleObstacle(double x, double y, double radius) {
        super(x, y, radius);
    }

    /**
     * Met à jour la position de l'obstacle simple en fonction du temps écoulé et de
     * la vitesse de défilement.
     * 
     * @param deltaTime      Le temps écoulé depuis la dernière mise à jour, en
     *                       secondes.
     * @param scrollingSpeed La vitesse de défilement de l'écran, en pixels par
     *                       seconde.
     */
    @Override
    public void update(double deltaTime, double scrollingSpeed) {
        // Met à jour la position x de l'obstacle pour simuler le défilement horizontal.
        setX(getX() - scrollingSpeed * deltaTime);
    }
}
