import ErrorLevel from "./ErrorLevel";
import FileReportEntryVm from "../../kostratypes/fileReportEntryVm";

const ErrorDetailsTable = ({reportEntries}: { reportEntries: NonNullable<FileReportEntryVm[]> }) =>
    <div className="card mt-3 mb-4">
        <div className="card-body">
            <h5 className="card-title mb-0">Rapport</h5>
        </div>
        <ul className="list-group list-group-flush">
            <li className="list-group-item">
                <div className="table-responsive">
                    <table className="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">Grad</th>
                            <th scope="col">Journalnummer</th>
                            <th scope="col">Saksbehandler</th>
                            <th scope="col">Kontroll</th>
                            <th scope="col">Melding</th>
                        </tr>
                        </thead>
                        <tbody data-testid="error-details-table-tbody">
                        {reportEntries.map((reportEntry, index) =>
                            <tr key={index}>
                                <th scope="row"><ErrorLevel level={reportEntry.severity}/></th>
                                <td>{reportEntry.journalId}</td>
                                <td>{reportEntry.caseworker}</td>
                                <td>{reportEntry.ruleName}</td>
                                <td>{reportEntry.messageText}</td>
                            </tr>)}
                        </tbody>
                    </table>
                </div>
            </li>
        </ul>
    </div>

export default ErrorDetailsTable