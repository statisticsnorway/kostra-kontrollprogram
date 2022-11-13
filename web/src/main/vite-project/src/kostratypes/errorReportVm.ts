import {KostraFormVm} from "./kostraFormVm";
import {ErrorDetailsVm} from "./errorDetailsVm";
import {KostraErrorCode} from "./kostraErrorCode";

export interface ErrorReportVm {
    readonly innparametere: NonNullable<KostraFormVm>
    readonly antallKontroller: NonNullable<number>
    readonly feilkode: NonNullable<KostraErrorCode>
    readonly feil: ErrorDetailsVm[]
}