import Nullable from "./nullable"

interface KostraFormTypeVm {
    readonly id: NonNullable<string>
    readonly tittel: NonNullable<string>
    readonly labelOrgnr: Nullable<string>
}

export default KostraFormTypeVm