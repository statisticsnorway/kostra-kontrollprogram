import {Link, useRouteError} from "react-router-dom"

const ErrorPage = () => {
    const error = useRouteError()
    console.error('Error details:', error)

    // Extract message and status text from the error
    const errorMessage = (error as Error)?.message ||
        (error as { statusText?: string })?.statusText ||
        "Beklager, det oppstod en ukjent feil."

    return (
        <div id="error-page" className="container align-items-center">
            <div className="container">
                <h1>Oops!</h1>
                <p>{errorMessage}</p>
                <div>
                    <p>Vennligst prøv igjen eller gå tilbake til skjema.</p>
                    <Link to="/" className="btn btn-primary">Tilbake til skjema</Link>
                </div>
            </div>
        </div>
    )
}

export default ErrorPage