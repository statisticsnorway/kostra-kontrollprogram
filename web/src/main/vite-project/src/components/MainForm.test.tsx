import MainForm from "./MainForm";
import {fireEvent, render, screen} from '@testing-library/react'
import {describe, expect, test} from 'vitest';

//screen.debug()

describe("MainForm", () => {
    describe("Layout", () => {
        test("initial screen, displays 2 selects, 1 input and 1 button", () => {
            render(<MainForm
                showForm={true}
                uiData={{
                    releaseVersion: "N/A",
                    formTypes: [],
                    years: [(new Date()).getFullYear()]
                }}
                onSubmit={() => {
                }}/>);

            expect((screen.getByRole("option", {name: "Velg skjematype"}) as HTMLOptionElement).selected).toBe(true)
            expect((screen.getByRole("option", {name: "Velg årgang"}) as HTMLOptionElement).selected).toBe(true)
            expect((screen.getByLabelText("Regionsnummer") as HTMLInputElement).placeholder).toBe("6 siffer")
            expect((screen.getByRole("button", {name: "Kontroller fil"}) as HTMLButtonElement).disabled).toBe(true)
        })
    })

    describe("Interactions", () => {
        test("when a form type with company-id is selected", () => {
            render(<MainForm
                showForm={true}
                uiData={{
                    releaseVersion: "N/A",
                    formTypes: [{
                        id: "~id~",
                        tittel: "~tittel~",
                        labelOrgnr: "~labelOrgnr~",
                        labelOrgnrVirksomhetene: null
                    }],
                    years: [(new Date()).getFullYear()]
                }}
                onSubmit={() => {
                }}/>);

            // verify that input does not exist
            expect(() => screen.getByText("Organisasjonsnummer")).toThrow()

            const formSelect = screen.getByLabelText("Skjema") as HTMLSelectElement
            fireEvent.change(formSelect, {target: {value: "~id~"}})

            // verify that input exists
            expect(() => screen.getByLabelText("Organisasjonsnummer")).toBeDefined()
        })

        test("when a form type with sub-company-id is selected", async () => {
            render(<MainForm
                showForm={true}
                uiData={{
                    releaseVersion: "N/A",
                    formTypes: [{
                        id: "~id~",
                        tittel: "~tittel~",
                        labelOrgnr: null,
                        labelOrgnrVirksomhetene: "~labelOrgnrVirksomhetene~"
                    }],
                    years: [(new Date()).getFullYear()]
                }}
                onSubmit={() => {
                }}/>);

            // verify that input does not exist
            expect(() => screen.getByText("Organisasjonsnummer for virksomhetene")).toThrow()

            const formSelect = screen.getByLabelText("Skjema") as HTMLSelectElement
            fireEvent.change(formSelect, {target: {value: "~id~"}})

            // verify that input exists
            expect(() => screen.getByLabelText("Organisasjonsnummer for virksomhetene")).toBeDefined()
        })
    })
})
