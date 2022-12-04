import {describe, test, expect, vi, beforeEach} from "vitest";
import {fireEvent, render, screen} from '@testing-library/react'
import ImageNameButton from "./ImageNameButton";

const imagePropsInTest = {
    text: "~text~",
    image: "~image~"
}

const setup = (onClick: () => void) => {
    render(<ImageNameButton
        onClick={onClick}
        text={imagePropsInTest.text}
        image={imagePropsInTest.image}
    />)
}

describe("ImageNameButton", () => {
    describe("Layout", () => {
        beforeEach(() => {
            setup(() => {})
        })

        test("has button", () => {
            expect(screen.getByRole("button")).toBeDefined()
        })
        test("has image", () => {
            expect(screen.getByRole<HTMLImageElement>("img").src).toContain(imagePropsInTest.image)
        })
        test("has text", () => {
            expect(screen.getByText(imagePropsInTest.text)).toBeDefined()
        })
    })

    describe("Interactions", () => {
        test("clicking button calls onClick", () => {
            const onClick = vi.fn().mockImplementation(() => {})
            setup(onClick)

            fireEvent.click(screen.getByRole("button"))

            expect(onClick).toHaveBeenCalled()
        })
    })
})