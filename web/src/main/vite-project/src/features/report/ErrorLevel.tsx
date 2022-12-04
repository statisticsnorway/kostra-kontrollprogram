import KostraErrorCode from "../../kostratypes/kostraErrorCode";

const getClassName = (level: NonNullable<KostraErrorCode>) => {
    switch (level) {
        case KostraErrorCode.NO_ERROR:
            return "text-success"
        case KostraErrorCode.NORMAL_ERROR:
            return "text-warning"
        default:
            return "text-danger"
    }
}

const getErrorText = (level: NonNullable<KostraErrorCode>) => {
    switch (level) {
        case KostraErrorCode.NO_ERROR:
            return "OK"
        case KostraErrorCode.NORMAL_ERROR:
            return "Advarsel"
        default:
            return "Kritisk"
    }
}

const ErrorLevel = ({level}: { level: NonNullable<KostraErrorCode> }) =>
    <span className={getClassName(level)}>{getErrorText(level)}</span>

export default ErrorLevel