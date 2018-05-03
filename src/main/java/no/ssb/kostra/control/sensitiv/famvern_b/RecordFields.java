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
        fieldValue = record.substring(54, 56);
        break;
      case 81:
        fieldValue = record.substring(56, 59);
        break;
      case 82:
        fieldValue = record.substring(59, 62);
        break;
      case 91:
        fieldValue = record.substring(62, 65);
        break;
      case 92:
        fieldValue = record.substring(65, 68);
        break;
      case 101:
        fieldValue = record.substring(68, 71);
        break;
      case 102:
        fieldValue = record.substring(71, 74);
        break;
      case 11:
        fieldValue = record.substring(74, 76);
        break;
      case 12:
        fieldValue = record.substring(76, 77);
        break;
      case 13:
        fieldValue = record.substring(77, 78);
        break;
      case 14:
        fieldValue = record.substring(78, 86);
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
    						 record.substring(46, 86);    //5-14
//                             record.substring(54, 77);    //6-13
//                             record.substring(95, 98)   + //8-9
//                             record.substring(128, 143) + //10-11.14
//                             record.substring(173, 185) + //12-18
//                             record.substring(215, 270) + //19-27.14
//                             record.substring(300, 317);       //28-30
    return numericalFields;
  }
}
