import {describe, expect, test} from "vitest";
import {render, screen} from '@testing-library/react'
import ErrorLevel from "./ErrorLevel";
import KostraSeverity from "../../kostratypes/kostraSeverity";

describe("ErrorLevel", () => {
    describe("Layout", () => {
        test("no error, expect text 'OK'", () => {
            render(<ErrorLevel level={KostraSeverity.OK}/>)
            expect(screen.queryByText("OK")).toBeInTheDocument()
        })
        test("no error, expect cssClass 'text-success'", () => {
            render(<ErrorLevel level={KostraSeverity.OK}/>)
            expect(screen.getByText("OK").classList.contains("text-success")).toBeTruthy()
        })

        test("warning, expect text 'Info'", () => {
            render(<ErrorLevel level={KostraSeverity.INFO}/>)
            expect(screen.queryByText("Info")).toBeInTheDocument()
        })
        test("warning, expect cssClass 'text-success'", () => {
            render(<ErrorLevel level={KostraSeverity.INFO}/>)
            expect(screen.getByText("Info").classList.contains("text-success")).toBeTruthy()
        })

        test("warning, expect text 'Advarsel'", () => {
            render(<ErrorLevel level={KostraSeverity.WARNING}/>)
            expect(screen.queryByText("Advarsel")).toBeInTheDocument()
        })
        test("warning, expect cssClass 'text-warning'", () => {
            render(<ErrorLevel level={KostraSeverity.WARNING}/>)
            expect(screen.getByText("Advarsel").classList.contains("text-warning")).toBeTruthy()
        })

        test("critical error, expect text 'Kritisk'", () => {
            render(<ErrorLevel level={KostraSeverity.ERROR}/>)
            expect(screen.queryByText("Kritisk")).toBeInTheDocument()
        })
        test("critical error, expect cssClass 'text-danger'", () => {
            render(<ErrorLevel level={KostraSeverity.ERROR}/>)
            expect(screen.getByText("Kritisk").classList.contains("text-danger")).toBeTruthy()
        })

        test("fatal error, expect text 'Kritisk'", () => {
            render(<ErrorLevel level={KostraSeverity.FATAL}/>)
            expect(screen.queryByText("Kritisk")).toBeInTheDocument()
        })
        test("critical error, expect cssClass 'text-danger'", () => {
            render(<ErrorLevel level={KostraSeverity.FATAL}/>)
            expect(screen.getByText("Kritisk").classList.contains("text-danger")).toBeTruthy()
        })
    })
})