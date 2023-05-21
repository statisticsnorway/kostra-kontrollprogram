import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import {QueryClient, QueryClientProvider} from "react-query"

import "bootstrap/dist/css/bootstrap.min.css"


const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false
        }
    }
})

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <App/>
        </QueryClientProvider>
    </React.StrictMode>
)
