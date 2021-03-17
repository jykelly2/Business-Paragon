package sheridan.yamazaki.businessparagon.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import sheridan.yamazaki.businessparagon.databinding.FragmentLoginBinding
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.TextView
import android.content.Intent
import android.util.Log
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.BusinessActivity
import sheridan.yamazaki.businessparagon.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sheridan.yamazaki.businessparagon.model.Business

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: UserViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_baseline_email_24,
            0,
            0,
            0
        )
        binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_baseline_keyboard_24,
            0,
            0,
            0
        )


        binding.email.addTextChangedListener(loginTextWatcher)
        binding.password.addTextChangedListener(loginTextWatcher)


        val text = "Don't have an account? Sign up"
        val spannableString = SpannableString(text)
        val signUpFragment = SignUpFragment()

        val signUpClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_login_to_signup)
                //  Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
            }
        }
        spannableString.setSpan(signUpClickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signUp.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.signUp.movementMethod = LinkMovementMethod.getInstance()

        binding.loginButton.setOnClickListener { loginClicked() }

        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    var drawable = resources.getDrawable(
                        R.drawable.ic_baseline_email_24,
                        resources.newTheme()
                    ) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable,
                        resources.getColor(R.color.colordarkblue, resources.newTheme())
                    ) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_baseline_email_24,
                            resources.newTheme()
                        ),
                        null,
                        resources.getDrawable(
                            R.drawable.ic_baseline_cancel_24,
                            resources.newTheme()
                        ),
                        null
                    )
                } else if (s.isEmpty()) {
                    binding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_baseline_email_24,
                        0, 0, 0
                    )
                    var drawable = resources.getDrawable(
                        R.drawable.ic_baseline_email_24,
                        resources.newTheme()
                    ) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable,
                        resources.getColor(R.color.colorDefault, resources.newTheme())
                    ) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_baseline_email_24,
                            resources.newTheme()
                        ),
                        null, null, null
                    )
                }
            }
        })

        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    var drawable = resources.getDrawable(
                        R.drawable.ic_baseline_keyboard_24,
                        resources.newTheme()
                    ) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable,
                        resources.getColor(R.color.colordarkblue, resources.newTheme())
                    ) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_baseline_keyboard_24,
                            resources.newTheme()
                        ),
                        null,
                        resources.getDrawable(
                            R.drawable.ic_baseline_cancel_24,
                            resources.newTheme()
                        ),
                        null
                    )
                } else if (s.isEmpty()) {
                    binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_baseline_keyboard_24,
                        0, 0, 0
                    )
                    var drawable = resources.getDrawable(
                        R.drawable.ic_baseline_keyboard_24,
                        resources.newTheme()
                    ) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable,
                        resources.getColor(R.color.colorDefault, resources.newTheme())
                    ) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_baseline_keyboard_24,
                            resources.newTheme()
                        ),
                        null, null, null
                    )
                }
            }
        })

        binding.email.setOnTouchListener(View.OnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                if (binding.email.compoundDrawables[2] != null) {
                    if (event.x >= binding.email.right - binding.email.left -
                        binding.email.compoundDrawables[2].bounds.width()
                    ) {
                        if (binding.email.text.toString() != "") {
                            binding.email.setText("")
                        }
                    }
                }
            }
            false
        })

        binding.password.setOnTouchListener(View.OnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                if (binding.password.compoundDrawables[2] != null) {
                    if (event.x >= binding.password.right - binding.password.left -
                        binding.password.compoundDrawables[2].bounds.width()
                    ) {
                        if (binding.password.text.toString() != "") {
                            binding.password.setText("")
                        }
                    }
                }
            }
            false
        })
        return binding.root
    }

    private val loginTextWatcher: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            if (validateInput()) {
                binding.loginButton.setBackgroundResource(R.color.colordarkblue)
                binding.loginButton.isEnabled = true
            } else {
                binding.loginButton.isEnabled = false
                binding.loginButton.setBackgroundResource(R.color.colorwhiteblueshade)
            }
        }
    }

    private fun loginClicked() {
        signIn(binding.email.text.toString().trim(), binding.password.text.toString().trim())
    }

    fun validateInput(): Boolean{
        val mEmail: String = binding.email.text.toString().trim()
        val mPassword: String = binding.password.text.toString().trim()
        return  mEmail.isNotEmpty() && mPassword.isNotEmpty()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startBusinessActivity()
        }

        if (validateInput()) {
            binding.loginButton.setBackgroundResource(R.color.colordarkblue)
        } else {
            binding.loginButton.setBackgroundResource(R.color.colorwhiteblueshade)
        }
    }


    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("jugga", "signInWithEmail:success")
                        val user = auth.currentUser
                        startBusinessActivity()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("jugga", "signInWithEmail:failure", task.exception)
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                    }
                }
    }

    private fun startBusinessActivity(){
        requireActivity().run {
            startActivity(Intent(this, BusinessActivity::class.java))
            finish()
        }
    }
}
