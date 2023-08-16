import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class Boite extends JButton implements MouseListener{
	private Color couleur;
	private boolean bombe;
	private boolean ouvert;
	Color setCouleur(Color c) {
		return couleur = c;}
	boolean setBombe(boolean b) {
		return bombe = b;}
	boolean setOuvert(boolean o) {
		return ouvert = o;}
	Color getcouleur() {return couleur;}
	boolean getbombe() {return bombe;}
	boolean getOuvert() {return ouvert;}

	Color choixCouleur() {
		int alea = (int)(Math.random()*6);
		switch(alea)
			{
			case 0:
				return Color.BLUE;
			case 1:
				return Color.CYAN;
			case 2:
				return Color.GREEN;
			case 3:
				return Color.MAGENTA;
			case 4:
				return Color.ORANGE;
			default :
				return Color.RED;
			}
	}
	Boite(String n){
		super(n);
		couleur = choixCouleur();
		this.setBackground(couleur);
		int nbr =(int)(Math.random()*100);
		if(nbr<20)bombe =true;
		else bombe =false;
		ouvert = false;
		this.addMouseListener(this);
	}
	public void mouseEntered(MouseEvent e){
		this.setBackground(Color.BLACK);
		
	}
	public void mouseExited(MouseEvent e){
		this.setBackground(couleur);
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
