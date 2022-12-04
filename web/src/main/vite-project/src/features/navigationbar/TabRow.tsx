import FileReportVm from "../../kostratypes/fileReportVm";
import TabItem from "./TabItem";

// @ts-ignore
import FilterLeft from "../../assets/icon/filter-left.svg";
// @ts-ignore
import ListTask from "../../assets/icon/list-task.svg";


const TabRow = (
    {fileReports, activeTabIndex, onTabSelect, onReportDelete}: {
        fileReports: FileReportVm[],
        activeTabIndex: number,
        onTabSelect: (index: number) => void,
        onReportDelete: (index: number) => void
    }
) =>
    <ul className="nav nav-tabs" role={"navigation"}>
        {/** BACK TO FORM */}
        <TabItem
            text={activeTabIndex == 0 ?"Skjema" : "Tilbake til skjema"}
            image={FilterLeft}
            tabIsActive={activeTabIndex === 0}
            onSelect={() => onTabSelect(0)}
            showCloseButton={false}/>

        {/** REPORT TABS */}
        {fileReports.map((fileReport, index) =>
            <TabItem
                key={index}
                text={`${fileReport.innparametere.skjema} ${fileReport.innparametere.aar},`
                    + ` region ${fileReport.innparametere.region}`}
                image={ListTask}
                tabIsActive={activeTabIndex == index + 1}
                onSelect={() => onTabSelect(index + 1)}
                onClose={() => onReportDelete(index)}/>
        )}
    </ul>

export default TabRow