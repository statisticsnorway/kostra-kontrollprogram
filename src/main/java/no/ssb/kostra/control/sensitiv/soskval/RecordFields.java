package no.ssb.kostra.control.sensitiv.soskval;

/*
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
        fieldValue = record.substring(29, 30);
        break;
      case 8:
        fieldValue = record.substring(30, 31);
        break;
      case 91:
        fieldValue = record.substring(31, 32);
        break;
      case 92:
        fieldValue = record.substring(32, 34);
        break;
      case 10:
        fieldValue = record.substring(34, 40);
        break;
      case 11:
        fieldValue = record.substring(40, 46);
        break;
      case 12:
        fieldValue = record.substring(46, 52);
        break;
      case 141:
        fieldValue = record.substring(52, 53);
        break;
      case 142:
        fieldValue = record.substring(53, 57);
        break;
      case 143:
        fieldValue = record.substring(57, 58);
        break;
      case 151:
        fieldValue = record.substring(58, 59);
        break;
      case 152:
        fieldValue = record.substring(59, 60);
        break;
      case 153:
        fieldValue = record.substring(60, 61);
        break;
      case 154:
        fieldValue = record.substring(61, 62);
        break;
      case 155:
        fieldValue = record.substring(62, 63);
        break;
      case 171:
        fieldValue = record.substring(63, 65);
        break;
      case 172:
        fieldValue = record.substring(65, 67);
        break;
      case 173:
        fieldValue = record.substring(67, 69);
        break;
      case 174:
        fieldValue = record.substring(69, 71);
        break;
      case 175:
        fieldValue = record.substring(71, 73);
        break;
      case 176:
        fieldValue = record.substring(73, 75);
        break;
      case 177:
        fieldValue = record.substring(75, 77);
        break;
      case 178:
        fieldValue = record.substring(77, 79);
        break;
      case 179:
        fieldValue = record.substring(79, 81);
        break;
      case 1710:
        fieldValue = record.substring(81, 83);
        break;
      case 1711:
        fieldValue = record.substring(83, 85);
        break;
      case 1712:
        fieldValue = record.substring(85, 87);
        break;
      case 1713:
        fieldValue = record.substring(87, 89);
        break;
      case 1714:
        fieldValue = record.substring(89, 91);
        break;
      case 1715:
        fieldValue = record.substring(91, 93);
        break;
      case 1716:
        fieldValue = record.substring(93, 95);
        break;
      case 1717:
        fieldValue = record.substring(95, 97);
        break;
      case 1718:
        fieldValue = record.substring(97, 99);
        break;
      case 1719:
        fieldValue = record.substring(99, 129);
        break;
      case 201:
        fieldValue = record.substring(129, 130);
        break;
      case 202:
        fieldValue = record.substring(130, 131);
        break;
      case 203:
        fieldValue = record.substring(131, 132);
        break;
      case 204:
        fieldValue = record.substring(132, 133);
        break;
      case 205:
        fieldValue = record.substring(133, 134);
        break;
      case 206:
        fieldValue = record.substring(134, 135);
        break;
      case 211:
        fieldValue = record.substring(135, 137);
        break;
      case 212:
        fieldValue = record.substring(137, 139);
        break;
      case 213:
        fieldValue = record.substring(139, 141);
        break;
      case 214:
        fieldValue = record.substring(141, 143);
        break;
      case 215:
        fieldValue = record.substring(143, 145);
        break;
      case 216:
        fieldValue = record.substring(145, 147);
        break;
      case 217:
        fieldValue = record.substring(147, 149);
        break;
      case 218:
        fieldValue = record.substring(149, 151);
        break;
      case 219:
        fieldValue = record.substring(151, 153);
        break;
      case 2110:
        fieldValue = record.substring(153, 155);
        break;
      case 2111:
        fieldValue = record.substring(155, 157);
        break;
      case 2112:
        fieldValue = record.substring(157, 159);
        break;
      case 22:
        fieldValue = record.substring(159, 166);
        break;
      case 24:
        fieldValue = record.substring(166, 167);
        break;
      case 25:
        fieldValue = record.substring(167, 173);
        break;
      case 261:
        fieldValue = record.substring(173, 175);
        break;
      case 262:
        fieldValue = record.substring(175, 177);
        break;
      case 263:
        fieldValue = record.substring(177, 179);
        break;
      case 264:
        fieldValue = record.substring(179, 181);
        break;
      case 265:
        fieldValue = record.substring(181, 183);
        break;
      case 266:
        fieldValue = record.substring(183, 185);
        break;
      case 267:
        fieldValue = record.substring(185, 187);
        break;
      case 268:
        fieldValue = record.substring(187, 189);
        break;
      case 269:
        fieldValue = record.substring(189, 191);
        break;
      case 2611:
        fieldValue = record.substring(191, 193);
        break;
      case 2612:
        fieldValue = record.substring(193, 223);
        break;
      case 27:
        fieldValue = record.substring(223, 233);
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

  static String getKjonn (String record)
  {
    return getFieldValue (record, 7);
  }

  static String getSivilstand (String record)
  {
    return getFieldValue (record, 8);
  }

  static String getSaksbehandlernr (String record)  { return getFieldValue (record, 27);  }

  static String getAllNumericalFieldsConcatenated (String record)
  {
    return record.substring(0,8) +
    	   record.substring(18,34) +
    	   record.substring(52,99) +
    	   record.substring(129, 167) +
    	   record.substring(173, 193);
  }
  
}
