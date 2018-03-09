package com.bluebrim.formula.impl.server;

import com.bluebrim.formula.shared.*;



public class CoFormulaErrorResources_sv extends CoFormulaErrorResources {
	static final Object[][] contents = {
		{ FORMULA_INCORRECT_FORMULATED,"Formeln är felaktig formulerad."},
		{ UNEXPECTED_END,"Ofullständig formel."},
		{ THE_IDENTIFIER_NOT_DEFINED,"Definitionen saknas för identifieraren"},
		{ IDENTIFIER_NOT_DEFINED,"Identifieraren har inte definierats."},
		{ MISSING_LEFTPARANTHESIS,"Vänsterparentes saknas."},
		{ MISSING_LEFT_BRACKET,"Vänster hakparantes saknas."},
		{ MISSING_FORMULA,"Ingen formel finns att tillgå."},
		{ UNEXPECTED_TOKEN,"Ett oväntat tecken hittades i formeln."},
		{ THE_UNEXPECTED_TOKEN,"Formeln innehåller ett oväntat tecken :"},
		{ CAN_NOT_READ_FORMULA,"Kan inte läsa formeln."},
		// error messages from the Co__Node classes
		{ INCORRECT_USE_OF_OPERATOR_AND,"Felaktig användning av operatorn &."},
		{ INCORRECT_USE_OF_OPERATOR_DIVIDE,"Felaktig användning av operatorn /."},
		{ INCORRECT_USE_OF_OPERATOR_EQUAL,"Felaktig användning av operatorn =."},
		{ INCORRECT_USE_OF_OPERATOR_GREATER_EQUAL,"Felaktig användning av operatorn >=."},
		{ INCORRECT_USE_OF_OPERATOR_GREATER,"Felaktig användning av operatorn >."},
		{ INCORRECT_USE_OF_OPERATOR_LESS_EQUAL,"Felaktig användning av operatorn <=."},
		{ INCORRECT_USE_OF_OPERATOR_LESS,"Felaktig användning av operatorn <."},
		{ INCORRECT_USE_OF_OPERATOR_MINUS,"Felaktig användning av operatorn -."},
		{ INCORRECT_USE_OF_OPERATOR_PLUS,"Felaktig användning av operatorn +."},
		{ INCORRECT_USE_OF_OPERATOR_MULTIPLY,"Felaktig användning av operatorn *."},
		{ INCORRECT_USE_OF_OPERATOR_NOT,"Felaktig användning av operatorn !."},
		{ INCORRECT_USE_OF_OPERATOR_NEGATION,"Felaktig användning av operatorn -."},
		{ INCORRECT_USE_OF_OPERATOR_NOT_EQUAL,"Felaktig användning av operatorn !=."},
		{ INCORRECT_USE_OF_NUMBER,"Felaktig användning av nummer."},
		{ INCORRECT_USE_OF_OPERATOR_OR,"Felaktig användning av operatorn |."},
		{ INCORRECT_USE_OF_VARIABLE,"Felaktig användning av variabel."},
		{ INCORRECT_USE_OF_UNIT,"Felaktig användning av enhet."},
		{ DIVISION_BY_0,"Division med 0 är inte tillåtet."},
		{ INCORRECT_CONDITION_EXPRESSION,"Villkorssatser kräver ett boolskt uttryckt."},
		{ THE_CONDITION_EXPRESSION_MISSING,"Villkorssatsen saknar tecknet"},
		{ INCORRECT_USE_OF_BOOLEAN,"Felaktig användning av boolska värden."},
		{ INCORRECT_USE_OF_STRING,"Felaktig användning av textsträngar."},
		{ LOOPING_SEQUENCE_FOUND_IN_NODE,"Formeln kan inte beräknas pga en oändlig loop funnen i variabeln"},
		{ INTERVAL_MISSING_FOR_VARIABLE,"Inget intervall för dagens datum har definierats för variablen"},
		{ VALUE_MISSING_FOR_VARIABLE,"Värde saknas för variabeln"},
		{ THE_MISSING_VARIABLE,"Saknar variabel med namnet"},
		{ ERROR_NODE,"Felaktig formel."},
		// error messages from com.bluebrim.formula.impl.server.CoAbstractVariablesHolder
		{ THE_VARABLE_NAME_NOT_UNIQUE,"Inget unikt namn. Kan inte lägga till variabeln"},
		{ THE_VARABLE_NAME_NOT_DEFINED,"Variablen har inte definierats :"},
		{ VARABLE_NAME_EMPTY,"Ett variabelnamn måste anges."},
		{ VARABLE_TYPE_INCORRECT,"Variabeln kan inte vara av satt typ."},
	};	
public Object[][] getContents ( ) {
	return contents;
}
}
