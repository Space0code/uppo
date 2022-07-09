package uppo;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;

public class Zaslon extends JPanel {
	
	private JPanel z;
	
	/*
	 * rabil bi neko spremenljivko, ki bi hranila vse instance (objekte) tega razreda
	 * zato, da bi lahko na primer usposobil funkcijo skrijVse()
	 */
	
	public Zaslon(Color barva, String naslov, JFrame o) {
		z = new JPanel(new GridBagLayout());
		z.setBackground(barva);
		z.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), naslov.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(uppo_003.font,Font.PLAIN,14), Color.white));
		o.add(z, BorderLayout.CENTER);
		o.pack();
		o.setVisible(true);
		uppo_003.fullScreen(o);
		
	}
	
	/*
    public void skrijVse() {
    	try{ jZacZaslon.setVisible(false); } catch (Exception e) {}
    	try{ jPoizZaslon1.setVisible(false); } catch (Exception e) {}
    	try{ jPoizZaslon2.setVisible(false); } catch (Exception e) {}
    	try{ jPoizZaslon3.setVisible(false); } catch (Exception e) {}
    	
    }*/
	
	public void skrij() {
		try{ z.setVisible(false); } catch (Exception e) {}	
	}
    
	public void pokazi(JFrame o) {
		z.setVisible(true);
		o.pack();
		o.setVisible(true);
		uppo_003.fullScreen(o);
	}
	
	public JPanel zaslon() {
		return z;
	}
	
	public void dodaj(Component comp) {
		z.add(comp);
	}
	
	public void dodaj(Component comp, Object constraints) {
		z.add(comp, constraints);
	}
}
