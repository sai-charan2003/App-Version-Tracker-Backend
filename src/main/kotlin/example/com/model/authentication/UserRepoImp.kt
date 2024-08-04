package example.com.model.authentication

import example.com.model.DatabaseFactory.dbQuery
import example.com.security.JWTConfig
import example.com.security.hashPassword
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class UserRepoImp : UserRepo {
    override suspend fun createUser(user: User) : User? {
        return dbQuery {
            val insert = UserTable.insert {
                    it[emailId] = user.emailId
                    it[password] = hashPassword(user.password)
                    it[userName] = user.userName
                    it[apiKey] = UUID.randomUUID().toString()

            }
            insert.resultedValues?.singleOrNull()?.let(::rowToUser)


        }
    }

    override suspend fun isUserExists(user: User): Boolean {

        return  dbQuery {
            UserTable.selectAll().map(::rowToUser).any { it?.emailId == user.emailId }
        }
    }

    override suspend fun loginUser(user: User): User? {
        return dbQuery {
            val userPresentInDB = UserTable.selectAll()
                .map(::rowToUser)
                .find { it?.emailId == user.emailId }

            if (userPresentInDB != null && hashPassword(user.password) == userPresentInDB.password) {
                userPresentInDB
            } else {
                null
            }
        }
    }

    override suspend fun getUserApi(email: String): String? {
        return dbQuery {
             UserTable.selectAll().map(::rowToUser).find { it?.emailId == email }?.apiKey

        }
    }


    private fun rowToUser(row:ResultRow?): User?{
        return if(row == null){
            null
        } else{
            User(
                userName = row[UserTable.userName],
                password = row[UserTable.password],
                emailId = row[UserTable.emailId],
                apiKey = row[UserTable.apiKey]

            )
        }
    }
}