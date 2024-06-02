import FileReportVm from "../../kostratypes/fileReportVm";
import TabItem from "./TabItem";

// @ts-ignore
import FilterLeft from "../../assets/icon/filter-left.svg";
// @ts-ignore
import ListTask from "../../assets/icon/list-task.svg";
import {useNavigate} from "react-router-dom";


const TabRow = (
    {fileReports, activeTabIndex, onDeleteFileReport}: {
        fileReports: FileReportVm[],
        activeTabIndex: number,
        onDeleteFileReport: (index: number) => void
    }
) => {
    const navigate = useNavigate()

    return <ul className="nav nav-tabs" role={"navigation"}>
        {/** BACK TO FORM */}
        <TabItem
            text={activeTabIndex == 0 ? "Skjema" : "Tilbake til skjema"}
            image={FilterLeft}
            tabIsActive={activeTabIndex === 0}
            onSelect={() => navigate("/")}
            showCloseButton={false}/>

        {/** REPORT TABS */}
        {fileReports.map((fileReport, index) =>
            <TabItem
                key={index}
                text={`${fileReport.innparametere.skjema} ${fileReport.innparametere.aar},`
                    + ` region ${fileReport.innparametere.region}`}
                image={ListTask}
                tabIsActive={activeTabIndex == index + 1}
                onSelect={() => navigate(`file-reports/${index}`)}
                onClose={() => {
                    onDeleteFileReport(index)
                    navigate("/")
                }}/>
        )}
    </ul>
}
export default TabRow