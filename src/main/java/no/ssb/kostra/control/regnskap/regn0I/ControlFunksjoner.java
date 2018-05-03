package no.ssb.kostra.control.regnskap.regn0I;

import java.util.Vector;

import no.ssb.kostra.control.Constants;

/*
 * 
 */
final class ControlFunksjoner extends no.ssb.kostra.control.Control {
	private String[] spesielleKommuner = { "160100" };

	private String[] osloKommuner = { "030100", "030101", "030102", "030103",
			"030104", "030105", "030106", "030107", "030108", "030109",
			"030110", "030111", "030112", "030113", "030114", "030115",
			"030116", "030117", "030118", "030119", "030120", "030121",
			"030122", "030123", "030124", "030125", "030199", };

//	private String[] proveordningKommuner = { "010100", "010400", "010500",
//			"010600", "021300", "021700", "021900", "022000", "023000",
//			"023100", "023300", "023500", "023600", "023700", "040300",
//			"041200", "041700", "042700", "050100", "050200", "060200",
//			"060400", "060500", "062500", "062600", "062700", "070100",
//			"070400", "070600", "070900", "072200", "080500", "080600",
//			"090400", "090600", "100100", "110200", "110300", "110600",
//			"112400", "114900", "120100", "124600", "124700", "150200",
//			"150400", "150500", "170200", "171400", "180400", "183300",
//			"190200", "190300" };

	private String[] validFunksjoner = { "100", "110", "120", "121", "130",
			"170", "171", "172", "173", "180", "190", "201", "202", "211",
			"213", "215", "221", "222", "223", "231", "232", "233", "234",
			"241", "242", "243", "244", "251", "252", "253", "254", "256",
			"261", "265", "273", "275", "276", "281", "283", "285", "301",
			"302", "303", "315", "320", "321", "325", "329",
			"330", "332", "335", "338", "339", "340", "345", "350", "353",
			"354", "355", "360", "365", "370", "373", "375", "377", "380",
			"381", "383", "385", "386", "390", "392", "393",
			"841", "860", "870", "880", "899" };

	private String[] validFunksjonerForSpecialKommuner = { "100", "110", "120",
			"121", "130", "170", "171", "172", "173", "180", "190", "201",
			"202", "211", "213", "215", "221", "222", "223", "231", "232",
			"233", "234", "241", "242", "243", "244", "251", "252", "253",
			"254", "256", "261", "265", "273", "275", "276", "281", "283",
			"285", "301", "302", "303", "315", "320", "321", "325",
			"329", "330", "332", "335", "338", "339", "340", "345", "350",
			"353", "354", "355", "360", "365", "370", "373", "375", "377",
			"380", "381", "383", "385", "386", "390", "392", "393", "400",
			"410", "420", "421", "430", "460", "470", "471", "472", "473",
			"480", "490", "510", "515", "520", "521", "522", "523", "524",
			"525", "526", "527", "528", "529", "530", "531", "532", "533",
			"554", "559", "561", "562", "570", "581", "590", "660", "665",
			"701", "710", "711", "715", "716", "720", "721", "730", "731",
			"732", "733", "734", "740", "750", "760", "771", "772", "775",
			"790", "841", "860", "870", "880", "899" };

