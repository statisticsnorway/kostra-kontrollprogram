package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
 */

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
                fieldValue = record.substring(29, 41);
                break;
            case 8:
                fieldValue = record.substring(41, 42);
                break;
            case 9:
                fieldValue = record.substring(42, 43);
                break;
            case 101:
                fieldValue = record.substring(43, 44);
                break;
            case 102:
                fieldValue = record.substring(44, 46);
                break;
            case 11:
                fieldValue = record.substring(46, 47);
                break;
            case 12:
                fieldValue = record.substring(47, 49);
                break;
            case 13:
                fieldValue = record.substring(49, 51);
                break;
            case 141:
                fieldValue = record.substring(51, 53);
                break;
            case 142:
                fieldValue = record.substring(53, 55);
                break;
            case 143:
                fieldValue = record.substring(55, 57);
                break;
            case 144:
                fieldValue = record.substring(57, 59);
                break;
            case 145:
                fieldValue = record.substring(59, 61);
                break;
            case 146:
                fieldValue = record.substring(61, 63);
                break;
            case 147:
                fieldValue = record.substring(63, 65);
                break;
            case 148:
                fieldValue = record.substring(65, 67);
                break;
            case 149:
                fieldValue = record.substring(67, 69);
                break;
            case 1410:
                fieldValue = record.substring(69, 71);
                break;
            case 1411:
                fieldValue = record.substring(71, 73);
                break;
            case 1412:
                fieldValue = record.substring(73, 75);
                break;
            case 151:
                fieldValue = record.substring(75, 82);
                break;
            case 152:
                fieldValue = record.substring(82, 89);
                break;
            case 161:
                fieldValue = record.substring(89, 96);
                break;
            case 162:
                fieldValue = record.substring(96, 103);
                break;
            case 163:
                fieldValue = record.substring(103, 110);
                break;
            case 164:
                fieldValue = record.substring(110, 117);
                break;
            case 165:
                fieldValue = record.substring(117, 124);
                break;
            case 166:
                fieldValue = record.substring(124, 131);
                break;
            case 167:
                fieldValue = record.substring(131, 138);
                break;
            case 168:
                fieldValue = record.substring(138, 145);
                break;
            case 169:
                fieldValue = record.substring(145, 152);
                break;
            case 1610:
                fieldValue = record.substring(152, 159);
                break;
            case 1611:
                fieldValue = record.substring(159, 166);
                break;
            case 1612:
                fieldValue = record.substring(166, 173);
                break;
            case 1613:
                fieldValue = record.substring(173, 180);
                break;
            case 1614:
                fieldValue = record.substring(180, 187);
                break;
            case 1615:
                fieldValue = record.substring(187, 194);
                break;
            case 1616:
                fieldValue = record.substring(194, 201);
                break;
            case 1617:
                fieldValue = record.substring(201, 208);
                break;
            case 1618:
                fieldValue = record.substring(208, 215);
                break;
            case 1619:
                fieldValue = record.substring(215, 222);
                break;
            case 1620:
                fieldValue = record.substring(222, 229);
                break;
            case 1621:
                fieldValue = record.substring(229, 236);
                break;
            case 1622:
                fieldValue = record.substring(236, 243);
                break;
            case 1623:
                fieldValue = record.substring(243, 250);
                break;
            case 1624:
                fieldValue = record.substring(250, 257);
                break;
            case 17:
                fieldValue = record.substring(257, 258);
                break;
            case 18:
                fieldValue = record.substring(258, 259);
                break;
            case 19:
                fieldValue = record.substring(259, 269);
                break;
            case 20:
                fieldValue = record.substring(269, 270);
                break;
            case 21:
                fieldValue = record.substring(270, 271);
                break;
            case 22:
                fieldValue = record.substring(271, 272);
                break;
            case 23:
                fieldValue = record.substring(272, 278);
                break;
            case 24:
                fieldValue = record.substring(278, 284);
                break;
            case 251:
                fieldValue = record.substring(284, 286);
                break;
            case 252:
                fieldValue = record.substring(286, 288);
                break;
            case 254:
                fieldValue = record.substring(288, 290);
                break;
            case 256:
                fieldValue = record.substring(290, 292);
                break;
            case 257:
                fieldValue = record.substring(292, 294);
                break;
            case 258:
                fieldValue = record.substring(294, 296);
                break;
            case 2510:
                fieldValue = record.substring(296, 298);
                break;
            case 2511:
                fieldValue = record.substring(298, 300);
                break;
            case 2514:
                fieldValue = record.substring(300, 302);
                break;
            case 2515:
                fieldValue = record.substring(302, 304);
                break;
            case 2516:
                fieldValue = record.substring(304, 306);
                break;
            case 26:
                fieldValue = record.substring(306, 312);
                break;
            case 271:
                fieldValue = record.substring(312, 314);
                break;
            case 272:
                fieldValue = record.substring(314, 316);
                break;
            case 273:
                fieldValue = record.substring(316, 318);
                break;
            case 274:
                fieldValue = record.substring(318, 320);
                break;
            case 281:
                fieldValue = record.substring(320, 322);
                break;
            case 282:
                fieldValue = record.substring(322, 324);
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

    static String getDUFnummer(String record) {
        return getFieldValue(record, 7);
    }

    static String getKjonn(String record) {
        return getFieldValue(record, 8);
    }

    static String getSivilstand(String record) {
        return getFieldValue(record, 9);
    }

    static String getBidrag(String record) {
        return getFieldValue(record, 151);
    }

    static String getLaan(String record) {
        return getFieldValue(record, 152);
    }

    static String getSaksbehandlernr(String record) {
        return getFieldValue(record, 19);
    }

    static String getAllNumericalFieldsConcatenated(String record) {
        return record.substring(0, 8)
                + record.substring(18, 29)
                + record.substring(41, 259)
                + record.substring(269, 272)
                + record.substring(284, 304)
                + record.substring(310, 322);
    }
}
