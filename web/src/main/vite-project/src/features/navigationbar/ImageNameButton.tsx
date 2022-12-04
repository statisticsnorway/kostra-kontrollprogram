import {Button} from "react-bootstrap";

interface ImageNameButtonProps {
    onClick: () => void
    text: string
    image: string
}

const ImageNameButton = ({onClick, text, image}: ImageNameButtonProps) =>
    <Button
        onClick={onClick}
        className="btn bg-transparent btn-outline-light p-0"
        title={text}>

        <img src={image} className="pe-2" alt="Kostra"/>
        <span className="text-black">{text}</span>
    </Button>

export default ImageNameButton