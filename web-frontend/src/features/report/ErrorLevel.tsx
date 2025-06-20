import KostraSeverity from "../../kostratypes/kostraSeverity"

const getClassName = (level: NonNullable<KostraSeverity>) => {
    switch (level) {
        case KostraSeverity.OK:
            return "text-success"
        case KostraSeverity.INFO:
            return "text-info"
        case KostraSeverity.WARNING:
            return "text-warning"
        case KostraSeverity.ERROR:
        case KostraSeverity.FATAL:
            return "text-danger"
        default:
            return "text-muted"
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
        case KostraSeverity.ERROR:
            return "Feil"
        case KostraSeverity.FATAL:
            return "Kritisk"
        default:
            return "Ukjent"
    }
}

const ErrorLevel = ({level}: { level: NonNullable<KostraSeverity> }) =>
    <span className={getClassName(level)}>{getErrorText(level)}</span>

export default ErrorLevel