import axios, {AxiosRequestConfig} from "axios"
import {KostraFormVm} from "../kostratypes/kostraFormVm";
import {KostraFormTypeVm} from "../kostratypes/kostraFormTypeVm";
import {ErrorReportVm} from "../kostratypes/errorReportVm";

const config: AxiosRequestConfig = {baseURL: "/api"}
export const api = axios.create(config)

export const listSkjemaTyperAsync = (): Promise<KostraFormTypeVm[]> =>
    api.get("/skjematyper").then(response => response.data as KostraFormTypeVm[])

export const kontrollerSkjemaAsync = (skjema: NonNullable<KostraFormVm>): Promise<ErrorReportVm> =>
    api.post("/kontroller-skjema", skjema).then(response => response.data as ErrorReportVm)
