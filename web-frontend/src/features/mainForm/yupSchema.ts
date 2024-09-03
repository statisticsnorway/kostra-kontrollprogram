import * as yup from "yup"
import KostraFormVm from "../../kostratypes/kostraFormVm"
import Nullable from "../../kostratypes/nullable"
import CompanyIdVm from "../../kostratypes/companyIdVm"

const COMPANY_ID_REQUIRED_MSG = "Organisasjonsnummer er påkrevet"
const COMPANY_ID_REGEX_MSG = "Må starte med '8' eller '9' etterfulgt av 8 siffer"
const MEBIBYTE_10 = 10485760

export const createValidationSchema = (includeCompanyId: boolean): yup.ObjectSchema<KostraFormVm> =>
    yup.object().shape({
        skjema: yup.string().required("Skjematype er påkrevet"),
        aar: yup.number().typeError("Årgang er påkrevet")
            .required().positive("Årgang er påkrevet"),

        region: yup.string()
            .required("Region er påkrevet")
            .matches(/^\d{6}$/, "Region må bestå av 6 siffer"),

        orgnrForetak: yup.string().nullable().default(null).when(([], schema) =>
            includeCompanyId
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

        skjemaFil: yup.mixed<FileList>()
            .required("Vennligst velg fil")
            .test(
                "required",
                "Vennligst velg fil",
                (files: FileList) => {
                    return files ? files.length > 0 : false
                }
            ).test(
                "file-size",
                "Maks. filstørrelse er 10 MiB",
                (files: FileList) => {
                    const file = files?.[0]
                    return file ? file.size < MEBIBYTE_10 : false
                }
            )
    })
