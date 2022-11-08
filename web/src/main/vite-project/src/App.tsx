import "./scss/check-out.scss"
import "./scss/form-validation.scss"
import {useEffect, useState} from "react"
import {listSkjemaTyperAsync} from "./api/apiCalls"
import {KostraFormTypeVm} from "./kostraTypes"

function App() {

    const [loadError, setLoadError] = useState<string>()
    const [regionsnummer, setRegionsnummer] = useState<string>()
    const [skjematyper, setSkjematyper] = useState<KostraFormTypeVm[]>([])

    useEffect(() => {
        listSkjemaTyperAsync()
            .then(skjematyper => {
                setSkjematyper(skjematyper)
                setLoadError("")
            })
            .catch(() => setLoadError("Lasting av skjematyper feilet"))
    }, [])

    return <main>
        {loadError && <span className="text-center text-danger">{loadError}</span>}

        <div className="py-5 text-center">
            <h2>Kontrollprogram</h2>
        </div>
        <div className="row g-5">

            <div className="row g-3">
                <div className="col-sm-6">
                    <label htmlFor="aargang" className="form-label">Årgang</label>
                    <select className="form-select" id="aargang" required>
                        <option value="2022">2022</option>
                        <option value="2022">2021</option>
                    </select>
                </div>

                <div className="col-sm-6">
                    <label htmlFor="regionsnummer" className="form-label">Regionsnummer</label>
                    <input type="text" className="form-control" id="regionsnummer" placeholder="6 siffer"
                           defaultValue=""
                           value={regionsnummer}
                           onChange={e => setRegionsnummer(e.currentTarget.value)}
                           required/>
                </div>

                <div className="col-12">
                    <label htmlFor="skjematype" className="form-label">Skjema</label>
                    <select className="form-select" id="skjematype" required>
                        {skjematyper.map(skjematype =>
                            <option key={skjematype.id}
                                    value={skjematype.id}>{skjematype.tittel}</option>)}
                    </select>
                </div>

                <div className="col-sm-6">
                    <label htmlFor="orgnrForetak" className="form-label">
                        Organisasjonsnummer for foretaket
                    </label>
                    <div className="input-group has-validation">
                        <input
                            type="text"
                            className="form-control"
                            id="orgnrForetak"
                            placeholder="9 siffer"
                            required/>
                    </div>
                </div>

                <div className="col-sm-6">
                    <label htmlFor="orgnrVirksomhet" className="form-label">
                        Organisasjonsnummer for virksomhetene
                    </label>
                    <div className="input-group has-validation">
                        <input
                            type="text"
                            className="form-control"
                            id="orgnrVirksomhet"
                            placeholder="9 siffer"
                            required/>
                    </div>
                </div>

                <div className="col-12 mt-4">
                    <div className="input-group has-validation">
                        <input type="file" id="formFile" className="form-control"/>
                    </div>
                </div>
            </div>

            <hr className="my-4"/>

            <button className="btn btn-primary btn-lg" type="submit">
                Kontroller fil
            </button>
        </div>
    </main>
}

export default App