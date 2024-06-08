package no.ssb.kostra.area.famvern

object FamilievernConstants {
    data class KontorFylkeRegionMapping(
        val kontor: String,
        val fylke: String,
        val region: String
    )

    val kontorFylkeRegionMappingList = listOf(
        KontorFylkeRegionMapping("017", "31", "667600"),
        KontorFylkeRegionMapping("023", "32", "667600"),
        KontorFylkeRegionMapping("024", "32", "667600"),
        KontorFylkeRegionMapping("025", "32", "667600"),
        KontorFylkeRegionMapping("027", "32", "667600"),
        KontorFylkeRegionMapping("030", "03", "667600"),
        KontorFylkeRegionMapping("037", "03", "667600"),
        KontorFylkeRegionMapping("038", "03", "667600"),
        KontorFylkeRegionMapping("039", "03", "667600"),
        KontorFylkeRegionMapping("046", "34", "667600"),
        KontorFylkeRegionMapping("047", "34", "667600"),
        KontorFylkeRegionMapping("052", "34", "667600"),
        KontorFylkeRegionMapping("061", "33", "667500"),
        KontorFylkeRegionMapping("065", "33", "667500"),
        KontorFylkeRegionMapping("071", "39", "667500"),
        KontorFylkeRegionMapping("073", "39", "667500"),
        KontorFylkeRegionMapping("081", "40", "667500"),
        KontorFylkeRegionMapping("082", "40", "667500"),
        KontorFylkeRegionMapping("091", "42", "667500"),
        KontorFylkeRegionMapping("101", "42", "667500"),
        KontorFylkeRegionMapping("111", "11", "667400"),
        KontorFylkeRegionMapping("112", "11", "667400"),
        KontorFylkeRegionMapping("125", "46", "667400"),
        KontorFylkeRegionMapping("127", "46", "667400"),
        KontorFylkeRegionMapping("141", "46", "667400"),
        KontorFylkeRegionMapping("142", "46", "667400"),
        KontorFylkeRegionMapping("151", "15", "667300"),
        KontorFylkeRegionMapping("152", "15", "667300"),
        KontorFylkeRegionMapping("153", "15", "667300"),
        KontorFylkeRegionMapping("162", "50", "667300"),
        KontorFylkeRegionMapping("171", "50", "667300"),
        KontorFylkeRegionMapping("172", "50", "667300"),
        KontorFylkeRegionMapping("181", "18", "667200"),
        KontorFylkeRegionMapping("183", "18", "667200"),
        KontorFylkeRegionMapping("184", "18", "667200"),
        KontorFylkeRegionMapping("185", "18", "667200"),
        KontorFylkeRegionMapping("192", "55", "667200"),
        KontorFylkeRegionMapping("193", "55", "667200"),
        KontorFylkeRegionMapping("194", "55", "667200"),
        KontorFylkeRegionMapping("202", "56", "667200"),
        KontorFylkeRegionMapping("203", "56", "667200"),
        KontorFylkeRegionMapping("205", "56", "667200")
    )
}