import {describe, expect, it, vi} from "vitest"
import {fileReportInTest, kostraFormInTest} from "../specData"
import {api, kontrollerSkjemaAsync, kostraFormToMultipartBody, MULTIPART_HEADER_CONFIG, uiDataAsync} from "./apiCalls"

describe('apiCalls', () => {
    describe("ui-data (GET)", () => {
        it("calls /ui-data", async () => {
            const expectedUiDataVmJson = {
                releaseVersion: "N/A",
                years: [2023,2024],
                formTypes: [{
                    id: "~id~",
                    tittel: "~tittel~",
                    labelOrgnr: "~labelOrgnr~",
                    labelOrgnrVirksomhetene: "~labelOrgnrVirksomhetene~"
                }]
            }

            const mockGetResponseAsync = vi.fn().mockImplementation(
                () => new Promise(resolve => resolve({data: expectedUiDataVmJson}))
            )

            // set mock
            api.get = mockGetResponseAsync

            // make call and verify result
            await expect(uiDataAsync()).resolves.toEqual(expectedUiDataVmJson)
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