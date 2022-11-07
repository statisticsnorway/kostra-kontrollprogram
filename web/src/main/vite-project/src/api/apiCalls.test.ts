import {describe, expect, it, vi} from 'vitest'
import {api, kontrollerSkjemaAsync, listSkjemaTyperAsync} from "./apiCalls"
import {ErrorReportVm, KostraFormTypeVm, KostraFormVm} from "../kostraTypes"

describe('apiCalls', () => {
    describe("skjematyper (GET)", () => {

        it("calls /skjematyper", async () => {

            const kostraFormTypeArrayJson = [{
                id: "~id~",
                tittel: "~tittel~",
                labelOrgnr: "~labelOrgnr~",
                labelOrgnrVirksomhetene: "~labelOrgnrVirksomhetene~"
            }]

            const mockGetResponseAsync = vi.fn().mockImplementation(
                () => new Promise(resolve => resolve({data: kostraFormTypeArrayJson}))
            )

            // set mock
            api.get = mockGetResponseAsync

            // make call and verify result
            await expect(listSkjemaTyperAsync()).resolves.toEqual(kostraFormTypeArrayJson as KostraFormTypeVm[])
            expect(mockGetResponseAsync).toBeCalledWith("/skjematyper")
        })
    })

    describe("kontroller-skjema (POST)", () => {

        it("calls /kontroller-skjema", async () => {

            const kostraFormJson = {
                aar: 2022,
                skjema: "0X",
                region: "030100",
                navn: "Oslo",
                orgnrForetak: "987654321",
                orgnrVirksomhet: [{orgnr: "876543219"}],
                base64EncodedContent: "dfgsdfgsdfgfdsgsdfgdfgfdg"
            }

            const errorReportJson = {
                innparametere: kostraFormJson,
                antallKontroller: 42,
                feilkode: 1,
                feil: [{
                    journalnummer: "~journalnummer~",
                    saksbehandler: "~saksbehandler~",
                    kontrollnummer: "~kontrollnummer~",
                    kontrolltekst: "~kontrolltekst~",
                    feilkode: 1
                }]
            }

            const mockPostResponseAsync = vi.fn().mockImplementation(
                () => new Promise(resolve => resolve({data: errorReportJson}))
            )

            // set mock
            api.post = mockPostResponseAsync

            // make call and verify result
            await expect(kontrollerSkjemaAsync(kostraFormJson as KostraFormVm))
                .resolves.toEqual(errorReportJson as ErrorReportVm)

            expect(mockPostResponseAsync).toBeCalledWith(
                "/kontroller-skjema", kostraFormJson as KostraFormVm)
        })
    })
})