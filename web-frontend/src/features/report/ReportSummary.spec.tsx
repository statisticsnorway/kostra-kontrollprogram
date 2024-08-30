import {beforeEach, describe, expect, test} from "vitest";
import {render, screen} from '@testing-library/react'
import ReportSummary from "./ReportSummary";
import {appReleaseVersionInTest, fileReportInTest, kostraFormInTest} from "../../specData";

describe("ReportSummary", () => {
    describe("Layout with all fields set", () => {
        beforeEach(() => {
            render(<ReportSummary
                appReleaseVersion={appReleaseVersionInTest}
                fileReport={fileReportInTest}
            />)
        })

        test("'Programvareversjon' should be in the document", () => {
            expect(screen.queryByText(`Programvareversjon: ${appReleaseVersionInTest}`)).toBeInTheDocument()
        })
        test("'Skjema' should be in the document", () => {
            expect(screen.queryByText(`Skjema: ${kostraFormInTest.skjema}`)).toBeInTheDocument()
        })
        test("'Årgang' should be in the document", () => {
            expect(screen.queryByText(`Årgang: ${kostraFormInTest.aar}`)).toBeInTheDocument()
        })
        test("'Regionsnummer' should be in the document", () => {
            expect(screen.queryByText(`Regionsnummer: ${kostraFormInTest.region}`)).toBeInTheDocument()
        })
        test("'Organisasjonsnummer' should be in the document", () => {
            expect(screen.queryByText(`Organisasjonsnummer: ${kostraFormInTest.orgnrForetak}`)).toBeInTheDocument()
        })
        test("'Organisasjonsnummer virksomhet(er)' should be in the document", () => {
            expect(screen.queryByText(
                `Organisasjonsnummer virksomhet(er): ${kostraFormInTest.orgnrVirksomhet?.[0].orgnr}`)).toBeInTheDocument()
        })
        test("'Høyeste alvorlighetsgrad' should be in the document", () => {
            expect(screen.getByText("Høyeste alvorlighetsgrad:").children[0].textContent).toBe("Advarsel")
        })
        test("'Antall kontroller utført' should be in the document", () => {
            expect(screen.queryByText(`Antall kontroller utført: ${fileReportInTest.antallKontroller}`)).toBeInTheDocument()
        })
    })

    describe("Layout with not all fields set", () => {
        test("with minimal innparametere, 'Organisasjonsnummer:' should not be in the document", () => {
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