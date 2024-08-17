package example.com.model.app_data

import example.com.model.DatabaseFactory.dbQuery
import example.com.model.authentication.UserRepoImp
import org.jetbrains.exposed.sql.*

class AppDataRepoImp(userRepoImp : UserRepoImp) : AppDataRepo {
    val userRepoImp = userRepoImp
    override suspend fun getAllData(): List<AppData?> = dbQuery {
            AppDataTable.selectAll().map(::rowToAutoUpdate)

    }

    override suspend fun getDataByAppName(appName:String): AppData? = dbQuery{
        AppDataTable.select {
            AppDataTable.appName.eq(appName)
        }.map(::rowToAutoUpdate).singleOrNull()


    }

    override suspend fun addData(appDataItem: AppData, userEmail:String) {
        dbQuery {
            val api = userRepoImp.getUserApi(userEmail)
            val insert = AppDataTable.insert {
                it[appName] = appDataItem.appName.trim()
                it[appVersion] = appDataItem.appVersion
                it[appVersionCode] = appDataItem.appVersionCode
                it[appDownloadLink] = appDataItem.appDownloadLink
                it[apiKey] = api!!
            }
            insert.resultedValues?.singleOrNull()?.let(::rowToAutoUpdate)
        }

    }

    override suspend fun removeData(appDataItem: AppData) {

    }

    override suspend fun updateData(appDataItem: AppData, userEmail:String) {
        dbQuery {
            val api = userRepoImp.getUserApi(userEmail)
            println(api)
            println(appDataItem)
            AppDataTable.update(
                where = { AppDataTable.appName eq appDataItem.appName and (AppDataTable.apiKey eq api!!) }
            )  {

                    it[appName] = appDataItem.appName
                    it[appVersion] = appDataItem.appVersion
                    it[appVersionCode] = appDataItem.appVersionCode
                    it[apiKey] = api!!

            }

        }
    }

    override suspend fun getDataByEmail(email: String): List<AppData?> {
        return dbQuery {
            val api = userRepoImp.getUserApi(email)
            AppDataTable.select( where = { AppDataTable.apiKey eq api!!}).map(::rowToAutoUpdate)
        }
    }

    override suspend fun getDataByAPI(api: String): List<AppData?> {
        return dbQuery {
            AppDataTable.select(where = { AppDataTable.apiKey eq api}).map(::rowToAutoUpdate)
        }
    }

    override suspend fun getDataByAppNameAndAPI(api: String, appName: String): AppData? {
        return dbQuery {
            AppDataTable.select ( where = {AppDataTable.apiKey eq api and (AppDataTable.appName eq appName)} ).map(::rowToAutoUpdate).singleOrNull()
        }
    }

    private fun rowToAutoUpdate(row:ResultRow?): AppData?{
        return if(row == null){
            null
        } else{
            AppData(
                appName = row[AppDataTable.appName],
                appVersion = row[AppDataTable.appVersion],
                appVersionCode = row[AppDataTable.appVersionCode],
                appDownloadLink = row[AppDataTable.appDownloadLink]


            )
        }
    }
}