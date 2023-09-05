package no.ssb.kostra.area.sosial

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.STMND_9_COL_NAME
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.FieldDefinition

object SosialFelles {

    val calendarMonths = arrayOf(
        FieldDefinition(
            name = STMND_1_COL_NAME,
            size = 2,
            code = Code("01", "Januar")
        ),
        FieldDefinition(
            name = STMND_2_COL_NAME,
            size = 2,
            code = Code("02", "Februar")
        ),
        FieldDefinition(
            name = STMND_3_COL_NAME,
            size = 2,
            code = Code("03", "Mars")
        ),
        FieldDefinition(
            name = STMND_4_COL_NAME,
            size = 2,
            code = Code("04", "April")
        ),
        FieldDefinition(
            name = STMND_5_COL_NAME,
            size = 2,
            code = Code("05", "Mai")
        ),
        FieldDefinition(
            name = STMND_6_COL_NAME,
            size = 2,
            code = Code("06", "Juni")
        ),
        FieldDefinition(
            name = STMND_7_COL_NAME,
            size = 2,
            code = Code("07", "Juli")
        ),
        FieldDefinition(
            name = STMND_8_COL_NAME,
            size = 2,
            code = Code("08", "August")
        ),
        FieldDefinition(
            name = STMND_9_COL_NAME,
            size = 2,
            code = Code("09", "September")
        ),
        FieldDefinition(
            name = STMND_10_COL_NAME,
            size = 2,
            code = Code("10", "Oktober")
        ),
        FieldDefinition(
            name = STMND_11_COL_NAME,
            size = 2,
            code = Code("11", "November")
        ),
        FieldDefinition(
            name = STMND_12_COL_NAME,
            size = 2,
            code = Code("12", "Desember")
        )
    )
}