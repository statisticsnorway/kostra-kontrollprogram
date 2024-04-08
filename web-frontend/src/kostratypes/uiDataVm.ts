import KostraFormTypeVm from "./kostraFormTypeVm"

interface UiDataVm {
    readonly releaseVersion: NonNullable<string>
    readonly years: NonNullable<number[]>
    readonly formTypes: NonNullable<KostraFormTypeVm[]>
}

export default UiDataVm