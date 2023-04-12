package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRecordRequest): BudgetRecord = withContext(Dispatchers.IO) {
        try {
            transaction {
                val entity = BudgetEntity.new {
                    this.year = body.year
                    this.month = body.month
                    this.amount = body.amount
                    this.type = body.type
                    this.authorId = body.authorId
                }

                return@transaction entity.toResponse()
            }
        } catch (e: ExposedSQLException) {
            throw IllegalArgumentException("Invalid authorId: ${body.authorId}", e)
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val query = BudgetTable
                .join(AuthorTable, JoinType.LEFT, BudgetTable.authorId, AuthorTable.id)
                .select {BudgetTable.year eq param.year}

            val total = query.count()
            val sumByType = BudgetEntity.wrapRows(query)
                .map { it.toResponse() }
                .groupBy { it.type.name }
                .mapValues { it.value.sumOf { v -> v.amount } }

            query
                .limit(param.limit, param.offset)
                .orderBy(BudgetTable.month to SortOrder.ASC, BudgetTable.amount to SortOrder.DESC)


            val data = BudgetEntity.wrapRows(query).map { it.toResponse() }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }
}