// @ts-ignore
import IconKostra from "./assets/icon/ikon-kostra.svg"

import './scss/buttons.scss'
import {createBrowserRouter, RouterProvider} from "react-router-dom"
import Root from "./routes/root"
import ErrorPage from "./error-page"
import Index from "./routes"
import FileReport from "./routes/file-report"
import {useState} from "react"
import FileReportVm from "./kostratypes/fileReportVm"

const App = () => {

    const [fileReports, setFileReports] = useState<FileReportVm[]>([])

    const onAddFileReport = (fileReport: FileReportVm) => {
        setFileReports(prevState => [fileReport, ...prevState])
        return 0
    }

    const deleteReport = (incomingIndex: NonNullable<number>): void => {
        // put all reports back to state except for the one that matches selected index
        setFileReports(prevState =>
            prevState.filter((_, index) => index != incomingIndex))
    }

    const router = createBrowserRouter([{
        path: "/",
        element: <Root
            fileReports={fileReports}
            onDeleteFileReport={deleteReport}/>,
        errorElement: <ErrorPage/>,
        children: [
            {index: true, element: <Index onAddFileReport={onAddFileReport}/>},
            {
                path: "file-reports/:reportId",
                element: <FileReport fileReports={fileReports}/>
            },
        ]
    }])

    return <RouterProvider router={router}/>
}

export default App