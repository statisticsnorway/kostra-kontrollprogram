import MainForm from "../components/MainForm"
import {useQuery} from "react-query"
import {kontrollerSkjemaAsync, uiDataAsync} from "../api/apiCalls"
import KostraFormVm from "../kostratypes/kostraFormVm"
import {useState} from "react"
import FileReportVm from "../kostratypes/fileReportVm"
import {useNavigate} from "react-router-dom"

const Index = ({onAddFileReport}: {
    onAddFileReport: (fileReport: NonNullable<FileReportVm>) => number
}) => {

    const navigate = useNavigate()
    const [isPostError, setIsPostError] = useState<boolean>(false)

    // Fetch UI-data from backend
    const {
        isError: isUiDataLoadError,
        data: uiData
    } = useQuery("uiData", async () =>
        uiDataAsync().then(uiData => uiData)
    )

    // Form submit handler.
    // Submits form to backend and stores returned report at start of fileReports array state.
    const onSubmit = (form: KostraFormVm) =>
        kontrollerSkjemaAsync(form)
            .then(fileReport => {
                setIsPostError(false)
                const fileReportId = onAddFileReport(fileReport)
                navigate(`/file-reports/${fileReportId}`)
            })
            .catch((error) => {
                if (error.response) console.log(error.response)
                setIsPostError(true)
            })

    return !uiData ? <></> : <main>
        {isPostError && <span className="text-danger">Feil ved kontroll av fil</span>}
        {isUiDataLoadError && <span className="text-danger">Feil ved lasting av data</span>}

        {/** FORM */}
        <MainForm
            formTypes={uiData.formTypes}
            years={uiData.years}
            onSubmit={onSubmit}
        />
    </main>
}

export default Index