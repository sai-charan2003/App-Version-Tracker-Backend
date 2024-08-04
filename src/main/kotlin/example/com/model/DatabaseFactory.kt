package example.com.model

import example.com.model.authentication.UserTable
import example.com.model.auto_update.AutoUpdateTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory{

    fun init(){
         val database = Database.connect(
            url = System.getenv("JDBC_URL"),
            driver = "org.postgresql.Driver",
            user = System.getenv("JDBC_username"),
            password = System.getenv("JDBC_password")
        )


        transaction (database) {
            SchemaUtils.createMissingTablesAndColumns(AutoUpdateTable,UserTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


}