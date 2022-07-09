package uppo;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;

public class UstvariPorocilo {

	private Tabela t;	// uporabnikova vnosna tabela
	private Tabela tr;	// tabela, ki prikazuje poroèilo (rezultate)
	private Zaslon z;	// zaslon na katerem prikažemo poroèilo (rezultate)
	private JFrame o;
	private Object [][] rezultati;
	private Double [] r;	// razmerja
	private Object [][] vsebina;
	private String [] glava;
	private Double [] Zf;	// dejanska zaloga vrednosti (Zf)
	private Double [][] Zf_st;	// Zf z dejanskimi vrednostmi
										// [i_kat][min, max, d]
	private int stk;	 // število kategorij
	public int id;	// število primerkov
	private int max_tocke;	// število toèk, ki jih doseže najboljši primerek
	

	
	public UstvariPorocilo(int st_kat, Tabela tab) {
		ponastavi(st_kat, tab);
		//stevilskeNaSkupnoZf();
		//rezultati = tabelaRezultatov();
		
		r = tabelaRazmerij();		// tabela primerljivih koliènikov
		zaokroziRazmerja();
		rezultati = tabelaRezultatov();
		
		ustvariTabeloRezultatov();
	}
	
	private void ponastavi(int st_kat, Tabela tab) {
		t = tab;
		stk = st_kat;
		id = t.pridobiID();	 	// da vemo koliko vrstic je 
		max_tocke = 1000;
		t.prestaviFokusCelice();		// zato, da se shrani nazadnje zapisana vrednost
		
		Zf = new Double[] {1.0, 10.0, 1.0}; 
							// {min, max, d}; d = diferenca / korak
		Zf_st = new Double[stk][3];	// Zf z dejanskimi vrednostmi
						// samo indeksi številskih kategorij bodo imeli vrednost
		
		o = uppo_003.pridobiOkvir();
	}
	
	// številske vrednosti bomo stisnili/razširili na skupno zalogo vrednosti
	// tole ne vem èe dela glih ok..?
	private void stevilskeNaSkupnoZf1() {
		Double min, max, d;
		for (int i = 1; i < stk+1; i++) {	// i=1, ker stolpca ID ne smemo stiskat/razširjat
			if (	t.getValueAt(0, i).getClass() != Double.class)	
				continue;
			min = minDouble(i);
			max = maxDouble(i);
			d = (max-min) / (Zf[1]-1);		// diferenca
			Zf_st[i-1][0] = min;
			Zf_st[i-1][1] = max;
			Zf_st[i-1][2] = d;	
		}
	}
	
	// številske vrednosti bomo stisnili/razširili na skupno zalogo vrednosti
	private void stevilskeNaSkupnoZf2() {
		Double k;
		boolean cim_vecje;
		
		// za vsako kategorijo
		for (int i = 1; i < stk+1; i++){		// i=1, ker stolpca ID ne smemo stiskat/razširjat
			if (t.getValueAt(0,i).getClass() != Double.class)
				continue;
			
			cim_vecje = (uppo_003.kategorije[i-1][1].equals(uppo_003.st_cim_vecje)); 
			if (cim_vecje)
				k = uppo_003.st_bes_nivojev / maxDouble(i);
			else
				k = uppo_003.st_bes_nivojev / (1.0/minDouble(i));
			
			Zf_st[i-1][2] = k;
		}
	}
	
	private Double maxDouble(int indx_kat) {
		Double max = (Double) t.getValueAt(0, indx_kat);
		Double st;
		for (int i = 1; i < id; i++) {
			if (t.getValueAt(i, indx_kat) != null) {
				st = (Double) t.getValueAt(i, indx_kat);
				if (st > max)
					max = st;
			}
		}
		return max;
	}
	
	private Double minDouble(int indx_kat) {
		Double min = (Double) t.getValueAt(0, indx_kat);
		Double st;
		for (int i = 1; i < id; i++) {
			if (t.getValueAt(i, indx_kat) != null) {
				st = (Double) t.getValueAt(i, indx_kat);
				if (st < min)
					min = st;
			}
		}
		return min;
	}
	
	private Object [][] tabelaRezultatov(){
		Object [][] tab = new Object[id][stk+3];
		/* vrstica vsebuje n-terko, stolpec pa atribut
		 * tab { { doseženo mesto (int) , toèke/score (int) , ID (int) , atributi ki jih je doloèil uporabnik sam} * id (št. primerkov) }
		 */	
		
		// za vsako vrstico vpiši v tabelo VSE* podatke posamezne vrstice, razen doseženega mesta
		// *vsi podatki = {doseženo mesto, toèke, id, atributi ki jih je doloèil uporabnik}
		for (int i = 0; i < id; i++) {
			
			if (r[i] != null) {
				tab[i][1] = r[i];	// toèke = razmerja[i]
				for (int j = 2; j < stk+3; j++) {
					tab[i][j] = t.getValueAt(i, j-2);
				}
			}	
			else {
				tab[i][1] = "null";
				for (int j = 2; j < stk+3; j++) {
					tab[i][j] = "null";
				}
			}
			
		}
		
		// uredi primerke padajoèe po toèkah in jim dodaj vrednost atributa "doseženo mesto" (na indeks 0)
		// tab = kindaBubbleSort(tab); --> manj primeren v tem primeru, ker ne morem sproti pisati doseženega mesta
		tab = urediZIzbiranjem(tab);	
		
		return tab;

	}
	
