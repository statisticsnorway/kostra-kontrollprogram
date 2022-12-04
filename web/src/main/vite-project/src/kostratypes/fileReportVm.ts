import KostraFormVm from "./kostraFormVm"
import FileReportEntryVm from "./fileReportEntryVm"
import KostraErrorCode from "./kostraErrorCode"
import Nullable from "./nullable";

interface FileReportVm {
    readonly innparametere: NonNullable<KostraFormVm>
    readonly antallKontroller: NonNullable<number>
    readonly feilkode: NonNullable<KostraErrorCode>
    readonly feil: Nullable<FileReportEntryVm[]>
}

export default FileReportVm