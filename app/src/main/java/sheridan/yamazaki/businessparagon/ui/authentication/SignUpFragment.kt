package sheridan.yamazaki.businessparagon.ui.authentication

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sheridan.yamazaki.businessparagon.databinding.FragmentSignupBinding
import android.util.Log
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
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import sheridan.yamazaki.businessparagon.BusinessActivity
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class SignUpFragment : Fragment(){

    private lateinit var binding: FragmentSignupBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.username.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_person_24, 0, 0, 0)
        binding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_email_24, 0, 0, 0)
        binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_keyboard_24, 0, 0, 0)
        binding.phoneNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_local_phone_24, 0, 0, 0)
        binding.address.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_home_24, 0, 0, 0)

        binding.username.addTextChangedListener(loginTextWatcher)
        binding.email.addTextChangedListener(loginTextWatcher)
        binding.password.addTextChangedListener(loginTextWatcher)
        binding.phoneNumber.addTextChangedListener(loginTextWatcher)
        binding.address.addTextChangedListener(loginTextWatcher)

        binding.signUpButton.setOnClickListener {signUpClicked()}

        val text = "By signing up, you agree with the \nTerms of Service & Privacy Policy"
        val spannableString = SpannableString(text)

        val termsClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
              Log.d("termss", "terms")
            }
        }

        val privacyClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.d("termss", "privacy")
            }
        }

        spannableString.setSpan(termsClickableSpan, 35, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(privacyClickableSpan, 54, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.agreementText.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.agreementText.movementMethod = LinkMovementMethod.getInstance()

         binding.username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNotEmpty())
                {
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_person_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colordarkblue, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.username.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.username.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_person_24, resources.newTheme()),
                        null, resources.getDrawable(R.drawable.ic_baseline_cancel_24, resources.newTheme()), null)
                }
                else if (s.isEmpty())
                {
                    binding.username.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_person_24,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_person_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.username.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.username.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.ic_baseline_person_24, resources.newTheme()),
                        null, null, null
                    )
                }
            }
        })

        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNotEmpty())
                {
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_email_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colordarkblue, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_email_24, resources.newTheme()),
                        null, resources.getDrawable(R.drawable.ic_baseline_cancel_24, resources.newTheme()), null)
                }
                else if (s.isEmpty())
                {
                    binding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_email_24,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_email_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.ic_baseline_email_24, resources.newTheme()),
                        null, null, null
                    )
                }
            }
        })

        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNotEmpty())
                {
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_keyboard_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colordarkblue, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_keyboard_24, resources.newTheme()),
                        null, resources.getDrawable(R.drawable.ic_baseline_cancel_24, resources.newTheme()), null)
                }
                else if (s.isEmpty())
                {
                    binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_keyboard_24,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_keyboard_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_keyboard_24, resources.newTheme()),
                        null, null, null
                    )
                }
            }
        })

        binding.phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNotEmpty())
                {
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_local_phone_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colordarkblue, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.phoneNumber.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.phoneNumber.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_local_phone_24, resources.newTheme()),
                        null, resources.getDrawable(R.drawable.ic_baseline_cancel_24, resources.newTheme()), null)
                }
                else if (s.isEmpty())
                {
                    binding.phoneNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_local_phone_24,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_local_phone_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.phoneNumber.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.phoneNumber.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.ic_baseline_local_phone_24, resources.newTheme()),
                        null, null, null
                    )
                }
            }
        })

        binding.address.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNotEmpty())
                {
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_home_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colordarkblue, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.address.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.address.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_home_24, resources.newTheme()),
                        null, resources.getDrawable(R.drawable.ic_baseline_cancel_24, resources.newTheme()), null)
                }
                else if (s.isEmpty())
                {
                    binding.address.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_home_24,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_home_24, resources.newTheme()) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault, resources.newTheme())) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.address.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.address.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.ic_baseline_home_24, resources.newTheme()),
                        null, null, null
                    )
                }
            }
        })

        binding.username.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
            {
                if (binding.username.compoundDrawables[2] != null)
                {
                    if (event.x >= binding.username.right - binding.username.left -
                        binding.username.compoundDrawables[2].bounds.width())
                    {
                        if (binding.username.text.toString() != "")
                        {
                            binding.username.setText("")
                        }
                    }
                }
            }
            false
        })

        binding.email.setOnTouchListener(View.OnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_DOWN)
            {
                if (binding.email.compoundDrawables[2] != null)
                {
                    if (event.x >= binding.email.right - binding.email.left -
                        binding.email.compoundDrawables[2].bounds.width())
                    {
                        if (binding.email.text.toString() != "")
                        {
                            binding.email.setText("")
                        }
                    }
                }
            }
            false
        })

        binding.password.setOnTouchListener(View.OnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_DOWN)
            {
                if (binding.password.compoundDrawables[2] != null)
                {
                    if (event.x >= binding.password.right - binding.password.left -
                        binding.password.compoundDrawables[2].bounds.width()
                    )
                    {
                        if (binding.password.text.toString() != "")
                        {
                            binding.password.setText("")
                        }
                    }
                }
            }
            false
        })

        binding.phoneNumber.setOnTouchListener(View.OnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_DOWN)
            {
                if (binding.phoneNumber.compoundDrawables[2] != null)
                {
                    if (event.x >= binding.phoneNumber.right - binding.phoneNumber.left -
                        binding.phoneNumber.compoundDrawables[2].bounds.width()
                    )
                    {
                        if (binding.phoneNumber.text.toString() != "")
                        {
                            binding.phoneNumber.setText("")
                        }
                    }
                }
            }
            false
        })

        binding.address.setOnTouchListener(View.OnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_DOWN)
            {
                if (binding.address.compoundDrawables[2] != null)
                {
                    if (event.x >= binding.address.right - binding.address.left -
                        binding.address.compoundDrawables[2].bounds.width()
                    )
                    {
                        if (binding.address.text.toString() != "")
                        {
                            binding.address.setText("")
                        }
                    }
                }
            }
            false
        })
        return binding.root
    }

    private val loginTextWatcher: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
        {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
        {
        }

        override fun afterTextChanged(s: Editable)
        {
            if (validateInput())
            {
                binding.signUpButton.setBackgroundResource(R.color.colordarkblue)
                binding.signUpButton.isEnabled = true
            }
            else
            {
                binding.signUpButton.isEnabled = false
                binding.signUpButton.setBackgroundResource(R.color.colorwhiteblueshade)
            }

        }
    }

    private fun signUpClicked(){
        if (validateInput()) {
            val user = createUserObj()
            requireActivity().run {
                startActivity(Intent(this, BusinessActivity::class.java))
                finish()
            }
        }else{
            Toast.makeText(
                getActivity(),
                "Please fill out all the inputs!",
                Toast.LENGTH_SHORT
            ).show()
        }
        //findNavController().navigate(R.id.action_signup_to_browse)
    }

    fun validateInput(): Boolean{
        val mUsername: String = binding.username.text.toString().trim()
        val mEmail: String = binding.email.text.toString().trim()
        val mPassword: String = binding.password.text.toString().trim()
        val mPhoneNumber: String = binding.phoneNumber.text.toString().trim()
        val mAddress: String = binding.address.text.toString().trim()
        return mUsername.isNotEmpty() && mEmail.isNotEmpty() && mPassword.isNotEmpty() && mPhoneNumber.isNotEmpty() && mAddress.isNotEmpty()
    }

    private fun createUserObj(): User {
        val mUsername: String = binding.username.text.toString().trim()
        val mEmail: String = binding.email.text.toString().trim()
        val mPassword: String = binding.password.text.toString().trim()
        val mPhoneNumber: String = binding.phoneNumber.text.toString().trim()
        val mAddress: String = binding.address.text.toString().trim()

        return User(mUsername, mEmail, mPassword, mPhoneNumber, mAddress, "", 0,0, Date())
    }

    override fun onStart()
    {
        super.onStart()
        if (validateInput())
        {
            binding.signUpButton.setBackgroundResource(R.color.colordarkblue)
        }
        else
        {
            binding.signUpButton.setBackgroundResource(R.color.colorwhiteblueshade)
        }
    }

}