/*
 * Classe Personnage
 * Represente un personnage et admet les fonctions de deplacements dans le labyrinthe.
 * 
 * @author Alex Marcotte (MARA10068704)
 * @see Laby
 * @see Labyrinthe
 */
public class Personnage {

	private int hauteur;
	private int largeur;
	private Labyrinthe laby; 

	private int vies;
	private int viesMax;

	private boolean sortie;
	
	/*
	 * Constructeur 
	 * @param Labyrinthe l
	 * @param int v Nombre de vies
	 * 
	 */
	public Personnage(Labyrinthe l, int v) {
		/* On cherche a placer le personnage aleatoirement
		 * dans le labyrinthe mais toujours dans la colonne de gauche
		 * on aura alors besoin de la hauteur du labyrinthe pour debuter
		 */
		this.hauteur = 1 + (int)(Math.random() * l.lireHauteur());
		this.largeur = 1; // Toujours dans la premiere colonne au depart
		this.laby = l;

		this.viesMax = v;
		this.vies = v;

		this.sortie = false;
	}

	/*
	 * Monte le personnage d'une case si possible, enleve une vie sinon.
	 * @return boolean
	 */
	public boolean monter() {
		if (this.hauteur == 1) {
			return true;
		}

		if (this.laby.murHorizontalSur(this.hauteur-1, this.largeur)) {
			descendreVies();
			return false;			
		}

		this.hauteur--;
		return true;
	}

	/*
	 * Descend le personnage d'une case si possible, enleve une vie sinon.
	 * @return boolean
	 */
	public boolean descendre(){
		if (this.hauteur == this.laby.lireHauteur()) {
			return true;
		}

		if (this.laby.murHorizontalSur(this.hauteur, this.largeur)) {
			descendreVies();
			return false;
		}
		
		this.hauteur++;
		return true;

	}
	
	/*
	 * Deplace le personnage d'une case a gauche si possible, enleve une vie sinon.
	 * @return boolean
	 */	
	public boolean gauche() {
		if (this.largeur == 1) {
			return true;
		}		

		if (this.laby.murVerticalSur(this.hauteur, this.largeur)) {
			descendreVies();
			return false;
		}
		
		this.largeur--;
		return true;
	}
	
	/*
	 * Deplace le personnage d'une case a droite si possible, enleve une vie sinon.
	 * @return boolean
	 */	
	public boolean droite() {
		if (this.largeur == this.laby.lireLargeur()) {
			if (!this.laby.murVerticalSur(this.hauteur, this.largeur+1)) {
				this.sortie = true;
			}
			return true;
		} 
			 
		if (this.laby.murVerticalSur(this.hauteur, this.largeur+1)) {
			descendreVies();
			return false;
		}

		this.largeur++;
		return true;
	}

	/*
	 * Enleve une vie au personnage
	 */
	private int descendreVies() {
		this.vies = this.vies > 0 ? --this.vies : 0;
		return this.vies;
	}

	/*
	 * Access publique a la hauteur (ie. position ligne) du personnage
	 * @return int hauteur
	 */
	public int lireHauteur() {
		return this.hauteur;
	}

	/*
	 * Access publique a la largeur (ie. position colonne) du personnage
	 * @return int largeur
	 */
	public int lireLargeur() {
		return this.largeur;
	}

	/*
	 * Access publique au nombre de vies restantes
	 * @return int vies
	 */
	public int lireVies() {
		return this.vies;
	}

	/*
	 * Verifie si le personnage est sortie du labyrinthe
	 * @return boolean
	 */
	public boolean estSortie() {
		return this.sortie;
	}

	/*
	 * Access publique au nombre de vies max
	 * @return int vies
	 */
	public int lireViesMax() {
		return this.viesMax;
	}
	
}





