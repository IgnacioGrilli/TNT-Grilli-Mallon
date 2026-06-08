package unpsjb.tnt.gestion_gastos.data

import unpsjb.tnt.gestion_gastos.data.model.LoggedInUser
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            if(username == "admin" && password == "123456"){
                val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Admin")
                return Result.Success(fakeUser)
            }
            return Result.Error(IOException("Usuario y/o contraseña incorrecto/s"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}