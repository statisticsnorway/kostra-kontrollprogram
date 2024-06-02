import {beforeEach, describe, expect, test} from "vitest";
import {render, screen} from '@testing-library/react'
import ErrorDetailsTable, {
    CASE_WORKER_HEADER, CONTEXT_ID_HEADER,
    INDIVID_ID_HEADER,
    JOURNAL_ID_HEADER, LINES_HEADER,
    SEVERITY_HEADER
} from "./ErrorDetailsTable";
import {fileReportEntryInTestWithAllPropsSet, fileReportEntryInTestWithRequiredPropsSet} from "../../specData";

describe("ErrorDetailsTable", () => {
    describe("Layout without entries", () => {
        test("there are no errors", () => {
            render(<ErrorDetailsTable reportEntries={[]}/>)
            expect(screen.getByTestId("error-details-table-tbody").children.length).toBe(0)
        })
    })
    describe("Layout with entries, all props set", () => {
        beforeEach(() => {
            render(<ErrorDetailsTable reportEntries={[fileReportEntryInTestWithAllPropsSet]}/>)
        })

        test("there are one error, expect one entry in table", () => {
            expect(screen.getByTestId("error-details-table-tbody").children.length).toBe(1)
        })
        test("expect 'Feilkode' to be in the document", () => {
            expect(screen.getByText("Advarsel")).toBeDefined()
        })
        test("expect 'Saksbehandler' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTestWithAllPropsSet.caseworker as string)).toBeDefined()
        })
        test("expect 'Journalnummer' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTestWithAllPropsSet.journalId as string)).toBeDefined()
        })
        test("expect 'Individ-ID' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTestWithAllPropsSet.individId as string)).toBeDefined()
        })
        test("expect 'Kontekst-ID' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTestWithAllPropsSet.contextId as string)).toBeDefined()
        })
        test("expect 'Kontroll' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTestWithAllPropsSet.ruleName)).toBeDefined()
        })
        test("expect 'Melding' to be in the document", () => {
            expect(screen.getByText(fileReportEntryInTestWithAllPropsSet.messageText)).toBeDefined()
        })
        test("expect 'Linje' to be in the document", () => {
            expect(screen.getByText((fileReportEntryInTestWithAllPropsSet.lineNumbers as number[]).join(", "))).toBeDefined()
        })
    })

    describe("Layout with entries, required props set", () => {
        beforeEach(() => {
            render(<ErrorDetailsTable reportEntries={[fileReportEntryInTestWithRequiredPropsSet]}/>)
        })

        test("expect 'Feilkode' to be in the document", () => {
            expect(screen.queryByText(SEVERITY_HEADER)).toBeDefined()
        })

        test("expect 'Saksbehandler' not to be in the document", () => {
            expect(screen.queryByText(CASE_WORKER_HEADER)).toBeNull()
        })
        test("expect 'Journalnummer' not to be in the document", () => {
            expect(screen.queryByText(JOURNAL_ID_HEADER)).toBeNull()
        })
        test("expect 'Individ-ID' not to be in the document", () => {
            expect(screen.queryByText(INDIVID_ID_HEADER)).toBeNull()
        })
        test("expect 'Kontekst-ID' not to be in the document", () => {
            expect(screen.queryByText(CONTEXT_ID_HEADER)).toBeNull()
        })
        test("expect 'Linje' not to be in the document", () => {
            expect(screen.queryByText(LINES_HEADER)).toBeNull()
        })
    })
})