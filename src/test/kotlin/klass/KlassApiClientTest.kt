package klass

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KlassApiClientTest {
    @Test
    fun `fetch code list from klass api`() {
        val id: String = "463"
        val codesAt: String = "2024"

        val result = KlassApiClient.fetchCodes(id, codesAt)
        assertThat(result).isNotNull
        println(result)
    }

}
