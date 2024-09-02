import './scss/buttons.scss'
import {createHashRouter, RouterProvider} from "react-router-dom"
import Root from "./routes/root"
import ErrorPage from "./error-page"
import Index from "./routes"
import FileReport from "./routes/file-report"
import {useCallback, useState} from "react"
import FileReportVm from "./kostratypes/fileReportVm"

const App = () => {
    const [fileReports, setFileReports] = useState<FileReportVm[]>([])

    const onAddFileReport = useCallback(
        (fileReport: FileReportVm) =>
            // add new report to head (index = 0)
            setFileReports(prevState => [fileReport, ...prevState]),
        []
    )

    const onDeleteReport = useCallback(
        (incomingIndex: NonNullable<number>): void =>
            // put all reports back to state except for the one that matches selected index
            setFileReports(prevState =>
                prevState.filter((_, index) => index !== incomingIndex)),
        []
    )

    const router = createHashRouter([{
        path: "/",
        element: <Root
            fileReports={fileReports}
            onDeleteFileReport={onDeleteReport}/>,
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