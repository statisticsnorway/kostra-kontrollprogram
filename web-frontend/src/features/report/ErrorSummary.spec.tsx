import {beforeEach, describe, expect, test} from "vitest";
import {render, screen} from "@testing-library/react";
import ErrorSummary from "./ErrorSummary";
import {fileReportEntryInTestWithAllPropsSet} from "../../specData";

describe("ErrorSummary", () => {
    describe("Layout without entries", () => {
        test("there are no errors", () => {
            render(<ErrorSummary reportEntries={[]}/>)
            expect(screen.getByTestId("error-summary-table-tbody").children.length).toBe(0)
        })
    })
    describe("Layout with entries", () => {
        beforeEach(() => {
            render(<ErrorSummary reportEntries={[fileReportEntryInTestWithAllPropsSet]}/>)
        })

        test("there are one error, expect one entry in table", () => {
            expect(screen.getByTestId("error-summary-table-tbody").children.length).toBe(1)
        })
        test("expect 'Feilkode' to be in the document", () => {
            expect(screen.queryByText("Advarsel")).toBeInTheDocument()
        })
        test("expect 'Antall' to be in the document", () => {
            expect(screen.queryByText("1")).toBeInTheDocument()
        })
        test("expect 'Kontrolltype' to be in the document", () => {
            expect(screen.queryByText(fileReportEntryInTestWithAllPropsSet.ruleName)).toBeInTheDocument()
        })
    })
})