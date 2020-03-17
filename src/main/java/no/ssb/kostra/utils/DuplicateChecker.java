package no.ssb.kostra.utils;

import java.util.*;

public class DuplicateChecker 
{
  private HashMap<String, Vector<Integer>> hashMap = new HashMap<>();
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
      container = hashMap.get(key);
      container.add(lineNumber);
    }
    else
    {
      container = new Vector<>();
      container.add(lineNumber);
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
    HashMap<Integer, Vector<Integer>> duplicateLineNumbers = new HashMap<>();
    Set<String> keys = hashMap.keySet();
    Iterator<String> keyIterator = keys.iterator();
    Vector<Integer> container;
    Vector<Integer> lines;
    while (keyIterator.hasNext())
    {
      container = hashMap.get(keyIterator.next());
      if (container.size() > 1)
      {
        Iterator<Integer> lineNumberIterator = container.iterator();
        Integer firstLine = lineNumberIterator.next();
        lines = new Vector<>();
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