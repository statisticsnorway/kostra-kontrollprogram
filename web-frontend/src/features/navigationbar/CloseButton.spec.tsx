import {beforeEach, describe, expect, it, vi} from "vitest"
import {fireEvent, render, screen} from '@testing-library/react'
import CloseButton, {CHAR_MULTIPLICATION_X} from "./CloseButton"

describe("CloseButton", () => {
    describe("Layout", () => {
        beforeEach(() => {
            render(<CloseButton onClick={() => {
            }}/>)
        })

        it("has button", () => {
            expect(screen.queryByRole("button")).toBeInTheDocument()
        })
        it("has close character as button text", () => {
            expect(screen.queryByText(CHAR_MULTIPLICATION_X)).toBeInTheDocument()
        })
    })

    describe("Interaction", () => {
        it("clicking button calls onClick", () => {
            const onClick = vi.fn()
            render(<CloseButton onClick={onClick}/>)

            fireEvent.click(screen.getByRole("button"))
            expect(onClick).toHaveBeenCalled()
        })
    })
})