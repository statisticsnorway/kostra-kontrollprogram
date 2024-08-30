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
            expect(screen.queryByText("OK")).toBeInTheDocument()
        })
        test("expect no error summary in document", () => {
            expect(screen.queryByText("error-summary-table-tbody")).not.toBeInTheDocument()
        })
        test("expect no error details in document", () => {
            expect(screen.queryByTestId("error-details-table-tbody")).not.toBeInTheDocument()
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
            expect(screen.queryByTestId("error-summary-table-tbody")).toBeInTheDocument()
        })
        test("expect error details in document", () => {
            expect(screen.queryByTestId("error-details-table-tbody")).toBeInTheDocument()
        })
    })
})