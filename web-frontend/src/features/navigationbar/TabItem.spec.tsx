import {describe, expect, it, vi} from "vitest";
import {fireEvent, render, screen} from '@testing-library/react'
import TabItem from "./TabItem";

const tabItemPropsInTest = {
    text: "~text~",
    image: "~image~"
}

const setupForLayoutTests = (
    tabIsActive: boolean,
    showCloseButton: boolean
) => render(<TabItem {...tabItemPropsInTest}
                     tabIsActive={tabIsActive}
                     showCloseButton={showCloseButton}
                     onSelect={() => {
                     }}
                     onClose={() => {
                     }}/>)

const setupForInteractionTests = (
    onSelect: () => void,
    onClose: () => void
) => render(<TabItem {...tabItemPropsInTest}
                     tabIsActive={true}
                     showCloseButton={true}
                     onSelect={onSelect}
                     onClose={onClose}/>)

describe("TabItem", () => {
    describe("Layout", () => {
        it("expect text to appear in document", () => {
            setupForLayoutTests(false, false)
            expect(screen.queryByText(tabItemPropsInTest.text)).toBeInTheDocument()
        })
        it("when tabIsActive is false", () => {
            setupForLayoutTests(false, false)
            expect(screen.getByTestId("tab-item-div").classList.contains("active")).toBeFalsy()
        })
        it("when tabIsActive is true", () => {
            setupForLayoutTests(true, false)
            expect(screen.getByTestId("tab-item-div").classList.contains("active")).toBeTruthy()
        })
        it("expect no close button when showCloseButton is false", () => {
            setupForLayoutTests(true, false)
            expect(screen.queryAllByRole("button").length).toBe(1)
        })
        it("expect close button when showCloseButton is true", () => {
            setupForLayoutTests(true, true)
            expect(screen.queryAllByRole("button").length).toBe(2)
        })
    })

    describe("Interactions", () => {
        it("clicking image button calls onSelect", () => {
            const onSelect = vi.fn().mockImplementation(() => {
            })
            setupForInteractionTests(onSelect, () => {
            })

            fireEvent.click(screen.getByText(tabItemPropsInTest.text))

            expect(onSelect).toHaveBeenCalled()
        })
        it("clicking close button calls onClose", () => {
            const onClose = vi.fn().mockImplementation(() => {
            })
            setupForInteractionTests(() => {
            }, onClose)

            fireEvent.click(screen.getByTitle<HTMLButtonElement>("Slett rapport"))

            expect(onClose).toHaveBeenCalled()
        })
    })
})