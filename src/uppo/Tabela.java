package uppo;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class Tabela extends JTable {
	
	private JScrollPane sp;
	private JTable t;
	private Object [][] vsebina;
	private String [] glava;
	private Class [] types;	// podatkovni tipi stolpcev (Integer, String)
	private String f;	// font
	private int stk;    // stevilo kategorij
	private int ID;
	private int st_stolpcev;
	
	
 	public Tabela(String vrsta, int st_kategorij, String [] naslovi) {
		f = uppo_003.font;
		stk = st_kategorij;
		switch (vrsta) {
		case "vnosna": 
			ustvariVnosna(naslovi);
			break;
	
		}
 	}

 	public Tabela(String vrsta, int st_kategorij, String [] naslovi, Object [][] podatki) {
		f = uppo_003.font;
		stk = st_kategorij;
		switch (vrsta) {
		case "porocilo":
			ustvariPorocilo(naslovi, podatki);
	
		}
 	}
 	
	private void ustvariVsebinaGlavaVnosna(String [] naslovi) {
		st_stolpcev = stk + 1;	// toliko kot je kategorij + 1 za ID-stevilko
		vsebina = new Object[1][st_stolpcev];
		vsebina[0][0] = 1;	// ID = 1
		
		// inicializiraj vsebino glave - prepiši vsebino tabele naslovi[]
		int j = 0;
		glava = new String[st_stolpcev];
		glava[j++] = "ID";	// prvi stolpec
		for (int i = 0; i < naslovi.length; i++) {
			if (naslovi[i] != null) {
				glava[j++] = naslovi[i];
			}
		}
	}
	
	private void ustvariVsebinaGlavaPorocilo(String [] naslovi, Object [][] podatki) {
		st_stolpcev = stk + 3;	// toliko kot je kategorij + {mesto, toèke, ID}
		this.vsebina = podatki;	
		this.glava = naslovi;
	}
	
	private Class [] tipi() {
		types = new Class[st_stolpcev];
		
		// doloèi tipe stolpcev tabele t
		types[0] = Integer.class;
		for (int i = 1; i < st_stolpcev; i++) {
			if (uppo_003.kategorije[i-1][1].equals( uppo_003.st_cim_vecje ) || uppo_003.kategorije[i-1][1].equals( uppo_003.st_cim_manjse ))	// a morem klicanje kategorije[][] narediti drugaèe?
				types[i] = Double.class;
			else if (uppo_003.kategorije[i-1][1] == "besedni")
				types[i] = String.class;
			else {
				types[i] = Object.class;
				System.out.println("Napaka pri branju tipov kategorij!");
			}
		}
		
		return types;
	}
	
	private boolean [] smeUrejati(String vrsta) {
		boolean[] canEdit = new boolean[st_stolpcev];
		
		switch(vrsta) {
		case "vnosna":
			// uporabnik sme urejati vse stolpce, razen ID (prvega) stolpca
			canEdit[0] = false;
			for(int i = 1; i < st_stolpcev; i++)
				canEdit[i] = true;
			break;
		case "porocilo":
			// uporabnik ne sme urejati nobenega stolpca
			for(int i = 0; i < st_stolpcev; i++)
				canEdit[i] = false;
			break;			
		}
		return canEdit;
	}
	
	public void ustvariTabelo() {
		sp = new JScrollPane();
		t = new JTable();
		t.setModel(new DefaultTableModel(vsebina, glava));
				
		nastaviPrivzeteVrednosi();
		ponastavi();

		sp.setViewportView(t);	// doda t na sp

	}
	
	private void ustvariVnosna(String [] naslovi) {
		// inicializiraj vsebino vsebine
		ustvariVsebinaGlavaVnosna(naslovi);
			
		// ustvari tabelo
		ustvariTabelo();
		nastaviModel("vnosna");
		dodajKeyListener();
		dodajVnosniCB();
	}
	
	private void ustvariPorocilo(String naslovi [], Object [][] podatki) {
		ustvariVsebinaGlavaPorocilo(naslovi, podatki);
		ustvariTabelo();
		nastaviModel("porocilo");
	}
			
	private void nastaviModel(String vrsta) {
		switch (vrsta) {
		
		case "vnosna":
			t.setModel(new DefaultTableModel(vsebina, glava) {
		 		Class[] types = tipi();
		 		boolean[] canEdit = smeUrejati(vrsta);
		 		
		 		public Class getColumnClass(int columnIndex) {
	                return types [columnIndex];
	            }

	            public boolean isCellEditable(int rowIndex, int columnIndex) {
	                return canEdit [columnIndex];
	            }
	 			});
			break;
			
		case "porocilo":
			t.setModel(new DefaultTableModel(vsebina, glava) {
		 		boolean[] canEdit = smeUrejati(vrsta);
		 		
	            public boolean isCellEditable(int rowIndex, int columnIndex) {
	                return canEdit [columnIndex];
	            }
	 			});
			break;
		}
	}
	
    private void nastaviPrivzeteVrednosi() {
    	t.setRowHeight(30);
    	t.setRowMargin(5);
    	t.setFont(new Font(f, Font.PLAIN, 14));
    	t.getTableHeader().setReorderingAllowed(false);	// onemogoèi, da bi uporabnik premikal stolpce tabele
    	t.changeSelection(0, 0, false, false);
    	// tekst v celicah naj bo na sredini
    	DefaultTableCellRenderer r = new DefaultTableCellRenderer();
    	r.setHorizontalAlignment( JLabel.CENTER );
    	r.invalidate();
    	t.getColumnModel().getColumn(0).setCellRenderer( r );
		t.setCellSelectionEnabled(true);

    	
    }
   
	public JFrame pozicioniraj(JFrame o, Zaslon z) {
    		
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridwidth = 4;
    	c.gridheight = 1;
    	c.gridx = 0;
    	c.gridy = 1;
    	c.weightx= 0.1;
    	c.weighty = 1.0;
    	c.ipadx = 400;
    	c.ipady = 400;
    	c.fill = GridBagConstraints.BOTH;
    	
    	//JScrollPane scrollpane = new JScrollPane(t);
    	sp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "".toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(f, Font.PLAIN, 14), Color.darkGray));
    	z.dodaj(sp, c);
    	
    	o.add(z);
    	return o;
    }
    	
	private void dodajVnosniCB() {
		int j = 0;
		for (int i = 1; i < st_stolpcev; i++) {
			if (types[i] == String.class) {
				dodajStolpcuCB(i, j);
				j++;
			}
		}
	}
	
	private void dodajStolpcuCB(int i_spl, int i_bes) {	
		// i_spl = indeks v tabeli vseh kategorij
		// i_bes = indeks v tabeli besednih kategorij
		String [] opcije = new String[100];	// število razliènih možnosti (besed) 
		int i_opc = 0; // indeks v tabeli opcije
		for (int i = 1; i < 11; i++) {	// gre skozi vse teže besed
			if (uppo_003.besede[i_bes][i] != null) {
				for (int j = 0; j < uppo_003.besede[i_bes][i].length; j++) {	// gre skozi vse besede posamezne teže
					if (uppo_003.besede[i_bes][i][j] != null)
						opcije[i_opc++] = uppo_003.besede[i_bes][i][j];
				}
			}
		}
		
		for (String s : opcije)
			if (s != null)
				System.out.print(";" + s + ";");
		System.out.println();
		
		dodajCB(opcije, i_spl);
		
	}
	
	public void dodajCB(String [] opcije, int stolpc) {
		// dodaj tabeli comboBox
    	// npr.: za doloèanje ali vsebuje podatek številsko ali besedno vrednost
		
		String o[] = skrciTabeloOpcije(opcije);
		
		TableColumn stolpec = t.getColumnModel().getColumn(stolpc);
    	JComboBox comboBox = new JComboBox();
    	for (int i = 0; i < o.length; i++)
    		comboBox.addItem(o[i]);
    	stolpec.setCellEditor(new DefaultCellEditor(comboBox));    		
	}
	
	private String[] skrciTabeloOpcije(String [] opcije) {
    	// zato, da nimamo v comboboxu potem ponujenih praznih vnosov
		
		int l = 0;
    	while(true) {
			if (opcije[l++] == null)
				break; 
		}
		l--;
		
		String o[] = new String[l];
		for (int i = 0; i < o.length; i++)
			o[i] = opcije[i];	
		
		return o;
	}
	
	public void ponastavi() {
		ID = 1;
	}
	
	public JFrame pokazi(JFrame o, Zaslon z) {
		t.setVisible(true);
		z.pokazi(o);
		o.pack();
		o.setVisible(true);
		return o;
	}
	
	public void skrij(JFrame o, Zaslon z) {
		t.setVisible(false);
		z.pokazi(o);
		o.pack();
		o.setVisible(true);
	}
	
	public boolean preveriPozicijo() {
		
		boolean na_koncu_vrstice = ( t.getSelectedColumn() == (st_stolpcev-1) );
		boolean v_zadnji_vrstici = ( t.getSelectedRow() == ID-1 );
		
		if (na_koncu_vrstice && v_zadnji_vrstici) return true;
		return false;
	}
	
	public boolean preveriVrstico() {
		// preveri ali je kaksen podatek v vrstici
		// ker èe ni, ne smemo dodati nove vrstice
		// false, èe je vrstivca prazna
		// true, èe je kak podatek v vrstici
				
    	// fokus je potrebno prestaviti iz celice, èe želiš, da se vrednost v njej shrani
		t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()+1, false, false);
		t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());
		t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()-1, false, false);
		t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());

		for (int i = 1; i < t.getColumnCount(); i++) {
			Object vrednost = t.getValueAt(t.getRowCount()-1, i);
			if (vrednost != null)
				return true;
		}
		return false;
	}
	
	public void dodajVrstico() {

		if (preveriVrstico()) {
			DefaultTableModel model = (DefaultTableModel) t.getModel();
			model.addRow(new Object[st_stolpcev]);
			//model.addRow(new Object[] {0, "", ""});
			t.setValueAt(Integer.toString(++ID), ID-1, 0);
			//t.setValueAt(++ID, ID-1, 0);
	    	uppo_003.jNapaka2.setText("");
		}
		else {
	    	uppo_003.jNapaka2.setText("V zadnji vrstici ni podatka.");
		}
	}
	
    public void tabuliraj() {

		int vrsta;
		int stolpec;
		boolean na_koncu_vrstice = ( t.getSelectedColumn() == (st_stolpcev-1) );
		
		if (na_koncu_vrstice) {
			t.changeSelection(t.getSelectedRow()+1, 0, false, false);
			t.editCellAt(t.getSelectedRow(), 0);
		}
		t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()+1, false, false);
		t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());
			
		//System.out.println(";" + t.getSelectedRow() + ";" + t.getSelectedColumn() + ";");
		//t.setSelectionBackground(Color.cyan);    			
		
    }
    
    private void dodajKeyListener() {
    	t.addKeyListener(new KeyListener() {
    		@Override
    		public void keyTyped(KeyEvent e) {}
    		
    		@Override
    		public void keyReleased(KeyEvent e) {}
    		
    		@Override
    		public void keyPressed(KeyEvent e) {
    			// pritisnjen enter
      			/* 
    			 * trenutno onemogoèeno premikanje po tabeli z enter;
    			 * uporabnik naj se premika s klikom miške
    			if (e.getKeyCode() == KeyEvent.VK_ENTER){
    				e.consume();
    				if(preveriPozicijo())
    					dodajVrstico();
    				tabuliraj();    
    			 }*/
    			
			}
    	});
    }

    public String [][] preberi() {
    	String izhod[][];
    	izhod = new String [ID][st_stolpcev];
    	
    	for (int i = 0; i < ID; i++) {
    		for (int j = 0; j < st_stolpcev; j++) {
    			izhod[i][j] = (String) t.getValueAt(i, j);
    		}    		
    	}
    	
    	return izhod;
    }
    
    public Object getValueAt(int row, int col) {
    	return t.getValueAt(row, col);
    }
    
    public String izpisiVsebino() {
    	String s = "";
    	
    	for (int i = 0; i < ID; i++) {
    		for (int j = 0; j < st_stolpcev; j++) {
    			s += (String) t.getValueAt(i, j) + "; ";
    		}    		
    		s += "\n";
    	}
    	
    	return s;
    }
    
    public int pridobiID() {
    	return ID;
    }
    
    public void prestaviFokusCelice() {
    	// metoda prestavi fokus za eno celico desno (pogojno levo) z namenom, da 
    	// se shrani vrednost celice preden bomo brali vrednosti iz vnosne tabele
    	if (t.getSelectedColumn() < st_stolpcev-1) {
    		t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()+1, false, false);
    		t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());
    	}
    	else {
    		t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()-1, false, false);
    		t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());
    	}
    }
    
    public Tabela objekt() {
    	return (Tabela) t;
    }
    
}
