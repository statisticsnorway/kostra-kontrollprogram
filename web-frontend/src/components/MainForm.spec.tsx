import {afterEach, beforeEach, describe, expect, test} from 'vitest'
import {cleanup, fireEvent, render, screen, act} from '@testing-library/react'
import MainForm from "./MainForm"

describe("MainForm", () => {
    describe("Layout", () => {
        test("initial screen, displays 2 selects, 1 text input, 1 file input and 1 button", () => {
            render(<MainForm
                showForm={true}
                formTypes={[]}
                years={[(new Date()).getFullYear()]}
                onSubmit={() => {
                }}/>)

            expect(screen.getByRole<HTMLOptionElement>("option", {name: "Velg skjematype"}).selected).toBeTruthy()
            expect(screen.getByRole<HTMLOptionElement>("option", {name: "Velg årgang"}).selected).toBeTruthy()
            expect(screen.getByLabelText<HTMLInputElement>("Regionsnummer").placeholder).toBe("6 siffer")
            expect(screen.getByLabelText<HTMLInputElement>("Datafil (.dat eller .xml)")).toBeDefined()
            expect(screen.getByRole<HTMLButtonElement>("button", {name: "Kontroller fil"}).disabled).toBeTruthy()
        })
    })

    describe("Interactions", () => {

        const formTypeOne = {
            id: "~id~",
            tittel: "~tittel1~",
            labelOrgnr: null,
            labelOrgnrVirksomhetene: null
        }
        const formTypeTwo = {
            id: "~idWithCompanyId~",
            tittel: "~tittel2~",
            labelOrgnr: "Organisasjonsnummer",
            labelOrgnrVirksomhetene: null
        }
        const formTypeThree = {
            id: "~idWithSubCompanyId~",
            tittel: "~tittel3~",
            labelOrgnr: "Organisasjonsnummer for foretaket",
            labelOrgnrVirksomhetene: "Organisasjonsnummer for virksomhetene"
        }

        let formTypeSelect: HTMLSelectElement

        beforeEach(() => {
            render(<MainForm
                showForm={true}
                formTypes={[formTypeOne, formTypeTwo, formTypeThree]}
                years={[(new Date()).getFullYear()]}
                onSubmit={() => {
                }}/>)

            formTypeSelect = screen.getByLabelText<HTMLSelectElement>("Skjema")
        })

        afterEach(() => {
            cleanup()
        })

        test("when a form type without company-id and sub-company-id is selected", async () => {
            await act(async () => {
                fireEvent.change(formTypeSelect, {target: {value: formTypeOne.id}})
            })

            // verify that inputs are not in the document
            expect(() => screen.getByLabelText(formTypeTwo.labelOrgnr)).toThrow()
            expect(() => screen.getByLabelText(formTypeThree.labelOrgnr)).toThrow()
            expect(() => screen.getByText(formTypeThree.labelOrgnrVirksomhetene)).toThrow()
        })

        test("when a form type with company-id is selected", async () => {

            // verify that input is not in the document
            expect(() => screen.getByLabelText(formTypeTwo.labelOrgnr)).toThrow()

            await act(async () => {
                fireEvent.change(formTypeSelect, {target: {value: formTypeTwo.id}})
            })

            // verify that input is in the document
            expect(screen.getByLabelText(formTypeTwo.labelOrgnr)).toBeDefined()

            // ... and not the other one
            expect(() => screen.getByLabelText(formTypeThree.labelOrgnr)).toThrow()
        })

        test("when a form type with company-id and sub-company-id is selected", async () => {

            // verify that inputs are not in the document
            expect(() => screen.getByLabelText(formTypeThree.labelOrgnr)).toThrow()
            expect(() => screen.getByText(formTypeThree.labelOrgnrVirksomhetene)).toThrow()

            await act(async () => {
                fireEvent.change(formTypeSelect, {target: {value: formTypeThree.id}})
            })

            // verify that inputs are in the document
            expect(screen.getByLabelText(formTypeThree.labelOrgnr)).toBeDefined()
            expect(screen.getByText(formTypeThree.labelOrgnrVirksomhetene)).toBeDefined()
        })
    })
})
