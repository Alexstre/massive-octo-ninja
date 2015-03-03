import java.util.Arrays;

/*
 * Classe Labyrinthe
 * Genere le labyrinthe et inclu les fonctions pour manipuler les labyrinthes
 *
 * @author Alex Marcotte (MARA10068704)
 * @see Personnage
 * @see Laby 
 */
public class Labyrinthe {

	private int hauteur;
	private int largeur;
	private double densite;

	private static final int LMURET = 8;
	private static final int HMURET = 4;

	private char[][] laby;
	
	private int sortie;
	
	/*
	 * Constructeur avec hauteur, largeur et densite
	 * @param int hauteur
	 * @param int largeur
	 * @param double densite
	 */
	public Labyrinthe(int hauteur, int largeur, double densite) {
		this.largeur = Labyrinthe.LMURET*largeur + 1;
		this.hauteur = Labyrinthe.HMURET*hauteur + 1;
		
		// On accepte les densite en pourcentage (0.5) ou entier (50)
		if (densite > 1) {
			this.densite = densite/100;	
		} else {
			this.densite = densite;
		}
		
		creeTableau();
		effaceTableau();
		dessinMurs();

		dessineMursInterieur();
		placerSortie();
	}

	/*
	 * Constructeur de copie
	 * @param Labyrinthe l Le labyrinthe a copier
	 */
	protected Labyrinthe(Labyrinthe l) {
		hauteur = l.hauteur;
		largeur = l.largeur;
		densite = l.densite*100;

		// Creer le labyrinthe pour la copie
		creeTableau();

		// Copie le contenue du Labyrinthe passe en parametre
		for (int i=0; i<hauteur; i++) {
			for (int j=0; j<largeur; j++) {
				this.laby[i][j] = l.laby[i][j];
			}
		}

		// Puis on efface l'interieur
		effaceTableau();
	}

	/*
	 * Creer le tableau
	 */
	private void creeTableau() {
		this.laby = new char [this.hauteur][this.largeur];
	}

	/*
	 * Rempli le tableau d'espace vide
	 */
	private void effaceTableau() {
		for (int i=1; i<this.hauteur-1; i++) {
			for (int j=1; j<this.largeur-1; j++) {
				this.laby[i][j] = ' ';
			}
		}
	}

	/*
	 * Dessine les murs et les coins (+) exterieurs du tableau
	 */
	private void dessinMurs() {
		for (int i = 0; i < this.hauteur; i++) {
			for (int j = 0; j < this.largeur; j++) {
				if (i==0 || i==this.hauteur-1) {
					Arrays.fill(this.laby[i], '-');
				}
				else if (j==0 || j==this.largeur-1) {
					this.laby[i][j] = '|';
				}
			}
		}

		// On place les + dans chaque coins
		this.laby[0][0] = '+';
		this.laby[0][this.largeur-1] = '+';
		this.laby[this.hauteur-1][0] = '+';
		this.laby[this.hauteur-1][this.largeur-1] = '+';
		
	}

	/*
	 * Genere des murs interieurs en fonction de la densite du labyrinthe
	 */
	private void dessineMursInterieur() {
		// On passe les cases une par une
		for (int i=1; i <= (this.hauteur-1)/Labyrinthe.HMURET; i++) {
			for (int j=1; j <= (this.largeur-1)/Labyrinthe.LMURET; j++) {
				// On dessine un mur selon la densite
				double r1 = Math.random();
				double r2 = Math.random();
				if (this.densite >= r1) {
					dessineMurVertical(i,j);
				}
				if (this.densite >= r2) {
					dessineMurHorizontal(i,j);
				}
			}
		}
	}

	/*
	 * Dessine un mur vertical a la position (i, j)
	 */
	public void dessineMurVertical(int ligne, int col) {
		int debutx = Labyrinthe.HMURET * (col - 1);
		int debuty = Labyrinthe.LMURET * (ligne - 1);

		for (int j=1; j < Labyrinthe.HMURET; j++) {
			this.laby[debutx+j][debuty] = '|';
		}
	}
	
	/*
	 * Dessine un mur horizontal a la position (i, j)
	 */
	public void dessineMurHorizontal(int ligne, int col) {
		int debutx = Labyrinthe.HMURET * (col - 1);
		int debuty = Labyrinthe.LMURET * (ligne - 1);

		for (int j=1; j < Labyrinthe.LMURET; j++) {
			this.laby[debutx][debuty+j] = '-';
		}
	}

