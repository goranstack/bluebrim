package com.bluebrim.base.shared;

import java.util.*;

import com.bluebrim.resource.shared.*;

/**
 * Default resource for country names, the call CoContryResources.getName("SE")
 * would give Sweden as result.
 */
public class CoCountryResources extends CoOldResourceBundle {
	public static CoOldResourceBundle rb = null;
	
	private static Hashtable ctry2LangMapping;
	private	static final Object[][] contents = {};
	private static final String compressedCtry2LangMapping =
		"ADfresAEarenAFpsAGenAIrnALsqAMhyruANnlenAOptAResASensmATdeAUenAWnlenAZazhyru"
		+ "BAsrshhrslmksqBBenBDbnhibhenBEfrnldeBFfrBGbgtrBHarenBIrnfrswBJfrBMenBNmsenzh"
		+ "BOesayquBRptBSenBTdzenneBVnoBWentnBYberuBZenesCAenfrCCenCFfrsgCGfrCHfrdeitrm"
		+ "CIfrCKmienCLesCMenfrCNzhboCOesCResCUesCVptCXenCYeltrenCZcsskDEdeDJarfrsoDKda"
		+ "DMenfrDOesDZarfrECesquEEetruEGarenfrEHarfritERamtiarenitESeseucaglETamaren"
		+ "FIfisvFJenfjhiFKenFMenFOfodaFRfreubrcoFXfrGAfrGBengdcyGDenfrGEkahyruGFfrGHen"
		+ "GIenesGLdaikklGMenwoGNfrGPfrenGQesGRelGTesGUenGWptGYenhiurHKzhenHNesHRhrHTfr"
		+ "HUhuIDinennlIEengaILiwarjiINhienguknksmlmrneorpasatateIOenIQarkutkIRfaarku"
		+ "ISisITitfrdeJMenJOarJPjaKEenswKGkyKHkmKIenKMfrarKNenKPkoKRkoKWarenKYenKZkkru"
		+ "LAlofrLBarenfrLCenfrLIdeLKtasienLRenLSstenLTltruplLUfrdeLVlvltruLYarenit"
		+ "MAarfresMCfrenitMDmorobgMGmgenfrMKmkshtrMLfrMMmyMNmnruMOzhptMQfrMRarfrMSen"
		+ "MTmtenitMUenfrhiMWenMXesMYmsenMZptNAenafdeNEfrhaNFenNGenhayoNIesNLnlfyNOno"
		+ "NPneNRnaenNUenNZenmiOMarenPAesenPEesquayPFfrPGenPHentlesPKurenpspasdPLplPMfren"
		+ "PNenPResenPTptPWenPYesgnQAarenREfrtaROrohuRUruRWenfrrwSAarSBenSCenfrSDarsu"
		+ "SEsvSGzhenmstaSHenSIslSJnoSKskhuplshSLenSMitSNfrSOarenitsoSRnleneshiSTptSVes"
		+ "SYarSZenssTCenTDfrarTFfrTGfrTHthTJtgruuzTKenmiTMtkruTNarTOentoTRtrkuTTenTVen"
		+ "TWzhTZenswUAukruUGenswUMenUSenesUYesUZuzruVAlaitVCenVEesVGenVIenVNvizhfr"
		+ "VUenfrbiWFfrWSensmYEarYTfrmgswYUsrshmkhuZAafenZMenZRfrswZWensn";
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle() {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoCountryResources.class.getName());
	return rb;
}
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
	private static String getLanguageForCountry(String country) {
		if (ctry2LangMapping == null) {
			ctry2LangMapping = new Hashtable();

			int i = 0;
			int j;
			while (i < compressedCtry2LangMapping.length()) {
				String key = compressedCtry2LangMapping.substring(i, i + 2);
				i += 2;
				for (j = i; j < compressedCtry2LangMapping.length(); j += 2)
					if (Character.isUpperCase(compressedCtry2LangMapping.charAt(j)))
						break;
				String compressedValues = compressedCtry2LangMapping.substring(i, j);
				String[] values = new String[compressedValues.length() / 2];
				for (int k = 0; k < values.length; k++)
					values[k] = compressedValues.substring(k * 2, (k * 2) + 2);
				ctry2LangMapping.put(key, values);
				i = j;
			}
		}
		String[] result = (String[])ctry2LangMapping.get(country);
		if (result == null)
			result = new String[0];
		return result.length > 0 ? result[0] : "";
	}
/**
  Svara med det namn som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static String getName(String aKey ) {
	try
	{
		return getBundle().getString(aKey);
	}
	catch (MissingResourceException e)
	{
		String language = getLanguageForCountry(aKey);
		return !language.equals("") ? new Locale(language, aKey).getDisplayCountry(Locale.getDefault()) : "";
	}			
}
/**
  Sätter om rb när Locale har ändrats. 
 */
public static void resetBundle() {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoCountryResources.class.getName());
	rb.resetLookup();
}
}
