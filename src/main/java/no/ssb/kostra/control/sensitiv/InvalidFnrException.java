/*
 * InvalidFnrException.java
 *
 * Created on 28. september 2009, 08:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.ssb.kostra.control.sensitiv;

//import no.ssb.kostra.control.Constants;

/**
 *
 * @author pll
 */
public class InvalidFnrException extends Exception
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String message;
    
    
    public InvalidFnrException (String message) {
        super (message);
        this.message = message;
    }
    
    
    public String getExceptionMessage() {
        return message;
    }
        
} // End class InvalidFnrException.
