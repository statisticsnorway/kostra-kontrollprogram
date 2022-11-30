import {KostraErrorCode} from "../kostratypes/kostraErrorCode"

// gets text value from enum KostraErrorCode
export const getEnumText = (untypedString: string): string =>
    KostraErrorCode[(untypedString as keyof typeof KostraErrorCode)]

export const getEnumValue = (untypedString: string): KostraErrorCode =>
    KostraErrorCode[untypedString as keyof typeof KostraErrorCode]
