import KostraFormVm from "./kostratypes/kostraFormVm";
import KostraErrorCode from "./kostratypes/kostraErrorCode";
import FileReportVm from "./kostratypes/fileReportVm";
import FileReportEntryVm from "./kostratypes/fileReportEntryVm";

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
    orgnrVirksomhet: [{orgnr: "876543219"}],
    skjemaFil: createMockFileList()
}

export const fileReportEntryInTest: FileReportEntryVm = {
    journalnummer: "~journalnummer~",
    saksbehandler: "~saksbehandler~",
    kontrollnummer: "~kontrollnummer~",
    kontrolltekst: "~kontrolltekst~",
    feilkode: KostraErrorCode.NORMAL_ERROR
}

export const fileReportInTest: FileReportVm = {
    innparametere: kostraFormInTest,
    antallKontroller: 42,
    feilkode: KostraErrorCode.NORMAL_ERROR,
    feil: [fileReportEntryInTest]
}
