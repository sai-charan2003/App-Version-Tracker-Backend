package example.com.model

import example.com.model.authentication.UserTable
import example.com.model.auto_update.AutoUpdateTable
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

import io.github.cdimascio.dotenv.dotenv


object DatabaseFactory{

    fun init(){
        val dotEnv = Dotenv.configure().directory("C:\\Users\\saich\\Developer\\Ktor\\Auto_Update").filename("secrets.env").load()
         val database = Database.connect(
            url = dotEnv["JDBC_URL"],
            driver = "org.postgresql.Driver",
            user = dotEnv["JDBC_username"],
            password = dotEnv["JDBC_password"]
        )


        transaction (database) {
            SchemaUtils.createMissingTablesAndColumns(AutoUpdateTable,UserTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


}