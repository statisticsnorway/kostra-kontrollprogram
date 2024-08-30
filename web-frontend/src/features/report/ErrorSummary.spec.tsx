import {beforeEach, describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import ErrorSummary from "./ErrorSummary";
import {fileReportEntryInTestWithAllPropsSet} from "../../specData";

describe("ErrorSummary", () => {
    describe("Layout without entries", () => {
        it("there are no errors", () => {
            render(<ErrorSummary reportEntries={[]}/>)
            expect(screen.getByTestId("error-summary-table-tbody").children.length).toBe(0)
        })
    })
    describe("Layout with entries", () => {
        beforeEach(() => {
            render(<ErrorSummary reportEntries={[fileReportEntryInTestWithAllPropsSet]}/>)
        })

        it("there are one error, expect one entry in table", () => {
            expect(screen.getByTestId("error-summary-table-tbody").children.length).toBe(1)
        })
        it("expect 'Feilkode' to be in the document", () => {
            expect(screen.queryByText("Advarsel")).toBeInTheDocument()
        })
        it("expect 'Antall' to be in the document", () => {
            expect(screen.queryByText("1")).toBeInTheDocument()
        })
        it("expect 'Kontrolltype' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.ruleName)).toBeInTheDocument()
        })
    })
})