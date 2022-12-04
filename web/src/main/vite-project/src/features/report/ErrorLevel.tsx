import KostraErrorCode from "../../kostratypes/kostraErrorCode";

const getClassName = (level: KostraErrorCode) => {
    switch (level) {
        case KostraErrorCode.NO_ERROR:
            return "text-success"
        case KostraErrorCode.NORMAL_ERROR:
            return "text-warning"
        default:
            return "text-danger"
    }
}

const getErrorText = (level: KostraErrorCode) => {
    switch (level) {
        case KostraErrorCode.NO_ERROR:
            return "OK"
        case KostraErrorCode.NORMAL_ERROR:
            return "Advarsel"
        default:
            return "Kritisk"
    }
}

const ErrorLevel = ({level}: { level: KostraErrorCode }) =>
    <span className={getClassName(level)}>{getErrorText(level)}</span>

export default ErrorLevel