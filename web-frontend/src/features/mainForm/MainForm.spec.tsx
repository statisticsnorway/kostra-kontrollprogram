import {beforeEach, describe, expect, it, Mock, vi} from 'vitest'
import {act, fireEvent, render, screen, waitFor} from '@testing-library/react'
import MainForm from "./MainForm"
import KostraFormTypeVm from "../../kostratypes/kostraFormTypeVm"

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
// const formTypeThree = {
//     id: "~idWithSubCompanyId~",
//     tittel: "~tittel3~",
//     labelOrgnr: "Organisasjonsnummer for foretaket",
//     labelOrgnrVirksomhetene: "Organisasjonsnummer for virksomhetene"
// }

const yearInTests = (new Date()).getFullYear()
const yearInTestAsString = String(yearInTests)
const mockFile = new File(["file content"], "some_file.dat", {
    type: "text/plain"
})

const setupForSubmit = async (formType: KostraFormTypeVm = formTypeOne) => {
    const fileInput = screen.getByLabelText("Datafil (.dat eller .xml)")
    fireEvent.change(fileInput, {target: {files: [mockFile]}})

    const skjemaSelect = screen.getByLabelText("Skjema")
    fireEvent.change(skjemaSelect, {target: {value: formType.id}})

    const yearSelect = screen.getByLabelText("Årgang")
    fireEvent.change(yearSelect, {target: {value: yearInTestAsString}})

    const regionInput = screen.getByLabelText("Regionsnummer")
    fireEvent.change(regionInput, {target: {value: "123456"}})

    let orgnrInput: HTMLInputElement, orgnrVirksomhetInput: HTMLInputElement

    if (formType.labelOrgnr) {
        orgnrInput = screen.getByLabelText(formType.labelOrgnr as string)
        fireEvent.change(orgnrInput, {target: {value: "999999999"}})
    }

    if (formType.labelOrgnrVirksomhetene) {
        orgnrVirksomhetInput = screen.getByTestId("orgnrVirksomhet.0.orgnr")
        fireEvent.change(orgnrVirksomhetInput, {target: {value: "888888888"}})
    }

    await waitFor(() => {
        // @ts-ignore
        expect(fileInput.files[0].name).toBe(mockFile.name)
        expect(skjemaSelect).toHaveValue(formType.id)
        expect(yearSelect).toHaveValue(yearInTestAsString)
        expect(regionInput).toHaveValue("123456")

        if (formType.labelOrgnr) {
            expect(orgnrInput).toHaveValue("999999999")
        }

        if (formType.labelOrgnrVirksomhetene) {
            expect(orgnrVirksomhetInput).toHaveValue("888888888")
        }
    })
}

