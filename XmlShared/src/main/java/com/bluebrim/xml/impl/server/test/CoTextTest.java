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
					"<Ingress>Omkring tio i tre i går eftermiddag tvärstannade en grön Saab 9000 utanför Östgöta Enskilda Bank i Kisa. Tre män iförda mörka kläder och rånarhuvor, beväpnade med kpistar, tog sig ur bilen. Två av dem rusade in på banken. Männen blev kvar inne på banken mellan 10 och 15 minuter, medan deras medbrottsling stod kvar utanför banken och vaktade.\n" +
					"<Mellanrubrik>Mellanrubrik\n" +
					"<Normal>Bla bla bla bla bla.\n" +
					"<Signatur>Gustav Andersson\n";
					
	txt = txt +	"<Kortisrubrik>De tre rånarna var maskerade och tungt beväpnade\n" + 
			"<Hållrad>De lämnade banken med dragna vapen.\n" +
			"<Normal>Rånarna satte sig i bilen, gjorde en U-sväng och körde i snabb fart söder ut på riksväg 34.\n";
			
		
	txt = txt + 	"<Rubrik>Det var rena avrättningen\n" +	
			"<Ingress>Dödsskjutningarna skedde bara ett par hundra meter från Malexander på vägen till Kisa. Ännu en halvtimme efter händelsen låg de döda kropparna kvar på vägen, täckta av gula filtar.\n" +			
			"<Mellanrubrik>Två polismän sköts till döds \n" +
			"<Hänvisning>Det är helt fruktansvärt. De måste vara totalt desperata, och det kunde skett med vem som helst av oss. Vem vet vad de kan göra nu när de gått över alla gränser\n" + 
			"<Normal>sade polismannen Peter Viveland bittert." +
			"Lars Engström berättade senare på kvällen om den djupa förstämning som rådde bland polismän i distriktet. Polispräster och personal från Universitetssjukhuset var inkallade för att stötta de mördades kolleger.\n" +
			"De båda omkomna polismännen var Olle Borén, 43 år, och Robert Karlström, 30, båda från Mjölby.";
			
	txt = txt + "<Ingress indrag>Kinda-Ydres närpolischef Kenneth Eklund var den ende polisen på orten.\n" +
			"<Brödtext>Han kom snabbt till platsen och avvaktade i polisbilen två hundra meter söderut på riksväg 34.\n" + 
			"Fredagshandeln var som vanligt intensiv och mycket folk rörde sig på gatorna.\n" +
			"En del uppmärksammade inte att det var ett rån förrän de passerade i närheten av den maskerade vakten.\n" + 
			"Iförd vita handskar dirigerade han gångarna om de kom för nära banken.\n";

	System.out.println(txt);		

}
}
