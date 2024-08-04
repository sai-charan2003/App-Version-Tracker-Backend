package example.com.model.authentication

interface UserRepo {
    suspend fun createUser(user: User) : User?

    suspend fun isUserExists(user:User):Boolean

    suspend fun loginUser(user: User) : User?

    suspend fun getUserApi(email: String) : String?

}