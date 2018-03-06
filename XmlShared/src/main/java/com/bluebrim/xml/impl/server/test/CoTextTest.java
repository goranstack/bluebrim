package com.bluebrim.xml.impl.server.test;



public class CoTextTest {
/**
 * CoXmlTest constructor comment.
 */
public CoTextTest() {
	super();
	
	
}
/**
 * This method was created in VisualAge.
 */
public static void main( String[] args ) {
	float[] fa = {1.0f, 2.34f, 219.2332f};
	System.out.println(fa.toString());
	String txt = 	"<Rubrik>Polismorden i Kisa\n" +
					"<Ingress>Omkring tio i tre i g�r eftermiddag tv�rstannade en gr�n Saab 9000 utanf�r �stg�ta Enskilda Bank i Kisa. Tre m�n if�rda m�rka kl�der och r�narhuvor, bev�pnade med kpistar, tog sig ur bilen. Tv� av dem rusade in p� banken. M�nnen blev kvar inne p� banken mellan 10 och 15 minuter, medan deras medbrottsling stod kvar utanf�r banken och vaktade.\n" +
					"<Mellanrubrik>Mellanrubrik\n" +
					"<Normal>Bla bla bla bla bla.\n" +
					"<Signatur>Gustav Andersson\n";
					
	txt = txt +	"<Kortisrubrik>De tre r�narna var maskerade och tungt bev�pnade\n" + 
			"<H�llrad>De l�mnade banken med dragna vapen.\n" +
			"<Normal>R�narna satte sig i bilen, gjorde en U-sv�ng och k�rde i snabb fart s�der ut p� riksv�g 34.\n";
			
		
	txt = txt + 	"<Rubrik>Det var rena avr�ttningen\n" +	
			"<Ingress>D�dsskjutningarna skedde bara ett par hundra meter fr�n Malexander p� v�gen till Kisa. �nnu en halvtimme efter h�ndelsen l�g de d�da kropparna kvar p� v�gen, t�ckta av gula filtar.\n" +			
			"<Mellanrubrik>Tv� polism�n sk�ts till d�ds \n" +
			"<H�nvisning>Det �r helt fruktansv�rt. De m�ste vara totalt desperata, och det kunde skett med vem som helst av oss. Vem vet vad de kan g�ra nu n�r de g�tt �ver alla gr�nser\n" + 
			"<Normal>sade polismannen Peter Viveland bittert." +
			"Lars Engstr�m ber�ttade senare p� kv�llen om den djupa f�rst�mning som r�dde bland polism�n i distriktet. Polispr�ster och personal fr�n Universitetssjukhuset var inkallade f�r att st�tta de m�rdades kolleger.\n" +
			"De b�da omkomna polism�nnen var Olle Bor�n, 43 �r, och Robert Karlstr�m, 30, b�da fr�n Mj�lby.";
			
	txt = txt + "<Ingress indrag>Kinda-Ydres n�rpolischef Kenneth Eklund var den ende polisen p� orten.\n" +
			"<Br�dtext>Han kom snabbt till platsen och avvaktade i polisbilen tv� hundra meter s�derut p� riksv�g 34.\n" + 
			"Fredagshandeln var som vanligt intensiv och mycket folk r�rde sig p� gatorna.\n" +
			"En del uppm�rksammade inte att det var ett r�n f�rr�n de passerade i n�rheten av den maskerade vakten.\n" + 
			"If�rd vita handskar dirigerade han g�ngarna om de kom f�r n�ra banken.\n";

	System.out.println(txt);		

}
}
