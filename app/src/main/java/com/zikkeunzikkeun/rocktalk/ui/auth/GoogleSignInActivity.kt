package com.zikkeunzikkeun.rocktalk.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.zikkeunzikkeun.rocktalk.MainActivity
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings
import kotlinx.coroutines.launch

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
class GoogleSignInActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    // [START declare_credential_manager]
    private lateinit var credentialManager: CredentialManager
    // [END declare_credential_manager]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // [START initialize_credential_manager]
        // Initialize Credential Manager
        credentialManager = CredentialManager.create(baseContext)
        // [END initialize_credential_manager]

        launchCredentialManager()
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    // [END on_start_check_user]


    private fun launchCredentialManager() {
        // [START create_credential_manager_request]
        // Instantiate a Google sign-in request
        val googleIdOption = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(true)
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(Strings.Key.DEFAULT_WEB_CLIENT_ID)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        // [END create_credential_manager_request]

        lifecycleScope.launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    context = baseContext,
                    request = request
                )

                // Extract credential from the result returned by Credential Manager
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    // [START handle_sign_in]
    private fun handleSignIn(credential: Credential) {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }
    // [END handle_sign_in]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    // [START sign_out]
    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // When a user signs out, clear the current user credential state from all credential providers.
        lifecycleScope.launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
                updateUI(null)
            } catch (e: ClearCredentialException) {
                Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
    }
    // [END sign_out]

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // 로그인 성공 시 MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // 로그인 실패/로그아웃
            Toast.makeText(this, Strings.Errors.INVALID_LOGIN, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    companion object {
        private const val TAG = "GoogleActivity"
    }
}