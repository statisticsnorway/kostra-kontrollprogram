import ReportView from "../components/ReportView"
import FileReportVm from "../kostratypes/fileReportVm"
import {useParams} from 'react-router-dom'
import {useQuery} from "react-query"
import {uiDataAsync} from "../api/apiCalls"

const FileReport = ({fileReports}: {
    fileReports: FileReportVm[]
}) => {
    const {reportId} = useParams()
    const fileReport = reportId ? fileReports[+reportId] : null

    // Fetch UI-data from backend
    const {data: uiData} =
        useQuery("uiData", () => uiDataAsync().then(uiData => uiData))

    return uiData && fileReport ? <ReportView
        fileReport={fileReport}
        appReleaseVersion={uiData.releaseVersion}
    /> : <></>
}

export default FileReport