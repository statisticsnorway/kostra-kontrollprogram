import {Nullable} from "./nullable";

export interface KostraFormTypeVm {
    readonly id: NonNullable<string>
    readonly tittel: NonNullable<string>
    readonly labelOrgnr: Nullable<string>
    readonly labelOrgnrVirksomhetene: Nullable<string>
}