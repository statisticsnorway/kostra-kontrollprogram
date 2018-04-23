package no.ssb.kostra.control.regnskap.regn0A;

import java.util.HashMap;
import java.util.Vector;

import no.ssb.kostra.control.Constants;

/*
 *
 */

public final class ControlFunksjoner extends no.ssb.kostra.control.Control {
	private HashMap<String, String> spesielleKommuner = new HashMap<String, String>();
	private HashMap<String, String> osloKommuner = new HashMap<String, String>();
	private HashMap<String, String> spesielleProveordning = new HashMap<String, String>();

	private String[] validFunksjoner = { "100", "110", "120", "121", "130",
			"170", "171", "172", "173", "180", "190", "201", "202", "211",
			"213", "215", "221", "222", "223", "231", "232", "233", "234",
			"241", "242", "243", "244", "251", "252", "253", "254", "256",
			"261", "265", "273", "275", "276", "281", "283", "285", "290",
			"301", "302", "303", "315", "320", "321", "325",
			"329", "330", "332", "335", "338", "339", "340", "345", "350",
			"353", "354", "355", "360", "365", "370", "373", "375", "377",
			"380", "381", "383", "385", "386", "390", "392", "393", "800",
			"840", "841", "850", "860", "870", "880", "899" };

	private String[] validFunksjonerForSpecialKommuner = { "100", "110", "120",
			"121", "130", "170", "171", "172", "173", "180", "190", "201",
			"202", "211", "213", "215", "221", "222", "223", "231", "232",
			"233", "234", "241", "242", "243", "244", "251", "252", "253",
			"254", "256", "261", "265", "273", "275", "276", "281", "283",
			"285", "290", "301", "302", "303", "315", "320", "321",
			"325", "329", "330", "332", "335", "338", "339", "340", "345",
			"350", "353", "354", "355", "360", "365", "370", "373", "375",
			"377", "380", "381", "383", "385", "386", "390", "392", "393",
			"400", "410", "420", "421", "430", "460", "465", "470", "471",
			"472", "473", "480", "490", "510", "515", "520", "521", "522",
			"523", "524", "525", "526", "527", "528", "529", "530", "531",
			"532", "533", "554", "559", "561", "562", "570", "581", "590",
			"660", "665", "701", "710", "711", "715", "716", "722", "730",
			"731", "732", "733", "734", "740", "750", "760", "771", "772",
			"775", "790", "800", "840", "841", "850", "860", "870", "880",
			"899" };

	private String[] validFunksjonerForOsloKommuner = { "100", "110", "120",
			"121", "130", "170", "171", "172", "173", "180", "190", "201",
			"202", "211", "213", "215", "221", "222", "223", "231", "232",
			"233", "234", "241", "242", "243", "244", "251", "252", "253",
			"254", "256", "261", "265", "273", "275", "276", "281", "283",
			"285", "290", "301", "302", "303", "315", "320", "321",
			"325", "329", "330", "332", "335", "338", "339", "340", "345",
			"350", "353", "354", "355", "360", "365", "370", "373", "375",
			"377", "380", "381", "383", "385", "386", "390", "392", "393",
			"400", "410", "420", "421", "430", "460", "465", "470", "471",
			"472", "473", "480", "490", "510", "515", "520", "521", "522",
			"523", "524", "525", "526", "527", "528", "529", "530", "531",
			"532", "533", "554", "559", "561", "562", "570", "581", "590",
			"660", "665", "691", "692", "693", "694", "696", "701", "710",
			"711", "715", "716", "722", "730", "731", "732", "733", "734",
			"740", "750", "760", "771", "772", "775", "790", "800", "840",
			"841", "850", "860", "870", "880", "899" };

//	private String[] validFunksjonerForSpecialProveordning = { "100", "110",
//			"120", "121", "130", "170", "171", "172", "173", "180", "190",
//			"201", "202", "211", "213", "215", "221", "222", "223", "231",
//			"232", "233", "234", "241", "242", "243", "244", "251", "252",
//			"253", "254", "256", "261", "265", "273", "275", "276", "281",
//			"283", "285", "290", "301", "303", "304", "305", "315", "320",
//			"321", "325", "329", "330", "332", "335", "338", "339", "340",
//			"345", "350", "353", "354", "355", "360", "365", "370", "373",
//			"375", "377", "380", "381", "383", "385", "386", "390", "392",
//			"393", "800", "840", "841", "850", "860", "870", "880", "899" };

