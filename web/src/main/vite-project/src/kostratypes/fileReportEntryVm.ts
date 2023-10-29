import KostraSeverity from "./kostraSeverity"
import Nullable from "./nullable";

interface FileReportEntryVm {
    readonly severity: NonNullable<KostraSeverity>
    readonly caseworker: NonNullable<string>
    readonly journalId: Nullable<string>
    readonly individId: Nullable<string>
    readonly contextId: NonNullable<string>
    readonly ruleName: NonNullable<string>
    readonly messageText: NonNullable<string>
    readonly lineNumbers: Nullable<number[]>
}

export default FileReportEntryVm