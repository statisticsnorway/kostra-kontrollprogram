import {describe, expect, it, vi} from "vitest"
import {
    api,
    kontrollerSkjemaAsync,
    uiDataAsync,
    kostraFormToMultipartBody,
    MULTIPART_HEADER_CONFIG
} from "./apiCalls"

describe('apiCalls', () => {
    describe("ui-data (GET)", () => {

        it("calls /ui-data", async () => {

            const uiDataVmJson = {
                releaseVersion: "N/A",
                years: [2022,2023],
                formTypes: [{
                    id: "~id~",
                    tittel: "~tittel~",
                    labelOrgnr: "~labelOrgnr~",
                    labelOrgnrVirksomhetene: "~labelOrgnrVirksomhetene~"
                }]
            }

            const mockGetResponseAsync = vi.fn().mockImplementation(
                () => new Promise(resolve => resolve({data: uiDataVmJson}))
            )

            // set mock
            api.get = mockGetResponseAsync

            // make call and verify result
            await expect(uiDataAsync()).resolves.toEqual(uiDataVmJson)
            expect(mockGetResponseAsync).toBeCalledWith("/ui-data")
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
                skjemaFil: createMockFileList()
            }

            const fileReportJson = {
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
                () => new Promise(resolve => resolve({data: fileReportJson}))
            )

            // set mock
            api.post = mockPostResponseAsync

            // make call and verify result
            await expect(kontrollerSkjemaAsync(kostraFormJson)).resolves.toEqual(fileReportJson)
            expect(mockPostResponseAsync).toBeCalledWith(
                "/kontroller-skjema",
                kostraFormToMultipartBody(kostraFormJson),
                MULTIPART_HEADER_CONFIG)
        })
    })
})

const createMockFileList = (): FileList => {
    const fakeFileInput = document.createElement("input")
    fakeFileInput.setAttribute("type", "file")

    let mockFileList = Object.create(fakeFileInput.files)

    mockFileList[0] = new File(
        ["foo"],
        "foo.dat",
        {
            type: "text/plain"
        })

    return mockFileList
}