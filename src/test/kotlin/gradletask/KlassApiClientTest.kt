package gradletask

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KlassApiClientTest {
    @Test
    fun `fetch code list from klass api`() {
        val result = KlassApiClient().fetchCodes(
            classificationId = "463",
            year = "2024"
        )
        assertThat(result).isNotNull
        println(result)
    }

}
