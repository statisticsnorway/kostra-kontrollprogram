import {beforeEach, describe, expect, it} from "vitest"
import {render, screen} from '@testing-library/react'
import ReportSummary from "./ReportSummary"
import {appReleaseVersionInTest, fileReportInTest, kostraFormInTest} from "../../specData"

describe("ReportSummary", () => {
    describe("Layout with all fields set", () => {
        beforeEach(() => {
            render(<ReportSummary
                appReleaseVersion={appReleaseVersionInTest}
                fileReport={fileReportInTest}
            />)
        })

        it("'Programvareversjon' should be in the document", () => {
            expect(screen.queryByText(`Programvareversjon: ${appReleaseVersionInTest}`)).toBeInTheDocument()
        })
        it("'Skjema' should be in the document", () => {
            expect(screen.queryByText(`Skjema: ${kostraFormInTest.skjema}`)).toBeInTheDocument()
        })
        it("'Årgang' should be in the document", () => {
            expect(screen.queryByText(`Årgang: ${kostraFormInTest.aar}`)).toBeInTheDocument()
        })
        it("'Regionsnummer' should be in the document", () => {
            expect(screen.queryByText(`Regionsnummer: ${kostraFormInTest.region}`)).toBeInTheDocument()
        })
        it("'Organisasjonsnummer' should be in the document", () => {
            expect(screen.queryByText(`Organisasjonsnummer: ${kostraFormInTest.orgnrForetak}`)).toBeInTheDocument()
        })
        it("'Organisasjonsnummer virksomhet(er)' should be in the document", () => {
            expect(screen.queryByText(
                `Organisasjonsnummer virksomhet(er): ${kostraFormInTest.orgnrVirksomhet?.[0].orgnr}`)).toBeInTheDocument()
        })
        it("'Høyeste alvorlighetsgrad' should be in the document", () => {
            expect(screen.getByText("Høyeste alvorlighetsgrad:").children[0].textContent).toBe("Advarsel")
        })
        it("'Antall kontroller utført' should be in the document", () => {
            expect(screen.queryByText(`Antall kontroller utført: ${fileReportInTest.antallKontroller}`)).toBeInTheDocument()
        })
    })

    describe("Layout with not all fields set", () => {
        it("with minimal innparametere, 'Organisasjonsnummer:' should not be in the document", () => {
            const localKostraFormInTest = {
                ...kostraFormInTest,
                orgnrForetak: "",
                orgnrVirksomhet: null
            }

            render(<ReportSummary
                appReleaseVersion={appReleaseVersionInTest}
                fileReport={{
                    ...fileReportInTest,
                    innparametere: localKostraFormInTest
                }}
            />)

            expect(screen.queryByText(`Organisasjonsnummer: ${localKostraFormInTest.orgnrForetak}`))
                .not.toBeInTheDocument()
        })
    })
})