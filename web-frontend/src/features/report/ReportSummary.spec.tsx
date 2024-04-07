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
            expect(screen.getByText(`Programvareversjon: ${appReleaseVersionInTest}`)).toBeDefined()
        })
        test("'Skjema' should be in the document", () => {
            expect(screen.getByText(`Skjema: ${kostraFormInTest.skjema}`)).toBeDefined()
        })
        test("'Årgang' should be in the document", () => {
            expect(screen.getByText(`Årgang: ${kostraFormInTest.aar}`)).toBeDefined()
        })
        test("'Regionsnummer' should be in the document", () => {
            expect(screen.getByText(`Regionsnummer: ${kostraFormInTest.region}`)).toBeDefined()
        })
        test("'Organisasjonsnummer' should be in the document", () => {
            expect(screen.getByText(`Organisasjonsnummer: ${kostraFormInTest.orgnrForetak}`)).toBeDefined()
        })
        test("'Organisasjonsnummer virksomhet(er)' should be in the document", () => {
            expect(screen.getByText(
                `Organisasjonsnummer virksomhet(er): ${kostraFormInTest.orgnrVirksomhet?.[0].orgnr}`)).toBeDefined()
        })
        test("'Høyeste alvorlighetsgrad' should be in the document", () => {
            expect(screen.getByText("Høyeste alvorlighetsgrad:").children[0].textContent).toBe("Advarsel")
        })
        test("'Antall kontroller utført' should be in the document", () => {
            expect(screen.getByText(`Antall kontroller utført: ${fileReportInTest.antallKontroller}`)).toBeDefined()
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

            expect(() => screen.getByText(`Organisasjonsnummer: ${localKostraFormInTest.orgnrForetak}`)).toThrow()
        })
    })
})