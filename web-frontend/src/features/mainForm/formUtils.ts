import Nullable from "../../kostratypes/nullable"
import KostraFormVm from "../../kostratypes/kostraFormVm"

export const FORM_LOCAL_STORAGE_KEY = "kostra-form"

export const loadFormFromLocalStorage = (): Nullable<KostraFormVm> => {
    const savedData = localStorage.getItem(FORM_LOCAL_STORAGE_KEY)
    if (!savedData) return null

    try {
        const parsedForm = JSON.parse(savedData) as KostraFormVm
        return {...parsedForm, skjemaFil: null}
    } catch (error) {
        return null
    }
}
