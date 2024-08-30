import {describe, expect, it, vi} from "vitest";
import FileReportVm from "../../kostratypes/fileReportVm";
import {fireEvent, render, screen} from "@testing-library/react";
import TabRow from "./TabRow";
import {fileReportInTest, kostraFormInTest} from "../../specData";
import {MemoryRouter} from "react-router-dom";

const expectedTabTitle = `${kostraFormInTest.skjema} ${kostraFormInTest.aar}, region ${kostraFormInTest.region}`

const setupForLayoutTests = (fileReports: FileReportVm[], activeTabIndex: number) =>
    render(<MemoryRouter>
        <TabRow
            fileReports={fileReports}
            activeTabIndex={activeTabIndex}
            onDeleteFileReport={() => {
            }}/>
    </MemoryRouter>)

const setupForInteractionTests = (onReportDelete: () => void) =>
    render(<MemoryRouter>
        <TabRow
            fileReports={[fileReportInTest]}
            activeTabIndex={1}
            onDeleteFileReport={onReportDelete}/>
    </MemoryRouter>)

describe("TabRow", () => {
    describe("Layout", () => {
        it("when no file reports, expect tab title 'Skjema'", () => {
            setupForLayoutTests([], 0)
            expect(screen.queryByText("Skjema")).toBeInTheDocument()
        })
        it("delete button is not displayed for left-most tab", () => {
            setupForLayoutTests([], 0)
            expect(screen.getAllByRole("button").length).toBe(1)
        })
        it("when one file report and file report selected, back to form button changes text", () => {
            setupForLayoutTests([fileReportInTest], 1)
            expect(screen.queryByText("Skjema")).not.toBeInTheDocument()
            expect(screen.queryByText("Tilbake til skjema")).toBeInTheDocument()
        })
        it("when one file report, report info is displayed as tab", () => {
            setupForLayoutTests([fileReportInTest], 1)
            expect(screen.queryByText(expectedTabTitle)).toBeInTheDocument()
        })
    })

    describe("Interactions", () => {
        it("clicking close button calls onReportDelete", () => {
            const onReportDelete = vi.fn()
            setupForInteractionTests(onReportDelete)

            fireEvent.click(screen.getByTitle<HTMLButtonElement>("Slett rapport"))
            expect(onReportDelete).toBeCalledWith(0)
        })
    })
})