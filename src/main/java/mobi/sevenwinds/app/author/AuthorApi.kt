package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route

fun NormalOpenAPIRoute.author() {
    route("/author") {
        route("/add").post<Unit, AuthorSaveRecord, AuthorSaveRecord>(info("Добавить автора внесения записей")) { param, body ->
            respond(AuthorService.addRecord(body))
        }
    }
}

data class AuthorSaveRecord(
    val fullName: String
)
