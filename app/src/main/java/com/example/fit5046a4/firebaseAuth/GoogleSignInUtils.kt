package com.example.fit5046a4.firebaseAuth

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.fit5046a4.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleSignInUtils {

    companion object {
        /**
         * Initiates Google Sign-In using the Credential Manager API.
         * If successful, signs into Firebase and calls the `login()` callback.
         */
        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            login: () -> Unit
        ) {
            val credentialManager = CredentialManager.Companion.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()
            scope.launch {
                try {
                    val result = credentialManager.getCredential(context,request)

                    // Handle the retrieved credential
                    when(result.credential){
                        is CustomCredential ->{
                            if(result.credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                                val googleIdTokenCredential = GoogleIdTokenCredential.Companion.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId,null) // Sign in to Firebase with Google token
                                val user = try {
                                    Firebase.auth.signInWithCredential(authCredential).await().user
                                } catch (e: Exception) {
                                    Log.e("GoogleAuth", "Firebase login failed", e)
                                    null
                                }

                                // If login is successful and not anonymous, proceed
                                user?.let {
                                    if(it.isAnonymous.not()){
                                        login.invoke()
                                    }
                                }
                            }
                        }
                        else -> Log.w("GoogleAuth", "Unexpected credential type")
                    }
                }catch (e: NoCredentialException){
                    Log.e("GoogleAuth", "Error: ${e.javaClass.simpleName}", e)

                    // If a Google account is available, launch account chooser
                    if (isGoogleAccountAvailable(context)) {
                        launcher?.launch(getIntent())
                    } else {
                        launcher?.launch(getIntent()) // Fallback: open Google account settings
                    }
                }catch (e: GetCredentialException){
                    e.printStackTrace()
                }
            }
        }

        /**
         * Checks if the device has a Google account available.
         */
        private fun isGoogleAccountAvailable(context: Context): Boolean {
            val accountManager = AccountManager.get(context)
            return accountManager.getAccountsByType("com.google").isNotEmpty()
        }

        /**
         * Returns an intent to add a Google account via device settings.
         */
        private fun getIntent(): Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        /**
         * Builds the credential option for Google Sign-In.
         */
        private fun getCredentialOptions(context: Context): CredentialOption {
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()
        }
    }
}