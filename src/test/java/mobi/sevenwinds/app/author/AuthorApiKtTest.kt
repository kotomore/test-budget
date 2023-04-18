package mobi.sevenwinds.app.author

import io.restassured.RestAssured
import mobi.sevenwinds.common.ServerTest
import mobi.sevenwinds.common.jsonBody
import mobi.sevenwinds.common.toResponse
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthorApiKtTest : ServerTest() {

    @BeforeEach
    internal fun setUp() {
        transaction { AuthorTable.deleteAll() }
    }

    @Test
    fun testValidAuthorsAdd() {
        RestAssured.given()
            .jsonBody(AuthorSaveRecord(null, "John Doe"))
            .post("/author/add")
            .then().statusCode(200)

        RestAssured.given()
            .jsonBody(AuthorSaveRecord(null, "John Doe II"))
            .post("/author/add")
            .toResponse<AuthorSaveRecord>().let { response ->
                Assert.assertEquals(AuthorSaveRecord(2, "John Doe II"), response)
            }
    }
}