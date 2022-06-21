package com.example.androidkotlinfinal.data

import com.example.androidkotlinfinal.data.model.LoggedInUser
import java.io.IOException
import java.util.*


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    fun login(username: String?, password: String?): Result<Any?> {
        return try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(
                UUID.randomUUID().toString(),
                "Jane Doe"
            )
            Result.Success<Any?>(fakeUser)
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}