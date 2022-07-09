package uppo;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JFrame;

public class UstvariPorocilo {

	private Tabela t;	// uporabnikova vnosna tabela
	private Tabela tr;	// tabela, ki prikazuje poro�ilo (rezultate)
	private Zaslon z;	// zaslon na katerem prika�emo poro�ilo (rezultate)
	private JFrame o;
	private Object [][] rezultati;
	private Double [] r;	// razmerja
	private Object [][] vsebina;
	private String [] glava;
	private Double [] Zf;	// dejanska zaloga vrednosti (Zf)
	private Double [][] Zf_st;	// Zf z dejanskimi vrednostmi
										// [i_kat][min, max, d]
	private int stk;	 // �tevilo kategorij
	public int id;	// �tevilo primerkov
	private int max_tocke;	// �tevilo to�k, ki jih dose�e najbolj�i primerek
	

	
	public UstvariPorocilo(int st_kat, Tabela tab) {
		ponastavi(st_kat, tab);
		//stevilskeNaSkupnoZf();
		//rezultati = tabelaRezultatov();
		
		r = tabelaRazmerij();		// tabela primerljivih koli�nikov
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
						// samo indeksi �tevilskih kategorij bodo imeli vrednost
		
		o = uppo_003.pridobiOkvir();
	}
	
