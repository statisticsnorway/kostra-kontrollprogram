import {beforeEach, describe, expect, test} from 'vitest'
import {render, screen} from '@testing-library/react'
import ReportView from "./ReportView";
import {appReleaseVersionInTest, fileReportInTest} from "../specData";
import KostraSeverity from "../kostratypes/kostraSeverity";


describe("ReportView", () => {
    describe("Layout without report entries", () => {
        beforeEach(() => {
            render(<ReportView
                appReleaseVersion={appReleaseVersionInTest}
                fileReport={{
                    ...fileReportInTest,
                    feilkode: KostraSeverity.OK,
                    feil: []
                }}
            />)
        })

        test("expect 'OK'", () => {
            expect(screen.getByText("OK")).toBeDefined()
        })
        test("expect no error summary in document", () => {
            expect(() => screen.getByTestId("error-summary-table-tbody")).toThrow()
        })
        test("expect no error details in document", () => {
            expect(() => screen.getByTestId("error-details-table-tbody")).toThrow()
        })
    })

    describe("Layout with report entries", () => {
        beforeEach(() => {
            render(<ReportView
                appReleaseVersion={appReleaseVersionInTest}
                fileReport={fileReportInTest}
            />)
        })

        test("expect error summary in document", () => {
            expect(screen.getByTestId("error-summary-table-tbody")).toBeDefined()
        })
        test("expect error details in document", () => {
            expect(screen.getByTestId("error-details-table-tbody")).toBeDefined()
        })
    })
})