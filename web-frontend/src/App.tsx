import './scss/buttons.scss'
import {createHashRouter, RouteObject, RouterProvider} from "react-router-dom"
import Root from "./routes/root"
import ErrorPage from "./routes/error-page"
import FileReport from "./routes/file-report"
import Index from "./routes"
import { useCallback, useMemo, useState } from "react"
import FileReportVm from "./kostratypes/fileReportVm"
import { useQuery } from "@tanstack/react-query"
import { uiDataAsync } from "./api/apiCalls"

const App = () => {
    const [fileReports, setFileReports] = useState<FileReportVm[]>([])

    // Fetch UI-data from backend
    const { data: uiData, isLoading, isError } = useQuery({
        queryKey: ['uiData'],
        queryFn: uiDataAsync,
    })

    const onAddFileReport = useCallback(
        (fileReport: FileReportVm) =>
            setFileReports(prevState => [fileReport, ...prevState]),
        []
    )

    const onDeleteReport = useCallback(
        (incomingIndex: number): void =>
            setFileReports(prevState =>
                prevState.filter((_, index) => index !== incomingIndex)),
        []
    )

    // Memoize routing configuration to avoid unnecessary recalculation
    const router = useMemo(() =>
        uiData ? createHashRouter([
            {
                path: '/',
                errorElement: <ErrorPage/>,
                element: <Root fileReports={fileReports}
                               onDeleteFileReport={onDeleteReport}/>,
                children: [
                    {
                        index: true,
                        element: <Index uiData={uiData} onAddFileReport={onAddFileReport}/>,
                    },
                    {
                        path: 'file-reports/:reportId',
                        element: <FileReport
                            fileReports={fileReports}
                            releaseVersion={uiData.releaseVersion}
                        />,
                    },
                ],
            },
        ] as RouteObject[]) : null, [fileReports, uiData, onDeleteReport, onAddFileReport])

    if (isLoading) {
        return <div>Laster data...</div>
    }

    if (isError || !uiData || !router) {
        return <div className="text-danger">Kunne ikke initialisere applikasjonen.</div>
    }

    return <RouterProvider router={router} />
}

export default App
