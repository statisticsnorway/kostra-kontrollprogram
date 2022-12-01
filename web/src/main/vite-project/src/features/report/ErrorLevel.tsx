import {getEnumText, getEnumValue} from "../../util/enumUtils";
import {KostraErrorCode} from "../../kostratypes/kostraErrorCode";


const ErrorLevel = ({ level }: { level: string }) => {

    let cssStyle

    switch (getEnumValue(level)) {
        case KostraErrorCode.NO_ERROR:
            cssStyle = "text-success"
            break
        case KostraErrorCode.NORMAL_ERROR:
            cssStyle = "text-warning"
            break
        default:
            cssStyle = "text-danger"
            break
    }

  return <span className={cssStyle}>{getEnumText(level)}</span>;
}

export default ErrorLevel