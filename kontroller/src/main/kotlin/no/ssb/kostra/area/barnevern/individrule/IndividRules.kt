package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.ValidationReportEntry

class IndividRules {

    fun validate(
        context: KostraIndividType,
        arguments: Arguments
    ): List<ValidationReportEntry> = rules.mapNotNull { it.validate(context, arguments) }.flatten()

    private val rules = setOf(
        Individ02a(),
        Individ02b(),
        Individ02d(),
        Individ03(),
        Individ06(),
        Individ07(),
        Individ08(),
        Individ09(),
        Individ10(),
        Individ11(),
        Individ12(),
        Individ19(),

        Lovhjemmel02(),
        Lovhjemmel03(),
        Lovhjemmel04(),

        Melder02(),

        Melding02a(),
        Melding02c(),
        Melding02d(),
        Melding02e(),
        Melding03(),
        Melding04(),
        Melding05(),

        Plan02a(),
        Plan02b(),
        Plan02c(),
        Plan02d(),
        Plan02e(),

        Saksinnhold02(),

        Tiltak02a(),
        Tiltak02b(),
        Tiltak02c(),
        Tiltak02d(),
        Tiltak02e(),

        Tiltak04(),
        Tiltak05(),
        Tiltak06(),
        Tiltak07(),
        Tiltak08(),
        Tiltak09(),

        Undersokelse02a(),
        Undersokelse02b(),
        Undersokelse02c(),
        Undersokelse02d(),
        Undersokelse02e(),
        Undersokelse03(),
        Undersokelse04(),
        Undersokelse07(),
        Undersokelse08(),

        Vedtak02()
    )
}