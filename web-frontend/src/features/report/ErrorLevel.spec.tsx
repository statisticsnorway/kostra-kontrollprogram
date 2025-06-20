import {describe, expect, it} from "vitest"
import {render, screen} from '@testing-library/react'
import ErrorLevel from "./ErrorLevel"
import KostraSeverity from "../../kostratypes/kostraSeverity"

describe("ErrorLevel", () => {
    describe("Layout", () => {
        it("ok, expect text 'OK'", () => {
            render(<ErrorLevel level={KostraSeverity.OK}/>)
            expect(screen.queryByText("OK")).toBeInTheDocument()
        })
        it("ok, expect cssClass 'text-success'", () => {
            render(<ErrorLevel level={KostraSeverity.OK}/>)
            expect(screen.getByText("OK").classList.contains("text-success")).toBeTruthy()
        })

        it("info, expect text 'Info'", () => {
            render(<ErrorLevel level={KostraSeverity.INFO}/>)
            expect(screen.queryByText("Info")).toBeInTheDocument()
        })
        it("if, expect cssClass 'text-info'", () => {
            render(<ErrorLevel level={KostraSeverity.INFO}/>)
            expect(screen.getByText("Info").classList.contains("text-info")).toBeTruthy()
        })

        it("warning, expect text 'Advarsel'", () => {
            render(<ErrorLevel level={KostraSeverity.WARNING}/>)
            expect(screen.queryByText("Advarsel")).toBeInTheDocument()
        })
        it("warning, expect cssClass 'text-warning'", () => {
            render(<ErrorLevel level={KostraSeverity.WARNING}/>)
            expect(screen.getByText("Advarsel").classList.contains("text-warning")).toBeTruthy()
        })

        it("error, expect text 'Feil'", () => {
            render(<ErrorLevel level={KostraSeverity.ERROR}/>)
            expect(screen.queryByText("Feil")).toBeInTheDocument()
        })
        it("error, expect cssClass 'text-danger'", () => {
            render(<ErrorLevel level={KostraSeverity.ERROR}/>)
            expect(screen.getByText("Feil").classList.contains("text-danger")).toBeTruthy()
        })

        it("fatal error, expect text 'Kritisk'", () => {
            render(<ErrorLevel level={KostraSeverity.FATAL}/>)
            expect(screen.queryByText("Kritisk")).toBeInTheDocument()
        })
        it("critical error, expect cssClass 'text-danger'", () => {
            render(<ErrorLevel level={KostraSeverity.FATAL}/>)
            expect(screen.getByText("Kritisk").classList.contains("text-danger")).toBeTruthy()
        })
    })
})