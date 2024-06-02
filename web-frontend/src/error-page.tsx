import {useRouteError} from "react-router-dom";

const ErrorPage = () => {
    const error = useRouteError()
    console.error(error)

    return (
        <div id="error-page">
            <h1>Oops!</h1>
            <p>Beklager, en uventet feil har oppstått.</p>
        </div>
    )
}

export default ErrorPage