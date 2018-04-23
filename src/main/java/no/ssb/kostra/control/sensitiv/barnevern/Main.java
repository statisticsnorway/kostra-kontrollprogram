package no.ssb.kostra.control.sensitiv.barnevern;

/*
 * $Log: Main.java,v $
 * Revision 1.21  2010/10/19 09:05:40  pll
 * Klar for 2010-rapp. Ny kontroll: 41.
 *
 * Revision 1.20  2009/10/11 15:25:14  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.19  2009/10/07 07:06:35  pll
 * Fjernet kontroll 14 og 19.
 *
 * Revision 1.18  2008/10/27 10:18:47  pll
 * Nytt versjonsnummer, ny kontroll lagt til.
 * 2008-rapportering.
 *
 * Revision 1.17  2008/02/27 13:06:49  pll
 * Ny kontroll og nytt versjonsnummer.
 *
 * Revision 1.16  2008/01/11 13:52:10  pll
 * Kritiske feil markeres i kontrollrapporten.
 *
 * Revision 1.15  2007/12/02 12:56:51  pll
 * Metoden start() fanger opp IOException og Exception ifbm. innlesing av
 * filuttrekket. Ved fanging av Exception kastes et nytt
 * UnreadableDataException-objekt. IOException kastes pï¿½ nytt.
 *
 * Revision 1.14  2007/11/27 20:00:13  pll
 * Byttet String med StringBuffer av hensyn til ytelse.
 *
 * Revision 1.13  2007/11/14 09:28:03  pll
 * Skriver feilmeldinger til System.out isteden for System.err.
 * Bruker exit-kode definert i no.ssb.kostra.control.Constants.
 *
 * Revision 1.12  2007/11/09 13:19:04  pll
 * Nytt versjonsnummer.
 *
 * Revision 1.11  2007/11/06 12:43:28  pll
 * Nytt versjonsnummer.
 *
 * Revision 1.10  2007/11/05 15:37:30  pll
 * Nytt versjonsnummer ifbm. bugfix 2 i K20b.
 *
 * Revision 1.9  2007/11/05 12:57:53  pll
 * Nytt versjonsnummer ifbm. bugfix i K20b.
 *
 * Revision 1.8  2007/10/26 09:07:29  pll
 * Nytt versjonsnummer.
 * Metoden start returnerer int-verdi som angir hvilke feiltyper som er funnet.
 * Forbedret hï¿½ndtering av tomme filer; returnerer CRITICAL_ERROR.
 *
 * Revision 1.7  2007/10/23 08:17:25  pll
 * Lagt til ny kontroll.
 * Nytt versjonsnummer 03.
 *
 * Revision 1.6  2007/10/22 10:54:43  pll
 * Nytt versjonsnummer.
 *
 * Revision 1.5  2007/10/15 13:28:27  pll
 * Fjernet kontroll 32.
 *
 * Revision 1.4  2007/10/12 13:59:48  pll
 * Nye kontroller in progress...
 *
 * Revision 1.3  2007/10/08 09:42:12  pll
 * Modifisert for alternativ bruk av System.in/System.out i steden for filer.
 *
 * Revision 1.2  2007/10/05 04:40:35  pll
 * Benytter no.ssb.kostra.utils.Regioner isteden for
 * no.ssb.kostra.utils.Regioner2006.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.4  2006/09/25 11:12:55  lwe
 * Lagt til control 35 og 36- og utvidet arrayen
 *
 * Revision 1.3  2006/09/25 09:58:44  lwe
 * Lagt til control 35 og 36
 *
 * Revision 1.2  2006/09/22 09:13:46  lwe
 * Oppdatert ï¿½rgang
 *
 * Revision 1.1  2006/09/22 08:18:23  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert ï¿½rstallene
 *
 * Revision 1.3  2006/01/05 08:11:54  lwe
 * added logmessage
 *
 */

import java.io.*;
import java.util.*;

import no.ssb.kostra.control.*;

public final class Main {
	private String regionNumber;
	private File sourceFile;
	private File reportFile;

	public Main(String regionNumber, File sourceFile, File reportFile) {
		this.regionNumber = regionNumber;
		this.sourceFile = sourceFile;
		this.reportFile = reportFile;
	}

	public int start() throws Exception {
		Map<String, String> avgiver = new TreeMap<String, String>();
		ErrorReport er = new ErrorReport();
		XMLReader r = new XMLReader();

		try {
			r.setRegion(regionNumber);
			// We can add several handlers which are triggered for a given node
			// name. The complete sub-dom of this node is then parsed and made
			// available as a StructuredNode
			r.addHandler("Avgiver", new AvgiverNodeHandler(er, regionNumber,
					avgiver));
			r.addHandler("Individ", new IndividNodeHandler(er, regionNumber,
					avgiver));

			if (sourceFile != null) {
				r.parse(new FileInputStream(sourceFile));
			} else {
				// bruker standard in i setedet for å åpne fil for så å lese innhold
				r.parse(System.in);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
			// throw e;

		} catch (Exception e) {
			e.printStackTrace();
			// throw new UnreadableDataException();
		}

		try {
			String report = er.generateReport(regionNumber, sourceFile,
					reportFile);

			if (reportFile != null) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						this.reportFile));
				writer.write(report);
				writer.close();
			} else {
				System.out.println(report);
			}
		} catch (Exception e) {
			System.out.println("Can't write report file: ");
			System.out.println(e.toString());
			e.printStackTrace();
			System.exit(Constants.IO_ERROR);
		}

		IndividNodeHandler.reset();

		return er.getErrorType();
	}
}
