import FileReportVm from "../kostratypes/fileReportVm"

import ErrorSummary from "../features/report/ErrorSummary";
import ReportSummary from "../features/report/ReportSummary";
import ErrorDetailsTable from "../features/report/ErrorDetailsTable";


const ReportView = ({appReleaseVersion, fileReport}: {
    appReleaseVersion: NonNullable<string>,
    fileReport: FileReportVm
}) =>
    <>
        <ReportSummary appReleaseVersion={appReleaseVersion} fileReport={fileReport}/>
        {fileReport.feil?.length  && <ErrorSummary reportEntries={fileReport.feil}/>}
        {fileReport.feil?.length && <ErrorDetailsTable reportEntries={fileReport.feil}/>}
    </>

export default ReportView