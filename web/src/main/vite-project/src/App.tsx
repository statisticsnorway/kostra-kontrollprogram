import {useQuery} from 'react-query'
import {useState} from "react";

// app components
import MainForm from "./components/MainForm"
import ReportView from "./components/ReportView"
import TabRow from "./features/navigationbar/TabRow";

// app types
import KostraFormVm from "./kostratypes/kostraFormVm"
import FileReportVm from "./kostratypes/fileReportVm"

// API calls
import {kontrollerSkjemaAsync, uiDataAsync} from "./api/apiCalls"

// @ts-ignore
import IconKostra from "./assets/icon/ikon-kostra.svg"

import './scss/buttons.scss'

const App = () => {

    const [isPostError, setIsPostError] = useState<boolean>(false)
    const [fileReports, setFileReports] = useState<FileReportVm[]>([])
    const [activeTabIndex, setActiveTabIndex] = useState<number>(0)

    // Fetch UI-data from backend
    const {
        isError: isUiDataLoadError,
        data: uiData
    } = useQuery("uiData", () => {
            console.log("Fetching UI-data")
            return uiDataAsync().then(uiData => uiData)
        }
    )

    // Form submit handler.
    // Submits form to backend and stores returned report at start of fileReports array state.
    const onSubmit = (form: KostraFormVm) => {
        kontrollerSkjemaAsync(form)
            .then(fileReport => {
                setFileReports(prevState => [fileReport, ...prevState])
                setActiveTabIndex(1)
                setIsPostError(false)
            })
            .catch((error) => {
                if (error.response) console.log(error.response)
                setIsPostError(true)
            })
    }

    const deleteReport = (incomingIndex: NonNullable<number>): void => {
        console.log(`Deleting report at index ${incomingIndex}`)

        // there's always a tab to the left, go there
        setActiveTabIndex(incomingIndex)

        // put all reports back to state except for the one that matches selected index
        setFileReports(prevState =>
            prevState.filter((_, index) => index != incomingIndex))
    }

    return <>
        <header className="py-3 text-center">
            <h2>
                <img src={IconKostra}
                     height="70px"
                     className="pe-4"
                     alt="Kostra"/>
                Kostra Kontrollprogram
            </h2>

            {isUiDataLoadError && <span className="text-danger">Feil ved lasting av UI-data</span>}
            {isPostError && <span className="text-danger">"Feil ved kontroll av fil"</span>}

            <hr className="my-0"/>
        </header>

        {/** TABS */}
        {fileReports.length > 0 && <TabRow
            fileReports={fileReports}
            activeTabIndex={activeTabIndex}
            onTabSelect={setActiveTabIndex}
            onReportDelete={deleteReport}/>}

        {/** show when UI-data is loaded */}
        {uiData && <main>

            {/** FORM */}
            <MainForm
                showForm={activeTabIndex == 0}
                formTypes={uiData.formTypes}
                years={uiData.years}
                onSubmit={onSubmit}
            />

            {/** FILE REPORT */}
            {activeTabIndex > 0 &&
                <ReportView
                    fileReport={fileReports[activeTabIndex - 1]}
                    appReleaseVersion={uiData.releaseVersion}
                />
            }
        </main>}
    </>
}

export default App