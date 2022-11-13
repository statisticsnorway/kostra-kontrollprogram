import {ChangeEvent, useEffect, useState} from "react"
import {listSkjemaTyperAsync} from "./api/apiCalls"
import {useForm} from "react-hook-form"
import {KostraFormVm} from "./kostratypes/kostraFormVm";
import {KostraFormTypeVm} from "./kostratypes/kostraFormTypeVm";
import {Nullable} from "./kostratypes/nullable";
import {Button, Form} from "react-bootstrap";

function App() {

    const [loadError, setLoadError] = useState<string>()
    const [skjematyper, setSkjematyper] = useState<KostraFormTypeVm[]>([])
    const [valgtSkjematype, setValgtSkjematype] = useState<Nullable<KostraFormTypeVm>>()
    const [datafil, setDatafil] = useState<Nullable<string>>(null)

    const {
        register,
        getValues,
        setValue,
        handleSubmit,
        formState: {errors, touchedFields, isDirty},
        formState,
        watch
    } = useForm<KostraFormVm>({mode: "onBlur"})
    const onSubmit = handleSubmit(data => console.log(touchedFields));

    useEffect(() => {
        listSkjemaTyperAsync()
            .then(skjematyper => {
                setSkjematyper(skjematyper)
                setLoadError("")
            })
            .catch(() => setLoadError("Lasting av skjematyper feilet"))
    }, [])

    useEffect(() => {
        if (skjematyper.length) {
            const subscription = watch((value, {name, type}) => {
                if (name === 'skjema' && type === 'change') {
                    setValgtSkjematype(skjematyper.find(it => it.id === value.skjema))
                }
            });
            return () => subscription.unsubscribe()
        }
    }, [skjematyper, watch])

    const handleFileUpload = (event: ChangeEvent<HTMLInputElement>) => {
        const {files} = event.target
        if (files == null || files.length === 0) {
            return
        }
        setDatafil(null)

        let reader = new FileReader()
        reader.onloadend = () => {
            setDatafil(reader.result as string)
            event.target.value = ""
        }
        reader.readAsDataURL(files[0])
    }

    return <Form noValidate validated={formState.isValid} onSubmit={onSubmit}>
        <Button
            className="btn-secondary m-2"
            onClick={() => {
                setValue("aar", 2022)
                setValue("region", "030100")
                setValue("skjema", "0X")
                setValue("orgnrForetak", "999999999")

                setDatafil(null)
                setValgtSkjematype(skjematyper.find(it => it.id == "0X"))
            }}
        >Sett testverdier 0X</Button>

        <Button
            className="btn-secondary m-2"
            onClick={() => {
                const values = getValues()
                console.log(values)
            }}>Get Values</Button>

        <div className="py-5 text-center">
            <h2>Kostra kontrollprogram</h2>
            {loadError && <span className="text-center text-danger">{loadError}</span>}
        </div>

        <div className="row g-4">
            { /** ÅRGANG */}
            <Form.Group
                className="col-sm-6"
                controlId="aar">
                <Form.Label>Årgang</Form.Label>
                <Form.Select
                    {...register("aar", {required: true})}
                    isValid={touchedFields.aar && !errors.aar}
                    isInvalid={errors.aar != null}>
                    <option value="">Velg år</option>
                    <option value="2022">2022</option>
                    <option value="2021">2021</option>
                </Form.Select>
                <Form.Control.Feedback type="invalid">Årgang er påkrevet</Form.Control.Feedback>
            </Form.Group>

            { /** REGION */}
            <Form.Group
                className="col-sm-6"
                controlId="region">
                <Form.Label>Regionsnummer</Form.Label>
                <Form.Control
                    {...register("region", {required: true, pattern: /^\d{6}$/i})}
                    isValid={touchedFields.region && !errors.region}
                    isInvalid={errors.region?.type != null}
                    type="text"
                    maxLength={6}
                    placeholder="6 siffer"/>
                {errors.region?.type === 'required' &&
                    <Form.Control.Feedback type="invalid">Region er påkrevet</Form.Control.Feedback>}
                {errors.region?.type === 'pattern' &&
                    <Form.Control.Feedback type="invalid">Region må bestå av 6 siffer</Form.Control.Feedback>}
            </Form.Group>

            { /** SKJEMATYPE */}
            <Form.Group
                className="col-sm-12"
                controlId="skjema">
                <Form.Label>Skjema</Form.Label>
                <Form.Select
                    {...register("skjema", {required: true})}
                    isValid={touchedFields.skjema && !errors.skjema}
                    isInvalid={errors.skjema != null}>
                    <option value="">Velg skjematype</option>
                    {skjematyper.map(skjematype =>
                        <option key={skjematype.id}
                                value={skjematype.id}>{skjematype.tittel}</option>)}
                </Form.Select>
                <Form.Control.Feedback type="invalid">Skjematype er påkrevet</Form.Control.Feedback>
            </Form.Group>

            { /** ORGNR */}
            {valgtSkjematype?.labelOrgnr &&
                <Form.Group className="col-sm-6" controlId="orgnrForetak">
                    <Form.Label>{valgtSkjematype.labelOrgnr}</Form.Label>
                    <Form.Control
                        {...register("orgnrForetak", {required: true, pattern: /^[8|9]\d{8}$/i})}
                        isValid={touchedFields.orgnrForetak && !errors.orgnrForetak}
                        isInvalid={errors.orgnrForetak?.type != null}
                        type="text"
                        maxLength={9}
                        placeholder="9 siffer"/>
                    {errors.orgnrForetak?.type === 'required' &&
                        <Form.Control.Feedback type="invalid">Organisasjonsnummer er påkrevet</Form.Control.Feedback>}
                    {errors.orgnrForetak?.type === 'pattern' &&
                        <Form.Control.Feedback type="invalid">Må starte med 8 eller 9, etterfulgt av 8
                            siffer</Form.Control.Feedback>}
                </Form.Group>
            }

            { /** ORGNR 2 */}
            {valgtSkjematype?.labelOrgnrVirksomhetene &&
                <Form.Group
                    className="col-sm-6"
                    controlId="orgnrVirksomhet">
                    <Form.Label>{valgtSkjematype.labelOrgnrVirksomhetene}</Form.Label>
                    <Form.Control
                        type="number"
                        placeholder="9 siffer"/>
                </Form.Group>
            }

            { /** FILE UPLOAD */}
            <Form.Group
                className="col-sm-12 mt-4"
                controlId="filnavn">
                <Form.Control
                    type="file"
                    isValid={isDirty && datafil != null}
                    isInvalid={isDirty && datafil == null}
                    onChange={handleFileUpload}/>
                <Form.Text className="text-muted">
                    {!datafil && <div className="invalid-feedback">Vennligst velg fil</div>}
                    {datafil && <div className="valid-feedback">Fil lastet opp</div>}
                </Form.Text>
            </Form.Group>

            <hr className="my-4"/>

            <Button
                type="submit"
                disabled={!formState.isValid || !datafil}
            >Kontroller fil</Button>
        </div>
    </Form>
}

export default App