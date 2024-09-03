import {Button} from "react-bootstrap"

export const CHAR_MULTIPLICATION_X = "\u2715"

const CloseButton = ({onClick}: { onClick: () => void }) =>
    <Button
        onClick={onClick}
        className="bg-transparent btn-outline-light ps-0 pe-1 rounded-circle text-black-50"
        title="Slett rapport">
        {CHAR_MULTIPLICATION_X}
    </Button>

export default CloseButton