import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale du programme. Cette classe s'occupe de creer les partis pour le joueur.
 * On utilise les classes Personnage et Labyrinthe.
 * 
 * @author Alex Marcotte (MARA10068704)
 * @see Personnage
 * @see Labyrinthe
 **/
public class Laby {
	
	private static int hauteur;
	private static int largeur;
	private static double densite;
	private static int temps;
	private static int vies;
	
	private static boolean regenere;
	
	/**
	 * Methode main
	 * @param String[] args
	 * @return void
	 * Methode d'entree du programme
	 */
	public static void main(String[] args) {
		if (args.length < 5) {
			System.out.println("Laby: <hauteur> <largeur> <densite> <temps> <vies>");
			System.exit(1);
		}

		hauteur = Integer.parseInt(args[0]);
		largeur = Integer.parseInt(args[1]);
		densite = Double.parseDouble(args[2]);
		temps = Integer.parseInt(args[3]);
		vies = Integer.parseInt(args[4]);

		// Boucle principale
		do {
			jeuLabyrintheInvisible();	
		} while (regenere || lireRejouer());
		
		// Fin du programme.
		return;
   	}

	/**
	 * Methode lireMouvements
	 * @param Scanner scan
	 * @return String direction
	 */
	private static String lireMouvements() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Quelle direction souhaitez-vous prendre?\n(droite: d; gauche g ou s; haut: h ou e; bas: b ou x) ");
		String direction = scan.next();

		return direction;
	}

	/**
	 * Methode lireRejouer
	 * @param void
	 * @return boolean 
	 */
	private static boolean lireRejouer() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Voulez-vous faire une autre partie? [o/n]: ");
		String rejouer = scan.next();	
		
		return (rejouer.equals("o") || rejouer.equals("O"));
	}

	/**
	 * Methode afficherVies
	 * @param Personnage p
	 * @return void
	 */
	private static void afficherVies(Personnage p) {
		System.out.format("Il vous reste %d vies sur %d.%n%n", p.lireVies(), p.lireViesMax());
	}
	
	/**
	 * Methode jeuLabyrintheInvisible
	 * @param void
	 * @return void
	 */
	private static void jeuLabyrintheInvisible() {
		String direction;
		boolean debutAI = false;
		regenere = false;
		
		Labyrinthe lab = new Labyrinthe(hauteur, largeur, densite);
		Personnage perso = new Personnage(lab, vies);

		// Le labyrinthe 'vide' qu'on affiche une fois le temps epuise
		Labyrinthe faux = new Labyrinthe(lab);
		faux.dessinePersonnage(perso);
		lab.dessinePersonnage(perso);

		lab.afficher();
		System.out.format("Vous avez %d secondes pour regarder le labyrinthe.%n", temps);

		// Puis on attend le nombre de secondes voulues
		sleep(temps*1000);
		updateEcran(lab, faux, perso);
		
		// Boucle principale pour la lecture des mouvements / commandes
		boolean sortir = false;
		while (!sortir) {
			direction = lireMouvements();
			
			faux.effacePersonnage(perso);
			lab.effacePersonnage(perso);
			
			if (direction.equals("q")) {
				sortir = true;
			} else if (direction.equals("d") && !perso.droite()) {
				faux.dessineMurVertical(perso.lireLargeur()+1, perso.lireHauteur());
			} else if ( (direction.equals("g") || direction.equals("s")) && !perso.gauche() ) {
				faux.dessineMurVertical(perso.lireLargeur(), perso.lireHauteur());
			} else if ( (direction.equals("h") || direction.equals("e")) && !perso.monter() ) {
				faux.dessineMurHorizontal(perso.lireLargeur(), perso.lireHauteur());
			} else if ( (direction.equals("b") || direction.equals("x")) && !perso.descendre() ) {
				faux.dessineMurHorizontal(perso.lireLargeur(), perso.lireHauteur()+1);
			}
			/* Genere un nouveau labyrinthe avec les memes options
			 * peut etre utile si le labyrinthe n'a pas de sortie
			 */
			else if (direction.equals("p")) {
				regenere = true;
				sortir = true;
			} else if (direction.equals("v")) { // Triche: On copie l'interieur du vrai labo dans le 'vide' pour montrer les murs
				faux.copieInterieur(lab);
			} else if (direction.equals("o")) { // On enleve le control au joueur pour debuter la sortie automatique (si possible)
				debutAI = true;
				sortir = true;
			} else { // Aucune action pour la lettre entree
				System.out.println("Invalide.\n");
			}

			updateEcran(lab, faux, perso);
			
			// Sortie du labyrinthe, on affiche un message avec le nombre de vies restant
			if (perso.estSortie()) {
				if (vies == perso.lireVies()) {
					System.out.println("Bravo, vous avez reussi le labyrinthe sans perdre aucune vie.%n");
				} else {
					System.out.format("Bravo, vous etes parvenu jusqu'a la sortie en comettant seulement %d erreurs.%n", vies - perso.lireVies());	
				}
				sortir = true;
			}
			
			// Aucune vie restante, on quitte le jeu
			if (perso.lireVies() == 0) {
				System.out.format("Vous avez perdu! Vous avez epuise vos %d vies!%n", vies);
				sortir = true;
			}
			
		}
		
		// On a quitte le jeu en demander au programme de trouver la sortie
		if (debutAI) {
			trouverSortie(perso, lab);
		}
	}

	/**
	 * sleep
	 * @param long millisecondes
	 * @return void
	 */
	public static void sleep(long millisecondes) {
		try {
			Thread.sleep(millisecondes);
		}
		catch(InterruptedException e){
			System.out.println("Sleep interrompu");
		}
	}
	
	/**
	 *  Met l'ecran a jour en affichant des lignes vides, le labyrinthe et les vies
	 *  @param Labyrinthe lab
	 *  @param Labyrinthe faux
	 *  @param Personnage pero
	 */
	public static void updateEcran(Labyrinthe lab, Labyrinthe faux, Personnage perso) {
		Labyrinthe.effaceEcran();
		lab.dessinePersonnage(perso);
		faux.dessinePersonnage(perso);
		faux.afficher();
		afficherVies(perso);
	}
	
	/**
	 * Methode trouverSortie
	 * Trouve la sortie a l'aide d'un algorithme de recherche a partir de la position du personnage
	 * @param Personnage p
	 * @param Labyrinthe l
	 */
	private static void trouverSortie(Personnage p, Labyrinthe l) {
		
		// On affiche le labyrinthe (visible) avant de debuter le solveur
		Labyrinthe.effaceEcran();
		l.dessinePersonnage(p);
		l.afficher();
		
		Coord sortie = new Coord(l.lireSortie(), l.lireLargeur());
		
		List<Coord> chemins = new ArrayList<Coord>();
		// On ajoute le départ à la liste des positions
		Coord depart = new Coord(p.lireHauteur(), p.lireLargeur());
		System.out.println("Depart de l'agorithme a: " + depart.toString());
		chemins.add(depart);
		
		// Si la sortie n'a pas de voisins valides, il n'y a pas de chemins qui y mene
		if (sortie.voisins(l).isEmpty()) {
			System.out.println("Pas de chemin qui mene a la sortie!");
			return;
		}
		
		// Si la position actualle n'a pas de voisins valide (ie. on debut la partie entre 4 murs) on ne peut pas resoudre
		if (depart.voisins(l).isEmpty()) {
			System.out.println("Pas de mouvement possible a partir de cette position!");
			return;
		}
		
		int position = 0;
		while (position < chemins.size()) {
			// On trouve tout les voisins de la position actuelle
			for (Coord v : chemins.get(position).voisins(l)) {
				if (!chemins.contains(v)) {
					chemins.add(v); // S'il n'est pas deja dans la liste, on l'ajoute
				}
			}
			position++;
		}
		
		// Si la sortie n'est pas parmis les cases sur les chemins parcourable, il est impossible d'y acceder
		if (!chemins.contains(sortie)) {
			System.out.println("Impossible d'acceder a la sortie!");
			return;
		}
		
		List<Coord> suivre = new ArrayList<Coord>(chemins.subList(0, chemins.indexOf(sortie)+1));
		
		// A partir de la sortie, on efface les cases qui ne mene pas au personnage
		for (int i = suivre.size()-1; i > 0; i--) {
			if (!suivre.get(i).voisins(l).contains(suivre.get(i-1))) {
				suivre.remove(i-1);
			}
		}
		
		// Puis on deplace le personnage en suivant le chemin
		for (Coord c : suivre) {
			l.effacePersonnage(p);
			l.mettreTrace(p.lireHauteur(), p.lireLargeur());
			if (c.y > p.lireLargeur()) {
				p.droite();
			} else if (c.x > p.lireHauteur()) {
				p.descendre();
			} else if (c.y < p.lireLargeur()) {
				p.gauche();
			} else if (c.x < p.lireHauteur()) {
				p.monter();
			}
			
			Labyrinthe.effaceEcran();
			l.dessinePersonnage(p);
			l.afficher();
			sleep(250); // Avec un delai pour voir l'animation
		}
	}	
}