describe("MainForm", () => {
    let formTypeSelect: HTMLSelectElement
    let mockOnSubmit: Mock

    beforeEach(async () => {
        mockOnSubmit = vi.fn()

        await act(() => render(<MainForm
            formTypes={[formTypeOne, formTypeTwo/*, formTypeThree*/]}
            years={[yearInTests]}
            onSubmit={mockOnSubmit}/>))

        formTypeSelect = screen.getByLabelText<HTMLSelectElement>("Skjema")
    })

    describe("Layout", () => {
        it("displays 2 selects, 1 text input, 1 file input and 1 button initially", async () => {
            expect(screen.getByRole<HTMLOptionElement>("option", {name: "Velg skjematype"}).selected).toBeTruthy()
            expect(screen.getByRole<HTMLOptionElement>("option", {name: "Velg årgang"}).selected).toBeTruthy()
            expect(screen.getByLabelText<HTMLInputElement>("Regionsnummer").placeholder).toBe("6 siffer")
            expect(screen.getByLabelText("Datafil (.dat eller .xml)")).toBeInTheDocument()
            expect(screen.getByRole("button", {name: "Kontroller fil"})).toBeDisabled()
        })

        // it("displays validation errors initially", () => {
        //     expect(screen.queryByText("Skjematype er påkrevet")).toBeInTheDocument()
        //     expect(screen.queryByText("Årgang er påkrevet")).toBeInTheDocument()
        //     expect(screen.queryByText("Region er påkrevet")).toBeInTheDocument()
        //     expect(screen.queryByText("Vennligst velg fil")).toBeInTheDocument()
        // })
        //
        // it("hides validation errors when provided valid inputs", async () => {
        //     await setupForSubmit()
        //
        //     expect(screen.queryByText("Skjematype er påkrevet")).not.toBeInTheDocument()
        //     expect(screen.queryByText("Årgang er påkrevet")).not.toBeInTheDocument()
        //     expect(screen.queryByText("Region er påkrevet")).not.toBeInTheDocument()
        //     expect(screen.queryByText("Vennligst velg fil")).not.toBeInTheDocument()
        // })

        it("displays invalid value validation error for Regionsnummer when provided invalid value", async () => {
            fireEvent.change(screen.getByLabelText("Regionsnummer"), {target: {value: "123"}})

            await waitFor(() =>
                expect(screen.queryByText("Region må bestå av 6 siffer")).toBeInTheDocument())
        })

        it("displays required validation error for Organisasjonsnummer when not provided", async () => {
            fireEvent.change(formTypeSelect, {target: {value: formTypeTwo.id}})

            await waitFor(() =>
                expect(screen.queryByText("Organisasjonsnummer er påkrevet")).toBeInTheDocument())
        })

        it("displays invalid value validation error for Organisasjonsnummer when provided invalid value", async () => {
            fireEvent.change(formTypeSelect, {target: {value: formTypeTwo.id}})
            fireEvent.change(screen.getByLabelText(formTypeTwo.labelOrgnr), {target: {value: "123"}})

            await waitFor(() =>
                expect(screen.queryByText("Må starte med '8' eller '9' etterfulgt av 8 siffer")).toBeInTheDocument())
        })

        // it("displays validation error for Organisasjonsnummer for virksomhetene when provided invalid value", async () => {
        //     fireEvent.change(formTypeSelect, {target: {value: formTypeThree.id}})
        //
        //     const orgnrVirksomhet = await waitFor(() =>
        //         screen.getByTestId("orgnrVirksomhet.0.orgnr"))
        //
        //     fireEvent.change(orgnrVirksomhet as HTMLInputElement, {target: {value: "123"}})
        //
        //     await waitFor(() =>
        //         expect(orgnrVirksomhet?.className).toBe("form-control is-invalid"))
        // })

        // it("displays plus button when valid Organisasjonsnummer for virksomhetene", async () => {
        //     await setupForSubmit(formTypeThree)
        //     expect(screen.getByRole("button", {description: "Legg til virksomhetsnummer"})).toBeInTheDocument()
        // })

        // it("displays minus button when second Organisasjonsnummer for virksomhetene exists", async () => {
        //     await setupForSubmit(formTypeThree)
        //
        //     fireEvent.click(screen.getByRole("button", {description: "Legg til virksomhetsnummer"}))
        //
        //     await waitFor(() =>
        //         expect(screen.getByRole("button", {description: "Fjern virksomhetsnummer"})).toBeInTheDocument())
        // })

        // it("hides inputs for company-id and sub-company-id when a form type is selected", async () => {
        //     fireEvent.change(formTypeSelect, {target: {value: formTypeOne.id}})
        //
        //     // verify that inputs are not in the document
        //     await waitFor(() => {
        //         expect(screen.queryByText(formTypeTwo.labelOrgnr)).not.toBeInTheDocument()
        //         expect(screen.queryByText(formTypeThree.labelOrgnr)).not.toBeInTheDocument()
        //         expect(screen.queryByText(formTypeThree.labelOrgnrVirksomhetene)).not.toBeInTheDocument()
        //     })
        // })

        it("displays input for company-id when form type is selected", async () => {
            // verify that input is not in the document
//            expect(screen.queryByText(formTypeTwo.labelOrgnr)).not.toBeInTheDocument()

            fireEvent.change(formTypeSelect, {target: {value: formTypeTwo.id}})

            await waitFor(() => {
                // verify that input is in the document
                expect(screen.queryByText(formTypeTwo.labelOrgnr)).toBeInTheDocument()

                // // ... and not the other one
                // expect(screen.queryByText(formTypeThree.labelOrgnr)).not.toBeInTheDocument()
            })
        })

        // it("displays inputs for company-id and sub-company-id when form type is selected", async () => {
        //     // verify that inputs are not in the document
        //     expect(screen.queryByText(formTypeThree.labelOrgnr)).not.toBeInTheDocument
        //     expect(screen.queryByText(formTypeThree.labelOrgnrVirksomhetene)).not.toBeInTheDocument
        //
        //     fireEvent.change(formTypeSelect, {target: {value: formTypeThree.id}})
        //
        //     await waitFor(() => {
        //         // verify that inputs are in the document
        //         expect(screen.queryByText(formTypeThree.labelOrgnr)).toBeInTheDocument()
        //         expect(screen.queryByText(formTypeThree.labelOrgnrVirksomhetene)).toBeInTheDocument()
        //     })
        // })

        it("enables the submit button when provided valid form", async () => {
            const submitButton = screen.getByRole("button", {name: "Kontroller fil"})
            expect(submitButton).toBeDisabled()

            await setupForSubmit()

            expect(submitButton).toBeEnabled()
        })
    })

    // describe("Interactions", () => {
        // it("removes second Organisasjonsnummer for virksomhetene when minus button is clicked", async () => {
        //     await setupForSubmit(formTypeThree)
        //
        //     const plusButton =
        //         screen.getByRole("button", {description: "Legg til virksomhetsnummer"})
        //
        //     fireEvent.click(plusButton)
        //
        //     const minusButton =
        //         await waitFor(() => screen.getByRole("button", {description: "Fjern virksomhetsnummer"}))
        //
        //     fireEvent.click(minusButton)
        //
        //     await waitFor(() =>
        //         expect(minusButton).not.toBeInTheDocument())
        // })


        // const expectedBaseCallArgs = {
        //     aar: yearInTests,
        //     orgnrForetak: null,
        //     orgnrVirksomhet: [],
        //     region: "123456",
        //     skjema: formTypeOne.id,
        //     skjemaFil: [expect.objectContaining({
        //         name: mockFile.name,
        //         type: mockFile.type,
        //     })]
        // }

        // const runSubmitTest = async (formType: KostraFormTypeVm, expectedCallArgs: object) => {
        //     await setupForSubmit(formType)
        //     const submitButton = screen.getByRole("button", {name: "Kontroller fil"})
        //
        //     fireEvent.click(submitButton)
        //
        //     await waitFor(() => {
        //         // expect(mockOnSubmit).toHaveBeenCalledTimes(1)
        //         // expect(mockOnSubmit).toBeCalledWith(expectedCallArgs)
        //     })
        // }
        //
        // it("calls onSubmit when submit button is clicked", async () => {
        //     await runSubmitTest(formTypeOne, expectedBaseCallArgs)
        // })

        // it("calls onSubmit with orgnrForetak when submit button is clicked", async () => {
        //     await runSubmitTest(
        //         formTypeTwo,
        //         {
        //             ...expectedBaseCallArgs,
        //             skjema: formTypeTwo.id,
        //             orgnrForetak: "999999999"
        //         }
        //     )
        // })

        // it("calls onSubmit with orgnrForetak and orgnrVirksomhet when submit button is clicked", async () => {
        //     await runSubmitTest(
        //         formTypeThree,
        //         {
        //             ...expectedBaseCallArgs,
        //             skjema: formTypeThree.id,
        //             orgnrForetak: "999999999",
        //             orgnrVirksomhet: [{orgnr: "888888888"}]
        //         }
        //     )
        // })
    // })
})
