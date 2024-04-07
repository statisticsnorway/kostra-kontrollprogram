import ImageNameButton from "./ImageNameButton";
import CloseButton from "./CloseButton";

const TabItem = (props: {
    text: string,
    image: string,
    tabIsActive: boolean,
    onSelect: () => void,
    showCloseButton: boolean,
    onClose: () => void
}) =>
    <li className="nav-item">
        <div data-testid="tab-item-div" className={props.tabIsActive ? "nav-link active pt-1 pb-1" : "nav-link pt-1 pb-1"}>
            <ImageNameButton
                onClick={props.onSelect}
                text={props.text}
                image={props.image}
            />

            {props.showCloseButton && <CloseButton onClick={props.onClose}/>}
        </div>
    </li>

// Set default props
TabItem.defaultProps = {
    showCloseButton: true,
    onClose: () => {}
}

export default TabItem