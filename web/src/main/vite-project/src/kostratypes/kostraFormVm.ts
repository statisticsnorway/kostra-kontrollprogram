import {Nullable} from "./nullable";

export interface KostraFormVm {
    aar: Nullable<number>
    skjema: Nullable<string>
    region: Nullable<string>
    navn: Nullable<string>
    orgnrForetak: Nullable<string>
    orgnrVirksomhet: Nullable<string[]>
    base64EncodedContent: Nullable<string>
}