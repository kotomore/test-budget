package mobi.sevenwinds.app.budget

import mobi.sevenwinds.app.author.*
import mobi.sevenwinds.app.author.AuthorTable.nullable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object BudgetTable : IntIdTable("budget") {
    val year = integer("year")
    val month = integer("month")
    val amount = integer("amount")
    val type = enumerationByName("type", 100, BudgetType::class)
    val authorId = integer("author_id")
}

class BudgetEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BudgetEntity>(BudgetTable)

    var year by BudgetTable.year
    var month by BudgetTable.month
    var amount by BudgetTable.amount
    var type by BudgetTable.type
    val authorFullname by AuthorTable.fullName.nullable()
    val authorCreatedAt by AuthorTable.createdAt.nullable()
    var authorId by BudgetTable.authorId.nullable()


    fun toResponse(): BudgetRecord {
        val fullname = authorFullname
        val createdAt = authorCreatedAt
        return when {
            fullname == null || createdAt == null -> {
                authorId?.let { BudgetRecordWithAuthorId(year, month, amount, type, it) }
                    ?: BudgetRecord(year, month, amount, type)
            }
            else -> {
                val authorRecord = AuthorRecord(fullname, createdAt.toLocalDateTime().toString())
                BudgetRecordWithAuthor(year, month, amount, type, authorRecord)
            }
        }
    }
}

class BudgetRecordWithAuthorId(year: Int, month: Int, amount: Int, type: BudgetType, val authorId: Int)
    : BudgetRecord(year, month, amount, type)

class BudgetRecordWithAuthor(year: Int, month: Int, amount: Int, type: BudgetType, val author: AuthorRecord)
    : BudgetRecord(year, month, amount, type)