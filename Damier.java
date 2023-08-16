import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Damier extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int briquesSupprimees;//Compteur de briques supprimées
	private static int points;//Compteur de points
	private static JTextArea briquesSupp;//Affichage des briques supprimées
	private static JTextArea pointsCumul;//Affichage des points
	private static JPanel damier;
	private static JPanel score;
	private static Brique[][] briques;//Matrice contenant les briques
	
	public Damier() {
		briquesSupprimees = 0;
		points = 0;
		
		//Création d'un damier de taille aléatoire comprise entre 5 et 10
		int lignes = 5+(int)(Math.random()*6);
		int colonnes = 5+(int)(Math.random()*6);
		GridLayout layoutDamier = new GridLayout(lignes,colonnes);
		damier = new JPanel(layoutDamier);
		
		//Création de la matrice et ajout dans le JPanel damier
		briques = new Brique[lignes][colonnes];
		for(int l=0;l<lignes;l++)
			for(int c=0;c<colonnes;c++)
			{
				briques[l][c] = new Brique(l,c);
				briques[l][c].addActionListener(this);//Permet de lier une action au clic d'une brique
				damier.add(briques[l][c]);
			}
		
		//Création de la partie "score" en haut de la fenêtre 
		briquesSupp = new JTextArea("Briques supprimmées : 0");
		pointsCumul = new JTextArea("Points cumulés : 0");
		briquesSupp.setEditable(false);
		pointsCumul.setEditable(false);
		GridLayout layoutScore = new GridLayout(1,2);
		score = new JPanel(layoutScore);
		score.add(briquesSupp);
		score.add(pointsCumul);
		
		//Paramètres de la fenêtre
		this.setTitle("Jeu du Damier");
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(colonnes*100,lignes*100));
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		//Ajout des JPanel a la fenêtre
		this.add(score,BorderLayout.NORTH);
		this.add(damier,BorderLayout.CENTER);
	}
	
	public static void supprimeBriques(int l, int c) {//Supprime la brique à la ligne l et colonne c
		if(briques[l][c].isActive())
		{
			Color couleur = briques[l][c].getCouleur();//Sauvegarde de la couleur de la brique avant desactivation
			briques[l][c].desactive();
			int n = briquesSupprimees;//Sauvegarde du nombre de briques supprimées avant récursivité pour le compte des points
			int p = points;//Sauvegarde des points avant récursivité pour l'affichage du score
			briquesSupprimees++;
			
			//brique du dessus
			if(l>0 && briques[l-1][c].getCouleur()==couleur)
				supprimeBriques(l-1,c);
			
			//brique du dessous
			if(l<briques.length-1 && briques[l+1][c].getCouleur()==couleur)
				supprimeBriques(l+1,c);
			
			//brique de gauche
			if(c>0 && briques[l][c-1].getCouleur()==couleur)
				supprimeBriques(l,c-1);
			
			//brique de droite
			if(c<briques[0].length-1 && briques[l][c+1].getCouleur()==couleur)
				supprimeBriques(l,c+1);
			
			//calcul des points
			n = briquesSupprimees - n;//nombre de briques supprimées après l'appel de toute les méthodes
			points=p + n*n;//Ancien score plus le nombre de briques supprimées au carré
		}
	}
	
	public static void tomberBriques() {//Fais tomber les briques si elles ont des cases vides en dessous d'elles
		for(int c=0;c<briques[0].length;c++)
			for(int l=briques.length-1;l>0;l--)
			{
				if(!briques[l][c].isActive())
				{
					Brique dessus = premiereDessus(l,c);//Dans le cas d'un écart de plusieurs cases prends la première brique active au dessus
					if(dessus!=null)
					{
						briques[l][c].setCouleur(dessus.getCouleur());
						briques[l][c].active();
						dessus.desactive();						
					}
				}
			}
	}
	
	public static Brique premiereDessus(int l, int c) {//Retourne la première brique active dans la colonne c au dessus de la ligne l / null si il n'y a pas de briques active au dessus
		Brique res = null;
		if(l>0)
		{
			for(int l2=l-1;l2>=0;l2--)
			{
				if(briques[l2][c].isActive() && res ==null)
					res = briques[l2][c];
			}
		}
		return res;
	}
	
	public static boolean colonneVide(int c) {//Test si la colonne c est vide
		boolean res = true;
		for(int i=0;i<briques.length;i++)
			if(briques[i][c].isActive())//Dès qu'une brique dans la colonne est active alors la colonne n'est pas vide
				res=false;
		return res;
	}
	
	public static void copierColonne(int i, int j) {//Copie de la colonne j dans la colonne i
		Brique[] copie = new Brique[briques.length];
		boolean[] copieActive = new boolean[briques.length];
		for(int l=0;l<briques.length;l++)//Copie de la colonne j
		{
			copie[l]=briques[l][j];
			copieActive[l]=briques[l][j].isActive();
		}
		
		for(int l=0;l<briques.length;l++)//Copie dans la colonne i
		{
			if(copieActive[l])//Copie des cases uniquement actives
			{
				briques[l][i].setCouleur(copie[l].getCouleur());
				briques[l][i].active();
			}
			else//Désactive les autres cases
				briques[l][i].desactive();
		}
	}
	
	public static void regrouperColonnes() {//Regroupe les colonnes vers la gauche du damier
		for(int c=0;c<briques[0].length-1;c++)
		{
			if(colonneVide(c))
			{
				int c2 = premiereNonVide(c);//Dans le cas d'un écart de plusieurs colonnes choisi la première colonne à droite n'étant pas vide
				if(c2!=-1)
				{
					copierColonne(c,c2);
					
					for(int l=0;l<briques.length;l++)//Désactive la colonne copiée
						briques[l][c2].desactive();
				}
			}
		}
	}
	
	public static int premiereNonVide(int c) {//Retourne la première colonne non vide a droite de la colonne c / -1 s'il n'y a pas de colonne vide à droite de la colonne c
		int res = -1;
		for(int c2=c+1;c2<briques[0].length;c2++)
			if(!colonneVide(c2) && res ==-1)
				res = c2;
		return res;
	}
	
	public void actionPerformed(ActionEvent arg0) {//Action qu'il se passe après le clic d'une brique
		Brique source = (Brique) arg0.getSource();//Récupère la brique ayant été cliquée
		if(source.isActive())//Si la brique est active alors
		{
			supprimeBriques(source.getLigne(),source.getColonne());//Supprime les briques adjacentes de la même couleur récursivement
			tomberBriques();//Fais tomber les briques ayant du vide sous elles
			regrouperColonnes();//Regroupe les colonnes vers la gauche
			
			//Actualisation du score
			briquesSupp.setText("Briques supprimmées : " + briquesSupprimees);
			pointsCumul.setText("Points cumulés : " + points);
			
			//Fin de partie
			if(briquesSupprimees == briques.length*briques[0].length)
			{
				//Le joueur choisi de faire une autre partie ou non
				int result = JOptionPane.showConfirmDialog(damier,"Vous avez fini la partie avec " + points + " points. BRAVO ! \n Voulez vous faire une nouvelle partie ?", "Fin de partie",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.OK_OPTION)
				{
					this.setVisible(false);
					new Damier();
				}
				else
					this.dispose();
			}
		}
		else//Sinon affiche un message d'erreur disant au joueur de cliquer sur une case active
		{
			JOptionPane.showMessageDialog(damier, "MERCI DE SELECTIONNER UNE CASE COLOREE", "ERREUR",JOptionPane.ERROR_MESSAGE);
		}			
	}
}
