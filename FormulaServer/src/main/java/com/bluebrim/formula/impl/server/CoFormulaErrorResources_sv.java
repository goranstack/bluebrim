package com.bluebrim.formula.impl.server;

import com.bluebrim.formula.shared.*;



public class CoFormulaErrorResources_sv extends CoFormulaErrorResources {
	static final Object[][] contents = {
		{ FORMULA_INCORRECT_FORMULATED,"Formeln �r felaktig formulerad."},
		{ UNEXPECTED_END,"Ofullst�ndig formel."},
		{ THE_IDENTIFIER_NOT_DEFINED,"Definitionen saknas f�r identifieraren"},
		{ IDENTIFIER_NOT_DEFINED,"Identifieraren har inte definierats."},
		{ MISSING_LEFTPARANTHESIS,"V�nsterparentes saknas."},
		{ MISSING_LEFT_BRACKET,"V�nster hakparantes saknas."},
		{ MISSING_FORMULA,"Ingen formel finns att tillg�."},
		{ UNEXPECTED_TOKEN,"Ett ov�ntat tecken hittades i formeln."},
		{ THE_UNEXPECTED_TOKEN,"Formeln inneh�ller ett ov�ntat tecken :"},
		{ CAN_NOT_READ_FORMULA,"Kan inte l�sa formeln."},
		// error messages from the Co__Node classes
		{ INCORRECT_USE_OF_OPERATOR_AND,"Felaktig anv�ndning av operatorn &."},
		{ INCORRECT_USE_OF_OPERATOR_DIVIDE,"Felaktig anv�ndning av operatorn /."},
		{ INCORRECT_USE_OF_OPERATOR_EQUAL,"Felaktig anv�ndning av operatorn =."},
		{ INCORRECT_USE_OF_OPERATOR_GREATER_EQUAL,"Felaktig anv�ndning av operatorn >=."},
		{ INCORRECT_USE_OF_OPERATOR_GREATER,"Felaktig anv�ndning av operatorn >."},
		{ INCORRECT_USE_OF_OPERATOR_LESS_EQUAL,"Felaktig anv�ndning av operatorn <=."},
		{ INCORRECT_USE_OF_OPERATOR_LESS,"Felaktig anv�ndning av operatorn <."},
		{ INCORRECT_USE_OF_OPERATOR_MINUS,"Felaktig anv�ndning av operatorn -."},
		{ INCORRECT_USE_OF_OPERATOR_PLUS,"Felaktig anv�ndning av operatorn +."},
		{ INCORRECT_USE_OF_OPERATOR_MULTIPLY,"Felaktig anv�ndning av operatorn *."},
		{ INCORRECT_USE_OF_OPERATOR_NOT,"Felaktig anv�ndning av operatorn !."},
		{ INCORRECT_USE_OF_OPERATOR_NEGATION,"Felaktig anv�ndning av operatorn -."},
		{ INCORRECT_USE_OF_OPERATOR_NOT_EQUAL,"Felaktig anv�ndning av operatorn !=."},
		{ INCORRECT_USE_OF_NUMBER,"Felaktig anv�ndning av nummer."},
		{ INCORRECT_USE_OF_OPERATOR_OR,"Felaktig anv�ndning av operatorn |."},
		{ INCORRECT_USE_OF_VARIABLE,"Felaktig anv�ndning av variabel."},
		{ INCORRECT_USE_OF_UNIT,"Felaktig anv�ndning av enhet."},
		{ DIVISION_BY_0,"Division med 0 �r inte till�tet."},
		{ INCORRECT_CONDITION_EXPRESSION,"Villkorssatser kr�ver ett boolskt uttryckt."},
		{ THE_CONDITION_EXPRESSION_MISSING,"Villkorssatsen saknar tecknet"},
		{ INCORRECT_USE_OF_BOOLEAN,"Felaktig anv�ndning av boolska v�rden."},
		{ INCORRECT_USE_OF_STRING,"Felaktig anv�ndning av textstr�ngar."},
		{ LOOPING_SEQUENCE_FOUND_IN_NODE,"Formeln kan inte ber�knas pga en o�ndlig loop funnen i variabeln"},
		{ INTERVAL_MISSING_FOR_VARIABLE,"Inget intervall f�r dagens datum har definierats f�r variablen"},
		{ VALUE_MISSING_FOR_VARIABLE,"V�rde saknas f�r variabeln"},
		{ THE_MISSING_VARIABLE,"Saknar variabel med namnet"},
		{ ERROR_NODE,"Felaktig formel."},
		// error messages from com.bluebrim.formula.impl.server.CoAbstractVariablesHolder
		{ THE_VARABLE_NAME_NOT_UNIQUE,"Inget unikt namn. Kan inte l�gga till variabeln"},
		{ THE_VARABLE_NAME_NOT_DEFINED,"Variablen har inte definierats :"},
		{ VARABLE_NAME_EMPTY,"Ett variabelnamn m�ste anges."},
		{ VARABLE_TYPE_INCORRECT,"Variabeln kan inte vara av satt typ."},
	};	
public Object[][] getContents ( ) {
	return contents;
}
}
