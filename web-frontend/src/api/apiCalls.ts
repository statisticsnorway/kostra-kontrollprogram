import axios, {AxiosRequestConfig} from "axios"
import KostraFormVm from "../kostratypes/kostraFormVm"
import FileReportVm from "../kostratypes/fileReportVm"
import UiDataVm from "../kostratypes/uiDataVm"

export const MULTIPART_HEADER_CONFIG = {headers: {"Content-Type": "multipart/form-data"}}

const config: AxiosRequestConfig = {baseURL: "/api"}
export const api = axios.create(config)

export const uiDataAsync = (): Promise<UiDataVm> =>
    api.get<UiDataVm>("/ui-data").then(({data}) => data)

export const kontrollerSkjemaAsync = (skjema: NonNullable<KostraFormVm>): Promise<FileReportVm> =>
    api.post<FileReportVm>(
        "/kontroller-skjema",
        kostraFormToMultipartBody(skjema),
        MULTIPART_HEADER_CONFIG
    ).then(({data}) => data)

/**
 * builds FormData from KostraFormVm
 */
export const kostraFormToMultipartBody = (skjema: NonNullable<KostraFormVm>): FormData => {
    const bodyFormData = new FormData()

    if (!skjema.skjemaFil) throw new Error("skjemaFil is required")

    // append form as JSON-string without file, add filename
    bodyFormData.append(
        "kostraFormAsJson",
        JSON.stringify({
            ...skjema,
            skjemaFil: null,
            filnavn: skjema.skjemaFil[0].name
        })
    )

    // append file
    bodyFormData.append("file", skjema.skjemaFil[0])

    return bodyFormData
}