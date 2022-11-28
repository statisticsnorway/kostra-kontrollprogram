import {FileReportVm} from "../kostratypes/fileReportVm";
import {KostraErrorCode} from "../kostratypes/kostraErrorCode";
import {FileReportEntryVm} from "../kostratypes/fileReportEntryVm";


export const ReportView = (props: {
    readonly fileReport: NonNullable<FileReportVm>
}) => {
    const {fileReport} = props

    // gets text value from enum KostraErrorCode
    const getEnumText = (untypedString: string): string =>
        KostraErrorCode[(untypedString as keyof typeof KostraErrorCode)]

    const getEnumValue = (untypedString: string): KostraErrorCode =>
        KostraErrorCode[untypedString as keyof typeof KostraErrorCode]

    const reduceErrors = (reportEntries: FileReportEntryVm[]): {
        feilkode: KostraErrorCode,
        kontrollnummer: string,
        itemCount: number
    }[] => reportEntries.reduce(
        (accumulator: {
            feilkode: KostraErrorCode
            kontrollnummer: string,
            itemCount: number
        }[], currentValue) => {
            const findIndex = accumulator.findIndex(it => it.kontrollnummer === currentValue.kontrollnummer)
            if (findIndex < 0) {
                accumulator.push({
                    feilkode: currentValue.feilkode,
                    kontrollnummer: currentValue.kontrollnummer,
                    itemCount: 1
                })
            } else {
                accumulator[findIndex].itemCount++
            }
            return accumulator
        }, []).sort((a, b) => b.itemCount - a.itemCount)

    const renderErrorLevel = (untypedErrorCode: string) => {
        let cssStyle

        switch (getEnumValue(untypedErrorCode)) {
            case KostraErrorCode.NO_ERROR: {
                cssStyle = "text-success"
                break;
            }
            case KostraErrorCode.NORMAL_ERROR: {
                cssStyle = "text-warning"
                break;
            }
            default: {
                cssStyle = "text-danger"
                break;
            }
        }
        return <span className={cssStyle}>{getEnumText(untypedErrorCode)}</span>
    }

    const renderErrorSummary = (reportEntries: FileReportEntryVm[]) => <div className="card mt-3">
        <div className="card-body">
            <h5 className="card-title mb-0">Oversikt feilkoder og antall</h5>
        </div>
        <ul className="list-group list-group-flush">
            <li className="list-group-item">

                <table className="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th scope="col">Feilkode</th>
                        <th scope="col">Antall</th>
                        <th scope="col">Kontrolltype</th>
                    </tr>
                    </thead>
                    <tbody>
                    {reduceErrors(reportEntries).map((it, index) =>
                        <tr key={index}>
                            <th scope="row">{renderErrorLevel(it.feilkode)}</th>
                            <td>{it.itemCount}</td>
                            <td>{it.kontrollnummer}</td>
                        </tr>)}
                    </tbody>
                </table>
            </li>
        </ul>
    </div>

    const renderReportSummary = () => <div className="card mt-3">
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

    const renderErrorDetailsTable = () => <div className="table-responsive mt-4">
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
            {fileReport.feil.map((reportEntry, index) =>
                <tr key={index}>
                    <th scope="row">{renderErrorLevel(reportEntry.feilkode)}</th>
                    <td>{reportEntry.journalnummer}</td>
                    <td>{reportEntry.saksbehandler}</td>
                    <td>{reportEntry.kontrollnummer}</td>
                    <td>{reportEntry.kontrolltekst}</td>
                </tr>)}
            </tbody>
        </table>
    </div>

    return <>
        {renderReportSummary()}
        {renderErrorSummary(fileReport.feil)}
        {renderErrorDetailsTable()}
    </>
}

export default ReportView