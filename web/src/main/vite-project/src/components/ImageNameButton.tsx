import {Button} from "react-bootstrap";

// @ts-ignore
import ListTask from "../assets/icon/list-task.svg";

interface ImageNameButtonProps {
    onClick: () => void
    text: string
}

export const ImageNameButton = ({onClick, text}: ImageNameButtonProps) =>
    <Button
        onClick={onClick}
        className="btn bg-transparent btn-outline-light p-0"
        title={`Vis rapport for ${text}`}>

        <img src={ListTask} className="pe-2" alt="Kostra"/>
        <span className="text-black">{text}</span>
    </Button>