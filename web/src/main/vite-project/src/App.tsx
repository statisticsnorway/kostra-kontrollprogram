import "./scss/check-out.scss"
import {ChangeEvent, useEffect, useState} from "react"
import {listSkjemaTyperAsync} from "./api/apiCalls"
import {KostraFormTypeVm, LocalKostraFormVm, Nullable} from "./kostraTypes"
import {useForm} from "react-hook-form"

function App() {

    const [loadError, setLoadError] = useState<string>()
    const [skjematyper, setSkjematyper] = useState<KostraFormTypeVm[]>([])
    const [valgtSkjematype, setValgtSkjematype] = useState<Nullable<KostraFormTypeVm>>()
    const [datafil, setDatafil] = useState<Nullable<string>>()

    const {register, getValues, setValue, handleSubmit, formState: {errors}} = useForm<LocalKostraFormVm>()
    const onSubmit = handleSubmit(data => console.log(data));

    useEffect(() => {
        listSkjemaTyperAsync()
            .then(skjematyper => {
                setSkjematyper(skjematyper)
                setLoadError("")
            })
            .catch(() => setLoadError("Lasting av skjematyper feilet"))
    }, [])

    const handleFormTypeChanged = (event: ChangeEvent<HTMLSelectElement>) =>
        setValgtSkjematype(skjematyper.find(it => it.id == event.target.value))

    const handleFileUpload = (event: ChangeEvent<HTMLInputElement>) => {
        const {files} = event.target
        if (files == null || files.length === 0) {
            return
        }
        setDatafil(null)

        const file = files[0]
        let reader = new FileReader()

        reader.onloadend = () => {
            setDatafil(reader.result as string)
            event.target.value = ""
        }
        reader.readAsDataURL(file)
    }

    return <form onSubmit={onSubmit}>
        <button
            type="button"
            onClick={() => {
                setValue("aar", 2022)
                setValue("region", "030100")
                setValue("skjema", "0X")
                setValue("orgnrForetak", "999999999")

                setValgtSkjematype(skjematyper.find(it => it.id == getValues("skjema")))
            }}
        >Sett testverdier 0X
        </button>
        <button
            type="button"
            onClick={() => {
                const values = getValues()
                console.log(values)
            }}
        >Get Values
        </button>

        <div className="py-5 text-center">
            <h2>Kostra kontrollprogram</h2>
            {loadError && <span className="text-center text-danger">{loadError}</span>}
        </div>

        { /** ÅR */}
        <div className="row g-4">
            <div className="col-sm-6">
                <label htmlFor="aar" className="form-label">Årgang</label>
                <select {...register("aar", {required: true})}
                        id="aar"
                        className="form-select">
                    <option value="">Velg år</option>
                    <option value="2022">2022</option>
                    <option value="2021">2021</option>
                </select>
                {errors.aar?.type === 'required' && <div className="text-danger">Årgang er påkrevet</div>}
            </div>

            { /** REGION */}
            <div className="col-sm-6">
                <label htmlFor="region" className="form-label">Regionsnummer</label>
                <input {...register("region", {required: true, pattern: /^\d{6}$/i})}
                       id="region"
                       type="number"
                       className="form-control"
                       placeholder="6 siffer"
                       aria-invalid={errors.region ? "true" : "false"}
                />
                {errors.region?.type === 'required' && <div className="text-danger">Region er påkrevet</div>}
                {errors.region?.type === 'pattern' && <div className="text-danger">Region må bestå av 6 siffer</div>}
            </div>

            { /** SKJEMATYPE */}
            <div className="col-12">
                <label htmlFor="skjema" className="form-label">Skjema</label>
                <select {...register("skjema", {required: true})}
                        onChange={handleFormTypeChanged}
                        id="skjema"
                        className="form-select">
                    <option value="">Velg skjematype</option>
                    {skjematyper.map(skjematype =>
                        <option key={skjematype.id}
                                value={skjematype.id}>{skjematype.tittel}</option>)}
                </select>
                {errors.skjema?.type === 'required' && <div className="text-danger">Skjematype er påkrevet</div>}
            </div>

            { /** ORGNR */}
            {valgtSkjematype?.labelOrgnr &&
                <div className="col-sm-6">
                    <label htmlFor="orgnrForetak" className="form-label">{valgtSkjematype.labelOrgnr}</label>
                    <div className="input-group has-validation">
                        <input {...register("orgnrForetak", {required: true, pattern: /^[8|9]\d{8}$/i})}
                               type="number"
                               className="form-control"
                               placeholder="9 siffer"
                               id="orgnrForetak"/>
                    </div>
                    {errors.orgnrForetak?.type === 'required' &&
                        <div className="text-danger">Organisasjonsnummer er påkrevet</div>}
                    {errors.orgnrForetak?.type === 'pattern' &&
                        <div className="text-danger">Må starte med 8 eller 9, etterfulgt av 8 siffer</div>}
                </div>
            }

            { /** ORGNR 2 */}
            {valgtSkjematype?.labelOrgnrVirksomhetene &&
                <div className="col-sm-6">
                    <label htmlFor="orgnrVirksomhet"
                           className="form-label">{valgtSkjematype.labelOrgnrVirksomhetene}</label>
                    <div className="input-group has-validation">
                        <input
                            type="number"
                            className="form-control"
                            placeholder="9 siffer"
                            id="orgnrVirksomhet"/>
                    </div>
                </div>
            }

            { /** FILE UPLOAD */}
            <div className="col-sm-12 mt-4">
                <input onChange={handleFileUpload}
                       type="file"
                       className="form-control"
                       id="filnavn"/>
                {!datafil && <div className="text-danger">Vennligst velg fil</div>}
                {datafil && <div className="text-success">Fil lastet opp</div>}
            </div>

            <hr className="my-4"/>

            <button className="btn btn-primary btn-lg" type="submit">
                Kontroller fil
            </button>
        </div>
    </form>
}

export default App