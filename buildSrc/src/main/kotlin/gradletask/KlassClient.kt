package gradletask

import gradletask.program.Code

interface KlassClient {
    fun fetchCodes(classificationId: String, year: String): List<Code>
    fun fetchCorrespondence(sourceClassificationId: String, targetClassificationId: String, year: String): List<Pair<Code, Code>>
}
