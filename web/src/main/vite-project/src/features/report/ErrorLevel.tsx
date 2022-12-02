import KostraErrorCode from "../../kostratypes/kostraErrorCode";

// gets text value from enum KostraErrorCode
const getEnumText = (untypedString: string): string =>
    KostraErrorCode[(untypedString as keyof typeof KostraErrorCode)]

const getEnumValue = (untypedString: string): KostraErrorCode =>
    KostraErrorCode[untypedString as keyof typeof KostraErrorCode]

const getClassName = (level: string) => {
    switch (getEnumValue(level)) {
        case KostraErrorCode.NO_ERROR:
            return "text-success"
        case KostraErrorCode.NORMAL_ERROR:
            return "text-warning"
        default:
            return "text-danger"
    }
}

const ErrorLevel = ({level}: { level: string }) =>
    <span className={getClassName(level)}>{getEnumText(level)}</span>

export default ErrorLevel