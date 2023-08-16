import java.awt.Color;

import javax.swing.JButton;

public class Brique extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Color getCouleur() {
		return couleur;
	}
	
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}
	
	public int getLigne() {
		return ligne;
	}
	
	public void setLigne(int ligne) {
		this.ligne = ligne;
	}
	
	public int getColonne() {
		return colonne;
	}
	
	public void setColonne(int colonne) {
		this.colonne = colonne;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void desactive() {//Désactive la brique
		this.active = false;
		couleur = Color.WHITE;
		this.setBackground(couleur);
	}
	
	public void active() {//active la brique
		this.active = true;
		this.setBackground(couleur);
	}
	
	private Color couleur;
	private int ligne;
	private int colonne;
	private boolean active; 
	public final int SIZE = 20;// taille brique 
	
	public Brique(int l, int c) {//Constructeur
		couleur = choixCouleur();
		ligne = l;
		colonne = c;
		active = true;
		this.setSize(SIZE, SIZE);
		this.setBackground(couleur);
	}
	
	private static Color choixCouleur() {//Choisi aléatoirement la couleur de la brique
		int coul = (int)(Math.random()*7);
		if (coul == 1) return Color.CYAN;
		if (coul == 2) return Color.YELLOW;
		if (coul == 3) return Color.RED;
		if (coul == 4) return Color.GREEN;
		if (coul == 5) return Color.BLUE;
		else return Color.MAGENTA;
	}
	
}
