import React, {useState} from "react"
import {Button} from "react-bootstrap";

// app components
import MainForm from "./components/MainForm";
import ReportView from "./components/ReportView";

// app types
import {KostraFormVm} from "./kostratypes/kostraFormVm";
import {FileReportVm} from "./kostratypes/fileReportVm";

// API calls
import {kontrollerSkjemaAsync} from "./api/apiCalls";

// icons
// @ts-ignore
import ArrowLeftCircle from "./assets/icon/arrow-left-circle.svg"
// @ts-ignore
import FilterLeft from "./assets/icon/filter-left.svg"
// @ts-ignore
import IconKostra from "./assets/icon/ikon-kostra.svg";
// @ts-ignore
import ListTask from "./assets/icon/list-task.svg";
// @ts-ignore
import IconX from "./assets/icon/x.svg";

import './scss/buttons.scss'

const App = () => {

    const [loadError, setLoadError] = useState<string>()
    const [fileReports, setFileReports] = useState<FileReportVm[]>([])
    const [activeTabIndex, setActiveTabIndex] = useState<number>(0)

    const deleteReport = (incomingIndex: NonNullable<number>): void => {
        // there's always a tab to the left, go there
        setActiveTabIndex(incomingIndex)

        // put all reports back to state except for the one that matches selected index
        setFileReports(prevState =>
            prevState.filter((it, index) => index != incomingIndex))
    }

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

    return <>
        <div className="py-3 text-center">
            <h2 className="mb-3">
                <img src={IconKostra}
                     height="70px"
                     className="pe-4"
                     alt="Kostra"/>
                Kostra kontrollprogram
            </h2>

            {loadError && <span className="text-danger">{loadError}</span>}

            <hr className="my-0"/>
        </div>

        { /** TABS */}
        {fileReports.length > 0 && <ul className="nav nav-tabs">
            {/** BACK TO FORM */}
            <li className="nav-item">
                <Button className={activeTabIndex == 0 ? "nav-link active" : "nav-link"}
                        onClick={() => setActiveTabIndex(0)}
                        title={activeTabIndex == 0 ? "Skjema" : "Tilbake til skjema"}>
                    <img src={FilterLeft}
                         className="pe-2"
                         alt="Kostra"/>
                    {activeTabIndex == 0 ? "Skjema" : "Tilbake til skjema"}
                </Button>
            </li>

            {/** REPORT TABS */}
            {fileReports.map((fileReport, index) =>
                <li key={index} className="nav-item">
                    <div className={activeTabIndex == index + 1 ? "nav-link active pt-1 pb-1" : "nav-link pt-1 pb-1"}>
                        <Button
                            onClick={() => setActiveTabIndex(index + 1)}
                            className="btn bg-transparent btn-outline-light p-0"
                            title={`Vis rapport for ${fileReport.innparametere.skjema} ${fileReport.innparametere.aar},`
                                + ` region ${fileReport.innparametere.region}`}>

                            <img src={ListTask} className="pe-2" alt="Kostra"/>

                            <span className="text-black-50">
                                {`${fileReport.innparametere.skjema} ${fileReport.innparametere.aar},`
                                    + ` region ${fileReport.innparametere.region}`}
                            </span>
                        </Button>
                        <Button
                            onClick={() => deleteReport(index)}
                            className="bg-transparent btn-outline-light ms-1 p-0 ps-1 pe-1 rounded-circle"
                            title="Slett rapport">
                            <img src={IconX} alt="Slett rapport"/>
                        </Button>
                    </div>
                </li>
            )}
        </ul>}

        { /** FORM */}
        <MainForm
            showForm={activeTabIndex == 0}
            onSubmit={onSubmit}
            onLoadError={message => setLoadError(message)}/>

        { /** FILE REPORT */}
        {activeTabIndex > 0 &&
            <ReportView
                fileReport={fileReports[activeTabIndex - 1]}
            />}
    </>
}

export default App