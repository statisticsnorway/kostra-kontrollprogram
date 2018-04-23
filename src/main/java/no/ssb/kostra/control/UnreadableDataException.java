/*
 * UnreadableDataException.java
 *
 * Created on 2. desember 2007, 12:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.ssb.kostra.control;

import no.ssb.kostra.control.Constants;

/**
 *
 * @author pll
 */
public class UnreadableDataException extends Exception
{
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private String lf = Constants.lineSeparator;  
  private String message =  lf + "Klarer ikke å lese filuttrekket! "  + lf +
                            "Sjekk at filuttrekket kun inneholder data " + 
                            "i henhold til spesifikasjonen." + lf;

    
    /** Creates a new instance of UnreadableDataException */
    public UnreadableDataException()
    {
        super();
    }

    public String getExceptionMessage() {
        return message;
    }
    
}
