import {KostraErrorCode} from "./kostraErrorCode"

export interface FileReportEntryVm {
    readonly journalnummer: NonNullable<string>
    readonly saksbehandler: NonNullable<string>
    readonly kontrollnummer: NonNullable<string>
    readonly kontrolltekst: NonNullable<string>
    readonly feilkode: NonNullable<KostraErrorCode>
}