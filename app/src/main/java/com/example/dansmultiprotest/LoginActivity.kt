package com.example.dansmultiprotest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var loginFacebook: LoginButton
    private lateinit var loginGoogle: SignInButton
    private lateinit var callbackManager: CallbackManager
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("ApiException", "Google sign in failed", e)
            }
        }

    private val facebookSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            callbackManager.onActivityResult(0, result.resultCode, result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginFacebook = findViewById(R.id.facebookLoginButton)
        loginGoogle = findViewById(R.id.googleSignInButton)

        loginFacebook.setPermissions("email", "public_profile")
        loginFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = loginResult.accessToken
                val request = GraphRequest.newMeRequest(accessToken) { jsonObject, response ->
                    jsonObject!!.let {
                        val id = it.getString("id")
                        val name = it.getString("name")
                        val email = it.getString("email")
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                // Handle cancelled login
            }

            override fun onError(error: FacebookException) {
                // Handle error in login
            }
        })
        loginFacebook.setOnClickListener { facebookLogin() }
        loginGoogle.setOnClickListener { googleLogin() }
    }

    private fun facebookLogin() {
        FacebookSdk.sdkInitialize(applicationContext)

        callbackManager = CallbackManager.Factory.create()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun googleLogin() {
// Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("242717759816-bp38baq5ko2udui024emlmj3q0ks3vn1.apps.googleusercontent.com")
            .requestEmail()
            .build()

// Create a GoogleSignInClient object with the options specified by gso.
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("GoogleAuthProvider", "signInWithCredential:success")
                    val user = auth.currentUser
                    // ...
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("GoogleAuthProvider", "signInWithCredential:failure", task.exception)
                    // ...
                }
            }
    }

}