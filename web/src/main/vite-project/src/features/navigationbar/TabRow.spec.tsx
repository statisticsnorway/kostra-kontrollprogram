import {describe, expect, test, vi} from "vitest";
import FileReportVm from "../../kostratypes/fileReportVm";
import {render, screen} from "@testing-library/react";
import TabRow from "./TabRow";
import {fileReportInTest, kostraFormInTest} from "../../specData";

const expectedTabTitle = `${kostraFormInTest.skjema} ${kostraFormInTest.aar}, region ${kostraFormInTest.region}`

const setupForLayoutTests = (fileReports: FileReportVm[], activeTabIndex: number) => {
    render(<TabRow
        fileReports={fileReports}
        activeTabIndex={activeTabIndex}
        onTabSelect={() => {
        }}
        onReportDelete={() => {
        }}/>)
}

const setupForInteractionTests = (onTabSelect: () => void, onReportDelete: () => void) => {
    render(<TabRow
        fileReports={[fileReportInTest]}
        activeTabIndex={1}
        onTabSelect={onTabSelect}
        onReportDelete={onReportDelete}/>)
}

describe("TabRow", () => {
    describe("Layout", () => {
        test("when no file reports, expect tab title 'Skjema'", () => {
            setupForLayoutTests([], 0)
            expect(screen.getByText("Skjema")).toBeDefined()
        })
        test("delete button is not displayed for left-most tab", () => {
            setupForLayoutTests([], 0)
            expect(screen.getAllByRole("button").length).toBe(1)
        })
        test("when one file report and file report selected, back to form button changes text", () => {
            setupForLayoutTests([fileReportInTest], 1)
            expect(() => screen.getByText("Skjema")).toThrow()
            expect(screen.getByText("Tilbake til skjema")).toBeDefined()
        })
        test("when one file report, report info is displayed as tab", () => {
            setupForLayoutTests([fileReportInTest], 1)
            expect(screen.getByText(expectedTabTitle)).toBeDefined()
        })
    })

    describe("Interactions", () => {
        test("clicking report name button calls onTabSelect", () => {
            const onTabSelect = vi.fn().mockImplementation(() => {
            })
            setupForInteractionTests(onTabSelect, () => {
            })

            screen.getByTitle<HTMLButtonElement>(expectedTabTitle).click()

            expect(onTabSelect).toBeCalledWith(1)
        })
        test("clicking close button calls onReportDelete", () => {
            const onReportDelete = vi.fn().mockImplementation(() => {
            })
            setupForInteractionTests(() => {
            }, onReportDelete)

            screen.getByTitle<HTMLButtonElement>("Slett rapport").click()

            expect(onReportDelete).toBeCalledWith(0)
        })
    })
})