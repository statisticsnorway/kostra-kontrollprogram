package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * $Log: RecordFields.java,v $
 * Revision 1.1  2013/07/09 raz
 * Oppdatert for 52B.
 */
 
final class RecordFields 
{
  static String getFieldValue (String record, int fieldNo)
  {
    String fieldValue;
    
    switch (fieldNo)
    {
      case 1:
        fieldValue = record.substring(0, 6);
        break;
      case 2:
        fieldValue = record.substring(6, 9);
        break;
      case 3:
        fieldValue = record.substring(9, 15);
        break;
      case 4:
        fieldValue = record.substring(15, 45);
        break;
      case 5:
        fieldValue = record.substring(45, 53);
        break;
      case 6:
        fieldValue = record.substring(53, 54);
        break;
      case 7:
        fieldValue = record.substring(54, 55);
        break;
      case 81:
        fieldValue = record.substring(55, 58);
        break;
      case 82:
        fieldValue = record.substring(58, 61);
        break;
      case 91:
        fieldValue = record.substring(61, 64);
        break;
      case 92:
        fieldValue = record.substring(64, 67);
        break;
      case 101:
        fieldValue = record.substring(67, 70);
        break;
      case 102:
        fieldValue = record.substring(70, 73);
        break;
      case 11:
        fieldValue = record.substring(73, 75);
        break;
      case 12:
        fieldValue = record.substring(75, 76);
        break;
      case 13:
        fieldValue = record.substring(76, 77);
        break;
      case 14:
        fieldValue = record.substring(77, 85);
        break;
      default:
        fieldValue = null;        
    }
    return fieldValue;
  }

  static String getRegionNr (String record)
  {
    return getFieldValue (record, 1);
  }

  static String getKontornummer (String record)
  {
    return getFieldValue (record, 2);
  }

  static String getDeltakerNr (String record)
  {
    return getFieldValue (record, 21);
  }

  static String getGruppeNr (String record)
  {
    return getFieldValue (record, 3);
  }

  static String getAllNumericalFieldsConcatenated (String record)
  {                                                       //Felt:
    String numericalFields = record.substring(0, 15)    + //1-3
    						 record.substring(46, 85);    //5-14
//                             record.substring(54, 77);    //6-13
//                             record.substring(95, 98)   + //8-9
//                             record.substring(128, 143) + //10-11.14
//                             record.substring(173, 185) + //12-18
//                             record.substring(215, 270) + //19-27.14
//                             record.substring(300, 317);       //28-30
    return numericalFields;
  }
}
