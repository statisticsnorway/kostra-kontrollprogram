/*
 * UnknownRegnskapException.java
 *
 * Created on 14. november 2007, 16:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.ssb.kostra.control.regnskap;

//import no.ssb.kostra.control.Constants;

/**
 *
 * @author pll
 */
public class UnknownRegnskapException extends Exception
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1301376754272109739L;
	private String message;
    
    
    public UnknownRegnskapException (String message) {
        super (message);
        this.message = message;
    }
    
    
    public String getExceptionMessage() {
        return message;
    }
        
} // End class UnknownRegnskapException.
