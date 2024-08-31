import FileReportEntryVm from "../../kostratypes/fileReportEntryVm"
import ErrorLevel from "./ErrorLevel"
import KostraSeverity from "../../kostratypes/kostraSeverity"

interface ErrorAggregateEntry {
    feilkode: KostraSeverity
    kontrollnummer: string,
    itemCount: number
}

const reduceErrors = (reportEntries: NonNullable<FileReportEntryVm[]>): ErrorAggregateEntry[] => reportEntries.reduce(
    (accumulator: ErrorAggregateEntry[], currentValue) => {
        const findIndex = accumulator.findIndex(it => it.kontrollnummer == currentValue.ruleName)
        if (findIndex < 0) {
            accumulator.push({
                feilkode: currentValue.severity,
                kontrollnummer: currentValue.ruleName,
                itemCount: 1
            })
        } else {
            accumulator[findIndex].itemCount++
        }
        return accumulator
    }, []).sort((a, b) => b.itemCount - a.itemCount)

const ErrorSummary = ({reportEntries}: {reportEntries: NonNullable<FileReportEntryVm[]>}) =>
    <div className="card mt-3">
        <div className="card-body">
            <h5 className="card-title mb-0">Oppsummering</h5>
        </div>
        <ul className="list-group list-group-flush">
            <li className="list-group-item">
                <table className="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th scope="col">Grad</th>
                        <th scope="col">Antall</th>
                        <th scope="col">Kontroll</th>
                    </tr>
                    </thead>
                    <tbody data-testid="error-summary-table-tbody">
                    {reduceErrors(reportEntries).map((it, index) =>
                        <tr key={index}>
                            <th scope="row"><ErrorLevel level={it.feilkode}/></th>
                            <td>{it.itemCount}</td>
                            <td>{it.kontrollnummer}</td>
                        </tr>)}
                    </tbody>
                </table>
            </li>
        </ul>
    </div>

export default ErrorSummary