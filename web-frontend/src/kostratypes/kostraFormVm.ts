import CompanyIdVm from "./companyIdVm"
import Nullable from "./nullable"

interface KostraFormVm {
    aar: NonNullable<number>
    skjema: NonNullable<string>
    region: NonNullable<string>
    orgnrForetak: Nullable<string>
    orgnrVirksomhet: Nullable<CompanyIdVm[]>
    skjemaFil: Nullable<FileList>
}

export default KostraFormVm