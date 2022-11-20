import {FileReportVm} from "../kostratypes/fileReportVm";
import {KostraErrorCode} from "../kostratypes/kostraErrorCode";


export const ReportView = (props: {
    readonly fileReport: NonNullable<FileReportVm>
}) => {
    const {fileReport} = props

    const getEnumText = (untypedString: string): string =>
        KostraErrorCode[(untypedString as keyof typeof KostraErrorCode)]

    const renderErrorLevel = (untypedErrorCode:string) => {
        const errorKodeText = getEnumText(untypedErrorCode)
        return <span className={errorKodeText == "Normal" ? "text-success" : "text-danger"}>{errorKodeText}</span>
    }

    return <div className="table-responsive mt-3">
        <table className="table table-striped table-sm">
            <thead>
            <tr>
                <th scope="col">Feilkode</th>
                <th scope="col">Journalnummer</th>
                <th scope="col">Saksbehandler</th>
                <th scope="col">Kontrollnummer</th>
                <th scope="col">Kontrolltekst</th>
            </tr>
            </thead>
            <tbody>
            {fileReport.feil.map((reportEntry, index) => {
                return <tr key={index}>
                    <td>{renderErrorLevel(reportEntry.feilkode)}</td>
                    <td>{reportEntry.journalnummer}</td>
                    <td>{reportEntry.saksbehandler}</td>
                    <td>{reportEntry.kontrollnummer}</td>
                    <td>{reportEntry.kontrolltekst}</td>
                </tr>
            })}
            </tbody>
        </table>
    </div>

}

export default ReportView