import {beforeEach, describe, expect, it, vi} from "vitest";
import {fireEvent, render, screen} from '@testing-library/react'
import ImageNameButton from "./ImageNameButton";

const imagePropsInTest = {
    text: "~text~",
    image: "~image~"
}

const setup = (onClick: () => void) =>
    render(<ImageNameButton
        onClick={onClick}
        text={imagePropsInTest.text}
        image={imagePropsInTest.image}
    />)

describe("ImageNameButton", () => {
    describe("Layout", () => {
        beforeEach(() => {
            setup(() => {
            })
        })

        it("has button", () => {
            expect(screen.queryByRole("button")).toBeInTheDocument()
        })
        it("has image", () => {
            expect(screen.getByRole<HTMLImageElement>("img").src).toContain(imagePropsInTest.image)
        })
        it("has text", () => {
            expect(screen.queryByText(imagePropsInTest.text)).toBeInTheDocument()
        })
    })

    describe("Interactions", () => {
        it("clicking button calls onClick", () => {
            const onClick = vi.fn().mockImplementation(() => {
            })
            setup(onClick)

            fireEvent.click(screen.getByRole("button"))

            expect(onClick).toHaveBeenCalled()
        })
    })
})