	private Object [][] urediZIzbiranjem(Object [][] tab){
		Object [] tab_zac;
		Double max;
		int i_max;
		
		for (int i = 0; i < tab.length-1; i++) {
			while (tab[i][1] == null)
				i++;
			max = (Double)tab[i][1];
			i_max = i;
			
			// poišèi najveèje število toèk v neurejenem delu
			for (int j = i+1; j < tab.length; j++) {
				if (tab[j][1] != null && (Double)tab[j][1] > max) {
					max = (Double)tab[j][1];
					i_max = j;
				}
			}
			
			// zamenjaj tabeli, èe potrebno
			if (i_max != i) {
				tab_zac = tab[i];
				tab[i] = tab[i_max];
				tab[i_max] = tab_zac;
			}
			
			// zapiši pridobljeno mesto
			tab[i][0] = i+1;
		}
		// zapiši še zadnje mesto
		tab[tab.length-1][0] = tab.length;
		
		return tab;
	}
	
	/*
	private Object[][] kindaBubbleSort(Object [][] tab){
		
		for (int i = 0; i < tab.length-1; i++) {
			for (int j = 0; j < tab.length-1; j++) {
				if (tab[j][1] == null || tab[j+1][1] == null)
					tab = zamenjajNull(tab, j);
				else if ((Double)tab[j][1] < (Double)tab[j+1][1]) {
					tab = zamenjajZNaslednjim(tab, j);
				}
			}		
		}
		
		return tab;
	}

	/*
	 *Te dve metodi nista v redu.
	 * Namesto, da bi zamenjali celi tabeli, zamenjata le vrednosti toèk.
	 * Pustil sem ju za primer, èe bi potreboval to idejo kdaj kasneje.
	 
	private Object [][] zamenjajNull(Object [][] tab, int indx) {
		Object zacasen = tab[indx+1][1];
		tab[indx+1][1] = tab[indx][1];
		tab[indx][1] = zacasen;
		return tab;
	}
	
	private Object [][] zamenjajZNaslednjim(Object[][] tab, int indx){
		Double a = (Double)tab[indx][1];
		Double b = (Double)tab[indx+1][1];
		
		a = a + b;
		b = a - b; 
		a = a - b;
		
		tab[indx][1] = a;
		tab[indx+1][1] = b;
		
		return tab;
	}
	*/
	
	private Double[] tabelaRazmerij() {
		Double tab [] = new Double[id];
		
		// poraèunaj razmerja za vsak primerek (vrstico) posebej
		for (int i = 0; i < id; i++) {
			tab[i] = poracunajRazmerja(i);
		}
		
		
		// vsa razmerja spremeni v toèke - najboljše ima max_tocke število toèk
		Double max = najdiMax(tab);
		Double k = max_tocke / max;	// koliènik s katerim pomnožimo vse vrednosti, zato da dobimo "lepe" rezultate (do 1000)
		
		// vsa razmerja razširi s k (torej vsa razmerja spremeni v toèke)
		for (int i = 0; i < tab.length; i++)
			if (tab[i] != null)
				tab[i] *= k;
		
		return tab;
	}
	
	private void zaokroziRazmerja() {
		/*for (Double d : r)
			d = Math.round(d*10)/10.0;
	*/
		for (int i = 0; i < r.length; i++)
			if (r[i] != null)
				r[i] = Math.round(r[i]*10)/10.0;
	}
	
	// poraèunaj toèke za vsak primerek (vrstico) posebej, shrani v tabelo primerek[] in prepiši vanjo še celotno n-terko
	// ni v uporabi - zamenjal s poracunajRazmerja
	private Object[] poracunaj(int vrsta) {
		Object [] primerek = new Object[stk+3];
		Double tocke = 0.0;
		int i_bes = -1;	// indeks besedne kategorije za tabelo uppo_003.besede[][][]
		
		// pretvori vrednost vsake celice posebej v toèke in prištej k Double tocke
		for (int i = 1; i < stk+1; i++) {
			Object atr = t.getValueAt(vrsta,  i);
			if (atr == null)
				continue;
			else if (atr.getClass() == String.class) 
				tocke += pretvoriVTocke((String)atr, ++i_bes);
			else if (atr.getClass() == Double.class)
				tocke += pretvoriVTocke((Double)atr, i-1);
			else
				System.out.println("Napaka v poracunaj() !");			
		}
		
		primerek[1] = tocke;
		System.out.println("tocke za " + vrsta + ". vrsto: " + tocke);
		
		// prepiše še ostale vrednosti... 
		primerek = prepisiOstaleVrednosti(primerek, vrsta);
		
		return primerek;
	}
	
