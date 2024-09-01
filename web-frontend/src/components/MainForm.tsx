import {useEffect, useState} from "react"
import {useFieldArray, useForm} from "react-hook-form"
import {Button, Form} from "react-bootstrap"
import {yupResolver} from "@hookform/resolvers/yup"
import * as yup from "yup"

// app types
import KostraFormVm from "../kostratypes/kostraFormVm"
import KostraFormTypeVm from "../kostratypes/kostraFormTypeVm"
import Nullable from "../kostratypes/nullable"

// icons
// @ts-ignore
import PlusCircle from "../assets/icon/plus-circle.svg"
// @ts-ignore
import DashCircle from "../assets/icon/dash-circle.svg"
import CompanyIdVm from "../kostratypes/companyIdVm"

// misc constants
const COMPANY_ID_REQUIRED_MSG = "Organisasjonsnummer er påkrevet"
const COMPANY_ID_REGEX_MSG = "Må starte med '8' eller '9' etterfulgt av 8 siffer"
const MEBIBYTE_10 = 10485760
const MAX_VIRKSOMHET_FIELDS = 20
const FORM_LOCAL_STORAGE_KEY = "kostra-form"

const MainForm = ({formTypes, years, onSubmit}: {
    formTypes: KostraFormTypeVm[],
    years: number[],
    onSubmit: (form: KostraFormVm) => void,
}) => {
    const [valgtSkjematype, setValgtSkjematype] = useState<Nullable<KostraFormTypeVm>>()

    const validationSchema: yup.ObjectSchema<KostraFormVm> = yup.object().shape({
        skjema: yup.string().required("Skjematype er påkrevet"),
        aar: yup.number().required().positive("Årgang er påkrevet"),

        region: yup.string()
            .required("Region er påkrevet")
            .matches(/^\d{6}$/, "Region må bestå av 6 siffer"),

        orgnrForetak: yup.string().default<Nullable<string>>(null).when(([], schema) =>
            valgtSkjematype?.labelOrgnr
                ? schema
                    .required(COMPANY_ID_REQUIRED_MSG)
                    .matches(/^[8|9]\d{8}$/i, COMPANY_ID_REGEX_MSG)
                : schema.nullable()
        ),

        orgnrVirksomhet: yup.array(
            yup.object({
                orgnr: yup.string()
                    .required(COMPANY_ID_REQUIRED_MSG)
                    .matches(/^[8|9]\d{8}$/i, COMPANY_ID_REGEX_MSG)
            })).default<Nullable<CompanyIdVm[]>>(null).nullable(),

        skjemaFil: yup.mixed<FileList>().defined()
            .test(
                "required",
                "Vennligst velg fil",
                (files: FileList) => files?.length > 0
            ).test(
                "file-size",
                "Maks. filstørrelse er 10 MiB",
                (files: FileList) => files?.[0]?.size < MEBIBYTE_10
            )
    })

    // main form
    const {
        control,
        register,
        reset,
        resetField,
        handleSubmit,
        formState: {
            errors,
            dirtyFields,
            touchedFields
        },
        formState,
        watch,
        getValues
    } = useForm<KostraFormVm>({
        mode: "onChange",
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
    const localOnSubmit = handleSubmit(data => onSubmit(data))

    // Restore form data from localStorage when component mounts
    const loadFormFromLocalStorage = () => {
        const savedData = localStorage.getItem(FORM_LOCAL_STORAGE_KEY)
        if (!savedData) return;

        const formData = JSON.parse(savedData)
        reset(formData) // reset inits the form from formData

        if (!formData.skjema) return;

        const restoredSkjemaType = formTypes.find(
            (it) => it.id === formData.skjema
        )
        setValgtSkjematype(restoredSkjemaType)
    }

    // change skjema handling
    useEffect(() => {
        loadFormFromLocalStorage()

        const subscription = watch((value, {name, type}) => {
            // Save form data to local storage whenever relevant fields change
            localStorage.setItem(FORM_LOCAL_STORAGE_KEY, JSON.stringify(watch()));

            if (formTypes.length && name === "skjema" && type === "change") {
                // reset dirty state for individual fields
                resetField("orgnrForetak", {keepDirty: false})
                resetField("orgnrVirksomhet", {keepDirty: false})
                resetField("skjemaFil", {keepDirty: false})

                // get and set next skjemaType
                const newSkjemaType = formTypes.find(it => it.id === value.skjema)
                setValgtSkjematype(newSkjemaType)

                if (newSkjemaType?.labelOrgnrVirksomhetene) {
                    // add empty value to right-hand stack of orgnr
                    appendOrgnr({orgnr: ""}, {shouldFocus: false})
                }
            }
        })
        return () => subscription.unsubscribe()
    }, []) // executed on mount only

    return <Form noValidate validated={formState.isValid} onSubmit={localOnSubmit}>
        <div className="row g-3">

            {/** SKJEMATYPE */}
            {formTypes && <Form.Group
                className="col-sm-12"
                controlId="skjema">
                <Form.Label>Skjema</Form.Label>
                <Form.Select
                    {...register("skjema")}
                    isValid={dirtyFields.skjema && !errors.skjema}
                    isInvalid={errors.skjema != null || (touchedFields.skjema && !getValues("skjema"))}>
                    <option value="">Velg skjematype</option>
                    {formTypes.map((skjematype, index) =>
                        <option key={index} value={skjematype.id}>{skjematype.tittel}</option>)}
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
                    isValid={dirtyFields.aar && !errors.aar}
                    isInvalid={errors.aar != null || (touchedFields.aar && !getValues("aar"))}>
                    <option value="">Velg årgang</option>
                    {years.map((it, index) => <option key={index}>{it}</option>)}
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
                    isValid={dirtyFields.region && !errors.region}
                    isInvalid={errors.region != null || (touchedFields.region && !getValues("region"))}
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
                        isValid={dirtyFields.orgnrForetak && !errors.orgnrForetak}
                        isInvalid={errors.orgnrForetak != null
                            || (touchedFields.orgnrForetak && !getValues("orgnrForetak"))}
                        type="text"
                        autoComplete="off"
                        maxLength={9}
                        placeholder="9 siffer"/>
                    <Form.Control.Feedback type="invalid">{errors.orgnrForetak?.message}</Form.Control.Feedback>
                </Form.Group>}

            {/** ORGNR VIRKSOMHET */}
            {valgtSkjematype?.labelOrgnrVirksomhetene && <div className="col-sm-6">
                {orgnrVirksomhetFields.map((_, index) => {
                    return <div key={index} className="d-flex justify-content-between mb-2">
                        <Form.Group className="col-sm-10 me-2">
                            {/** show label for first entry only */}
                            {index < 1 && <Form.Label>{valgtSkjematype.labelOrgnrVirksomhetene}</Form.Label>}
                            <Form.Control
                                {...register(`orgnrVirksomhet.${index}.orgnr`)}
                                isValid={
                                    getValues(`orgnrVirksomhet.${index}.orgnr`) !== ""
                                    && errors.orgnrVirksomhet?.[index]?.orgnr == null
                                }
                                isInvalid={errors.orgnrVirksomhet?.[index]?.orgnr != null
                                    || ((touchedFields.orgnrVirksomhet as { orgnr: boolean }[])?.[index]?.orgnr
                                        && !getValues(`orgnrVirksomhet.${index}.orgnr`))}
                                type="text"
                                maxLength={9}
                                placeholder="9 siffer"/>
                        </Form.Group>

                        <div className="mt-auto mb-2 d-flex justify-content-between">
                            {/** show plus icon for last entry only, when last entry is touched and valid */}
                            {orgnrVirksomhetFields.length <= MAX_VIRKSOMHET_FIELDS
                                && index === orgnrVirksomhetFields.length - 1
                                && getValues(`orgnrVirksomhet.${index}.orgnr`)
                                && errors.orgnrVirksomhet?.[index]?.orgnr == null
                                && <Button
                                    onClick={() => appendOrgnr({orgnr: ""})}
                                    className="btn bg-transparent btn-outline-light p-0 me-2"
                                    title="Legg til virksomhetsnummer">
                                    <img src={PlusCircle} alt="Legg tit virksomhetsnummer"/>
                                </Button>}

                            {/** show minus icon for index > 0 */}
                            {index > 0 && <Button
                                onClick={() => removeOrgnr(index)}
                                className="btn bg-transparent btn-outline-light p-0 me-2"
                                title="Fjern virksomhetsnummer">
                                <img src={DashCircle} alt="Fjern virksomhetsnummer"/>
                            </Button>}
                        </div>
                    </div>
                })}
            </div>}

            {/** FILE UPLOAD */}
            <Form.Group
                className="col-sm-12 mb-1"
                controlId="filnavn">
                <Form.Label>Datafil (.dat eller .xml)</Form.Label>
                <Form.Control
                    {...register("skjemaFil")}
                    isValid={dirtyFields.skjemaFil && !errors.skjemaFil}
                    isInvalid={errors.skjemaFil != null
                        || (touchedFields.skjemaFil && getValues("skjemaFil").length < 1)}
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