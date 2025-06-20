import FileReportVm from "../../kostratypes/fileReportVm"
import ErrorLevel from "./ErrorLevel"


const ReportSummary = ({appReleaseVersion, fileReport}: {
    appReleaseVersion: NonNullable<string>
    fileReport: NonNullable<FileReportVm>
}) =>
    <div className="card mt-3">
        <div className="card-body">
            <h5 className="card-title">Sammendrag</h5>
            <p className="card-text">
                For detaljinformasjon om hver enkelt feil, vennligst se tabell under.
            </p>
        </div>
        <ul className="list-group list-group-flush">
            <li className="list-group-item">
                Programvareversjon: {appReleaseVersion}
            </li>
            <li className="list-group-item">
                Skjema: {fileReport.innparametere.skjema}
            </li>
            <li className="list-group-item">
                Årgang: {fileReport.innparametere.aar}
            </li>
            <li className="list-group-item">
                Regionsnummer: {fileReport.innparametere.region}
            </li>
            {fileReport.innparametere.orgnrForetak &&
                <li className="list-group-item">
                    Organisasjonsnummer: {fileReport.innparametere.orgnrForetak}
                </li>}
            <li className="list-group-item">
                Høyeste alvorlighetsgrad: <ErrorLevel level={fileReport.severity}/>
            </li>
            <li className="list-group-item">
                Antall kontroller utført: {fileReport.antallKontroller}
            </li>
        </ul>
    </div>

export default ReportSummary