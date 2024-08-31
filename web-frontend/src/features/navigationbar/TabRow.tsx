import FileReportVm from "../../kostratypes/fileReportVm"
import TabItem from "./TabItem"

// @ts-ignore
import FilterLeft from "../../assets/icon/filter-left.svg"
import {Link, useNavigate, useParams} from "react-router-dom"


const TabRow = (
    {fileReports, onDeleteFileReport}: {
        fileReports: FileReportVm[],
        onDeleteFileReport: (index: number) => void
    }
) => {
    const navigate = useNavigate()
    const {reportId} = useParams()

    return <nav className="navbar navbar-light navbar-expand">
        <Link to="/" className="navbar-brand">
            <img src={FilterLeft} alt="Skjema"/>{reportId ? "Tilbake til skjema" : "Skjema"}
        </Link>
        <ul className="navbar-nav me-auto">
            {/** REPORT TABS*/}
            {fileReports.map((fileReport, index) =>
                <TabItem
                    key={index}
                    id={index}
                    reportName={`${fileReport.innparametere.skjema} ${fileReport.innparametere.aar},`
                        + ` region ${fileReport.innparametere.region}`}
                    tabIsActive={reportId == String(index)}
                    onClose={() => {
                        onDeleteFileReport(index)
                        navigate("/")
                    }}/>
            )}
        </ul>
    </nav>
}
export default TabRow