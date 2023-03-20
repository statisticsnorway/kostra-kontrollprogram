import CompanyIdVm from "./companyIdVm"

interface KostraFormVm {
    aar: NonNullable<number>
    skjema: NonNullable<string>
    region: NonNullable<string>
    orgnrForetak: string | undefined
    orgnrVirksomhet: CompanyIdVm[] | undefined
    skjemaFil: FileList
}

export default KostraFormVm