import {describe, expect, it, vi} from "vitest"
import {fileReportInTest, kostraFormInTest} from "../specData"
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

            const mockPostResponseAsync = vi.fn().mockImplementation(
                () => new Promise(resolve => resolve({data: fileReportInTest}))
            )

            // set mock
            api.post = mockPostResponseAsync

            // make call and verify result
            await expect(kontrollerSkjemaAsync(kostraFormInTest)).resolves.toEqual(fileReportInTest)
            expect(mockPostResponseAsync).toBeCalledWith(
                "/kontroller-skjema",
                kostraFormToMultipartBody(kostraFormInTest),
                MULTIPART_HEADER_CONFIG)
        })
    })
})