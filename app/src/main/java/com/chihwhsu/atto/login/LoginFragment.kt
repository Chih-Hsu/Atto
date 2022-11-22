package com.chihwhsu.atto.login


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.TutorialNavigationDirections
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel

    companion object {
        const val REQUEST_CODE_SIGN_IN = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        auth = Firebase.auth

        if (auth.currentUser != null) {
            findNavController().navigate(TutorialNavigationDirections.actionGlobalAfterLoginFragment())
        }

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(requireActivity(), options)

        binding.buttonSignIn.setOnClickListener {
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
            }
        }

        binding.buttonGuestLogin.setOnClickListener {
            this.findNavController()
                .navigate(TutorialNavigationDirections.actionGlobalWallpaperFragment())
        }

        viewModel.navigateToSync.observe(viewLifecycleOwner, Observer { userSignIn ->
            if (userSignIn) {
                findNavController().navigate(TutorialNavigationDirections.actionGlobalAfterLoginFragment())
            }
        })

        return binding.root
    }


    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.Default).launch {

            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Successfully Logged in", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {

                googleAuthForFirebase(it)
//                UserManager.userEmail = it.email
                val deviceId = Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                val user = User(it.id, it.email, it.displayName, it.photoUrl.toString(), deviceId)
                viewModel.uploadUser(user)
            }
        }


    }


}