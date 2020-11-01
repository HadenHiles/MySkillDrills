package com.hadenhiles.skilldrills

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.custom_login.*


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123 // woah ohh oah it's magic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // If the user is already signed in then send to the main activity
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        // Initialize the firebase ui intent
        // Email, Google, Facebook
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            //AuthUI.IdpConfig.FacebookBuilder().build()
            // TODO: figure out why Facebook sign in isn't working.
            //  Could be due to a change in the facebook graph api requirements that causes insufficient permissions, disabling for now.
        )

        // Override the firebase ui login layout with my custom layout.
        // Ensure that the button ID is set for every provider that's enabled.
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.custom_login)
            .setGoogleButtonId(R.id.googleSignInButton)
            .setEmailButtonId(R.id.emailSignInButton)
            //.setFacebookButtonId(R.id.facebookSignInButton)
            //.setTosAndPrivacyPolicyId(R.id.baz)
            .build()

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.skilldrills_light_bg)
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAuthMethodPickerLayout(customLayout) // custom_login layout
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    // After the sign in flow is complete, act according to the activity response
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                // Send them to the main activity
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Snackbar.make(this.findViewById(R.id.content), "Invalid Login", Snackbar.LENGTH_SHORT)
            }
        }
    }
}