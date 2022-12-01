import {useEffect, useState} from "react"

// app components
import MainForm from "./components/MainForm"
import ReportView from "./components/ReportView"
import {ImageNameButton} from "./components/ImageNameButton"
import {CloseButton} from "./components/CloseButton"

// app types
import {KostraFormVm} from "./kostratypes/kostraFormVm"
import {FileReportVm} from "./kostratypes/fileReportVm"
import {UiDataVm} from "./kostratypes/uiDataVm"

// API calls
import {kontrollerSkjemaAsync, uiDataAsync} from "./api/apiCalls"

// icons
// @ts-ignore
import ArrowLeftCircle from "./assets/icon/arrow-left-circle.svg"
// @ts-ignore
import FilterLeft from "./assets/icon/filter-left.svg"
// @ts-ignore
import IconKostra from "./assets/icon/ikon-kostra.svg"
// @ts-ignore
import ListTask from "./assets/icon/list-task.svg"

import './scss/buttons.scss'

const App = () => {

    const [loadError, setLoadError] = useState<string>()
    const [uiData, setUiData] = useState<UiDataVm>()

    const [fileReports, setFileReports] = useState<FileReportVm[]>([])
    const [activeTabIndex, setActiveTabIndex] = useState<number>(0)

    const deleteReport = (incomingIndex: NonNullable<number>): void => {
        // there's always a tab to the left, go there
        setActiveTabIndex(incomingIndex)

        // put all reports back to state except for the one that matches selected index
        setFileReports(prevState =>
            prevState.filter((_, index) => index != incomingIndex))
    }

    // get ui data
    useEffect(() => {
        uiDataAsync()
            .then(uiData => {
                setUiData(uiData)
                setLoadError("")
            })
            .catch(() => setLoadError("Lasting av UI-data feilet"))
    }, [])

    // Form submit handler.
    // Submits form to backend and stores returned report
    // at start of fileReports array state.
    const onSubmit = (form: KostraFormVm) => {
        kontrollerSkjemaAsync(form)
            .then(fileReport => {
                setFileReports(prevState => [fileReport, ...prevState])
                setActiveTabIndex(1)
                setLoadError("")
            })
            .catch((error) => {
                if (error.response) {
                    console.log(error.response)
                }
                setLoadError("Feil ved kontroll av fil")
            })
    }

    const mainElementInDisplay = !uiData ? <></>
        : (activeTabIndex == 0 ?
            <MainForm
                uiData={uiData}
                onSubmit={onSubmit}/>
            : <ReportView
                fileReport={fileReports[activeTabIndex - 1]}
                appReleaseVersion={uiData.releaseVersion}/>)

    return <>
        <header className="py-3 text-center">
            <h2>
                <img src={IconKostra}
                     height="70px"
                     className="pe-4"
                     alt="Kostra"/>
                Kostra Kontrollprogram
            </h2>

            {loadError && <span className="text-danger">{loadError}</span>}

            <hr className="my-0"/>
        </header>

        { /** TABS */}
        {fileReports.length > 0 && <ul className="nav nav-tabs" role={"navigation"}>
            {/** BACK TO FORM */}
            <li className="nav-item">
                <div className={activeTabIndex == 0 ? "nav-link active pt-1 pb-1" : "nav-link pt-1 pb-1"}>
                    <ImageNameButton
                        onClick={() => setActiveTabIndex(0)}
                        text={activeTabIndex == 0 ? "Skjema" : "Tilbake til skjema"}/>
                </div>
            </li>

            {/** REPORT TABS */}
            {fileReports.map((fileReport, index) =>
                <li key={index} className="nav-item">
                    <div className={activeTabIndex == index + 1 ? "nav-link active pt-1 pb-1" : "nav-link pt-1 pb-1"}>
                        <ImageNameButton
                            onClick={() => setActiveTabIndex(index + 1)}
                            text={`${fileReport.innparametere.skjema} ${fileReport.innparametere.aar},`
                                + ` region ${fileReport.innparametere.region}`}
                        />
                        <CloseButton onClick={() => deleteReport(index)}/>
                    </div>
                </li>
            )}
        </ul>}

        {/** show when UI-data is loaded */}
        <main>
            { /** FORM or FILE REPORT */}
            {mainElementInDisplay}
        </main>
    </>
}

export default App