/*
 * Classe Coord
 * Represente une coordonne dans le labyrinthe. Utile lors de l'algorithme qui
 * trouve le chemin vers la sortie a partir de la position actuelle du personnage.
 */
class Coord {
	public int x;
	public int y;
	
	/*
	 * Constructeur.
	 * @param int x 
	 * @param int y
	 */
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Override de la methode equals pour utiliser contains avec ArrayList
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coord) {
			Coord c = (Coord) obj; 
			return ( (this.x == c.x) && (this.y == c.y) );
		}
		return false;
	}
	
	/*
	 * Override de toString pour debug / afficher une coord a l'ecran
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	/*
	 * Genere la liste des voisins valides pour la case
	 */
	public List<Coord> voisins(Labyrinthe l) {
		List <Coord> voisins = new ArrayList<Coord>();
		
		int hauteur = l.lireHauteur();
		int largeur = l.lireLargeur();
		
		// Bas
		if (x < hauteur && !l.murHorizontalSur(x,y)) {
			voisins.add(new Coord(x+1, y));	
		}
		
		// Haut
		if (x > 1 && !l.murHorizontalSur(x-1, y)) {
			voisins.add(new Coord(x-1, y));	
		}
		
		// Gauche
		if (y > 1 && !l.murVerticalSur(x, y)) {
			voisins.add(new Coord(x, y-1));	
		}
		
		// Droite
		if (y < largeur && !l.murVerticalSur(x, y+1)) {
			voisins.add(new Coord(x, y+1));	
		}
		
		return voisins;
	}
	
}














