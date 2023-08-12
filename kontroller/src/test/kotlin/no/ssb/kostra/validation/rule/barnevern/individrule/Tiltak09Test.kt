package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleWithArgsTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.dateInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest

class Tiltak09Test : BehaviorSpec({
    include(
        validationRuleWithArgsTest(
            sut = Tiltak09(),
            expectedSeverity = Severity.WARNING,
            expectedContextId = tiltakTypeInTest.id,
            ForAllRowItem(
                "individ without tiltak",
                individInTest
            ),
            ForAllRowItem(
                "individ with single tiltak",
                individInTest.copy(
                    tiltak = mutableListOf(tiltakTypeInTest)
                )
            ),
            ForAllRowItem(
                "individ with single plasseringstiltak",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        )
                    )
                )
            ),
            ForAllRowItem(
                "two overlapping plasseringstiltak, less than 90 days",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = null,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = dateInTest.minusYears(1).plusDays(89),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
                        )
                    )
                )
            ),
            ForAllRowItem(
                "two overlapping plasseringstiltak, nore than 90 days",
                individInTest.copy(
                    tiltak = mutableListOf(
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = null,
                            kategori = kategoriTypeInTest.copy(kode = "1.1")
                        ),
                        tiltakTypeInTest.copy(
                            startDato = dateInTest.minusYears(1),
                            sluttDato = dateInTest.minusYears(1).plusDays(100),
                            kategori = kategoriTypeInTest.copy(kode = "1.2")
                        )
                    )
                ),
                expectedErrorMessage = "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder"
            )
        )
    )
})
