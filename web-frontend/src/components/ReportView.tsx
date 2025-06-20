import FileReportVm from "../kostratypes/fileReportVm"

import ErrorSummary from "../features/report/ErrorSummary"
import ReportSummary from "../features/report/ReportSummary"
import ErrorDetailsTable from "../features/report/ErrorDetailsTable"


const ReportView = ({appReleaseVersion, fileReport}: {
    appReleaseVersion: string,
    fileReport: FileReportVm
}) => {
    const feil = fileReport.feil ?? []
    const hasErrors = feil.length > 0

    return <>
        <ReportSummary appReleaseVersion={appReleaseVersion} fileReport={fileReport}/>
        {hasErrors && (
            <>
                <ErrorSummary reportEntries={feil}/>
                <ErrorDetailsTable reportEntries={feil}/>
            </>)}
    </>
}

export default ReportView