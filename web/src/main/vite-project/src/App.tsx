import React, {useState} from "react"

import MainForm from "./components/MainForm";
import ReportView from "./components/ReportView";

import {KostraFormVm} from "./kostratypes/kostraFormVm";
import {FileReportVm} from "./kostratypes/fileReportVm";

import {kontrollerSkjemaAsync} from "./api/apiCalls";

// @ts-ignore
import IconKostra from "./assets/icon/ikon-kostra.svg";
import {Button} from "react-bootstrap";

const App = () => {

    const [loadError, setLoadError] = useState<string>()
    const [fileReports, setFileReports] = useState<FileReportVm[]>([])
    const [activeTabIndex, setActiveTabIndex] = useState<number>(0)

    const onSubmit = (form: KostraFormVm) => {
        kontrollerSkjemaAsync(form)
            .then(fileReport => {
                console.log(fileReport)
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
            <li className="nav-item">
                <Button className={activeTabIndex == 0 ? "nav-link active" : "nav-link"}
                        onClick={() => setActiveTabIndex(0)}>Skjema</Button>
            </li>
            {fileReports.map((fileReport, index) =>
                <li key={index} className="nav-item">
                    <Button className={activeTabIndex == index + 1 ? "nav-link active" : "nav-link"}
                            onClick={() => setActiveTabIndex(index + 1)}>{fileReport.innparametere.skjema}</Button>
                </li>
            )}
        </ul>}

        { /** FORM */}
        {activeTabIndex == 0 && <MainForm
            onSubmit={onSubmit}
            onLoadError={message => setLoadError(message)}
        />}

        { /** REPORTS */}
        {activeTabIndex > 0 && <ReportView fileReport={fileReports[activeTabIndex - 1]}/>}
    </>
}

export default App