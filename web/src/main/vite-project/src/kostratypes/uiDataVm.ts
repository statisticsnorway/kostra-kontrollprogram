import {KostraFormTypeVm} from "./kostraFormTypeVm";

export interface UiDataVm {
    readonly releaseVersion: NonNullable<string>
    readonly years: NonNullable<number[]>
    readonly formTypes: NonNullable<KostraFormTypeVm[]>
}