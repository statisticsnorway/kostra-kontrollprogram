import {useEffect, useMemo, useState} from "react"
import {useForm} from "react-hook-form"
import {Button, Form} from "react-bootstrap"
import {yupResolver} from "@hookform/resolvers/yup"

// app types
import KostraFormVm from "../../kostratypes/kostraFormVm"
import KostraFormTypeVm from "../../kostratypes/kostraFormTypeVm"
import Nullable from "../../kostratypes/nullable"

import {FORM_LOCAL_STORAGE_KEY, loadFormFromLocalStorage} from "./formUtils"
import {createValidationSchema} from "./yupSchema"

const COMPANY_ID_FIELD_NAME = "orgnrForetak"

const MainForm = ({formTypes, years, onSubmit}: {
    formTypes: KostraFormTypeVm[],
    years: number[],
    onSubmit: (form: KostraFormVm) => void,
}) => {
    const [valgtSkjematype, setValgtSkjematype] = useState<Nullable<KostraFormTypeVm>>()
    const validationSchema = useMemo(
        () => createValidationSchema(valgtSkjematype?.labelOrgnr != null),
        [valgtSkjematype?.labelOrgnr]
    )

    // main form
    const {
        register,
        unregister,
        reset,
        getValues,
        handleSubmit,
        formState: {
            errors,
        },
        formState,
        watch,
        trigger
    } = useForm<KostraFormVm>({
        mode: "onChange",
        resolver: yupResolver(validationSchema)
    })

    // submit-handler, redirects call to parent
    const localOnSubmit = handleSubmit(data => {
        localStorage.setItem(FORM_LOCAL_STORAGE_KEY, JSON.stringify(watch()))
        onSubmit(data)
    })

    // Restore form data from localStorage when component mounts
    const loadPersistedForm = () => {
        const formData = loadFormFromLocalStorage()

        if (formData) {
            reset(formData)
            setValgtSkjematype(formTypes.find((it) => it.id === formData.skjema))
        } else {
            reset()
            setValgtSkjematype(null)
        }
    }

    // change skjematype handling
    useEffect(() => {
        const subscription = watch((value, {name, type}) => {
            if (formTypes.length && type === "change" && name === "skjema") {
                setValgtSkjematype(formTypes.find(it => it.id === value.skjema))
            }
        })

        loadPersistedForm()

        return () => subscription.unsubscribe()
    }, []) // executed on mount only

    useEffect(() => {
        if (valgtSkjematype
            && !valgtSkjematype.labelOrgnr
            && COMPANY_ID_FIELD_NAME in getValues()) {
            unregister(COMPANY_ID_FIELD_NAME)
        }

        // trigger form validation after form is configured
        trigger().then().catch(error => console.log(error))
    }, [valgtSkjematype])

    return <Form noValidate validated={formState.isValid} onSubmit={localOnSubmit}>
        <div className="row g-3">

            {/** SKJEMATYPE */}
            {formTypes && <Form.Group
                className="col-sm-12"
                controlId="skjema">
                <Form.Label>Skjema</Form.Label>
                <Form.Select
                    {...register("skjema")}
                    isValid={!errors.skjema}
                    isInvalid={errors.skjema != null}>
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
                    isValid={!errors.aar}
                    isInvalid={errors.aar != null}>
                    <option>Velg årgang</option>
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
                    isValid={!errors.region}
                    isInvalid={errors.region != null}
                    type="text"
                    autoComplete="off"
                    maxLength={6}
                    placeholder="6 siffer"/>
                <Form.Control.Feedback type="invalid">{errors.region?.message}</Form.Control.Feedback>
            </Form.Group>

            {/** ORGNR */}
            {valgtSkjematype?.labelOrgnr &&
                <Form.Group className="col-sm-6" controlId={COMPANY_ID_FIELD_NAME}>
                    <Form.Label>{valgtSkjematype.labelOrgnr}</Form.Label>
                    <Form.Control
                        {...register(COMPANY_ID_FIELD_NAME)}
                        isValid={!errors.orgnrForetak}
                        isInvalid={errors.orgnrForetak != null}
                        type="text"
                        autoComplete="off"
                        maxLength={9}
                        placeholder="9 siffer"/>
                    <Form.Control.Feedback type="invalid">{errors.orgnrForetak?.message}</Form.Control.Feedback>
                </Form.Group>}

            {/** FILE UPLOAD */}
            <Form.Group
                className="col-sm-12 mb-1"
                controlId="filnavn">
                <Form.Label>Datafil (.dat eller .xml)</Form.Label>
                <Form.Control
                    {...register("skjemaFil")}
                    isValid={!errors.skjemaFil}
                    isInvalid={errors.skjemaFil != null}
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