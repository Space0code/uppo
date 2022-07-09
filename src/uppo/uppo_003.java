package uppo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class uppo_003 extends javax.swing.JFrame {	// extends javax.swing.JFrame je zato, ker drugace 
													// new uppo_003().setVisible(true); ne dela
	
    public uppo_003() {
    	init();
    }
    
    public void init() {
    	ponastaviSpremenljivke();
    	ustvariZacetnoOkno();
    }
    
    private void ponastaviSpremenljivke() {
    	naslov = "";
    	font = "Helvetica";
    	glava = new String[3]; 
    	glava[0] = "Naslov kategorije";
    	glava[1] = "Izberi vrsto oz. tip podatka";
    	glava[2] = "Prioriteta (1 = najvišja prioriteta)";
	/*  prazna[]	se inicializira kasneje
    	kategorije[][];	se inicializira kasneje 
    	naslovi_kategorij[]; se inicializira kasneje 
    	besede[][][]; se inicializira kasneje  */
    	st_kat = 0;
    	st_bes_kat = 0;
    	Dimension dimenzije = Toolkit. getDefaultToolkit().getScreenSize();
        sirina_okna = (int)dimenzije.getWidth() - 10;
        visina_okna = (int)dimenzije.getHeight() - 30;
        st_bes_nivojev = 7;
    }
    
    private void ustvariZacetnoOkno() {
    	ustvariOkvir();
    	//postaviFrameNaSredino(okvir);
    	fullScreen(okvir);
    	ustvariZacZaslon();		// zaslon JPanel
    	ustvariDobrodosel();		// besedilo JLabel
    	ustvariUstvariNovBtn();	// gumb JButton
    	ustvariPreglejBtn();  		// gumb JButton
    }
    
    public void ustvariOkvir() {
    	okvir = new JFrame("Univerzalni program za podporo odloèanju");
    	okvir.setSize(sirina_okna, visina_okna);
    	okvir.setPreferredSize(new Dimension(sirina_okna, visina_okna));
    	okvir.setMinimumSize(new Dimension(sirina_okna, visina_okna));
    	okvir.setMaximumSize(new Dimension(sirina_okna, visina_okna));
    	okvir.getContentPane().setBackground(Color.gray);
    	okvir.pack();
    	okvir.setVisible(true);
    	okvir.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	fullScreen(okvir);
    	}    
    
    public void ustvariZacZaslon() {
    	jZacZaslon = new JPanel(new GridBagLayout());
    	jZacZaslon.setBackground(Color.gray);
    	jZacZaslon.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "UPPO", TitledBorder.CENTER, TitledBorder.TOP, new Font(font,Font.PLAIN,14), Color.black));
    	
    	// zato, da lahko dodam addKeyListener na jpanel za pritisk tipke enter 
    	jZacZaslon.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override 
    		public void keyPressed(KeyEvent e) {
    			System.out.println(e);
    			if (e.getKeyCode() == 10)	// Enter pritisnjen
    				ustvariNovSprozen();
    		}
    	});
    	
    	jZacZaslon.setFocusable(true);
    	jZacZaslon.requestFocusInWindow();
    	
    	okvir.add(jZacZaslon, BorderLayout.CENTER);
    	skrijVseZaslone();
    	jZacZaslon.setVisible(true);
    	okvir.pack();
    	okvir.setVisible(true);
    	//postaviFrameNaSredino(okvir);
    	fullScreen(okvir);
    }

    public void  ustvariPoizZaslon1() {	// za poizvedovanje o naslovih kategorij ter ali so vrednosti posamezne kategorije številske ali besedne
    	jPoizZaslon1 = new JPanel(new GridBagLayout());
    	jPoizZaslon1.setBackground(Color.darkGray);
    	jPoizZaslon1.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), naslov.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(font,Font.PLAIN,14), Color.white));
    	okvir.add(jPoizZaslon1, BorderLayout.CENTER);
    	skrijVseZaslone();
    	jPoizZaslon1.setVisible(true);
    	okvir.pack();;
    	okvir.setVisible(true);
    	//postaviFrameNaSredino(okvir);
    	fullScreen(okvir);

    }
    
    public void ustvariPoizZaslon2() {	// za poizvedovanje o besednih vrednostih kategorij
    	jPoizZaslon2 = new JPanel(new GridBagLayout());
    	jPoizZaslon2.setBackground(Color.darkGray);
    	jPoizZaslon2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), naslov.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(font, Font.PLAIN, 14), Color.white));
    	okvir.add(jPoizZaslon2, BorderLayout.CENTER);
    	skrijVseZaslone();
    	jPoizZaslon2.setVisible(true);
    	okvir.pack();
    	okvir.setVisible(true);
    	//postaviFrameNaSredino(okvir);
    	fullScreen(okvir);

    }
    
    private void ustvariPoizZaslon3() {	// za poizvedovanje o dejanskih vrednosti 
    									// zaslon s tabelo, v katero bo uporabnik vpisoval dejanske parametre
    	jPoizZaslon3 = new Zaslon(Color.darkGray, naslov, okvir);
    	skrijVseZaslone();
    	jPoizZaslon3.pokazi(okvir);
    	//postaviFrameNaSredino(okvir);
    	fullScreen(okvir);

    }
    
    public static void skrijVseZaslone() {
    	try{ jZacZaslon.setVisible(false); } catch (Exception e) {}
    	try{ jPoizZaslon1.setVisible(false); } catch (Exception e) {}
    	try{ jPoizZaslon2.setVisible(false); } catch (Exception e) {}
    	try{ jPoizZaslon3.setVisible(false); } catch (Exception e) {}
    	try{ jPoizZaslon3.skrij(); } catch (Exception e) {}
    	try { sp2.setVisible(false); } catch (Exception e) {}
    	
    	
    }

    public void ustvariDobrodosel() {
    	jDobrodosel = new JLabel("Dobrodošel!");
    	jDobrodosel.setFont(new Font(font, Font.CENTER_BASELINE, 24));
    	jDobrodosel.setForeground(Color.white);
    	//jDobrodosel.setBounds(350, 20, 100, 30);
    	GridBagConstraints c = new GridBagConstraints();
    	//c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	c.gridx = 1;
    	c.gridy = 0;
    	c.weighty = 10;
    	/*c.gridx = 0;
    	c.gridy = 0;*/
    	//c.anchor = GridBagConstraints.PAGE_START;
    	jZacZaslon.add(jDobrodosel, c);
    }
    
    public void ustvariUstvariNovBtn() {
    	jUstvariNovBtn = new JButton("Ustvari nov odloèevalni model");
    	//jUstvariNovBtn.setBounds(50, 350, 80, 40);
    	GridBagConstraints c = new GridBagConstraints();
    	//c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	c.gridx = 0;
    	c.gridy = 1;
    	c.weighty = 200;
    	c.ipady = 30;

    	jZacZaslon.add(jUstvariNovBtn, c);
    	
    	// gumb posluša kdaj bo kliknjen
    	jUstvariNovBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jUstvariNovMousePressed(evt);
            }
        });
    }
    
    public void ustvariPreglejBtn() {
    	jPreglejBtn = new JButton("Preglej dozdajšnje modele");   	
    	jPreglejBtn.setBounds(sirina_okna-50-80, 350, 80, 40);
    	GridBagConstraints c = new GridBagConstraints();
    	//c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	c.gridx = 2;
    	c.gridy = 1;
    	c.weighty = 200;
    	c.ipady = 30;
    	jZacZaslon.add(jPreglejBtn, c);
    }
    
    public void ustvariDodajVrsticoBtn() {
    	jDodajVrstico = new JButton("Dodaj vrstico");
    	jPoizZaslon1.add(Box.createRigidArea(new Dimension(200, 100)));
    	GridBagConstraints c = new GridBagConstraints();

    	//c.gridwidth = 1;
    	//c.gridheight = 1;
    	c.gridx = 2;
    	c.gridy = 2;
    	c.weightx = 0.0;
    	c.weighty = 1.0;
    	c.ipady = 15;
    	c.ipadx = 0;
    	c.insets = new Insets(0,0,0,0);
    	c.anchor = GridBagConstraints.SOUTH;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	jPoizZaslon1.add(jDodajVrstico, c);
    	
    	// gumb posluša kdaj bo kliknjen
    	jDodajVrstico.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
    			dodajVrstico(jTabelaKat);
       		}
    	});
    }
    
    public void ustvariDodajVrstico2Btn() {
    	jDodajVrstico2 = new JButton("Dodaj vrstico");
    	jPoizZaslon3.dodaj(Box.createRigidArea(new Dimension(200, 100)));
    	GridBagConstraints c = new GridBagConstraints();

    	//c.gridwidth = 1;
    	//c.gridheight = 1;
    	c.gridx = 2;
    	c.gridy = 2;
    	c.weightx = 0.0;
    	c.weighty = 1.0;
    	c.ipady = 15;
    	c.ipadx = 0;
    	c.insets = new Insets(0,0,0,0);
    	c.anchor = GridBagConstraints.SOUTH;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	jPoizZaslon3.dodaj(jDodajVrstico2, c);
    	
    	// gumb posluša kdaj bo kliknjen
    	jDodajVrstico2.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
    			jTabelaVnosna.dodajVrstico();
       		}
    	});
    }
    
    public JButton  ustvariPotrdiBtn(JButton g, JPanel z, String gstr) {
    	g = new JButton("Potrdi vnos besed");
    	z.add(Box.createRigidArea(new Dimension(200, 100)));
    	GridBagConstraints c = new GridBagConstraints();

    	//c.gridwidth = 1;
    	//c.gridheight = 1;
    	c.gridx = 3;
    	c.gridy = 2;
    	c.weightx = 0.15;
    	c.weighty = 1.0;
    	c.ipady = 15;
    	c.ipadx = 0;
    	c.insets = new Insets(0,0,0,0);
    	c.anchor = GridBagConstraints.SOUTHWEST;
    	//c.fill = GridBagConstraints.HORIZONTAL;
    	z.add(g, c);
    	
    	// gumb posluša kdaj bo kliknjen
    	g.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
    			switch(gstr) {
    				case("jPotrdiBtn"):
    					jPotrdiBtnMousePressed();
    					break;
    				case("jPotrdi2Btn"):
    					jPotrdi2BtnMousePressed();
    					break;
    			}
    		}
    	});
    	
    	return g;
    }
    
    /* ODSTRANJENA METODA 
    public void ustvariPotrdi2Btn() {	// to metodo bo možno odstraniti, ker je lahko samo en jPotrdiBtn
    	jPotrdi2Btn = new JButton("Potrdi vnos besed");
    	jPoizZaslon2.add(Box.createRigidArea(new Dimension(200, 100)));
    	GridBagConstraints c = new GridBagConstraints();

    	//c.gridwidth = 1;
    	//c.gridheight = 1;
    	c.gridx = 1;
    	c.gridy = 1;
    	c.weightx = 0.1;
    	c.weighty = 1.0;
    	c.ipady = 15;
    	c.ipadx = 0;
    	c.insets = new Insets(0,0,0,0);
    	c.anchor = GridBagConstraints.CENTER;
    	//c.fill = GridBagConstraints.BOTH;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	jPoizZaslon2.add(jPotrdi2Btn, c);
    	// gumb posluša kdaj bo kliknjen
    	jPotrdi2Btn.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
    			jPotrdi2BtnMousePressed(evt);
    		}
    	});
    }*/
    
    public void ustvariPotrdi3Btn() {
    	jPotrdi3Btn = new Gumb("Vnesel sem podatke");
    	jPotrdi3Btn.pozicionirajVneselSem(jPoizZaslon3);
    }
    
    public void ustvariPrekliciBtn(JPanel zaslon) {
    	jPrekliciBtn = new JButton("Preklièi");
    	GridBagConstraints c = new GridBagConstraints();

    	c.gridwidth = 1;
    	//c.gridheight = 1;
    	c.gridx = 0;
    	if (zaslon == jPoizZaslon2) c.gridx = 2;
    	c.gridy = 2;
    	//c.weightx = 0.1;
    	c.weighty = 1.0;
    	c.ipady = 15;
    	c.ipadx = 60;
    	c.insets = new Insets(0,0,0,0);
    	c.anchor = GridBagConstraints.SOUTHEAST;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	zaslon.add(jPrekliciBtn, c);
    	
    	// gumb posluša kdaj bo kliknjen
    	jPrekliciBtn.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
				preklici1();
    		}
    	});

    }
    
    private JLabel ustvariNavodilo(JLabel n, Object zaslon) {
    	n = new JLabel();

    	int sirina = 1250;
    	int visina = 330;
    	n.setMinimumSize(new Dimension(sirina, visina));
    	n.setMaximumSize(new Dimension(sirina, visina));
    	n.setPreferredSize(new Dimension(sirina, visina));
    	n.setFont(new Font(font, Font.CENTER_BASELINE, 17));
    	n.setForeground(Color.white);
    	GridBagConstraints c = new GridBagConstraints();
    	//c.fill = GridBagConstraints.HORIZONTAL;
    	c.anchor = GridBagConstraints.NORTH;
    	c.gridwidth = 3;
    	c.gridheight = 1;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.weighty = 0.5;
    	
    	((Container) zaslon).add(n, c);
    	
    	return n;
    	
    }
    
    private JLabel ustvariNapakaLbl(JLabel n, Object zaslon) {
    	n = new JLabel();

    	int sirina = 300;
    	int visina = 50;
    	n.setMinimumSize(new Dimension(sirina, visina));
    	n.setMaximumSize(new Dimension(sirina, visina));
    	n.setPreferredSize(new Dimension(sirina, visina));
    	n.setFont(new Font(font, Font.CENTER_BASELINE, 18));
    	n.setForeground(Color.pink);
    	GridBagConstraints c = new GridBagConstraints();
    	//c.fill = GridBagConstraints.HORIZONTAL;
    	//c.anchor = GridBagConstraints.SOUTH;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	c.gridx = 1;
    	c.gridy = 2;
    	c.weighty = 0.5;
    	
    	((Container) zaslon).add(n, c);
    	
    	return n;
    	
    }
    
    private void dodajBesediloNavodilu1(){
    	jNavodilo1.setText(
    			"<html>"
    			+ "Dobrodošel na prvi od treh postojank! :)"
    			+ "<br/>Najprej mora program izvedeti nekaj o kategorijah po katerih primerjaš med sabo odloèitve (npr. cena, barva ...)."
    			+ "<br/>Potrebno je vnesti ime vsake kategorije, kakšnega tipa so podatki znotraj posamezne kategorije in kako pomembna je zate neka kategorija."
    			+ "<br/>Nasvet: Priporoèeno je, da je prva vrstica naslednje oblike: kategorija = ime odloèitve (npr. \"ime telefona\", \"destinacija\" ipd.); vrsta = besedni; prioriteta = 1."
    			+ "<br/>"
    			+ "<br/>V prvi stolpec vnašaj naslove kategorij po katerih primerjaš možne odloèitve med sabo. Npr.: ime, cena, barva ..."
    			+ "<br/>"
    			+ "<br/>V drugem izberi s kakšno vrsto podatka je kategorija predstavljena. Cena npr. je navadno 'številski (tem manjše, tem bolje)'."
    			+ "<br/>Besedna podatka sta npr. ime in barva."
    			+ "<br/>"
    			+ "<br/>V zadnji stolpec vnesi naravno število (1,2,3...), ki bo oznaèevalo 'pomembnost' doloèene kategorije za tvojo konèno odloèitev. "
    			+ "<br/>Ta števila se LAHKO ponavljajo od kategorije do kategorije, èe predstavlja veè kategorij enako 'pomembnost'."
    			+ "<br/>Èe ti je npr. barva manj pomembna od cene, vpiši '1' zraven kategorije cena in '2' zraven kategorije barva."
    			+ "<br/>"
//    			+ "<br/>Ko vneseš vse kategorije, klikni 'Vnos besed konèan' na dnu zaslona."
    			+ "</html>");
    }
    
    private void dodajBesediloNavodilu2(){
    	jNavodilo2.setText(
    			"<html>"
    			+ "Program mora o besednih kategorijah vedeti nekoliko veè kot o številskih. Zato bo tukaj potrebno vnesti vse možne besede, ki bodo v tvojem primeru opisovale razliène možnosti."
    			+ "<br/>Za vsako kategorijo vnesi možne vrednosti (besede) po zaželenosti. Pod enako zaželenost lahko vneseš tudi veè razliènih vrednosti. V tem primeru posamezne besedne vrednosti v isti celici med sabo loèi z vejico. Npr., èe v kategoriji barva pod zaželjenost 'zelo dobro' pojmujemo veè barv, jih zapišemo takole: \"svetlo rdeè, moder, temno zelen\"."
    			+ "<br/>V primeru, ko so vsi podatki pri neki kategoriji enako pomembni - npr. ime produkta, vpiši vse besede v eno celico - vseeno katero."
    			+ "<br/>Celice lahko pušèaš prazne."
    			+ "<br/>"
    			+ "<br/>Pozor! Na dnu ekrana je mogoèe drsnik. Èe je, ga uporabi. ;)"
    			+ "<br/>"
    			//+ "<br/>Ko vneseš vse besede, klikni 'Vnos besed konèan' na dnu zaslona."
    			+ "</html>");
    }
    
    private void dodajBesediloNavodilu3(){
    	jNavodilo3.setText(
    			"<html>"
    			+ "<br/>Sedaj potrebuje program le še podatke za primerjavo. :)"
    			+ "<br/>"
    			+ "<br/>V vsako vrsto vnesi vse podatke za posamezno odloèitveno možnost."
    			+ "<br/>Celice lahko pušèaš prazne, vendar je to, zaradi uèinkovitosti delovanja programa, skrajno nezaželjeno."
    			+ "<br/>Decimalna števila je potrebno loèevati s piko.\r\n"
    			//+ "<br/>Ko vneseš vse podatke, klikni 'Vnos besed konèan' na dnu zaslona."
    			+ "</html>");
    }
    
    private void preveriAliJeKateraBesedna() {
    	if (st_bes_kat == 0)
    		jPotrdi2BtnMousePressed();
    }
    
    public void ponastaviTabelaKat() {
    	// vstavi podatke v tabelo
    	jTabelaKat = new JTable(prazna, glava);	
    	jTabelaKat.setModel(new DefaultTableModel(prazna, glava));
    	jTabelaKat = nastaviPrivzeteVrednosi(jTabelaKat);
    	
    	nastaviModelTabKat();
    	dodajTabeliComboBox(jTabelaKat);
    	dodajTabeliKatKeyListener(jTabelaKat);

    	
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
    	c.anchor = GridBagConstraints.CENTER;
    	
    	JScrollPane scrollpane = new JScrollPane(jTabelaKat);
    	scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "".toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(font, Font.PLAIN, 14), Color.darkGray));
    	jPoizZaslon1.add(scrollpane, c);
    	okvir.add(jPoizZaslon1);
    	jTabelaKat.setVisible(true);
    	okvir.pack();
    	okvir.setVisible(true);
    	fullScreen(okvir);
    }
    
    private void nastaviModelTabKat() {
    	jTabelaKat.setModel(new DefaultTableModel(prazna, glava) {
	 		Class[] types = tipiTabKat();
	 		
	 		public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
	 		
 			});
    }
    
    private Class [] tipiTabKat() {
		Class[] types = new Class[glava.length];
		
		// doloèi tipe stolpcev tabele jTabelaKat
		types[0] = String.class;
		types[1] = String.class;
		types[2] = Integer.class;
		
		
		return types;
	}
        
    static void ustvariVnosnaTabela() {    	
    	jTabelaVnosna = new Tabela("vnosna", st_kat, naslovi_kategorij);
    	okvir = jTabelaVnosna.pozicioniraj(okvir, jPoizZaslon3);
    	skrijVseZaslone();
    	okvir = jTabelaVnosna.pokazi(okvir, jPoizZaslon3);
    }
    
    public void dodajTabeliComboBox(JTable t) {
    	// comboBox za doloèanje ali vsebuje podatek številsko ali besedno vrednost
    	TableColumn stolpec = t.getColumnModel().getColumn(1);
    	JComboBox comboBox = new JComboBox();
    	comboBox.addItem(st_cim_vecje);
    	comboBox.addItem(st_cim_manjse);
    	comboBox.addItem("besedni");
    	stolpec.setCellEditor(new DefaultCellEditor(comboBox));    	
    }
    
    private void dodajTabeliKatKeyListener(JTable t) {
    	
    	t.addKeyListener(new KeyListener() {
    		@Override
    		public void keyTyped(KeyEvent e) {}
    		
    		@Override
    		public void keyReleased(KeyEvent e) {}
    		
    		@Override
    		public void keyPressed(KeyEvent e) {
    			/* 
    			 * trenutno onemogoèeno premikanje po tabeli z enter;
    			 * uporabnik naj se premika s klikom miške
    			 
    			if(e.getKeyCode() == KeyEvent.VK_ENTER){
    				e.consume();
    		     	dodajVrstico(t);
    				tabuliraj(t);    
    			 }*/
			}
    	});
    }
    
    private void dodajVrstico(JTable t) {
    	
    	boolean vsi_podatki_vneseni = preveriTabelaKat(t);

		if (vsi_podatki_vneseni) {
			DefaultTableModel model = (DefaultTableModel) t.getModel();
			model.addRow(new String[3]);
			t.setValueAt("izberi", t.getRowCount()-1, 1);
			jNapaka1.setText("");
		}
		else {
			jNapaka1.setText("Vsi podatki še niso vnešeni.");
		}
    }
    
    private boolean preveriTabelaKat(JTable t) {
    	
    	// fokus je potrebno prestaviti iz celice, èe želiš, da se vrednost v njej shrani
		t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()+1, false, false);
		t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());
		t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()-1, false, false);
		t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());

		boolean na_koncu_vrstice = ( t.getSelectedColumn() == t.getColumnCount()-1 );
		boolean v_zadnji_vrstici = ( t.getSelectedRow() == t.getRowCount()-1 );
		Object vrednost_celice = t.getValueAt(t.getRowCount()-1, 0);
		boolean besedilo_ni_prazno = ( 
				( vrednost_celice != null ) &&
				( (String)vrednost_celice != "" ) &&
				( ((String)vrednost_celice).split(" +").length > 0 )
				);
		boolean vrsta_oznacena = false;
		try {
			vrsta_oznacena = t.getValueAt(t.getRowCount()-1, 1) != "izberi";
		} catch (NullPointerException e) {}
		boolean prioriteta_vnesena = false;
		try {
			prioriteta_vnesena = (t.getValueAt(t.getRowCount()-1, 2) != null && (Integer)t.getValueAt(t.getRowCount()-1, 2)  > 0);
		} catch (NullPointerException e) {}		
		//System.out.println(na_koncu_vrstice + " " + v_zadnji_vrstici + " " + vrednost_celice + " " + besedilo_ni_prazno + vrsta_oznacena);
		
		return (besedilo_ni_prazno && vrsta_oznacena && prioriteta_vnesena);
    }
    
    private void tabuliraj(JTable t) {

		int vrsta;
		int stolpec;
		boolean na_koncu_vrstice = ( t.getSelectedColumn() == t.getColumnCount() );
		
		if (na_koncu_vrstice) {
			t.changeSelection(t.getSelectedRow()+1, 0, false, false);
			t.editCellAt(t.getSelectedRow(), 0);
		}
		else {
			t.changeSelection(t.getSelectedRow(), t.getSelectedColumn()+1, false, false);
			t.editCellAt(t.getSelectedRow(), t.getSelectedColumn());
		}
		
    }

    private void ponastaviTabelaBesed(int j) {
    	String glava1[] = {"Zaželjenost", "Besede"};
    	String indeksi_in_prazna[][] = new String[st_bes_nivojev][2];
    	
    	// vnašanje subjektivnih pripomb k besednim vrednostim
    	indeksi_in_prazna[0][0] = "odlièno";
    	indeksi_in_prazna[1][0] = "zelo dobro";
    	indeksi_in_prazna[2][0] = "dobro";
    	indeksi_in_prazna[3][0] = "nevtralno";
    	indeksi_in_prazna[4][0] = "slabo";
    	indeksi_in_prazna[5][0] = "zelo slabo";
    	indeksi_in_prazna[6][0] = "porazno";
    	
		jTabelaBes[j] = new JTable(indeksi_in_prazna, glava1);
    	jTabelaKat.setModel(new DefaultTableModel(indeksi_in_prazna, glava1));
		jTabelaBes[j] = nastaviPrivzeteVrednosi(jTabelaBes[j]);
    	
    }
    
    private JTable nastaviPrivzeteVrednosi(JTable t) {
    	t.setRowHeight(30);
    	t.setRowMargin(5);
    	t.setFont(new Font(font, Font.PLAIN, 14));
    	t.getTableHeader().setReorderingAllowed(false);	// onemogoèi, da bi uporabnik premikal stolpce tabele
    	t.changeSelection(0, 0, false, false);
    	// tekst v celicah naj bo na sredini
    	DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    	centerRenderer.setHorizontalAlignment( JLabel.CENTER );
    	t.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
    	
    	return t;
    }
    
    public void pokaziTabela(JTable t, JPanel z, String naslov_tabele, int i) {
    	/*
    	t.setBounds(50, 50, 400, 300);
    	JScrollPane scrollpane = new JScrollPane(t);
    	scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), naslov_tabele.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(font, Font.PLAIN, 14), Color.darkGray));
    	z.add(scrollpane);
    	okvir.add(z);
    	t.setVisible(true);
    	okvir.pack();
    	okvir.setVisible(true);    
    	*/
    	
    	/*
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	c.gridx = 1;
    	c.gridy = 0;
    	c.weightx= 0.1;
    	c.weighty = 1.0;
    	c.ipadx = 400;
    	c.ipady = 400;
    	c.fill = GridBagConstraints.BOTH;
    	
    	t.setBounds(50, 50, 400, 300);
    	JScrollPane scrollpane = new JScrollPane(t);
    	scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), naslov_tabele.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(font, Font.PLAIN, 14), Color.darkGray));
    	z.add(scrollpane, c);
    	okvir.add(z);
    	t.setVisible(true);
    	okvir.pack();
    	okvir.setVisible(true);
    	*/
    	
    	GridBagConstraints c = new GridBagConstraints();
    	
    	c.gridwidth = 3;
    	if (z == jPoizZaslon2) c.gridwidth = 1;	// èe je tabela za vnos besednih vrednosti
    	c.gridheight = 1;
    	c.gridx = i;
    	c.gridy = 0;
    	c.weightx= 0.1;
    	c.weighty = 1.0;
    	//c.ipadx = 400;
    	//c.ipady = 400;
    	//c.fill = GridBagConstraints.BOTH;
    	
    	c.anchor = GridBagConstraints.NORTHWEST;
    	c.insets =  new Insets(0,5,0,5);
        c.gridy=0;
        c.gridx=i+1;
        z.add(t.getTableHeader(),c);
        c.gridy=1;
        z.add(t,c);
        c.gridx=i+1;
        
       	JScrollPane scrollpane = new JScrollPane(t);
    	scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), naslov_tabele.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, new Font(font, Font.PLAIN, 14), Color.darkGray));
    	z.add(scrollpane, c);
 
    }
    
    public void postaviFrameNaSredino(JFrame object){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - object.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - object.getHeight()) / 2);        
        object.setLocation(x, y);
    }
    
    public static void fullScreen(JFrame frame) {
    	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    public void vnosBesed() {	// poizvedba o besednih vrednosti besednih kategorij
        ustvariPoizZaslon2();
    	ustvariTabeleZaBesedne();
    	initBesede(); 
    	jPotrdi2Btn = ustvariPotrdiBtn(jPotrdi2Btn, jPoizZaslon2, "jPotrdi2Btn");
    	preveriAliJeKateraBesedna();
    	ustvariPrekliciBtn(jPoizZaslon2);
    	jNavodilo2 = ustvariNavodilo(jNavodilo2, jPoizZaslon2);
    	dodajBesediloNavodilu2();
    }	
    
    public void ustvariTabeleZaBesedne() {	
    	// pregleda tabelo kategorije[][] in, èe je kategorija besednega tipa, 
    	// ustvari zanjo tabelo za poizvedbo o besednih kategorijah
    	for (int i = 0; i < kategorije.length; i++) {
    		if (kategorije[i][0] == null)
    			continue;
    		if (kategorije[i][1].equals("besedni")) 
            	st_bes_kat++;
    	}
    	
    	// deklariraj tabelo besed
    	jTabelaBes = new JTable[st_bes_kat];
    	    	
    	int j = 0;	// indeks tabele besed
    	for (int i = 0; i < kategorije.length; i++) {	// indeks kategorije
    		if (kategorije[i][0] == null)
    			continue;
    		if (kategorije[i][1].equals("besedni")) {
    			ponastaviTabelaBesed(j);
    			pokaziTabela(jTabelaBes[j], jPoizZaslon2, (String)kategorije[i][0], j);
    			j++;
    		}
    	}
    	okvir.add(sp2 = new JScrollPane(jPoizZaslon2));
    }
    
    public void preklici1() {
    	skrijVseZaslone();
    	ustvariZacZaslon();
    	ustvariDobrodosel();
    	ustvariUstvariNovBtn();
    	ustvariPreglejBtn();
    }
        
    private void jUstvariNovMousePressed(java.awt.event.MouseEvent evt) {                                         
        ustvariNovSprozen();
    } 
       
    public void jPotrdiBtnMousePressed() {
    	if (preveriTabelaKat(jTabelaKat)) {
    		jNapaka1.setText("");
    		shraniTabelaKat();
    		vnosBesed();	// poizvedba o besednih vrednostih

    	}else {
    		System.out.println("preveriTabelaKat == false");			
    		jNapaka1.setText("Vsi podatki še niso vnešeni.");
    	}
    }
    
    public void jPotrdi2BtnMousePressed() {
    	shraniBesede();
    	izpisiBesede();   	
    	ustvariPoizZaslon3();
    	ustvariVnosnaTabela();
    	ustvariPotrdi3Btn();
    	ustvariPrekliciBtn(jPoizZaslon3.zaslon());
    	ustvariDodajVrstico2Btn();
    	jNapaka2 = ustvariNapakaLbl(jNapaka2, jPoizZaslon3.zaslon());
    	jNavodilo3 = ustvariNavodilo(jNavodilo3, jPoizZaslon3.zaslon());
    	dodajBesediloNavodilu3();
	}
    
    private void shraniBesede() {
    	System.out.println("st_bes_kat = " + st_bes_kat);
    	for (int i = 0; i < st_bes_kat; i++) {	// i = indeks kategorije
        	jTabelaBes[i].editCellAt(0,0);	// fokus kurzorja je treba premakniti strani iz aktivne celice, èe želiš, da se vsebina celice shrani
        	
        	for(int j = 1; j < st_bes_nivojev+1; j++) {	// j = indeks moèi/teže besed v kategoriji
    			try {
    				besede[i][st_bes_nivojev+1-j] = ((String) jTabelaBes[i].getValueAt(j-1, 1)).trim().split("[,]+");	//split("[, ]+"); --> èe bi želel še po presledkih
    				
    				for (int k = 0; k < besede[i][st_bes_nivojev+1-j].length; k++)
    					besede[i][st_bes_nivojev+1-j][k] = besede[i][st_bes_nivojev+1-j][k].trim();

    			} catch (NullPointerException e) {}	// èe je vrednost celice Null
			}
		}
    }    
        
    private void izpisiBesede() {
		// izpis besednih vrednosti
		System.out.println();
    	for (int i = 0; i < st_bes_kat; i++) {	// i = indeks kategorije
			System.out.println(besede[i][0][0]);	// izpiše ime kategorije
			
			for (int j = 1; j < st_bes_nivojev+1; j++) {		// j = indeks moèi/teže besed v kategoriji
				System.out.print(j);
				try {	// drugaèe pride do napake, ko preglejuje dolžino tabele
	    			for (int k = 0; k < besede[i][j].length; k++) {	// k = beseda z moèjo j
	    				System.out.print(besede[i][j][k] + "; ");
	    			} 
				} catch (NullPointerException e) {	// èe v ni vrednosti (besede) za težo j
					System.out.print("; ");	
				}
				System.out.println();
			}
			System.out.println();
		}    	
    }
      
    public void ustvariNovSprozen() {
    	try {
    		ponastaviSpremenljivke();
	    	kaksenNaslov();	// uporabink vpiše naslov odloèevalnega sistema
	    	if (naslov == null)	// èe ni vpisal naslova al pa èe je stisnil preklièi... nisem veè ziher :')
	    		return;
	    	initPraznaKategorije();
	        ustvariPoizZaslon1();   
	        ponastaviTabelaKat();
	        //pokaziTabela(jTabelaKat, jPoizZaslon1, ""); --> zaèasno je to v metodi ponastaviTabelaKat
	        jPotrdiBtn = ustvariPotrdiBtn(jPotrdiBtn, jPoizZaslon1, "jPotrdiBtn");
	        ustvariDodajVrsticoBtn();
	        ustvariPrekliciBtn(jPoizZaslon1);
	        jNavodilo1 = ustvariNavodilo(jNavodilo1, jPoizZaslon1);
	        dodajBesediloNavodilu1();
	        jNapaka1 = ustvariNapakaLbl(jNapaka1, jPoizZaslon1);
    	} catch (Exception e) { System.out.println("Napaka: " + e); }
    }
        
    public void shraniTabelaKat() {
    	jTabelaKat.editCellAt(0,0);	// fokus kurzorja je treba premakniti strani iz aktivne celice, èe želiš, da se vsebina celice shrani

    	for (int i = 0; i < jTabelaKat.getRowCount(); i++) {
    		naslovi_kategorij[i] = (String) jTabelaKat.getValueAt(i, 0);
    		for (int j = 0; j < jTabelaKat.getColumnCount(); j++) { 
    			kategorije[i][j]  = jTabelaKat.getValueAt(i, j);
    			
    		}
    		if (naslovi_kategorij[i] != null) 
    			st_kat++;
    	}
    	
    	for (int i = 0; i < jTabelaKat.getRowCount(); i++) {
    		for (int j = 0; j < jTabelaKat.getColumnCount(); j++)
    			System.out.print(kategorije[i][j] + " ");
    		System.out.println();
    	}
    	
    }
    
    public void kaksenNaslov() {
    	do {
    		naslov = JOptionPane.showInputDialog(okvir, "Naslov odloèevalnega sistema: ");
    	} while (naslov != null && naslov.equals(""));	// naslov == null ==> kliknjen cancel
    	
    	if (naslov == null) {
    		preklici1();
    		return;
    	}
    }
    
    public void initPraznaKategorije() {
		// poizvedba o številu kategorij odstranjena - max št. (int maxst) kategorij je trenutno 100
    	int maxst = 100;
    	
    	//prazna = new String[maxst][glava.length];
    	prazna = new String[1][glava.length];
    	for(String[] t : prazna) {
			t[1] = "izberi";
		}
		kategorije = new Object[maxst][glava.length];
		naslovi_kategorij = new String[maxst];
    }
    
    public void initBesede() {
    	besede = new String [st_bes_kat][12][];
    	vstaviNaslove();
    }    

    private void vstaviNaslove() {
    	// vstavi ustrezne naslove besednih kategorij na indekse 0
		int j = 0;
    	for (int i = 0; i < kategorije.length; i++) {
    		if (kategorije[i][0] == null)	
    			continue;
    		if (kategorije[i][1].equals("besedni")) {
    			String [] naslov = {(String)kategorije[i][0]};
    			besede[j++][0] = naslov;
    		}
    	}
    }
    
    public static void konecVnosaPrimerkov() {
    	porocilo = new UstvariPorocilo(st_kat, jTabelaVnosna);
    	System.out.println(porocilo.toString());
    }
    
    public static String pridobiNaslov() {
    	return naslov;
    }
    
    public static JFrame pridobiOkvir() {
    	return okvir;
    }
    
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new uppo_003().setVisible(false);
            }
        });
	}
	

    // swing objekti
    public static JFrame okvir;
	private static JPanel jZacZaslon;
	private static JPanel jPoizZaslon1;	// zaslon za poizvedbo o kategorijah
	private static JPanel jPoizZaslon2;	// zaslon za poizvedbo besedah besednih kategorij
	private static JScrollPane sp2;		// na njem je jPoizZaslon2
    private static Zaslon jPoizZaslon3;
	private static JLabel jDobrodosel;
	private static JLabel jNavodilo1;
	private static JLabel jNavodilo2;
	private static JLabel jNavodilo3;
	private static JLabel jNapaka1;
	public static JLabel jNapaka2;
	private static JButton jUstvariNovBtn;
	private static JButton jPreglejBtn;
	private static JButton jDodajVrstico;
	private static JButton jDodajVrstico2;
	private static JButton jPrekliciBtn;
	private static JButton jPotrdiBtn;
	private static JButton jPotrdi2Btn;	//ob priliki odstrani in nadomesti z jPotrdiBtn 
	private static Gumb jPotrdi3Btn;
	private static JTable jTabelaKat;		// tabela za uporabnikov vnos kategorij
	private static JTable jTabelaBes[];	// tabela za uporabnikov vnos besednih vrednosti 
	public static Tabela jTabelaVnosna;	// tabela za uporabnikov vnos parametrov (npr.: lastnosti produkta)
	public static UstvariPorocilo porocilo;
	
    // globalne spremenljivke - kasneje gredo v BP
	private static String naslov;	// naslov odloèevalnega sistema
	public static String font;
	private static String glava[];
	private static String prazna[][];    
	public static Object kategorije[][];
	public static String naslovi_kategorij[];
	public static String besede[][][];	// [st_bes_kat][st_bes_nivojev+1][] - [naziv kategorije] [moè/teža] [besede]
									// na indeksu 0 druge dimenzije je naziv kategorije, na naslednjih desetih  
									// pa besedne vrednosti kategorije razvršèene padajoèe po teži (10->0)
	public static String st_cim_vecje = "številski (èim veèje, tem bolje)";
	public static String st_cim_manjse = "številski (èim manjše, tem bolje)";
	private static int st_kat;
	private static int st_bes_kat;
    private static int sirina_okna;
    private static int visina_okna;
    public static int st_bes_nivojev;	//najprej blo 10, potem zmanjsal na 7
    

}
