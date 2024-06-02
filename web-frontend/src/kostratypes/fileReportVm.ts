import KostraFormVm from "./kostraFormVm"
import FileReportEntryVm from "./fileReportEntryVm"
import KostraSeverity from "./kostraSeverity"
import Nullable from "./nullable";

interface FileReportVm {
    readonly innparametere: NonNullable<KostraFormVm>
    readonly antallKontroller: NonNullable<number>
    readonly feilkode: NonNullable<KostraSeverity>
    readonly feil: Nullable<FileReportEntryVm[]>
}

export default FileReportVm