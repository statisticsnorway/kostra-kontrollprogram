import Nullable from "./nullable"
import CompanyIdVm from "./companyIdVm"

interface KostraFormVm {
    aar: NonNullable<number>
    skjema: NonNullable<string>
    region: NonNullable<string>
    orgnrForetak: Nullable<string>
    orgnrVirksomhet: Nullable<CompanyIdVm[]>
    skjemaFil: FileList
}

export default KostraFormVm