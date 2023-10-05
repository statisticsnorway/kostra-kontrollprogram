package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import java.time.LocalDate
import java.time.Year

class Tiltak09Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Tiltak09(),
            expectedSeverity = Severity.WARNING,
            expectedContextId = tiltakTypeInTest.id,
            ForAllRowItem(
                "individ without tiltak",
                individInTest.copy(startDato = measureStartDateInTest)
            ),
            ForAllRowItem(
                "individ with single tiltak",
                individInTest.copy(
                    startDato = measureStartDateInTest,
                    tiltak = mutableListOf(tiltakTypeInTest.copy(startDato = measureStartDateInTest))
                )
            ),
            ForAllRowItem(
                "individ with single plasseringstiltak",
                individInTest.copy(
                    startDato = measureStartDateInTest,
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        )
                    )
                )
            ),
            ForAllRowItem(
                "two overlapping plasseringstiltak, less than 90 days",
                individInTest.copy(
                    startDato = measureStartDateInTest,
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest,
                            sluttDato = null,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest,
                            sluttDato = measureStartDateInTest.plusDays(89),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
                        )
                    )
                )
            ),
            ForAllRowItem(
                "two overlapping plasseringstiltak, more than 90 days",
                individInTest.copy(
                    startDato = measureStartDateInTest,
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest,
                            sluttDato = null,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest,
                            sluttDato = measureStartDateInTest.plusDays(100),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
                        )
                    )
                ),
                expectedErrorMessage = "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder"
            ),
            ForAllRowItem(
                "three overlapping plasseringstiltak, more than 90 days",
                individInTest.copy(
                    startDato = measureStartDateInTest,
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest,
                            sluttDato = null,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest.plusDays(1),
                            sluttDato = measureStartDateInTest.plusDays(91),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = measureStartDateInTest.plusDays(2),
                            sluttDato = measureStartDateInTest.plusDays(92),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
                        )
                    )
                ),
                expectedErrorMessage = "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder"
            )
        )
    )
}) {
    companion object {
        private val measureStartDateInTest = LocalDate.of(Year.now().value - 1, 1, 1)
    }
}
