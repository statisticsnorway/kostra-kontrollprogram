package no.ssb.kostra.control.sensitiv.famvern;

/*
 * Revision 1.1  2013/07/03 10:30:00  raz
 * Recordlengde = 151.
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
        fieldValue = record.substring(0, 6);
        break;
      case 2:
        fieldValue = record.substring(6, 9);
        break;
      case 3:
        fieldValue = record.substring(9, 18);
        break;
      case 4:
        fieldValue = record.substring(18, 26);
        break;
      case 5:
        fieldValue = record.substring(26, 27);
        break;
      case 6:
        fieldValue = record.substring(27, 28);
        break;
      case 7:
        fieldValue = record.substring(28, 29);
        break;
      case 8:
        fieldValue = record.substring(29, 33);
        break;
      case 9:
        fieldValue = record.substring(33, 34);
        break;
      case 91:
        fieldValue = record.substring(34, 35);
        break;
      case 10:
        fieldValue = record.substring(35, 36);
        break;
      case 11:
        fieldValue = record.substring(36, 37);
        break;
      case 12:
        fieldValue = record.substring(37, 38);
        break;
      case 13:
        fieldValue = record.substring(38, 39);
        break;
      case 14:
        fieldValue = record.substring(39, 40);
        break;
      case 15:
        fieldValue = record.substring(40, 41);
        break;
      case 161:
        fieldValue = record.substring(41, 42);
        break;
      case 162:
        fieldValue = record.substring(42, 43);
        break;
      case 163:
        fieldValue = record.substring(43, 44);
        break;
      case 164:
        fieldValue = record.substring(44, 45);
        break;
      case 165:
        fieldValue = record.substring(45, 46);
        break;
      case 166:
        fieldValue = record.substring(46, 47);
        break;
      case 167:
        fieldValue = record.substring(47, 48);
        break;
      case 168:
        fieldValue = record.substring(48, 49);
        break;
      case 17:
        fieldValue = record.substring(49, 57);
        break;
      case 18:
        fieldValue = record.substring(57, 58);
        break;
      case 191:
    	fieldValue = record.substring(58, 59);
        break;
      case 192:
      	fieldValue = record.substring(59, 60);
        break;
      case 193:
      	fieldValue = record.substring(60, 61);
        break;
      case 194:
      	fieldValue = record.substring(61, 62);
        break;
      case 195:
       	fieldValue = record.substring(62, 63);
        break;
      case 196:
       	fieldValue = record.substring(63, 64);
        break;
      case 197:
       	fieldValue = record.substring(64, 65);
        break;
      case 198:
       	fieldValue = record.substring(65, 66);
        break;
      case 199:
       	fieldValue = record.substring(66, 67);
        break;
      case 1910:
       	fieldValue = record.substring(67, 68);
        break;
      case 1911:
       	fieldValue = record.substring(68, 69);
        break;
      case 1912:
       	fieldValue = record.substring(69, 70);
        break;
      case 1913:
    	fieldValue = record.substring(70, 71);
        break;
      case 1914:
    	fieldValue = record.substring(71, 72);
        break;
      case 1915:
    	fieldValue = record.substring(72, 73);
        break;
      case 1916:
    	fieldValue = record.substring(73, 74);
        break;
      case 1917:
    	fieldValue = record.substring(74, 75);
        break;
      case 1918:
    	fieldValue = record.substring(75, 76);
        break;
      case 20:
        fieldValue = record.substring(76, 77);
        break;
      case 21:
        fieldValue = record.substring(77, 78);
        break;
      case 221:
        fieldValue = record.substring(78, 79);
        break;
      case 222:
        fieldValue = record.substring(79, 80);
        break;
      case 223:
        fieldValue = record.substring(80, 81);
        break;
      case 224:
        fieldValue = record.substring(81, 82);
        break;
      case 225:
        fieldValue = record.substring(82, 83);
        break;
      case 226:
        fieldValue = record.substring(83, 84);
        break;
      case 227:
        fieldValue = record.substring(84, 85);
        break;
      case 228:
        fieldValue = record.substring(85, 86);
        break;
      case 231:
        fieldValue = record.substring(86, 88);
        break;
      case 232:
        fieldValue = record.substring(88, 90);
        break;
      case 233:
        fieldValue = record.substring(90, 92);
        break;
      case 234:
        fieldValue = record.substring(92, 94);
        break;
      case 235:
        fieldValue = record.substring(94, 96);
        break;
      case 236:
        fieldValue = record.substring(96, 98);
        break;
      case 237:
        fieldValue = record.substring(98, 100);
        break;
      case 238:
        fieldValue = record.substring(100, 102);
        break;
      case 239:
        fieldValue = record.substring(102, 104);
        break;
      case 241:
        fieldValue = record.substring(104, 107);
        break;
      case 242:
        fieldValue = record.substring(107, 110);
        break;
      case 251:
        fieldValue = record.substring(110, 113);
        break;
      case 252:
        fieldValue = record.substring(113, 116);
        break;
      case 261:
        fieldValue = record.substring(116, 119);
        break;
      case 262:
        fieldValue = record.substring(119, 122);
        break;
      case 271:
        fieldValue = record.substring(122, 125);
        break;
      case 272:
        fieldValue = record.substring(125, 128);
        break;
      case 28:
        fieldValue = record.substring(128, 129);
        break;
      case 291:
        fieldValue = record.substring(129, 130);
        break;
      case 292:
        fieldValue = record.substring(130, 131);
        break;
      case 293:
        fieldValue = record.substring(131, 132);
        break;
      case 294:
        fieldValue = record.substring(132, 133);
        break;
      case 295:
        fieldValue = record.substring(133, 134);
        break;
      case 296:
        fieldValue = record.substring(134, 135);
        break;
      case 297:
        fieldValue = record.substring(135, 136);
        break;
      case 298:
        fieldValue = record.substring(136, 137);
        break;
      case 299:
        fieldValue = record.substring(137, 138);
        break;
      case 2910:
        fieldValue = record.substring(138, 139);
        break;
      case 2911:
        fieldValue = record.substring(139, 140);
        break;
      case 30:
        fieldValue = record.substring(140, 141);
        break;
      case 31:
        fieldValue = record.substring(141, 143);
        break;
      case 32:
        fieldValue = record.substring(143, 151);
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

  static String getJournalnummer (String record)
  {
    return getFieldValue (record, 3);
  }

  static String getAllNumericalFieldsConcatenated (String record)
  {                                                       //Felt:
    String numericalFields = record.substring(0, 151);     //1-151

    return numericalFields;
  }
}