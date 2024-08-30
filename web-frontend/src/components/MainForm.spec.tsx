import {beforeEach, describe, expect, test} from 'vitest'
import {act, fireEvent, render, screen} from '@testing-library/react'
import MainForm from "./MainForm"

describe("MainForm", () => {
    describe("Layout", () => {
        test("initial screen, displays 2 selects, 1 text input, 1 file input and 1 button", () => {
            render(<MainForm
                formTypes={[]}
                years={[(new Date()).getFullYear()]}
                onSubmit={() => {
                }}/>)

            expect(screen.getByRole<HTMLOptionElement>("option", {name: "Velg skjematype"}).selected).toBeTruthy()
            expect(screen.getByRole<HTMLOptionElement>("option", {name: "Velg årgang"}).selected).toBeTruthy()
            expect(screen.getByLabelText<HTMLInputElement>("Regionsnummer").placeholder).toBe("6 siffer")
            expect(screen.getByLabelText<HTMLInputElement>("Datafil (.dat eller .xml)")).toBeInTheDocument()
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
                formTypes={[formTypeOne, formTypeTwo, formTypeThree]}
                years={[(new Date()).getFullYear()]}
                onSubmit={() => {
                }}/>)

            formTypeSelect = screen.getByLabelText<HTMLSelectElement>("Skjema")
        })

        test("when a form type without company-id and sub-company-id is selected", () => {
            act(() => {
                fireEvent.change(formTypeSelect, {target: {value: formTypeOne.id}})
            })

            // verify that inputs are not in the document
            expect(screen.queryByLabelText(formTypeTwo.labelOrgnr)).not.toBeInTheDocument()
            expect(screen.queryByLabelText(formTypeThree.labelOrgnr)).not.toBeInTheDocument()
            expect(screen.queryByText(formTypeThree.labelOrgnrVirksomhetene)).not.toBeInTheDocument()
        })

        test("when a form type with company-id is selected", () => {

            // verify that input is not in the document
            expect(screen.queryByLabelText(formTypeTwo.labelOrgnr)).not.toBeInTheDocument()

            act(() => {
                fireEvent.change(formTypeSelect, {target: {value: formTypeTwo.id}})
            })

            // verify that input is in the document
            expect(screen.queryByLabelText(formTypeTwo.labelOrgnr)).toBeInTheDocument()

            // ... and not the other one
            expect(screen.queryByLabelText(formTypeThree.labelOrgnr)).not.toBeInTheDocument()
        })

        test("when a form type with company-id and sub-company-id is selected", () => {
            // verify that inputs are not in the document
            expect(screen.queryByText(formTypeThree.labelOrgnr)).not.toBeInTheDocument
            expect(screen.queryByText(formTypeThree.labelOrgnrVirksomhetene)).not.toBeInTheDocument

            act(() => {
                fireEvent.change(formTypeSelect, {target: {value: formTypeThree.id}})
            })

            // verify that inputs are in the document
            expect(screen.queryByLabelText(formTypeThree.labelOrgnr)).toBeInTheDocument()
            expect(screen.queryByText(formTypeThree.labelOrgnrVirksomhetene)).toBeInTheDocument()
        })
    })
})
