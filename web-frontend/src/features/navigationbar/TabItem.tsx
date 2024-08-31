import {Link} from "react-router-dom"

// @ts-ignore
import ListTask from "../../assets/icon/list-task.svg"
import CloseButton from "./CloseButton"

const TabItem = ({id, reportName, tabIsActive, onClose}: {
    id: number,
    reportName: string,
    tabIsActive: boolean,
    onClose: () => void,
}) => {
    let className = "nav-link pt-1 pb-1"
    if (tabIsActive) className += " active"

    return <li className="nav-item d-flex align-items-center">
            <Link to={`/file-reports/${id}`} className={className} data-testid="tab-item-div">
                <img src={ListTask} className="pe-2" alt="Kostra"/>
                {reportName}
            </Link>
            <CloseButton onClick={onClose}/>
    </li>
}

export default TabItem