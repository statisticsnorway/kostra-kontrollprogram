import MainForm from "./MainForm";
import {fireEvent, render, screen} from '@testing-library/react'
import {describe, expect, test} from 'vitest';

//screen.debug()

describe("MainForm", () => {
    describe("Layout", () => {
        test("initial screen, displays 2 selects, 1 text input, 1 file input and 1 button", () => {
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
            expect(() => screen.getByRole("file")).toBeDefined()
            expect((screen.getByRole("button", {name: "Kontroller fil"}) as HTMLButtonElement).disabled).toBe(true)
        })
    })

    describe("Interactions", () => {

        let formSelect: HTMLSelectElement

        beforeEach(() => {
            render(<MainForm
                showForm={true}
                uiData={{
                    releaseVersion: "N/A",
                    formTypes: [
                        {
                            id: "~id~",
                            tittel: "~tittel~",
                            labelOrgnr: "~labelOrgnr~",
                            labelOrgnrVirksomhetene: null
                        },
                        {
                            id: "~idWithCompanyId~",
                            tittel: "~tittel~",
                            labelOrgnr: "~labelOrgnr~",
                            labelOrgnrVirksomhetene: null
                        },
                        {
                            id: "~idWithSubCompanyId~",
                            tittel: "~tittel~",
                            labelOrgnr: "~labelOrgnr~",
                            labelOrgnrVirksomhetene: "~labelOrgnrVirksomhetene~"
                        },
                    ],
                    years: [(new Date()).getFullYear()]
                }}
                onSubmit={() => {}}/>);

            formSelect = screen.getByLabelText("Skjema") as HTMLSelectElement
        })

        test("when a form type without company-id and sub-company-id is selected", () => {
            fireEvent.change(formSelect, {target: {value: "~id~"}})

            // verify that inputs are not in the document
            expect(() => screen.getByLabelText("Organisasjonsnummer")).toThrow()
            expect(() => screen.getByLabelText("Organisasjonsnummer for virksomhetene")).toThrow()
        })

        test("when a form type with company-id is selected", () => {

            // verify that input is not in the document
            expect(() => screen.getByText("Organisasjonsnummer")).toThrow()

            fireEvent.change(formSelect, {target: {value: "~idWithCompanyId~"}})

            // verify that input is in the document
            expect(() => screen.getByLabelText("Organisasjonsnummer")).toBeDefined()
        })

        test("when a form type with sub-company-id is selected", () => {

            // verify that input is not in the document
            expect(() => screen.getByText("Organisasjonsnummer for virksomhetene")).toThrow()

            fireEvent.change(formSelect, {target: {value: "~labelOrgnrVirksomhetene~"}})

            // verify that input is in the document
            expect(() => screen.getByLabelText("Organisasjonsnummer for virksomhetene")).toBeDefined()
        })
    })
})
