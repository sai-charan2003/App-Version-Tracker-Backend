package example.com.model.app_data

interface AppDataRepo{
    suspend fun getAllData() : List<AppData?>
    suspend fun getDataByAppName(appName : String) : AppData?
    suspend fun addData(appDataItem : AppData, userEmail:String)
    suspend fun removeData(appUUID : String)
    suspend fun updateData(appDataItem: AppData, userEmail:String)
    suspend fun getDataByEmail(email:String): List<AppData?>

    suspend fun getDataByAPI(api:String) : List<AppData?>

    suspend fun getDataByAppNameAndAPI(api:String,appName: String) : AppData?

}