import FileReportEntryVm from "../../kostratypes/fileReportEntryVm";
import ErrorLevel from "./ErrorLevel";
import KostraErrorCode from "../../kostratypes/kostraErrorCode";

interface ErrorAggregateEntry {
    feilkode: KostraErrorCode
    kontrollnummer: string,
    itemCount: number
}

const reduceErrors = (reportEntries: FileReportEntryVm[]): ErrorAggregateEntry[] => reportEntries.reduce(
    (accumulator: ErrorAggregateEntry[], currentValue) => {
        const findIndex = accumulator.findIndex(it => it.kontrollnummer == currentValue.kontrollnummer)
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

const ErrorSummary = ({reportEntries}: {reportEntries: FileReportEntryVm[]}) =>
    <div className="card mt-3">
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