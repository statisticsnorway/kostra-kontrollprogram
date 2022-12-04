import {describe, expect, test} from "vitest";
import {render, screen} from '@testing-library/react'
import ErrorLevel from "./ErrorLevel";
import KostraErrorCode from "../../kostratypes/kostraErrorCode";

describe("ErrorLevel", () => {
    describe("Layout", () => {
        test("no error, expect text 'OK'", () => {
            render(<ErrorLevel level={KostraErrorCode.NO_ERROR}/>)
            expect(screen.getByText("OK")).toBeDefined()
        })
        test("no error, expect cssClass 'text-success'", () => {
            render(<ErrorLevel level={KostraErrorCode.NO_ERROR}/>)
            expect(screen.getByText("OK").classList.contains("text-success")).toBeTruthy()
        })

        test("warning, expect text 'Advarsel'", () => {
            render(<ErrorLevel level={KostraErrorCode.NORMAL_ERROR}/>)
            expect(screen.getByText("Advarsel")).toBeDefined()
        })
        test("warning, expect cssClass 'text-warning'", () => {
            render(<ErrorLevel level={KostraErrorCode.NORMAL_ERROR}/>)
            expect(screen.getByText("Advarsel").classList.contains("text-warning")).toBeTruthy()
        })

        test("critical error, expect text 'Kritisk'", () => {
            render(<ErrorLevel level={KostraErrorCode.CRITICAL_ERROR}/>)
            expect(screen.getByText("Kritisk")).toBeDefined()
        })
        test("critical error, expect cssClass 'text-danger'", () => {
            render(<ErrorLevel level={KostraErrorCode.CRITICAL_ERROR}/>)
            expect(screen.getByText("Kritisk").classList.contains("text-danger")).toBeTruthy()
        })
    })
})