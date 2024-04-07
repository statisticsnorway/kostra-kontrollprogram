import {beforeEach, describe, expect, test, vi} from "vitest";
import {fireEvent, render, screen} from '@testing-library/react'
import CloseButton, {CHAR_MULTIPLICATION_X} from "./CloseButton";

describe("CloseButton", () => {
    describe("Layout", () => {
        beforeEach(() => {
            render(<CloseButton onClick={() => {
            }}/>)
        })

        test("has button", () => {
            expect(screen.getByRole("button")).toBeDefined()
        })
        test("has close character as button text", () => {
            expect(screen.getByText(CHAR_MULTIPLICATION_X)).toBeDefined()
        })
    })

    describe("Interaction", () => {
        test("clicking button calls onClick", () => {
            const onClick = vi.fn().mockImplementation(() => {
            })
            render(<CloseButton onClick={onClick}/>)

            fireEvent.click(screen.getByRole("button"))

            expect(onClick).toHaveBeenCalled()
        })
    })
})