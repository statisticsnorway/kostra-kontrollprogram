import {FileReportVm} from "../kostratypes/fileReportVm";
import {KostraErrorCode} from "../kostratypes/kostraErrorCode";


export const ReportView = (props: {
    readonly fileReport: NonNullable<FileReportVm>
}) => {
    const {fileReport} = props

    // gets text value from enum KostraErrorCode
    const getEnumText = (untypedString: string): string =>
        KostraErrorCode[(untypedString as keyof typeof KostraErrorCode)]

    const renderErrorLevel = (untypedErrorCode: string) => {
        const errorKodeText = getEnumText(untypedErrorCode)
        return <span className={errorKodeText == "Normal" ? "text-success" : "text-danger"}>{errorKodeText}</span>
    }

    return <>
        <div className="card mt-3">
            <div className="card-body">
                <h5 className="card-title">Sammendrag</h5>
                <p className="card-text">
                    For detaljinformasjon om hver enkelt feil, vennligst se tabell under.
                </p>
            </div>
            <ul className="list-group list-group-flush">
                <li className="list-group-item">
                    Skjema: {fileReport.innparametere.skjema}
                </li>
                <li className="list-group-item">
                    Årgang: {fileReport.innparametere.aar}
                </li>
                <li className="list-group-item">
                    Regionsnummer: {fileReport.innparametere.region}
                </li>
                {fileReport.innparametere.orgnrForetak && <li className="list-group-item">
                    Organisasjonsnummer: {fileReport.innparametere.orgnrForetak}
                </li>}
                {fileReport.innparametere.orgnrVirksomhet != undefined
                    && fileReport.innparametere.orgnrVirksomhet?.length > 0
                    && <li className="list-group-item">
                        Organisasjonsnummer virksomhet(er):
                        {fileReport.innparametere.orgnrVirksomhet.map((value, index) =>
                            index > 0 ? `, ${value.orgnr}` : ` ${value.orgnr}`
                        )}
                    </li>}
                <li className="list-group-item">
                    Høyeste alvorlighetsgrad: {renderErrorLevel(fileReport.feilkode)}
                </li>
                <li className="list-group-item">
                    Antall kontroller utført: {fileReport.antallKontroller}
                </li>
            </ul>
        </div>

        <div className="table-responsive mt-4">
            <table className="table table-striped table-sm">
                <thead>
                <tr>
                    <th scope="col">Feilkode</th>
                    <th scope="col">Journalnummer</th>
                    <th scope="col">Saksbehandler</th>
                    <th scope="col">Kontrolltype</th>
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
    </>
}

export default ReportView