import {describe, expect, it, vi} from "vitest"
import FileReportVm from "../../kostratypes/fileReportVm"
import {fireEvent, render, screen} from "@testing-library/react"
import TabRow from "./TabRow"
import {fileReportInTest, kostraFormInTest} from "../../specData"
import {MemoryRouter, Route, Routes} from "react-router-dom"

const expectedReportTabTitle = `${kostraFormInTest.skjema} ${kostraFormInTest.aar}, region ${kostraFormInTest.region}`

const setupTabRowForTests = (
    fileReports: FileReportVm[] = [],
    path = "/",
    onDeleteFileReport = () => {
    }
) => render(<MemoryRouter initialEntries={[path]}>
    <Routes>
        <Route path="/" element={
            <TabRow fileReports={fileReports} onDeleteFileReport={onDeleteFileReport}/>}/>
        <Route path="/file-reports/:reportId" element={
            <TabRow fileReports={fileReports} onDeleteFileReport={onDeleteFileReport}/>}/>
    </Routes>
</MemoryRouter>)

describe("TabRow", () => {
    describe("Layout", () => {
        it("displays tab title 'Skjema' when no file reports", () => {
            setupTabRowForTests()
            expect(screen.queryByText("Skjema")).toBeInTheDocument()
        })
        it("displays 'Tilbake til skjema' when a file report selected", () => {
            setupTabRowForTests([fileReportInTest], "/file-reports/0")
            expect(screen.queryByText("Skjema")).not.toBeInTheDocument()
            expect(screen.queryByText("Tilbake til skjema")).toBeInTheDocument()
        })
        it("displays report info when report exists", () => {
            setupTabRowForTests([fileReportInTest])
            expect(screen.queryByText(expectedReportTabTitle)).toBeInTheDocument()
        })
    })

    describe("Interactions", () => {
        it("calls onReportDelete when 'Slett rapport' is clicked", () => {
            const onReportDelete = vi.fn()
            setupTabRowForTests([fileReportInTest], "/", onReportDelete)

            fireEvent.click(screen.getByTitle<HTMLButtonElement>("Slett rapport"))
            expect(onReportDelete).toBeCalledWith(0)
        })
    })
})