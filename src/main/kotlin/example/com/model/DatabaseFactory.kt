package example.com.model

import example.com.model.authentication.UserTable
import example.com.model.app_data.AppDataTable
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


import java.io.File

object DatabaseFactory {

    fun init() {
        val isDocker = !File("C:\\Users\\saich\\Developer\\Ktor\\Auto_Update\\secrets.env").exists()

        val (url, user, password) = if (isDocker) {
            // Use Docker environment variables
            Triple(
                System.getenv("JDBC_URL"),
                System.getenv("JDBC_username"),
                System.getenv("JDBC_password")
            )
        } else {
            // Use dotenv for local development
            val dotEnv = Dotenv.configure()
                .directory("C:\\Users\\saich\\Developer\\Ktor\\Auto_Update") // Local path
                .filename("secrets.env")
                .load()
            Triple(
                dotEnv["JDBC_URL"],
                dotEnv["JDBC_username"],
                dotEnv["JDBC_password"]
            )
        }

        val database = Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )

        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(AppDataTable, UserTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
