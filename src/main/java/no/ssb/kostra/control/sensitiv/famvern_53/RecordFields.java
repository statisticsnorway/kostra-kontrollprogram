package no.ssb.kostra.control.sensitiv.famvern_53;

/*
 * Revision 1.0  2013/08/26 15:00:00  raz
 * Recordlengde = 53.
 */

final class RecordFields 
{
  static String getFieldValue (String record, int fieldNo)
  {
    String fieldValue;
    
    switch (fieldNo)
    {
      case 1:
        fieldValue = record.substring(0, 2);
        break;
      case 2:
        fieldValue = record.substring(2, 5);
        break;
      case 51:
        fieldValue = record.substring(5, 8);
        break;
      case 52:
        fieldValue = record.substring(8, 11);
        break;
      case 61:
        fieldValue = record.substring(11, 14 );
        break;
      case 62:
        fieldValue = record.substring(14, 17 );
        break;
      case 71:
        fieldValue = record.substring(17, 20);
        break;
      case 72:
        fieldValue = record.substring(20, 23);
        break;
      case 91:
        fieldValue = record.substring(23, 26);
        break;
      case 92:
        fieldValue = record.substring(26, 29);
        break;
      case 101:
        fieldValue = record.substring(29, 32);
        break;
      case 102:
        fieldValue = record.substring(32, 35);
        break;
      case 111:
        fieldValue = record.substring(35, 38);
        break;
      case 112:
        fieldValue = record.substring(38, 41);
        break;
      case 121:
        fieldValue = record.substring(41, 44);
        break;
      case 122:
        fieldValue = record.substring(44, 47);
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
/***********************************************************/
  static int getFylkeIntValue (String record) throws Exception
  {
    return (new Integer (getRegionNr (record))).intValue();
  }
  
  static int getKontorIntValue (String record) throws Exception
  {
    return (new Integer (getKontornummer (record))).intValue();
  }
/***********************************************************/
  
  static String getKontornummer (String record)
  {
    return getFieldValue (record, 2);
  }

  static String getAllNumericalFieldsConcatenated (String record)
  {                                                       //Felt:
    String numericalFields = record.substring(0, 53);     //1-53

    return numericalFields;
  }
}