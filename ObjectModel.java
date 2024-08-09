/**
 * Modèle de base pour les objets dans le jeu.
 */
public abstract class ObjectModel {
    protected double x;
    protected double y;

    /**
     * Constructeur pour initialiser les coordonnées de l'objet.
     * 
     * @param x La coordonnée X de l'objet.
     * @param y La coordonnée Y de l'objet.
     */
    public ObjectModel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return La coordonnée X de l'objet.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Définit une nouvelle coordonnée X pour l'objet.
     * 
     * @param newX La nouvelle coordonnée X à définir.
     */
    public void setX(double newX) {
        this.x = newX;
    }

    /**
     * @return La coordonnée Y de l'objet.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Définit une nouvelle coordonnée Y pour l'objet.
     * 
     * @param newY La nouvelle coordonnée Y à définir.
     */
    public void setY(double newY) {
        this.y = newY;
    }
}
