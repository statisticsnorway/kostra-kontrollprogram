import KostraFormVm from "./kostraFormVm"
import FileReportEntryVm from "./fileReportEntryVm"
import KostraErrorCode from "./kostraErrorCode"

interface FileReportVm {
    readonly innparametere: NonNullable<KostraFormVm>
    readonly antallKontroller: NonNullable<number>
    readonly feilkode: NonNullable<KostraErrorCode>
    readonly feil: FileReportEntryVm[]
}

export default FileReportVm