package no.ssb.kostra.control;

public abstract class Control 
{
  protected final String lf = Constants.lineSeparator;

  public abstract boolean doControl(String line, int lineNumber, String region, String statistiskEnhet);

  public abstract String getErrorReport (int totalLineNumber);

  public abstract boolean foundError();

  public abstract int getErrorType();  
}
