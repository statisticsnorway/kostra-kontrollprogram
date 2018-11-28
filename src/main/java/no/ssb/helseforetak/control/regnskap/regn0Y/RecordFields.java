package no.ssb.helseforetak.control.regnskap.regn0Y;


final class RecordFields {
    static String getFieldValue(String record, int fieldNo) {
        String fieldValue;

        switch (fieldNo) {
            case 1:
                fieldValue = record.substring(0, 2);
                break;
            case 2:
                fieldValue = record.substring(2, 6);
                break;
            case 3:
                fieldValue = record.substring(6, 7);
                break;
            case 4:
                fieldValue = record.substring(7, 13);
                break;
            case 5:
                fieldValue = record.substring(13, 22);
                break;
            case 6:
                fieldValue = record.substring(22, 31);
                break;
            case 7:
                fieldValue = record.substring(31, 32);
                break;
            case 8:
                fieldValue = record.substring(32, 36);
                break;
            case 9:
                fieldValue = record.substring(36, 39);
                break;
            case 10:
                fieldValue = record.substring(39, 48);
                break;
            default:
                fieldValue = null;
        }
        return fieldValue;
    }

    static String getRegnskapstype(String record) {
        return getFieldValue(record, 1);
    }

    static String getAargang(String record) {
        return getFieldValue(record, 2);
    }

    static String getKvartal(String record) {
        return getFieldValue(record, 3);
    }

    static String getRegion(String record) {
        return getFieldValue(record, 4);
    }

    static String getOrgNummer(String record) {
        return getFieldValue(record, 5);
    }

    static String getForetaksNummer(String record) {
        return getFieldValue(record, 6);
    }

    static String getFunksjon(String record) {
        String funksjon = getFieldValue(record, 8);
        //Koder for funksjoner er venstrejusterte.
        //Ubrukte posisjoner er blanke. Fjerner disse hvis de finnes.
        int index = funksjon.indexOf(" ");
        index = (index == -1 ? funksjon.length() : index);
        return funksjon.substring(0, index);
    }

    static String getKontokode(String record) {
        String art = getFieldValue(record, 9);
        //Koder for kontokoder er venstrejusterte.
        //Ubrukte posisjoner er blanke. Fjerner disse hvis de finnes.
        int index = art.indexOf(" ");
        index = (index == -1 ? art.length() : index);
        return art.substring(0, index);
    }

    static String getBelop(String record) {
        String belop = getFieldValue(record, 10);
        //Belop er hoyrejusterte med evt. fortegn i posisjonen foran belopet.
        //Ubrukte posisjoner er blanke. Fjerner disse hvis de finnes.
        int index = belop.lastIndexOf(" ");
        index = (index == -1 ? 0 : index + 1);
        return belop.substring(index);
    }

    static int getAargangIntValue(String record) throws Exception {
        return (new Integer(getAargang(record))).intValue();
    }

    static int getKontokodeIntValue(String record) throws Exception {
        return (new Integer(getKontokode(record))).intValue();
    }

    static int getFunksjonIntValue(String record) throws Exception {
        String funksjon = getFunksjon(record);
        return (new Integer(funksjon)).intValue();
    }

    static int getBelopIntValue(String record) throws Exception {
        String belop = getBelop(record);
        return (new Integer(belop)).intValue();
    }
}