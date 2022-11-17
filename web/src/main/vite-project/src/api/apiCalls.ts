import axios, {AxiosRequestConfig} from "axios"
import {KostraFormVm} from "../kostratypes/kostraFormVm";
import {KostraFormTypeVm} from "../kostratypes/kostraFormTypeVm";
import {ErrorReportVm} from "../kostratypes/errorReportVm";

const config: AxiosRequestConfig = {baseURL: "/api"}
export const api = axios.create(config)

export const listSkjemaTyperAsync = (): Promise<KostraFormTypeVm[]> =>
    api.get<KostraFormTypeVm[]>("/skjematyper").then(response => response.data)

export const kontrollerSkjemaAsync = (skjema: NonNullable<KostraFormVm>): Promise<ErrorReportVm> =>
    api.post<ErrorReportVm>("/kontroller-skjema", skjema).then(response => response.data)
