/**
 * Modèle pour le personnage du jeu FlappyGhost.
 */
public class CharacterModel extends ObjectModel {
    private static final double MAX_VELOCITY = 300; // Vitesse maximale en pixels par seconde
    private static final double JUMP_STRENGTH = -300; // Force du saut en pixels par seconde
    private static final double RADIUS = 30; // Rayon du personnage

    private double gravity; // Gravité en pixels par seconde carrée
    private double velocity; // Vitesse actuelle du personnage

    /**
     * Constructeur pour initialiser le personnage avec ses coordonnées de départ.
     * 
     * @param startX La coordonnée X de départ du personnage.
     * @param startY La coordonnée Y de départ du personnage.
     */
    public CharacterModel(double startX, double startY) {
        super(startX, startY);
        this.velocity = 0;
        this.gravity = 500;
    }

    /**
     * @return Le rayon du personnage.
     */
    public double getRadius() {
        return RADIUS;
    }

    /**
     * @return La gravité appliquée au personnage.
     */
    public double getGravity() {
        return gravity;
    }

    /**
     * Définit une nouvelle valeur pour la gravité.
     * 
     * @param gravity La nouvelle valeur de la gravité à définir.
     */
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    /**
     * Met à jour la position et la vitesse du personnage en fonction du temps
     * écoulé.
     * 
     * @param deltaTime Le temps écoulé depuis la dernière mise à jour, en secondes.
     */
    public void update(double deltaTime) {
        velocity += gravity * deltaTime;

        if (velocity > MAX_VELOCITY) {
            velocity = MAX_VELOCITY;
        } else if (velocity < -MAX_VELOCITY) {
            velocity = -MAX_VELOCITY;
        }

        y += velocity * deltaTime;

        // Empêche le personnage de dépasser les limites verticales
        if (y < RADIUS) {
            y = RADIUS;
            velocity = -velocity;
        } else if (y > FlappyGhost.BACKGROUND_HEIGHT - RADIUS) {
            y = FlappyGhost.BACKGROUND_HEIGHT - RADIUS;
            velocity = -velocity;
        }
    }

    /**
     * Applique un saut au personnage.
     */
    public void jump() {
        velocity = JUMP_STRENGTH;
    }

    /**
     * Réinitialise la position et les attributs du personnage.
     */
    public void reset() {
        velocity = 0;
        gravity = 500;
        setY(Math.floor(FlappyGhost.BACKGROUND_HEIGHT / 2));
    }
}
