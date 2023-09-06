import {Outlet} from "react-router-dom";

// @ts-ignore
import IconKostra from "../assets/icon/ikon-kostra.svg";
import FileReportVm from "../kostratypes/fileReportVm";
import TabRow from "../features/navigationbar/TabRow";

const Root = ({fileReports, onDeleteFileReport}: {
    fileReports: FileReportVm[],
    onDeleteFileReport: (index: NonNullable<number>) => void
}) => {
    return (
        <>
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
            {fileReports.length > 0 && <TabRow
                fileReports={fileReports}
                activeTabIndex={0}
                onDeleteFileReport={onDeleteFileReport}/>}

            <main>
                <Outlet/>
            </main>
        </>
    )
}

export default Root