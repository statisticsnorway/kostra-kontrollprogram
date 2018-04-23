package no.ssb.kostra.utils;

import java.util.*;

public class DuplicateChecker 
{
  private HashMap<String, Vector<Integer>> hashMap = new HashMap<String, Vector<Integer>>();
  private boolean duplicatesFound = false;

  public DuplicateChecker()
  {
  }

  public boolean isDuplicate (String key, int lineNumber)
  {
    boolean isDuplicate = hashMap.containsKey(key);
    Vector<Integer> container;

    if (isDuplicate)
    {
      duplicatesFound = true;
      container = (Vector<Integer>) hashMap.get(key);
      container.add(new Integer (lineNumber));
    }
    else
    {
      container = new Vector<Integer>();
      container.add(new Integer (lineNumber));
      hashMap.put(key, container);
    }
    return isDuplicate;
  }

  public boolean duplicatesFound()
  {
    return duplicatesFound;
  }

  public HashMap<Integer, Vector<Integer>> getDuplicateLineNumbers()
  {
    HashMap<Integer, Vector<Integer>> duplicateLineNumbers = new HashMap<Integer, Vector<Integer>>();
    Set<String> keys = hashMap.keySet();
    Iterator<String> keyIterator = keys.iterator();
    Vector<Integer> container;
    Vector<Integer> lines;
    while (keyIterator.hasNext())
    {
      container = (Vector<Integer>) hashMap.get(keyIterator.next());
      if (container.size() > 1)
      {
        Iterator<Integer> lineNumberIterator = container.iterator();
        Integer firstLine = (Integer) lineNumberIterator.next();
        lines = new Vector<Integer>();
        while (lineNumberIterator.hasNext())
        {
          lines.add(lineNumberIterator.next());
        }
        duplicateLineNumbers.put(firstLine, lines);
      }
    }
    return duplicateLineNumbers;
  }
}