import {beforeEach, describe, expect, it} from 'vitest'
import {render, screen} from '@testing-library/react'
import ReportView from "./ReportView"
import {appReleaseVersionInTest, fileReportInTest} from "../specData"
import KostraSeverity from "../kostratypes/kostraSeverity"

describe("ReportView", () => {
    describe("Layout without report entries", () => {
        beforeEach(() => {
            render(<ReportView
                appReleaseVersion={appReleaseVersionInTest}
                fileReport={{
                    ...fileReportInTest,
                    severity: KostraSeverity.OK,
                    feil: []
                }}
            />)
        })

        it("expect 'OK'", () => {
            expect(screen.queryByText("OK")).toBeInTheDocument()
        })
        it("expect no error summary in document", () => {
            expect(screen.queryByText("error-summary-table-tbody")).not.toBeInTheDocument()
        })
        it("expect no error details in document", () => {
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

        it("expect error summary in document", () => {
            expect(screen.queryByTestId("error-summary-table-tbody")).toBeInTheDocument()
        })
        it("expect error details in document", () => {
            expect(screen.queryByTestId("error-details-table-tbody")).toBeInTheDocument()
        })
    })
})