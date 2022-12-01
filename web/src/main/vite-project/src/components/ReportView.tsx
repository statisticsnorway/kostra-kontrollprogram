import {FileReportVm} from "../kostratypes/fileReportVm"

import ErrorSummary from "../features/report/ErrorSummary";
import ReportSummary from "../features/report/ReportSummary";
import ErrorDetailsTable from "../features/report/ErrorDetailsTable";


export const ReportView = ({appReleaseVersion, fileReport}: {
    appReleaseVersion: NonNullable<string>,
    fileReport: NonNullable<FileReportVm>
}) =>
    <>
        <ReportSummary appReleaseVersion={appReleaseVersion} fileReport={fileReport}/>
        <ErrorSummary reportEntries={fileReport.feil}/>
        <ErrorDetailsTable reportEntries={fileReport.feil}/>
    </>

export default ReportView