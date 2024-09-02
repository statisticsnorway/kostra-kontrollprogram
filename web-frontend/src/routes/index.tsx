import MainForm from "../components/MainForm"
import {useQuery} from "react-query"
import {kontrollerSkjemaAsync, uiDataAsync} from "../api/apiCalls"
import KostraFormVm from "../kostratypes/kostraFormVm"
import {useCallback, useState} from "react"
import FileReportVm from "../kostratypes/fileReportVm"
import {useNavigate} from "react-router-dom";

const Index = ({onAddFileReport}: {
    onAddFileReport: (fileReport: NonNullable<FileReportVm>) => void
}) => {
    const [isPostError, setIsPostError] = useState<boolean>(false)
    const navigate = useNavigate()

    // Fetch UI-data from backend
    const {
        isError: isUiDataLoadError,
        data: uiData
    } = useQuery("uiData", async () =>
        uiDataAsync().then(uiData => uiData)
    )

    // Submits form to backend and stores returned report at start of fileReports array state.
    const onSubmit = useCallback((form: KostraFormVm) =>
        kontrollerSkjemaAsync(form)
            .then(fileReport => {
                setIsPostError(false)
                onAddFileReport(fileReport)
                navigate("/file-reports/0") // new reports are added to beginning of list
            })
            .catch((error) => {
                if (error.response) console.log(error.response)
                setIsPostError(true)
            }), [onAddFileReport, navigate])

    return !uiData ? <></> : <>
        {isPostError && <span className="text-danger">Feil ved kontroll av fil</span>}
        {isUiDataLoadError && <span className="text-danger">Feil ved lasting av data</span>}

        {/** FORM */}
        <MainForm
            formTypes={uiData.formTypes}
            years={uiData.years}
            onSubmit={onSubmit}
        />
    </>
}

export default Index