	private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

	public ControlFunksjoner() {
		spesielleKommuner.put("160100", "Trondheim");

		osloKommuner.put("030100", "030100");
		osloKommuner.put("030101", "030101");
		osloKommuner.put("030102", "030102");
		osloKommuner.put("030103", "030103");
		osloKommuner.put("030104", "030104");
		osloKommuner.put("030105", "030105");
		osloKommuner.put("030106", "030106");
		osloKommuner.put("030107", "030107");
		osloKommuner.put("030108", "030108");
		osloKommuner.put("030109", "030109");
		osloKommuner.put("030110", "030110");
		osloKommuner.put("030111", "030111");
		osloKommuner.put("030112", "030112");
		osloKommuner.put("030113", "030113");
		osloKommuner.put("030114", "030114");
		osloKommuner.put("030115", "030115");
		osloKommuner.put("030199", "030199");

//		spesielleProveordning.put("030100", "Oslokommune");
//		spesielleProveordning.put("120100", "Bergen");
//		spesielleProveordning.put("160100", "Trondheim");
//		spesielleProveordning.put("110300", "Stavanger");
//		spesielleProveordning.put("021900", "Bærum");
//		spesielleProveordning.put("100100", "Kristiansand");
//		spesielleProveordning.put("010600", "Fredrikstad");
//		spesielleProveordning.put("110200", "Sandnes");
//		spesielleProveordning.put("190200", "Tromsø");
//		spesielleProveordning.put("060200", "Drammen");
//		spesielleProveordning.put("022000", "Asker");
//		spesielleProveordning.put("010500", "Sarpsborg");
//		spesielleProveordning.put("080600", "Skien");
//		spesielleProveordning.put("023100", "Skedsmo");
//		spesielleProveordning.put("180400", "Bodø");
//		spesielleProveordning.put("150400", "Ålesund");
//		spesielleProveordning.put("070600", "Sandefjord");
//		spesielleProveordning.put("090600", "Arendal");
//		spesielleProveordning.put("070900", "Larvik");
//		spesielleProveordning.put("070400", "Tønsberg");
//		spesielleProveordning.put("114900", "Karmøy");
//		spesielleProveordning.put("110600", "Haugesund");
//		spesielleProveordning.put("023000", "Lørenskog");
//		spesielleProveordning.put("080500", "Porsgrunn");
//		spesielleProveordning.put("023500", "Ullensaker");
//		spesielleProveordning.put("041200", "Ringsaker");
//		spesielleProveordning.put("010400", "Moss");
//		spesielleProveordning.put("010100", "Halden");
//		spesielleProveordning.put("021300", "Ski");
//		spesielleProveordning.put("050200", "Gjøvik");
//		spesielleProveordning.put("040300", "Hamar");
//		spesielleProveordning.put("060500", "Ringerike");
//		spesielleProveordning.put("124700", "Askøy");
//		spesielleProveordning.put("050100", "Lillehammer");
//		spesielleProveordning.put("070100", "Horten");
//		spesielleProveordning.put("060400", "Kongsberg");
//		spesielleProveordning.put("021700", "Oppegård");
//		spesielleProveordning.put("150200", "Molde");
//		spesielleProveordning.put("112400", "Sola");
//		spesielleProveordning.put("183300", "Rana");
//		spesielleProveordning.put("062600", "Lier");
//		spesielleProveordning.put("124600", "Fjell");
//		spesielleProveordning.put("190300", "Harstad");
//		spesielleProveordning.put("150500", "Kristiansund");
//		spesielleProveordning.put("062500", "Nedre Eiker");
//		spesielleProveordning.put("023700", "Eidsvoll");
//		spesielleProveordning.put("171400", "Stjørdal");
//		spesielleProveordning.put("023300", "Nittedal");
//		spesielleProveordning.put("090400", "Grimstad");
//		spesielleProveordning.put("170200", "Steinkjer");
//		spesielleProveordning.put("072200", "Nøtterøy");
//		spesielleProveordning.put("062700", "Røyken");
//		spesielleProveordning.put("042700", "Elverum");
//		spesielleProveordning.put("023600", "Nes");
//		spesielleProveordning.put("041700", "Stange");
//		spesielleProveordning.put("201200", "Alta");
	}

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		boolean lineHasError = false;

