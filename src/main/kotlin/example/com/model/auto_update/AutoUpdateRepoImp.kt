package example.com.model.auto_update

import example.com.model.DatabaseFactory.dbQuery
import example.com.model.authentication.UserRepoImp
import org.jetbrains.exposed.sql.*

class AutoUpdateRepoImp(userRepoImp : UserRepoImp) : AutoUpdateRepo {
    val userRepoImp = userRepoImp
    override suspend fun getAllData(): List<AutoUpdate?> = dbQuery {
            AutoUpdateTable.selectAll().map(::rowToAutoUpdate)

    }

    override suspend fun getDataByAppName(appName:String): AutoUpdate? = dbQuery{
        AutoUpdateTable.select {
            AutoUpdateTable.appName.eq(appName)
        }.map(::rowToAutoUpdate).singleOrNull()


    }

    override suspend fun addData(autoUpdateData: AutoUpdate,userEmail:String) {
        dbQuery {
            val api = userRepoImp.getUserApi(userEmail)
            val insert = AutoUpdateTable.insert {
                it[appName] = autoUpdateData.appName
                it[appVersion] = autoUpdateData.appVersion
                it[appVersionCode] = autoUpdateData.appVersionCode
                it[apiKey] = api!!
            }
            insert.resultedValues?.singleOrNull()?.let(::rowToAutoUpdate)
        }

    }

    override suspend fun removeData(autoUpdateData: AutoUpdate) {

    }

    override suspend fun updateData(autoUpdateData: AutoUpdate, userEmail:String) {
        dbQuery {
            val api = userRepoImp.getUserApi(userEmail)
            println(api)
            println(autoUpdateData)
            AutoUpdateTable.update(
                where = { AutoUpdateTable.appName eq autoUpdateData.appName and (AutoUpdateTable.apiKey eq api!!) }
            )  {

                    it[appName] = autoUpdateData.appName
                    it[appVersion] = autoUpdateData.appVersion
                    it[appVersionCode] = autoUpdateData.appVersionCode
                    it[apiKey] = api!!

            }

        }
    }

    override suspend fun getDataByEmail(email: String): List<AutoUpdate?> {
        return dbQuery {
            val api = userRepoImp.getUserApi(email)
            AutoUpdateTable.select( where = { AutoUpdateTable.apiKey eq api!!}).map(::rowToAutoUpdate)
        }
    }

    override suspend fun getDataByAPI(api: String): List<AutoUpdate?> {
        return dbQuery {
            AutoUpdateTable.select(where = { AutoUpdateTable.apiKey eq api}).map(::rowToAutoUpdate)
        }
    }

    private fun rowToAutoUpdate(row:ResultRow?): AutoUpdate?{
        return if(row == null){
            null
        } else{
            AutoUpdate(
                appName = row[AutoUpdateTable.appName],
                appVersion = row[AutoUpdateTable.appVersion],
                appVersionCode = row[AutoUpdateTable.appVersionCode],


            )
        }
    }
}