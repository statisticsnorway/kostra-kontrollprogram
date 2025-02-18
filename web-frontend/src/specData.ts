import KostraFormVm from "./kostratypes/kostraFormVm"
import KostraSeverity from "./kostratypes/kostraSeverity"
import FileReportVm from "./kostratypes/fileReportVm"
import FileReportEntryVm from "./kostratypes/fileReportEntryVm"

export const appReleaseVersionInTest = "~appReleaseVersion~"

export const createMockFileList = (): FileList => {
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

export const kostraFormInTest: KostraFormVm = {
    aar: (new Date()).getFullYear(),
    skjema: "0X",
    region: "030100",
    orgnrForetak: "987654321",
    skjemaFil: createMockFileList()
}

export const fileReportEntryInTestWithRequiredPropsSet: FileReportEntryVm = {
    severity: KostraSeverity.WARNING,
    caseworker: null,
    journalId: null,
    individId: null,
    contextId: null,
    ruleName: "~kontrollnummer~",
    messageText: "~kontrolltekst~",
    lineNumbers: null
}

export const fileReportEntryInTestWithAllPropsSet: FileReportEntryVm = {
    severity: KostraSeverity.WARNING,
    caseworker: "~saksbehandler~",
    journalId: "~journalnummer~",
    individId: "~individId~",
    contextId: "~contextId~",
    ruleName: "~kontrollnummer~",
    messageText: "~kontrolltekst~",
    lineNumbers: [1, 2, 3]
}

export const fileReportInTest: FileReportVm = {
    innparametere: kostraFormInTest,
    antallKontroller: 42,
    feilkode: KostraSeverity.WARNING,
    feil: [fileReportEntryInTestWithAllPropsSet]
}
