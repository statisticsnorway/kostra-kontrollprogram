import React, {ChangeEvent, useEffect, useState} from "react"
import {listSkjemaTyperAsync} from "./api/apiCalls"
import {useFieldArray, useForm} from "react-hook-form"
import {KostraFormVm} from "./kostratypes/kostraFormVm"
import {KostraFormTypeVm} from "./kostratypes/kostraFormTypeVm"
import {Nullable} from "./kostratypes/nullable"
import {Button, Form} from "react-bootstrap"
import * as yup from "yup"
import {array, string} from "yup"
import {yupResolver} from "@hookform/resolvers/yup"

// @ts-ignore
import PlusCircle from "./assets/icon/plus-circle.svg"
// @ts-ignore
import DashCircle from "./assets/icon/dash-circle.svg"
// @ts-ignore
import IconKostra from "./assets/icon/ikon-kostra.svg"

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
                .required("Organisasjonsnummer er påkrevet")
                .matches(/^[8|9]\d{8}$/i, "Må starte med '8' eller '9' etterfulgt av 8 siffer")
        }),
        orgnrVirksomhet: array().of(string()
            .required("Organisasjonsnummer er påkrevet")
            .matches(/^[8|9]\d{8}$/i, "Må starte med '8' eller '9' etterfulgt av 8 siffer")
        )
    }).required()

    const {
        control,
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

    // @ts-ignore
    const {fields, append, remove} = useFieldArray<KostraFormVm, "orgnrVirksomhet", "id">({
        control,
        name: "orgnrVirksomhet"
    })

    const onSubmit = handleSubmit(data => console.log(getValues()))

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
                if (!(name == 'skjema' && type == 'change')) {
                    return;
                }

                const localValgtSkjema = skjematyper.find(it => it.id == value.skjema)
                setValgtSkjematype(localValgtSkjema)

                localValgtSkjema?.labelOrgnrVirksomhetene
                    ? append("")
                    : fields.forEach((it, index) => {
                        // do mot remove braces, code will not be executed
                        remove(index)
                    })
            })
            return () => subscription.unsubscribe()
        }
    }, [skjematyper, watch])

    const handleFileUpload = (event: ChangeEvent<HTMLInputElement>) => {
        const {files} = event.target
        if (files == null || files.length == 0) {
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
        <div className="py-5 text-center">
            <h2 className="mb-3">
                <img src={IconKostra}
                     height="70px"
                     className="pe-4"
                     alt="Kostra"/>
                Kostra kontrollprogram
            </h2>

            {loadError && <span className="text-danger">{loadError}</span>}

            <hr className="my-0"/>
        </div>

        <div className="row g-3">
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
            {skjematyper.length > 0 && <Form.Group
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
            </Form.Group>}

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
                </Form.Group>}

            { /** ORGNR 2 */}
            {fields.length > 0 && <div className="col-sm-6">
                <div className="container">
                    {fields.map((item, index) => {
                        return <div key={item.id} className={index < 1 ? "row" : "row mt-2"}>
                            <Form.Group className="col-sm-10">
                                {index < 1 && <Form.Label>{valgtSkjematype?.labelOrgnrVirksomhetene}</Form.Label>}

{/*
                                <OverlayTrigger
                                    placement="top"
                                    //show={errors.orgnrVirksomhet?.[index] != null}
                                    delay={{show: 500, hide: 100}}
                                    overlay={
                                        <Tooltip
                                            id={`button-tooltip-${index}`}>{errors.orgnrVirksomhet?.[index]?.message}
                                        </Tooltip>}>
*/}
                                    <Form.Control
                                        {...register(`orgnrVirksomhet.${index}`)}
                                        isValid={(touchedFields.orgnrVirksomhet as boolean[])?.[index]
                                            && !errors.orgnrVirksomhet?.[index]}
                                        isInvalid={errors.orgnrVirksomhet?.[index] != null}
                                        type="text"
                                        maxLength={9}
                                        placeholder="9 siffer"/>
                                {/*</OverlayTrigger>*/}
                            </Form.Group>
                            <div className="col-sm-2 mt-auto m-0 mb-2">
                                {index > 0 && <img
                                    onClick={() => remove(index)}
                                    src={DashCircle}
                                    title="Fjern virksomhetsnummer"
                                    alt="Fjern virksomhetsnummer"/>}

                                {fields.length < 21
                                    && index == fields.length - 1
                                    && !errors.orgnrVirksomhet?.[index]
                                    && (touchedFields.orgnrVirksomhet as boolean[])?.[index]
                                    && <img
                                        className={index < 1 ? "ps-4" : "ps-1"}
                                        onClick={() => append("")}
                                        src={PlusCircle}
                                        title="Legg til virksomhetsnummer"
                                        alt="Legg til virksomhetsnummer"/>}
                            </div>
                        </div>
                    })}
                </div>
            </div>}

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
                className="btn-secondary"
                disabled={!formState.isValid || !datafil}
            >Kontroller fil</Button>

            <Button
                className="btn-secondary mt-5"
                onClick={() => {
                    setValue("aar", 2022)
                    setValue("region", "030100")
                    setValue("skjema", "0X")
                    setValue("orgnrForetak", "999999999")

                    setDatafil(null)
                    setValgtSkjematype(skjematyper.find(it => it.id == getValues("skjema")))
                    append("")
                }}
            >Sett testverdier 0X</Button>
        </div>
    </Form>
}

export default App