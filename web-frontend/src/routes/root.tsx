import {Outlet} from "react-router-dom"

// @ts-ignore
import IconKostra from "../assets/icon/ikon-kostra.svg"
import FileReportVm from "../kostratypes/fileReportVm"
import TabRow from "../features/navigationbar/TabRow"

const Root = ({fileReports, onDeleteFileReport}: {
    fileReports: FileReportVm[],
    onDeleteFileReport: (index: NonNullable<number>) => void
}) => <>
    <header className="py-3 text-center">
        <h2>
            <img src={IconKostra}
                 height="70px"
                 className="pe-4"
                 alt="Kostra"/>
            Kostra Kontrollprogram
        </h2>

        <hr className="my-0"/>
    </header>

    {/** TABS */}
    <TabRow
        fileReports={fileReports}
        onDeleteFileReport={onDeleteFileReport}/>

    <main>
        <Outlet/>
    </main>
</>

export default Root