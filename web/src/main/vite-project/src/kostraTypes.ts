export type Nullable<T> = T | null

export enum KostraErrorCode {
    NO_ERROR = 0,
    NORMAL_ERROR,
    CRITICAL_ERROR,
    SYSTEM_ERROR,
    PARAMETER_ERROR
}

export interface CompanyIdVm {
    orgnr: NonNullable<string>
}

export interface ErrorDetailsVm {
    readonly journalnummer: NonNullable<string>
    readonly saksbehandler: NonNullable<string>
    readonly kontrollnummer: NonNullable<string>
    readonly kontrolltekst: NonNullable<string>
    readonly feilkode: NonNullable<KostraErrorCode>
}

export interface ErrorReportVm {
    readonly innparametere: NonNullable<KostraFormVm>
    readonly antallKontroller: NonNullable<number>
    readonly feilkode: NonNullable<KostraErrorCode>
    readonly feil: ErrorDetailsVm[]
}

export interface KostraFormTypeVm {
    readonly id: NonNullable<string>
    readonly tittel: NonNullable<string>
    readonly labelOrgnr: Nullable<string>
    readonly labelOrgnrVirksomhetene: Nullable<string>
}

export interface KostraFormVm {
    aar: Nullable<number>
    skjema: Nullable<string>
    region: Nullable<string>
    navn: Nullable<string>
    orgnrForetak: Nullable<CompanyIdVm>
    orgnrVirksomhet: Nullable<CompanyIdVm[]>
    base64EncodedContent: Nullable<string>
}

export interface LocalKostraFormVm {
    aar: number
    skjema: string
    region: string
    orgnrForetak: string
    orgnrVirksomhet: Nullable<CompanyIdVm[]>
    base64EncodedContent: Nullable<string>
}
