import {beforeEach, describe, expect, it} from "vitest";
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
        it("there are no errors", () => {
            render(<ErrorDetailsTable reportEntries={[]}/>)
            expect(screen.getByTestId("error-details-table-tbody").children.length).toBe(0)
        })
    })
    describe("Layout with entries, all props set", () => {
        beforeEach(() => {
            render(<ErrorDetailsTable reportEntries={[fileReportEntryInTestWithAllPropsSet]}/>)
        })

        it("there are one error, expect one entry in table", () => {
            expect(screen.getByTestId("error-details-table-tbody").children.length).toBe(1)
        })
        it("expect 'Feilkode' to be in the document", () => {
            expect(screen.queryByText("Advarsel")).toBeInTheDocument()
        })
        it("expect 'Saksbehandler' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.caseworker as string)).toBeInTheDocument()
        })
        it("expect 'Journalnummer' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.journalId as string)).toBeInTheDocument()
        })
        it("expect 'Individ-ID' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.individId as string)).toBeInTheDocument()
        })
        it("expect 'Kontekst-ID' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.contextId as string)).toBeInTheDocument()
        })
        it("expect 'Kontroll' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.ruleName)).toBeInTheDocument()
        })
        it("expect 'Melding' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.messageText)).toBeInTheDocument()
        })
        it("expect 'Linje' to be in the document", () => {
            expect(screen.queryByText((fileReportEntryInTestWithAllPropsSet.lineNumbers as number[]).join(", "))).toBeInTheDocument()
        })
    })

    describe("Layout with entries, required props set", () => {
        beforeEach(() => {
            render(<ErrorDetailsTable reportEntries={[fileReportEntryInTestWithRequiredPropsSet]}/>)
        })

        it("expect 'Feilkode' to be in the document", () => {
            expect(screen.queryByText(SEVERITY_HEADER)).toBeInTheDocument()
        })

        it("expect 'Saksbehandler' not to be in the document", () => {
            expect(screen.queryByText(CASE_WORKER_HEADER)).not.toBeInTheDocument()
        })
        it("expect 'Journalnummer' not to be in the document", () => {
            expect(screen.queryByText(JOURNAL_ID_HEADER)).not.toBeInTheDocument()
        })
        it("expect 'Individ-ID' not to be in the document", () => {
            expect(screen.queryByText(INDIVID_ID_HEADER)).not.toBeInTheDocument()
        })
        it("expect 'Kontekst-ID' not to be in the document", () => {
            expect(screen.queryByText(CONTEXT_ID_HEADER)).not.toBeInTheDocument()
        })
        it("expect 'Linje' not to be in the document", () => {
            expect(screen.queryByText(LINES_HEADER)).not.toBeInTheDocument()
        })
    })
})