	// �tevilske vrednosti bomo stisnili/raz�irili na skupno zalogo vrednosti
	// tole ne vem �e dela glih ok..?
	private void stevilskeNaSkupnoZf1() {
		Double min, max, d;
		for (int i = 1; i < stk+1; i++) {	// i=1, ker stolpca ID ne smemo stiskat/raz�irjat
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
	
	// �tevilske vrednosti bomo stisnili/raz�irili na skupno zalogo vrednosti
	private void stevilskeNaSkupnoZf2() {
		Double k;
		boolean cim_vecje;
		
		// za vsako kategorijo
		for (int i = 1; i < stk+1; i++){		// i=1, ker stolpca ID ne smemo stiskat/raz�irjat
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
		 * tab { { dose�eno mesto (int) , to�ke/score (int) , ID (int) , atributi ki jih je dolo�il uporabnik sam} * id (�t. primerkov) }
		 */	
		
		// za vsako vrstico vpi�i v tabelo VSE* podatke posamezne vrstice, razen dose�enega mesta
		// *vsi podatki = {dose�eno mesto, to�ke, id, atributi ki jih je dolo�il uporabnik}
		for (int i = 0; i < id; i++) {
			
			if (r[i] != null) {
				tab[i][1] = r[i];	// to�ke = razmerja[i]
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
		
		// uredi primerke padajo�e po to�kah in jim dodaj vrednost atributa "dose�eno mesto" (na indeks 0)
		// tab = kindaBubbleSort(tab); --> manj primeren v tem primeru, ker ne morem sproti pisati dose�enega mesta
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
			
			// poi��i najve�je �tevilo to�k v neurejenem delu
			for (int j = i+1; j < tab.length; j++) {
				if (tab[j][1] != null && (Double)tab[j][1] > max) {
					max = (Double)tab[j][1];
					i_max = j;
				}
			}
			
			// zamenjaj tabeli, �e potrebno
			if (i_max != i) {
				tab_zac = tab[i];
				tab[i] = tab[i_max];
				tab[i_max] = tab_zac;
			}
			
			// zapi�i pridobljeno mesto
			tab[i][0] = i+1;
		}
		// zapi�i �e zadnje mesto
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
	 * Namesto, da bi zamenjali celi tabeli, zamenjata le vrednosti to�k.
	 * Pustil sem ju za primer, �e bi potreboval to idejo kdaj kasneje.
	 
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
		
		// pora�unaj razmerja za vsak primerek (vrstico) posebej
		for (int i = 0; i < id; i++) {
			tab[i] = poracunajRazmerja(i);
		}
		
		
		// vsa razmerja spremeni v to�ke - najbolj�e ima max_tocke �tevilo to�k
		Double max = najdiMax(tab);
		Double k = max_tocke / max;	// koli�nik s katerim pomno�imo vse vrednosti, zato da dobimo "lepe" rezultate (do 1000)
		
		// vsa razmerja raz�iri s k (torej vsa razmerja spremeni v to�ke)
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
	
	// pora�unaj to�ke za vsak primerek (vrstico) posebej, shrani v tabelo primerek[] in prepi�i vanjo �e celotno n-terko
	// ni v uporabi - zamenjal s poracunajRazmerja
	private Object[] poracunaj(int vrsta) {
		Object [] primerek = new Object[stk+3];
		Double tocke = 0.0;
		int i_bes = -1;	// indeks besedne kategorije za tabelo uppo_003.besede[][][]
		
		// pretvori vrednost vsake celice posebej v to�ke in pri�tej k Double tocke
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
		
		// prepi�e �e ostale vrednosti... 
		primerek = prepisiOstaleVrednosti(primerek, vrsta);
		
		return primerek;
	}
	
	private Double pretvoriVTocke(String s, int i_bes) {
		Double tocke = -1.0;
		
		for (int i = 1; i < uppo_003.st_bes_nivojev+1; i++) {	// 1 <= i < uppo_003.st_bes_nivojev , ker je na 0. indeksu besede[][][] naslov kategorije in je treba pri 1 za�eti iskati besede 
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
		
		// cim_vecje --> tip �tevilskega atributa: true, �e je tipa "tem ve�je, tem bolje" in false, �e je tipa "tem manj�e, tem bolje"
		boolean cim_vecje = (uppo_003.kategorije[i_kat][1].equals(uppo_003.st_cim_vecje));

		System.out.println("Zf_st[i_kat][0] = " + Double.toString(Zf_st[i_kat][0]) + " Zf_st[i_kat][1] = " + Double.toString(Zf_st[i_kat][1]) + " Zf_st[i_kat][2] = " + Double.toString(Zf_st[i_kat][2]) + "; x =" + x);
		
		// �e je vrednost zunaj meja Zf, javi napako
		if (Zf_st[i_kat][0] > x || Zf_st[i_kat][1] < x) {
			System.out.println("Napaka v pretvoriVTocke int ! \n x je zunaj meja zaloge vrednosti.");
			return tocke;
		}

		
		// pretvorba dejanske vrednosti v to�ke s pomo�jo Zf_st (zaloge vrednosti �tevil)
		// !TOLE �E OPTIMIZIRAJ, DA BO VRA�ALO double PRIBLI�KE!; trenutno zaokro�uje navzgor vse (npr.: 1,3 ==> 2)
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
		// pora�una razmerja na na�in najprej podatke na skupno zalogo vrednosti, nato deljenje s prioriteto, nazadnje pri�tevanje vsake vrednosti k razmerju
		// razmerje += (1/)podatek * k / prioriteta
		
		Double razmerje = 0.0;
		Double vr;	// vrednost celice 
		int i_bes = -1;	// indeks besedne kategorije za tabelo uppo_003.besede[][][]
		boolean cim_vecje;	// tip �tevilskega atributa: true, �e je tipa "tem ve�je, tem bolje" in false, �e je tipa "tem manj�e, tem bolje"
		int null_stetje = 0;	// �teje koliko atributov je null
		int prioriteta;
		
		stevilskeNaSkupnoZf2();
		
		// za vsako kategorijo
		for (int i = 0; i < stk; i++) {
			Object atr = t.getValueAt(vrsta, i+1);
			
			if (atr == null) { null_stetje++; continue;	}
		
			prioriteta = (Integer) uppo_003.kategorije[i][2];
			
			// �e je atribut tipa String
			if (atr.getClass() == String.class) {
				vr = pretvoriVTocke((String)atr, ++i_bes);
				razmerje += vr / prioriteta;
			}
			
			// �e je atribut tipa Double
			else if (atr.getClass() == Double.class) {
				if ((Double)atr == 0)	// zato, da vrednosti "0" ne ru�ijo sistema
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
		
		if (null_stetje == stk)	// da ne pride do napake pri primeru, ko je celotna vrstica pu��ena prazna
			razmerje = null;		
		return razmerje;
	
	}
	
	
	
	/*
	private Double poracunajRazmerja(int vrsta) {
		// pora�una razmerja za dano vrstico v tabeli na na�in �im manj�e v imenovalec, �im ve�je v �tevec
		
		Double razmerje = 1.0;
		Double vr;	// �tevilska vrednost celice 
		int i_bes = -1;	// indeks besedne kategorije za tabelo uppo_003.besede[][][]
		boolean cim_vecje;	// tip �tevilskega atributa: true, �e je tipa "tem ve�je, tem bolje" in false, �e je tipa "tem manj�e, tem bolje"
		int null_stetje = 0;	// �teje koliko atributov je null
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
			
			// �e je atribut tipa String
			if (atr.getClass() == String.class) {
				vr = pretvoriVTocke((String)atr, ++i_bes);
				razmerje *= vr;
			}
			
			// �e je atribut tipa Double
			else if (atr.getClass() == Double.class) {
				if ((Double)atr == 0)	// zato, da vrednosti "0" ne ru�ijo sistema
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
		
		if (null_stetje == stk)	// da ne pride do napake pri primeru, ko je celotna vrstica pu��ena prazna
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
		glava[1] = "to�ke";
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
