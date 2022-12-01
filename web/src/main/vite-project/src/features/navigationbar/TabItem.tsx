import ImageNameButton from "./ImageNameButton";
import CloseButton from "./CloseButton";

export const TabItem = (props: {
    text: string,
    image: string,
    tabIsActive: boolean,
    onSelect: () => void,
    showCloseButton: boolean,
    onClose: () => void
}) => {
    const {
        text,
        image,
        tabIsActive,
        onSelect,
        showCloseButton = true,
        onClose} = props

    return <li className="nav-item">
        <div className={tabIsActive ? "nav-link active pt-1 pb-1" : "nav-link pt-1 pb-1"}>
            <ImageNameButton
                onClick={onSelect}
                text={text}
                image={image}
            />

            {showCloseButton && <CloseButton onClick={onClose}/>}
        </div>
    </li>
}

// Set default props
TabItem.defaultProps = {
    showCloseButton: true,
    onClose: () => {}
}

export default TabItem