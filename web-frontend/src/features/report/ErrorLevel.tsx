import KostraSeverity from "../../kostratypes/kostraSeverity";

const getClassName = (level: NonNullable<KostraSeverity>) => {
    switch (level) {
        case KostraSeverity.INFO:
        case KostraSeverity.OK:
            return "text-success"
        case KostraSeverity.WARNING:
            return "text-warning"
        default:
            return "text-danger"
    }
}

const getErrorText = (level: NonNullable<KostraSeverity>) => {
    switch (level) {
        case KostraSeverity.OK:
            return "OK"
        case KostraSeverity.INFO:
            return "Info"
        case KostraSeverity.WARNING:
            return "Advarsel"
        default:
            return "Kritisk"
    }
}

const ErrorLevel = ({level}: { level: NonNullable<KostraSeverity> }) =>
    <span className={getClassName(level)}>{getErrorText(level)}</span>

export default ErrorLevel