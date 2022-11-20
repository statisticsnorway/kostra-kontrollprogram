import React, {useEffect, useState} from "react"
import {useFieldArray, useForm} from "react-hook-form"
import {Button, Form} from "react-bootstrap"
import {yupResolver} from "@hookform/resolvers/yup";
import * as yup from "yup"

// app types
import {KostraFormVm} from "../kostratypes/kostraFormVm"
import {KostraFormTypeVm} from "../kostratypes/kostraFormTypeVm"
import {Nullable} from "../kostratypes/nullable"

// API calls
import {listSkjemaTyperAsync} from "../api/apiCalls"

// icons
// @ts-ignore
import PlusCircle from "../assets/icon/plus-circle.svg"
// @ts-ignore
import DashCircle from "../assets/icon/dash-circle.svg"
// @ts-ignore
import IconKostra from "../assets/icon/ikon-kostra.svg"

// misc constants
const COMPANY_ID_REQUIRED_MSG = "Organisasjonsnummer er påkrevet"
const COMPANY_ID_REGEX_MSG = "Må starte med '8' eller '9' etterfulgt av 8 siffer"
const MEBIBYTE_50 = 52428800
const MAX_VIRKSOMHET_FIELDS = 20

const MainForm = (props: {
    readonly showForm: boolean,
    onSubmit: (form: KostraFormVm) => void,
    onLoadError: (message: string) => void,
}) => {
    const {onSubmit, onLoadError, showForm} = props

    const [skjematyper, setSkjematyper] = useState<KostraFormTypeVm[]>([])
    const [valgtSkjematype, setValgtSkjematype] = useState<Nullable<KostraFormTypeVm>>()

    const validationSchema: yup.SchemaOf<KostraFormVm> = yup.object().shape({
            skjema: yup.string().required("Skjematype er påkrevet"),
            aar: yup.number().transform(value => (isNaN(value) ? 0 : value)).positive("Årgang er påkrevet"),
            region: yup.string()
                .required("Region er påkrevet")
                .matches(/^\d{6}$/, "Region må bestå av 6 siffer"),
            orgnrForetak: yup.string().when([], {
                is: () => valgtSkjematype?.labelOrgnr,
                then: yup.string()
                    .required(COMPANY_ID_REQUIRED_MSG)
                    .matches(/^[8|9]\d{8}$/i, COMPANY_ID_REGEX_MSG)
            }),
            orgnrVirksomhet: yup.array().of(
                yup.object().shape({
                    orgnr: yup.string()
                        .required(COMPANY_ID_REQUIRED_MSG)
                        .matches(/^[8|9]\d{8}$/i, COMPANY_ID_REGEX_MSG)
                })
            ),
            skjemaFil: yup.mixed()
                .test(
                    "required",
                    "Vennligst velg fil",
                    (files: FileList) => files?.length > 0
                ).test(
                    "file-size",
                    "Maks. filstørrelse er 50 MiB",
                    (files: FileList) => files?.[0]?.size < MEBIBYTE_50
                )
        }
    ).required()

    // main form
    const {
        control,
        register,
        reset,
        resetField,
        handleSubmit,
        formState: {errors, touchedFields},
        formState,
        watch
    } = useForm<KostraFormVm>({
        mode: "onBlur",
        resolver: yupResolver(validationSchema)
    })

    // array for orgnrVirksomhet
    const {
        fields: orgnrVirksomhetFields,
        append: appendOrgnr,
        remove: removeOrgnr
    } = useFieldArray<KostraFormVm>({
        control,
        name: "orgnrVirksomhet"
    })

    // submit-handler, redirects call to parent
    const localOnSubmit = handleSubmit(data => {
        onSubmit(data)
        // prevent re-submission of file
        resetField("skjemaFil", {keepTouched: false})
    })

    // get ui data
    useEffect(() => {
        listSkjemaTyperAsync()
            .then(skjematyper => {
                setSkjematyper(skjematyper)
                onLoadError("")
            })
            .catch(() => onLoadError("Lasting av skjematyper feilet"))
    }, [])

    // change skjema handling
    useEffect(() => {
        if (skjematyper.length) {
            const subscription = watch((value, {name, type}) => {
                if (!(name == "skjema" && type == "change")) return

                // reset keepTouched for individual fields before
                // resetting the whole form
                resetField("orgnrForetak", {keepTouched: false})
                resetField("orgnrVirksomhet.0.orgnr", {keepTouched: false})
                resetField("skjemaFil", {keepTouched: false})

                // reset form, keep touched fields
                reset({}, {keepTouched: true})

                // get next skjemaType
                const newSkjemaType = skjematyper.find(it => it.id == value.skjema)

                // set new skjemaType
                setValgtSkjematype(newSkjemaType)

                if (newSkjemaType?.labelOrgnrVirksomhetene) {
                    // add empty value to right-hand stack of orgnr
                    appendOrgnr({orgnr: ""}, {shouldFocus: false})
                }
            })
            return () => subscription.unsubscribe()
        }
    }, [skjematyper, watch])

    /** if active view is a file report, hide this component */
    return !showForm ? <></> : <Form noValidate validated={formState.isValid} onSubmit={localOnSubmit}>
        <div className="row g-3 mt-2">

            {/** SKJEMATYPE */}
            {skjematyper.length > 0 && <Form.Group
                className="col-sm-12"
                controlId="skjema">
                <Form.Label>Skjema</Form.Label>
                <Form.Select
                    {...register("skjema")}
                    isValid={touchedFields.skjema && !errors.skjema}
                    isInvalid={errors.skjema != null}>
                    <option value="">Velg skjematype</option>
                    {skjematyper.map((skjematype, index) =>
                        <option key={index}
                                value={skjematype.id}>{skjematype.tittel}</option>)}
                </Form.Select>
                <Form.Control.Feedback type="invalid">{errors.skjema?.message}</Form.Control.Feedback>
            </Form.Group>}

            {/** ÅRGANG */}
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

            {/** REGION */}
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

            {/** ORGNR */}
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

            {/** ORGNR VIRKSOMHET */}
            {valgtSkjematype?.labelOrgnrVirksomhetene && <div className="col-sm-6">
                <div className="container ps-0">
                    {orgnrVirksomhetFields.map((orgnrVirksomhet, index) => {
                        return <div key={orgnrVirksomhet.id} className={index < 1 ? "row" : "row mt-2"}>
                            <Form.Group className="col-sm-10">
                                {/** show label for first entry only */}
                                {index < 1 && <Form.Label>{valgtSkjematype?.labelOrgnrVirksomhetene}</Form.Label>}
                                <Form.Control
                                    {...register(`orgnrVirksomhet.${index}.orgnr`)}
                                    isValid={(touchedFields.orgnrVirksomhet as boolean[])?.[index]
                                        && !errors.orgnrVirksomhet?.[index]}
                                    isInvalid={errors.orgnrVirksomhet?.[index] != null}
                                    type="text"
                                    maxLength={9}
                                    placeholder="9 siffer"/>
                            </Form.Group>

                            <div className="col-sm-2 mt-auto mb-2">
                                {/** show minus icon for index > 0 */}
                                {index > 0 && <img
                                    onClick={() => removeOrgnr(index)}
                                    className="pe-2"
                                    src={DashCircle}
                                    title="Fjern virksomhetsnummer"
                                    alt="Fjern virksomhetsnummer"/>}

                                {/** show plus icon for last entry only, when last entry is touched and valid */}
                                {orgnrVirksomhetFields.length <= MAX_VIRKSOMHET_FIELDS
                                    && index == orgnrVirksomhetFields.length - 1
                                    && !errors.orgnrVirksomhet?.[index]
                                    && (touchedFields.orgnrVirksomhet as boolean[])?.[index]
                                    && <img
                                        className={index > 0 ? "ps-3" : "ps-5"}
                                        onClick={() => appendOrgnr({orgnr: ""})}
                                        src={PlusCircle}
                                        title="Legg til virksomhetsnummer"
                                        alt="Legg til virksomhetsnummer"/>}
                            </div>
                        </div>
                    })}
                </div>
            </div>}

            {/** FILE UPLOAD */}
            <Form.Group
                className="col-sm-12 mb-1"
                controlId="filnavn">
                <Form.Label>Datafil (.dat eller .xml)</Form.Label>
                <Form.Control
                    {...register("skjemaFil")}
                    isValid={touchedFields.skjemaFil && !errors.skjemaFil}
                    isInvalid={errors.skjemaFil?.type != null}
                    type="file"
                    accept=".dat,.xml"
                />
                <Form.Control.Feedback type="invalid">{errors.skjemaFil?.message}</Form.Control.Feedback>
            </Form.Group>

            <div className="d-flex justify-content-center mt-4">
                <Button
                    type="submit"
                    className="btn-secondary flex-fill"
                    disabled={!formState.isValid}>Kontroller fil</Button>
            </div>
        </div>
    </Form>
}

export default MainForm