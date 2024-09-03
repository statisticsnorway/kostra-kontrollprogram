import ReportView from "../components/ReportView"
import FileReportVm from "../kostratypes/fileReportVm"
import {useParams} from 'react-router-dom'

const FileReport = ({fileReports, releaseVersion}: {
    fileReports: FileReportVm[],
    releaseVersion: string
}) => {
    const {reportId} = useParams()
    const fileReport = reportId ? fileReports[+reportId] : null

    if (!fileReport) throw new Error("Valgt rapport finnes ikke")

    return <ReportView
        fileReport={fileReport}
        appReleaseVersion={releaseVersion}
    />
}

export default FileReport