	private String[] validFunksjonerForOsloKommuner = { "100", "110", "120",
			"121", "130", "170", "171", "172", "173", "180", "190", "201",
			"202", "211", "213", "215", "221", "222", "223", "231", "232",
			"233", "234", "241", "242", "243", "244", "251", "252", "253",
			"254", "256", "261", "265", "273", "275", "276", "281", "283",
			"285", "301", "332", "302", "303", "315", "320",
			"321", "325", "329", "330", "335", "338", "339", "340", "345",
			"350", "353", "354", "355", "360", "365", "370", "373", "375",
			"377", "380", "381", "383", "385", "386", "390", "392", "393",
			"400", "410", "420", "421", "430", "460", "470", "471", "472",
			"473", "480", "490", "510", "515", "520", "521", "522", "523",
			"524", "525", "526", "527", "528", "529", "530", "531", "532",
			"533", "554", "559", "561", "562", "570", "581", "582", "590",
			"660", "665", "691", "692", "693", "694", "701", "710", "711",
			"715", "716", "720", "721", "730", "731", "732", "733", "734",
			"740", "750", "760", "771", "772", "775", "790", "841",
			"860", "870", "880", "899" };

//	private String[] validFunksjonerForProveordning = { "100", "110", "120",
//			"121", "130", "170", "171", "172", "173", "180", "190", "201",
//			"202", "211", "213", "215", "221", "222", "223", "231", "232",
//			"233", "234", "241", "242", "243", "244", "251", "252", "253",
//			"254", "256", "261", "265", "273", "275", "276", "281", "283",
//			"285", "301", "303", "304", "305", "315", "320", "321", "325",
//			"329", "330", "332", "335", "338", "339", "340", "345", "350",
//			"353", "354", "355", "360", "365", "370", "373", "375", "377",
//			"380", "381", "383", "385", "386", "390", "392", "393", "800",
//			"840", "841", "850", "860", "870", "880", "899" };

	private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

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
			lineHasError = !validFunksjonForSpecialKommune(funksjon);

		} else if (isOsloKommune(region)) {
			lineHasError = !validFunksjonForOsloKommune(funksjon);

//		} else if (isProveordningKommune(region)) {
//			lineHasError = !validFunksjonForProveordningKommune(funksjon);

		} else {
			lineHasError = !validFunksjon(funksjon);
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
		String errorReport = "Kontroll 7, Ugyldig bruk av funksjoner:" + lf + lf;
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
		errorReport += lf + "Korreksjon: Rett opp feil funksjon med gyldig funksjon. For oversikt over gyldige funksjoner se gjeldende versjon av KOSTRA-kontoplanen."
				+ lf + lf;
		return errorReport;
	}

	public boolean foundError() {
		return (invalidFunksjoner.size() > 0);
	}

	private boolean isSpecialKommune(String region) {
		for (int i = spesielleKommuner.length - 1; i >= 0; i--) {
			if (region.equalsIgnoreCase(spesielleKommuner[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isOsloKommune(String region) {
		for (int i = osloKommuner.length - 1; i >= 0; i--) {
			if (region.equalsIgnoreCase(osloKommuner[i])) {
				return true;
			}
		}
		return false;
	}

//	private boolean isProveordningKommune(String region) {
//		for (int i = proveordningKommuner.length - 1; i >= 0; i--) {
//			if (region.equalsIgnoreCase(proveordningKommuner[i])) {
//				return true;
//			}
//		}
//		return false;
//	}

	private boolean validFunksjon(String funksjon) {
		for (int i = validFunksjoner.length - 1; i >= 0; i--) {
			if (funksjon.equalsIgnoreCase(validFunksjoner[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean validFunksjonForSpecialKommune(String funksjon) {
		for (int i = validFunksjonerForSpecialKommuner.length - 1; i >= 0; i--) {
			if (funksjon.equalsIgnoreCase(validFunksjonerForSpecialKommuner[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean validFunksjonForOsloKommune(String funksjon) {
		for (int i = validFunksjonerForOsloKommuner.length - 1; i >= 0; i--) {
			if (funksjon.equalsIgnoreCase(validFunksjonerForOsloKommuner[i])) {
				return true;
			}
		}
		return false;
	}

//	private boolean validFunksjonForProveordningKommune(String funksjon) {
//		for (int i = validFunksjonerForProveordning.length - 1; i >= 0; i--) {
//			if (funksjon.equalsIgnoreCase(validFunksjonerForProveordning[i])) {
//				return true;
//			}
//		}
//		return false;
//	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}
