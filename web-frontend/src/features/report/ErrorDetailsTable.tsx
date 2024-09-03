import ErrorLevel from "./ErrorLevel"
import FileReportEntryVm from "../../kostratypes/fileReportEntryVm"

export const SEVERITY_HEADER = "Grad"
export const CASE_WORKER_HEADER = "Saksbehandler"
export const JOURNAL_ID_HEADER = "Journalnummer"
export const INDIVID_ID_HEADER = "Individ-ID"
export const CONTEXT_ID_HEADER = "Kontekst-ID"
export const RULE_ID_HEADER = "Kontroll"
export const ERROR_MESSAGE_HEADER = "Melding"
export const LINES_HEADER = "Linje"

const ErrorDetailsTable = ({reportEntries}: { reportEntries: NonNullable<FileReportEntryVm[]> }) => {

    const headers = [SEVERITY_HEADER]
    if (reportEntries.filter(it => it.caseworker).length > 0) headers.push(CASE_WORKER_HEADER)
    if (reportEntries.filter(it => it.journalId).length > 0) headers.push(JOURNAL_ID_HEADER)
    if (reportEntries.filter(it => it.individId).length > 0) headers.push(INDIVID_ID_HEADER)
    if (reportEntries.filter(it => it.contextId).length > 0) headers.push(CONTEXT_ID_HEADER)
    headers.push(RULE_ID_HEADER)
    headers.push(ERROR_MESSAGE_HEADER)
    if (reportEntries.filter(it => it.lineNumbers).length > 0) headers.push(LINES_HEADER)

    return <div className="card mt-3 mb-4">
        <div className="card-body">
            <h5 className="card-title mb-0">Rapport</h5>
        </div>
        <ul className="list-group list-group-flush">
            <li className="list-group-item">
                <div className="table-responsive">
                    <table className="table table-striped table-sm">
                        <thead>
                        <tr>
                            {headers.map((header, index) => <th key={index} scope="col">{header}</th>)}
                        </tr>
                        </thead>
                        <tbody data-testid="error-details-table-tbody">
                        {reportEntries.map((reportEntry, index) =>
                            <tr key={index}>
                                <th scope="row"><ErrorLevel level={reportEntry.severity}/></th>
                                {headers.includes(CASE_WORKER_HEADER) ? <td>{reportEntry.caseworker}</td> : ""}
                                {headers.includes(JOURNAL_ID_HEADER) ? <td>{reportEntry.journalId}</td> : ""}
                                {headers.includes(INDIVID_ID_HEADER) ? <td>{reportEntry.individId}</td> : ""}
                                {headers.includes(CONTEXT_ID_HEADER) ? <td>{reportEntry.contextId}</td> : ""}
                                <td>{reportEntry.ruleName}</td>
                                <td>{reportEntry.messageText}</td>
                                {headers.includes(LINES_HEADER) ? <td>{reportEntry.lineNumbers?.join(", ")}</td> : ""}
                            </tr>)}
                        </tbody>
                    </table>
                </div>
            </li>
        </ul>
    </div>
}

export default ErrorDetailsTable