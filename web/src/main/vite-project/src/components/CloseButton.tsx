import {Button} from "react-bootstrap";
import React from "react";


export const CloseButton = ({onClick}: { onClick: React.MouseEventHandler<HTMLButtonElement> }) => {
    const multiplicationX = "\u2715"
    return (
        <Button
            onClick={onClick}
            className="bg-transparent btn-outline-light ms-1 p-0 ps-1 pe-1 rounded-circle text-black-50"
            title="Slett rapport">
            {multiplicationX}
        </Button>
    )
}