		String funksjon = RecordFields.getFunksjon(line);

		// Kontrollen skal ikke foretas hvis belop = 0 og funksjon er definert.
		try {
			int belop = RecordFields.getBelopIntValue(line);
			if (belop == 0 && funksjon.trim().length() > 0) {
				return false;
			}
		} catch (Exception e) {
			// Returnerer her ogsaa. Gir ikke mening med kontroll
			// hvis belop ikke er angitt.
			return false;
		}

		if (isSpecialKommune(region)) {
			if (!validFunksjonForSpecialKommune(funksjon))
				lineHasError = true;

		} else if (isOsloKommune(region)) {
			if (!validFunksjonForOsloKommune(funksjon))
				lineHasError = true;

//		} else if (isSpecialProveordning(region)) {
//			if (!validFunksjonForSpecialProveordning(funksjon))
//				lineHasError = true;

		} else {
			if (!validFunksjon(funksjon))
				lineHasError = true;
		}

		if (lineHasError) {
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = funksjon;
			invalidFunksjoner.add(container);
		}

		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 6, ugyldig bruk av funksjoner:" + lf + lf;
		int numOfRecords = invalidFunksjoner.size();
		if (numOfRecords > 0) {
			errorReport += "\tFeil: Ugyldig" + (numOfRecords == 1 ? "" : "e")
					+ " funksjon" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidFunksjoner.elementAt(i);
				errorReport += "\t\tfunksjon " + container[1] + " (Record nr. "
						+ container[0] + ")" + lf;
			}
		}
		errorReport += "\tKorreksjon: Rett opp feil funksjon med gyldig funksjon. For oversikt over gyldige funksjoner se gjeldende versjon av KOSTRA-kontoplanen."
				+ lf + lf;
		return errorReport;
	}

	public boolean foundError() {
		return (invalidFunksjoner.size() > 0);
	}

	public boolean isSpecialKommune(String region) {
		return spesielleKommuner.containsKey(region);
	}

	public boolean isOsloKommune(String region) {
		return osloKommuner.containsKey(region);
	}

	public boolean isSpecialProveordning(String region) {
		return spesielleProveordning.containsKey(region);
	}

//	public boolean isSpecialProveordningFearre20000(String region) {
//		return !spesielleProveordning.containsKey(region);
//	}

	public boolean validFunksjon(String funksjon) {
		for (int i = validFunksjoner.length - 1; i >= 0; i--) {
			if (funksjon.equalsIgnoreCase(validFunksjoner[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean validFunksjonForSpecialKommune(String funksjon) {
		for (int i = validFunksjonerForSpecialKommuner.length - 1; i >= 0; i--) {
			if (funksjon.equalsIgnoreCase(validFunksjonerForSpecialKommuner[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean validFunksjonForOsloKommune(String funksjon) {
		for (int i = validFunksjonerForOsloKommuner.length - 1; i >= 0; i--) {
			if (funksjon.equalsIgnoreCase(validFunksjonerForOsloKommuner[i])) {
				return true;
			}
		}
		return false;
	}

//	public boolean validFunksjonForSpecialProveordning(String funksjon) {
//		for (int i = validFunksjonerForSpecialProveordning.length - 1; i >= 0; i--) {
//			if (funksjon.equalsIgnoreCase(validFunksjonerForSpecialProveordning[i])) {
//				return true;
//			}
//		}
//		return false;
//	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}
