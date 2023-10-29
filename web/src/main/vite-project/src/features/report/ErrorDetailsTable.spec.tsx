import {beforeEach, describe, expect, test} from "vitest";
import {render, screen} from '@testing-library/react'
import ErrorDetailsTable from "./ErrorDetailsTable";
import {fileReportEntryInTest} from "../../specData";

describe("ErrorDetailsTable", () => {
    describe("Layout without entries", () => {
        test("there are no errors", () => {
            render(<ErrorDetailsTable reportEntries={[]}/>)
            expect(screen.getByTestId("error-details-table-tbody").children.length).toBe(0)
        })
    })
    describe("Layout with entries", () => {
        beforeEach(() => {
            render(<ErrorDetailsTable reportEntries={[fileReportEntryInTest]}/>)
        })

        test("there are one error, expect one entry in table", () => {
            expect(screen.getByTestId("error-details-table-tbody").children.length).toBe(1)
        })
        test("expect 'Feilkode' to be in the document", () => {
            expect(screen.getByText("Advarsel")).toBeDefined()
        })
        test("expect 'Journalnummer' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTest.journalId as string)).toBeDefined()
        })
        test("expect 'Saksbehandler' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTest.caseworker)).toBeDefined()
        })
        test("expect 'Kontrolltype' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTest.ruleName)).toBeDefined()
        })
    })
})