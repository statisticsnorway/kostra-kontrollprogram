package no.ssb.kostra.validation.rule.barnevern.individrule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.individInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.tiltakTypeInTest
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleTestData.kategoriTypeInTest as kostraKategoriTypeInTest1

class Tiltak07Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Tiltak07(),
            forAllRows = listOf(
                ForAllRowItem(
                    "individ without tiltak",
                    individInTest
                ),
                ForAllRowItem(
                    "individ with kategori that does not require presisering",
                    individInTest.copy(
                        tiltak = mutableListOf(tiltakTypeInTest)
                    )
                ),
                ForAllRowItem(
                    "individ with kategori with presisering",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                kategori = kostraKategoriTypeInTest1.copy(kode = "1.99")
                            )
                        )
                    )
                ),

                ForAllRowItem(
                    "individ with kategori without presisering",
                    individInTest.copy(
                        tiltak = mutableListOf(
                            tiltakTypeInTest.copy(
                                kategori = kostraKategoriTypeInTest1.copy(
                                    kode = "1.99",
                                    presisering = null
                                )
                            )
                        )
                    ),
                    expectedErrorMessage = "Tiltak (${tiltakTypeInTest.id}). Tiltakskategori (1.99) mangler presisering"
                )
            ),
            expectedSeverity = Severity.ERROR,
            expectedContextId = tiltakTypeInTest.id
        )
    )
})
