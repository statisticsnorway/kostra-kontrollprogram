package no.ssb.kostra.control.sensitiv.sosintro;

/*
 *
 */
 
final class RecordFields 
{
  static String getFieldValue (String record, int fieldNo)
  {
    String fieldValue;
    
    switch (fieldNo)
    {
      case 1:
        fieldValue = record.substring(0, 4);
        break;
      case 2:
        fieldValue = record.substring(4, 6);
        break;
      case 3:
        fieldValue = record.substring(6, 8);
        break;
      case 4:
        fieldValue = record.substring(8, 10);
        break;
      case 5:
        fieldValue = record.substring(10, 18);
        break;
      case 6:
        fieldValue = record.substring(18, 29);
        break;
      case 7:
        fieldValue = record.substring(29, 41);
        break;
      case 8:
        fieldValue = record.substring(41, 42);
        break;
      case 9:
        fieldValue = record.substring(42, 43);
        break;
      case 10:
        fieldValue = record.substring(43, 49);
        break;
      case 11:
        fieldValue = record.substring(49, 50);
        break;
      case 121:
        fieldValue = record.substring(50, 52);
        break;
      case 122:
        fieldValue = record.substring(52, 54);
        break;
      case 123:
        fieldValue = record.substring(54, 56);
        break;
      case 124:
        fieldValue = record.substring(56, 58);
        break;
      case 125:
        fieldValue = record.substring(58, 60);
        break;
      case 126:
        fieldValue = record.substring(60, 62);
        break;
      case 127:
        fieldValue = record.substring(62, 64);
        break;
      case 128:
        fieldValue = record.substring(64, 66);
        break;
      case 129:
        fieldValue = record.substring(66, 68);
        break;
      case 1210:
        fieldValue = record.substring(68, 70);
        break;
      case 1211: //ny i 2011
        fieldValue = record.substring(70, 72);
        break;
      case 13:
        fieldValue = record.substring(72, 74);
        break;
      case 14:
        fieldValue = record.substring(74, 80);
        break;
      case 1501:
        fieldValue = record.substring(80, 82);
        break;
      case 1502:
        fieldValue = record.substring(82, 84);
        break;
      case 1503:
        fieldValue = record.substring(84, 86);
        break;
      case 1504:
        fieldValue = record.substring(86, 88);
        break;
      case 1505:
        fieldValue = record.substring(88, 90);
        break;
      case 1506:
        fieldValue = record.substring(90, 92);
        break;
      case 1507:
        fieldValue = record.substring(92, 94);
        break;
      case 1508:
        fieldValue = record.substring(94, 96);
        break;
      case 1509:
        fieldValue = record.substring(96, 98);
        break;
      case 1510:
        fieldValue = record.substring(98, 100);
        break;
      case 1511:
        fieldValue = record.substring(100, 102);
        break;
      case 1512:
        fieldValue = record.substring(102, 104);
        break;
      case 1601:
        fieldValue = record.substring(104, 111);
        break;
      default:
        fieldValue = null;        
    }
    return fieldValue;
  }

  static String getKommunenummer (String record)
  {
    return getFieldValue (record, 1);
  }

  static String getOppgaveaar (String record)
  {
    return getFieldValue (record, 2);
  }

  static String getBydelsnummer (String record)
  {
    return getFieldValue (record, 3);
  }

  static String getDistriktsnummer (String record)
  {
    return getFieldValue (record, 4);
  }

  static String getJournalnummer (String record)
  {
    return getFieldValue (record, 5);
  }

  static String getFodselsnummer (String record)
  {
    return getFieldValue (record, 6);
  }

  static String getDUFnummer (String record)
  {
    return getFieldValue (record, 7);
  }

  static String getKjonn (String record)
  {
    return getFieldValue (record, 8);
  }

  static String getSivilstand (String record)
  {
    return getFieldValue (record, 9);
  }

  static String getAllNumericalFieldsConcatenated (String record)
  {
    return record.substring(0,10) + record.substring(18,111);
  }
}