	private Double pretvoriVTocke(String s, int i_bes) {
		Double tocke = -1.0;
		
		for (int i = 1; i < uppo_003.st_bes_nivojev+1; i++) {	// 1 <= i < uppo_003.st_bes_nivojev , ker je na 0. indeksu besede[][][] naslov kategorije in je treba pri 1 zaèeti iskati besede 
			//System.out.println("i = " + i + "; i_bes = " + i_bes);
			
			try { 
				String [] b = uppo_003.besede[i_bes][i]; 
				if (b != null && Arrays.asList(b).contains(s)) {
					tocke = i*1.0;
					break;
				}				
			} catch(ArrayIndexOutOfBoundsException e) { // vrednost ni podana
				tocke = 1.0;
				break;
			}
		}
			
		
		return tocke;
	}
	
	private Double pretvoriVTocke(Double x, int i_kat) {
		Double tocke = 0.0;
		Double vr;	// vrednost
		
		// cim_vecje --> tip številskega atributa: true, èe je tipa "tem veèje, tem bolje" in false, èe je tipa "tem manjše, tem bolje"
		boolean cim_vecje = (uppo_003.kategorije[i_kat][1].equals(uppo_003.st_cim_vecje));

		System.out.println("Zf_st[i_kat][0] = " + Double.toString(Zf_st[i_kat][0]) + " Zf_st[i_kat][1] = " + Double.toString(Zf_st[i_kat][1]) + " Zf_st[i_kat][2] = " + Double.toString(Zf_st[i_kat][2]) + "; x =" + x);
		
		// èe je vrednost zunaj meja Zf, javi napako
		if (Zf_st[i_kat][0] > x || Zf_st[i_kat][1] < x) {
			System.out.println("Napaka v pretvoriVTocke int ! \n x je zunaj meja zaloge vrednosti.");
			return tocke;
		}

		
		// pretvorba dejanske vrednosti v toèke s pomoèjo Zf_st (zaloge vrednosti števil)
		// !TOLE ŠE OPTIMIZIRAJ, DA BO VRAÈALO double PRIBLIŽKE!; trenutno zaokrožuje navzgor vse (npr.: 1,3 ==> 2)
		for (int i = 1; i < Zf[1]+1; i++) {
			// vr = min + tocke*diferenca
			vr = Zf_st[i_kat][0] + i*Zf_st[i_kat][2];
			System.out.println("i = " + i + "; x = " + x + "; vr = " + vr);
			if (x <= vr && cim_vecje) {
				tocke = i*1.0;
				break;
			}
			if (x <= vr && !cim_vecje) {
				tocke = (Zf[1]+1) - i;
				break;
			}
			if (x == vr && cim_vecje) {
				tocke = i*1.0;
				break;
			}
			if (x == vr && !cim_vecje) {
				tocke = (Zf[1]+1) - i ;
				break;
			}
		}
		
		System.out.println("tocke = " + tocke);						
		return tocke;		
	}

	private Double poracunajRazmerja(int vrsta) {
		// poraèuna razmerja na naèin najprej podatke na skupno zalogo vrednosti, nato deljenje s prioriteto, nazadnje prištevanje vsake vrednosti k razmerju
		// razmerje += (1/)podatek * k / prioriteta
		
		Double razmerje = 0.0;
		Double vr;	// vrednost celice 
		int i_bes = -1;	// indeks besedne kategorije za tabelo uppo_003.besede[][][]
		boolean cim_vecje;	// tip številskega atributa: true, èe je tipa "tem veèje, tem bolje" in false, èe je tipa "tem manjše, tem bolje"
		int null_stetje = 0;	// šteje koliko atributov je null
		int prioriteta;
		
		stevilskeNaSkupnoZf2();
		
		// za vsako kategorijo
		for (int i = 0; i < stk; i++) {
			Object atr = t.getValueAt(vrsta, i+1);
			
			if (atr == null) { null_stetje++; continue;	}
		
			prioriteta = (Integer) uppo_003.kategorije[i][2];
			
			// èe je atribut tipa String
			if (atr.getClass() == String.class) {
				vr = pretvoriVTocke((String)atr, ++i_bes);
				razmerje += vr / prioriteta;
			}
			
			// èe je atribut tipa Double
			else if (atr.getClass() == Double.class) {
				if ((Double)atr == 0)	// zato, da vrednosti "0" ne rušijo sistema
					continue;

				cim_vecje = (uppo_003.kategorije[i][1].equals(uppo_003.st_cim_vecje)); 

				if (cim_vecje)
					razmerje += ((Double)atr * Zf_st[i][2] / prioriteta);
				else
					razmerje += (1.0/(Double)atr * Zf_st[i][2] / prioriteta);
			}
			
			else
				System.out.println("Napaka v poracunajRazmerja() !");	
		}	
		
		if (null_stetje == stk)	// da ne pride do napake pri primeru, ko je celotna vrstica pušèena prazna
			razmerje = null;		
		return razmerje;
	
	}
	
	
	
