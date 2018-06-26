package no.ssb.kostra.control.sensitiv.soskval;

final class RecordFields {

    static String getFieldValue(String record, int fieldNo) {
        String fieldValue;

        switch (fieldNo) {
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
            case 201:
                fieldValue = record.substring(63, 64);
                break;
            case 202:
                fieldValue = record.substring(64, 65);
                break;
            case 203:
                fieldValue = record.substring(65, 66);
                break;
            case 204:
                fieldValue = record.substring(66, 67);
                break;
            case 205:
                fieldValue = record.substring(67, 68);
                break;
            case 206:
                fieldValue = record.substring(68, 69);
                break;
            case 211:
                fieldValue = record.substring(69, 71);
                break;
            case 212:
                fieldValue = record.substring(71, 73);
                break;
            case 213:
                fieldValue = record.substring(73, 75);
                break;
            case 214:
                fieldValue = record.substring(75, 77);
                break;
            case 215:
                fieldValue = record.substring(77, 79);
                break;
            case 216:
                fieldValue = record.substring(79, 81);
                break;
            case 217:
                fieldValue = record.substring(81, 83);
                break;
            case 218:
                fieldValue = record.substring(83, 85);
                break;
            case 219:
                fieldValue = record.substring(85, 87);
                break;
            case 2110:
                fieldValue = record.substring(87, 89);
                break;
            case 2111:
                fieldValue = record.substring(89, 91);
                break;
            case 2112:
                fieldValue = record.substring(91, 93);
                break;
            case 22:
                fieldValue = record.substring(93, 100);
                break;
            case 24:
                fieldValue = record.substring(100, 101);
                break;
            case 25:
                fieldValue = record.substring(101, 107);
                break;
            case 261:
                fieldValue = record.substring(107, 109);
                break;
            case 262:
                fieldValue = record.substring(109, 111);
                break;
            case 263:
                fieldValue = record.substring(111, 113);
                break;
            case 264:
                fieldValue = record.substring(113, 115);
                break;
            case 265:
                fieldValue = record.substring(115, 117);
                break;
            case 266:
                fieldValue = record.substring(117, 119);
                break;
            case 267:
                fieldValue = record.substring(119, 121);
                break;
            case 268:
                fieldValue = record.substring(121, 123);
                break;
            case 269:
                fieldValue = record.substring(123, 125);
                break;
            case 2611:
                fieldValue = record.substring(125, 127);
                break;
            case 2612:
                fieldValue = record.substring(127, 157);
                break;
            case 27:
                fieldValue = record.substring(157, 167);
                break;

            default:
                fieldValue = null;
        }
        return fieldValue;
    }

    static String getKommunenummer(String record) {
        return getFieldValue(record, 1);
    }

    static String getOppgaveaar(String record) {
        return getFieldValue(record, 2);
    }

    static String getBydelsnummer(String record) {
        return getFieldValue(record, 3);
    }

    static String getDistriktsnummer(String record) {
        return getFieldValue(record, 4);
    }

    static String getJournalnummer(String record) {
        return getFieldValue(record, 5);
    }

    static String getFodselsnummer(String record) {
        return getFieldValue(record, 6);
    }

    static String getKjonn(String record) {
        return getFieldValue(record, 7);
    }

    static String getSivilstand(String record) {
        return getFieldValue(record, 8);
    }

    static String getSaksbehandlernr(String record) {
        return getFieldValue(record, 27);
    }

    static String getAllNumericalFieldsConcatenated(String record) {
        return record.substring(0, 8) +      /*felt   1 -  3   */
                record.substring(18, 34) +   /*felt   6 - 14.3 */
                record.substring(100, 100) + /*felt  24        */
                record.substring(107, 127);  /*felt 26.1 - 26.11 */
    }
}
