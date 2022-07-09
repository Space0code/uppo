package uppo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.*;

public class Gumb {

	private JButton g;
	
	public Gumb(String naslov) {
		g = new JButton(naslov);
		poslusaj();
	}
	
	public void pozicionirajVneselSem(Zaslon z) {
    	z.add(Box.createRigidArea(new Dimension(200, 100)));
    	GridBagConstraints c = new GridBagConstraints();

    	c.gridwidth = 1;
    	//c.gridheight = 1;
    	c.gridx = 0;
    	c.gridy = 2;
    	c.weightx = 0.1;
    	c.weighty = 1.0;
    	c.ipady = 15;
    	c.ipadx = 60;
    	c.insets = new Insets(0,0,0,0);
    	c.anchor = GridBagConstraints.SOUTH;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	z.dodaj(g, c);
    	
	}
	
	public void poslusaj() {
    	// gumb posluša kdaj bo kliknjen
    	g.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
    			vneselSemStisnjen(evt);
    		}
    	});
	}
	
	private void vneselSemStisnjen(java.awt.event.MouseEvent evt) {

		if (uppo_003.jTabelaVnosna.preveriVrstico()) {
	    	uppo_003.jNapaka2.setText("");
			uppo_003.konecVnosaPrimerkov();
		}
		else {
	    	uppo_003.jNapaka2.setText("V zadnji vrstici ni podatka.");
		}
	}
	

}