	/*
	private Double poracunajRazmerja(int vrsta) {
		// poraèuna razmerja za dano vrstico v tabeli na naèin èim manjše v imenovalec, èim veèje v števec
		
		Double razmerje = 1.0;
		Double vr;	// številska vrednost celice 
		int i_bes = -1;	// indeks besedne kategorije za tabelo uppo_003.besede[][][]
		boolean cim_vecje;	// tip številskega atributa: true, èe je tipa "tem veèje, tem bolje" in false, èe je tipa "tem manjše, tem bolje"
		int null_stetje = 0;	// šteje koliko atributov je null
		int prioriteta;
		
		// za vsako kategorijo
		for (int i = 0; i < stk; i++) {
			Object atr = t.getValueAt(vrsta, i+1);
			
			if (atr == null) { null_stetje++; continue;	}
			
			// dodaj razmerju prioriteto
		//	try {
		//		prioriteta = (Integer) uppo_003.kategorije[i][2];
		//		razmerje /= prioriteta;
		//	} catch (NullPointerException e) {System.out.println("prioriteta v vrsti " + vrsta +  " za kategorijo " + i +" je napaka: " + e); }
			
			// èe je atribut tipa String
			if (atr.getClass() == String.class) {
				vr = pretvoriVTocke((String)atr, ++i_bes);
				razmerje *= vr;
			}
			
			// èe je atribut tipa Double
			else if (atr.getClass() == Double.class) {
				if ((Double)atr == 0)	// zato, da vrednosti "0" ne rušijo sistema
					continue;

				cim_vecje = (uppo_003.kategorije[i][1].equals(uppo_003.st_cim_vecje)); 

				prioriteta = (Integer) uppo_003.kategorije[i][2];

				if (cim_vecje)
					razmerje *= ((Double)atr / prioriteta);
				else
					razmerje /= ((Double)atr / prioriteta);
			}
			
			else
				System.out.println("Napaka v poracunajRazmerja() !");	
		}	
		
		if (null_stetje == stk)	// da ne pride do napake pri primeru, ko je celotna vrstica pušèena prazna
			razmerje = null;		
		return razmerje;
	}
	*/
	private Double najdiMax(Double [] d) {
		Double max = d[0];
		for (int i = 1; i < d.length; i++)
			if (d[i] != null && d[i] > max)
				max = d[i];
		return max;
	}
		
	private Object [] prepisiOstaleVrednosti(Object [] primerek, int vrsta) {
		for (int i = 2; i < stk+1; i++)
			primerek[i] = t.getValueAt(vrsta,  i-2);		
		return primerek;
	}
	
	private void ustvariZaslon() {
		z = new Zaslon(Color.gray, uppo_003.pridobiNaslov(), o);
		uppo_003.skrijVseZaslone();
		z.pokazi(o);
		o.pack();
		o.setVisible(true);
		uppo_003.fullScreen(o);
	}
	
	private void ustvariTabeloRezultatov() {
		
		inicializirajGlava();		
		tr = new Tabela("porocilo", stk, glava, rezultati);
		ustvariZaslon();
		o = tr.pozicioniraj(o, z);
		uppo_003.skrijVseZaslone();
		o = tr.pokazi(o, z);
	}
	
	private void inicializirajGlava() {
		glava = new String[stk+3];
		
		glava[0] = "mesto";
		glava[1] = "toèke";
		glava[2] = "ID";
		for (int i = 0; i < stk; i++) {
			glava[3+i] = uppo_003.naslovi_kategorij[i];
		}
	}
	
	public String toString() {
		String s = "Rezultati: \n";
		for (int i = 0; i < rezultati.length; i++) {
			for (int j = 0; j < rezultati[i].length; j++) {
				s += rezultati[i][j] + "; ";
			} 
			s += "\n";
		}
		/*
		for (int j = 0; j < r.length; j++) {
			s += r[j] + "; ";
		} s += "\n";*/
		
		return s;
	}
	
	public Object [][] pridobiRezultate(){
		return rezultati;
	}
	
	public String [] pridobiGlava() {
		return glava;
	}
	

}