	/*
	 * Place la sortie aleatoirement dans la colonne de gauche
	 */
	private void placerSortie() {
		// Position aleatoire
		int col = 1 + (int)(Math.random() * lireHauteur());
		int debuty = Labyrinthe.HMURET * (col-1);
		
		for (int j=1; j<Labyrinthe.HMURET; j++) {
			this.laby[debuty+j][this.largeur-1] = ' ';
		}
		// Et on garde la position en memoire dans l'objet Labyrinthe
		this.sortie = col;
	}
	
	/*
	 * Access public a la position de la sortie
	 * @return int sortie
	 */
	public int lireSortie() {
		return this.sortie;
	}
	
	/*
	 * Affiche le labyrinthe a l'ecran
	 */
	public void afficher() {
		for (char[] ligne: this.laby) {
			for (char col: ligne) {
				System.out.print(col);
			}
			System.out.println("");
		}
	}

	/*
	 * 'Dessine' le personnage en argument en ecrivant un @ dans le tableau
	 * @param Personnage p
	 */
	public void dessinePersonnage(Personnage p) {
		int h = (p.lireHauteur()-1)*Labyrinthe.HMURET + Labyrinthe.HMURET/2;
		int l = (p.lireLargeur()-1)*Labyrinthe.LMURET + Labyrinthe.LMURET/2;
		this.laby[h][l] = '@';
	}

	/*
	 * 'Efface' le personnage en argument en ecrivant un espace dans le tableau
	 * @param Personnage p 
	 */
	public void effacePersonnage(Personnage p) {
		int h = (p.lireHauteur()-1)*Labyrinthe.HMURET + Labyrinthe.HMURET/2;
		int l = (p.lireLargeur()-1)*Labyrinthe.LMURET + Labyrinthe.LMURET/2;
		this.laby[h][l] = ' ';
	}

	/*
	 * Met un X a la position (x, y) dans le labyrinthe. On l'utilise pour montrer
	 * le chemin lors de la resolution automatique du labyrinthe.
	 * @param int x
	 * @param int y
	 */
	public void mettreTrace(int x, int y) {
		int h = (x-1)*Labyrinthe.HMURET + Labyrinthe.HMURET/2;
		int l = (y-1)*Labyrinthe.LMURET + Labyrinthe.LMURET/2;
		this.laby[h][l] = 'X';
	}
	
	/*
	 * Access publique a la hauteur du labyrinthe en nombre de case
	 * @return int hauteur en nombre de case
	 */
	public int lireHauteur() {
		return (this.hauteur-1) / Labyrinthe.HMURET;
	}

	/*
	 * Access publique a la largeur du labyrinthe en nombre de case
	 * @return int largeur en nombre de case
	 */
	public int lireLargeur() {
		return (this.largeur-1) / Labyrinthe.LMURET;
	}

	/*
	 * Verifie si un mur vertical se trouve en (i, j)
	 * @param int i
	 * @param int j
	 * @return boolean
	 */
	public boolean murVerticalSur(int i, int j) {
		int debutx = Labyrinthe.HMURET * (i-1) +1;
		int debuty = Labyrinthe.LMURET * (j-1);

		// Les murs verticaux sont representes par des |
		return (laby[debutx][debuty] == '|') ;
			
	}

	/*
	 * Verifie si un mur horizontal se trouve en (i, j)
	 * @param int i
	 * @param int j
	 * @return boolean
	 */	
	public boolean murHorizontalSur(int i, int j) {
		int debutx = Labyrinthe.HMURET * i;
		int debuty = Labyrinthe.LMURET * (j-1) + 1;

		// Les murs horizontaux sont representes par des -
		return (laby[debutx][debuty] == '-');
	}

	/*
	 * Inscrit 200 lignes vides a l'ecran pour 'vider' l'ecran
	 */
	public static void effaceEcran() {
		for (int i=0; i<200; i++) {
			System.out.println("");
		}
	}
	
	/*
	 * Copie l'interieur d'un labyrinthe
	 * @param Labyrinthe l
	 */
	public void copieInterieur(Labyrinthe l) {
		for (int i=1; i<this.hauteur-1; i++) {
			for (int j=1; j<this.largeur-1; j++) {
				this.laby[i][j] = l.laby[i][j];
			}
		}
	}

}

