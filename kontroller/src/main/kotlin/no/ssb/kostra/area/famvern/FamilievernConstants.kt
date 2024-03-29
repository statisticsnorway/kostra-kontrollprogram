package no.ssb.kostra.area.famvern

object FamilievernConstants {
    data class KontorFylkeRegionMapping(
        val kontor: String,
        val fylke: String,
        val region: String
    )

    val kontorFylkeRegionMappingList = listOf(
        KontorFylkeRegionMapping("017", "30", "667600"),
        KontorFylkeRegionMapping("023", "30", "667600"),
        KontorFylkeRegionMapping("024", "30", "667600"),
        KontorFylkeRegionMapping("025", "30", "667600"),
        KontorFylkeRegionMapping("027", "30", "667600"),
        KontorFylkeRegionMapping("030", "03", "667600"),
        KontorFylkeRegionMapping("037", "03", "667600"),
        KontorFylkeRegionMapping("038", "03", "667600"),
        KontorFylkeRegionMapping("039", "03", "667600"),
        KontorFylkeRegionMapping("046", "34", "667600"),
        KontorFylkeRegionMapping("047", "34", "667600"),
        KontorFylkeRegionMapping("052", "34", "667600"),
        KontorFylkeRegionMapping("061", "30", "667500"),
        KontorFylkeRegionMapping("065", "30", "667500"),
        KontorFylkeRegionMapping("071", "38", "667500"),
        KontorFylkeRegionMapping("073", "38", "667500"),
        KontorFylkeRegionMapping("081", "38", "667500"),
        KontorFylkeRegionMapping("082", "38", "667500"),
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
        KontorFylkeRegionMapping("192", "54", "667200"),
        KontorFylkeRegionMapping("193", "54", "667200"),
        KontorFylkeRegionMapping("194", "54", "667200"),
        KontorFylkeRegionMapping("202", "54", "667200"),
        KontorFylkeRegionMapping("203", "54", "667200"),
        KontorFylkeRegionMapping("205", "54", "667200")
    )
}