package example.com.model.auto_update

interface AutoUpdateRepo{
    suspend fun getAllData() : List<AutoUpdate?>
    suspend fun getDataByAppName(appName : String) : AutoUpdate?
    suspend fun addData(autoUpdateData : AutoUpdate,userEmail:String)
    suspend fun removeData(autoUpdateData: AutoUpdate)
    suspend fun updateData(autoUpdateData: AutoUpdate,userEmail:String)
    suspend fun getDataByEmail(email:String): List<AutoUpdate?>

    suspend fun getDataByAPI(api:String) : List<AutoUpdate?>

}