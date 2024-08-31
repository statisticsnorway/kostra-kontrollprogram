import {describe, expect, it, vi} from "vitest"
import {fireEvent, render, screen} from '@testing-library/react'
import TabItem from "./TabItem"
import {MemoryRouter} from "react-router-dom"

const setupTabItemForTests = (
    tabIsActive: boolean = false,
    reportName: string = "report-name",
    onClose = () => {
    }
) => render(<MemoryRouter initialEntries={["/"]}>
    <TabItem
        id={1}
        reportName={reportName}
        tabIsActive={tabIsActive}
        onClose={onClose}/>
</MemoryRouter>)


describe("TabItem", () => {
    describe("Layout", () => {
        it("displays report name from props", () => {
            setupTabItemForTests(false)
            expect(screen.queryByText("report-name")).toBeInTheDocument()
        })
        it("does not have className=active", () => {
            setupTabItemForTests(false)
            expect(screen.getByTestId("tab-item-div").classList.contains("active")).toBeFalsy()
        })
        it("does have className=active", () => {
            setupTabItemForTests(true)
            expect(screen.getByTestId("tab-item-div").classList.contains("active")).toBeTruthy()
        })
        it("has a close button", () => {
            setupTabItemForTests(true)
            expect(screen.queryByRole("button")).toBeInTheDocument()
        })
    })

    describe("Interactions", () => {
        it("calls onClose when 'Slett rapport' is clicked", () => {
            const onClose = vi.fn()
            setupTabItemForTests(undefined, undefined, onClose)

            fireEvent.click(screen.getByTitle<HTMLButtonElement>("Slett rapport"))
            expect(onClose).toHaveBeenCalled()
        })
    })
})