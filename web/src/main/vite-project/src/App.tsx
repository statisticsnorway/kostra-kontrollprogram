import {ChangeEvent, useEffect, useState} from "react"
import {listSkjemaTyperAsync} from "./api/apiCalls"
import {useForm} from "react-hook-form"
import {KostraFormVm} from "./kostratypes/kostraFormVm"
import {KostraFormTypeVm} from "./kostratypes/kostraFormTypeVm"
import {Nullable} from "./kostratypes/nullable"
import {Button, Form} from "react-bootstrap"
import * as yup from "yup"
import {yupResolver} from "@hookform/resolvers/yup";

function App() {

    const [loadError, setLoadError] = useState<string>()
    const [skjematyper, setSkjematyper] = useState<KostraFormTypeVm[]>([])
    const [valgtSkjematype, setValgtSkjematype] = useState<Nullable<KostraFormTypeVm>>()
    const [datafil, setDatafil] = useState<Nullable<string>>(null)

    const validationSchema = yup.object({
        aar: yup.number().transform(value => (isNaN(value) ? 0 : value)).positive("Årgang er påkrevet"),
        region: yup.string().required("Region er påkrevet").matches(/^\d{6}$/, "Region må bestå av 6 siffer"),
        skjema: yup.string().required("Skjematype er påkrevet"),
        orgnrForetak: yup.string().when([], {
            is: () => valgtSkjematype?.labelOrgnr,
            then: yup.string()
                .required("Orgnr er påkrevet")
                .matches(/^[8|9]\d{8}$/i, "Må starte med [8,9] etterfulgt av 8 siffer")
        })
    }).required()

    const {
        register,
        getValues,
        setValue,
        handleSubmit,
        formState: {errors, touchedFields, isDirty},
        formState,
        watch
    } = useForm<KostraFormVm>({
        mode: "onBlur",
        resolver: yupResolver(validationSchema)
    })

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
                setValgtSkjematype(skjematyper.find(it => it.id === getValues("skjema")))
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
                    {...register("aar")}
                    isValid={touchedFields.aar && !errors.aar}
                    isInvalid={errors.aar != null}>
                    <option value="">Velg år</option>
                    <option value="2022">2022</option>
                    <option value="2021">2021</option>
                </Form.Select>
                <Form.Control.Feedback type="invalid">{errors.aar?.message}</Form.Control.Feedback>
            </Form.Group>

            { /** REGION */}
            <Form.Group
                className="col-sm-6"
                controlId="region">
                <Form.Label>Regionsnummer</Form.Label>
                <Form.Control
                    {...register("region")}
                    isValid={touchedFields.region && !errors.region}
                    isInvalid={errors.region?.type != null}
                    type="text"
                    autoComplete="off"
                    maxLength={6}
                    placeholder="6 siffer"/>
                <Form.Control.Feedback type="invalid">{errors.region?.message}</Form.Control.Feedback>
            </Form.Group>

            { /** SKJEMATYPE */}
            <Form.Group
                className="col-sm-12"
                controlId="skjema">
                <Form.Label>Skjema</Form.Label>
                <Form.Select
                    {...register("skjema")}
                    isValid={touchedFields.skjema && !errors.skjema}
                    isInvalid={errors.skjema != null}>
                    <option value="">Velg skjematype</option>
                    {skjematyper.map(skjematype =>
                        <option key={skjematype.id}
                                value={skjematype.id}>{skjematype.tittel}</option>)}
                </Form.Select>
                <Form.Control.Feedback type="invalid">{errors.skjema?.message}</Form.Control.Feedback>
            </Form.Group>

            { /** ORGNR */}
            {valgtSkjematype?.labelOrgnr &&
                <Form.Group className="col-sm-6" controlId="orgnrForetak">
                    <Form.Label>{valgtSkjematype.labelOrgnr}</Form.Label>
                    <Form.Control
                        {...register("orgnrForetak")}
                        isValid={touchedFields.orgnrForetak && !errors.orgnrForetak}
                        isInvalid={errors.orgnrForetak?.type != null}
                        type="text"
                        autoComplete="off"
                        maxLength={9}
                        placeholder="9 siffer"/>
                    <Form.Control.Feedback type="invalid">{errors.orgnrForetak?.message}</Form.Control.Feedback>
                </Form.Group>
            }

            { /** ORGNR 2 */}
            {valgtSkjematype?.labelOrgnrVirksomhetene &&
                <Form.Group
                    className="col-sm-6"
                    controlId="orgnrVirksomhet">
                    <Form.Label>{valgtSkjematype.labelOrgnrVirksomhetene}</Form.Label>
                    <Form.Control
                        type="text"
                        maxLength={9}
                        placeholder="9 siffer"/>
                </Form.Group>
            }

            { /** FILE UPLOAD */}
            <Form.Group
                className="col-sm-12 mt-4"
                controlId="filnavn">
                <Form.Control
                    type="file"
                    isInvalid={isDirty && datafil == null}
                    onChange={handleFileUpload}/>
                {!datafil && <div className="invalid-feedback">Vennligst velg fil</div>}
                {datafil && <div className="text-success mt-1">Fil lastet opp</div>}
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