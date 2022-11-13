import {Nullable} from "./nullable";

export interface KostraFormVm {
    aar: NonNullable<number>
    skjema: NonNullable<string>
    region: NonNullable<string>
    orgnrForetak: Nullable<string>
    orgnrVirksomhet: Nullable<string[]>
    base64EncodedContent: Nullable<string>
}