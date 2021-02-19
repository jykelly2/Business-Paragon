package sheridan.yamazaki.businessparagon

import android.util.Log
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.annotation.SuppressLint
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import sheridan.yamazaki.businessparagon.databinding.ActivityMainBinding
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
   // private lateinit var toolbar: Toolbar

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  toolbar = findViewById(R.id.toolbar_login)
       // setSupportActionBar(binding.toolbarLogin)
        supportActionBar?.hide()

        binding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_email_24, 0, 0, 0)
        binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_keyboard_24, 0, 0, 0)

        binding.email.addTextChangedListener(loginTextWatcher)
        binding.password.addTextChangedListener(loginTextWatcher)


        val text = "Don't have an account? Sign up"
        val spannableString = SpannableString(text)

        val clickableSpan3: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
            }
        }
        spannableString.setSpan(clickableSpan3, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signUp.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.signUp.movementMethod = LinkMovementMethod.getInstance()


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
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_email_24, theme ) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colordarkblue, theme)) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_email_24, theme),
                        null, resources.getDrawable(R.drawable.ic_baseline_cancel_24,theme), null)
                }
                else if (s.isEmpty())
                {
                    binding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_email_24,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_email_24, theme) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault, theme)) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.email.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.ic_baseline_email_24, theme),
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
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_keyboard_24, theme) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colordarkblue, theme)) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_keyboard_24, theme),
                        null, resources.getDrawable(R.drawable.ic_baseline_cancel_24, theme), null)
                }
                else if (s.isEmpty())
                {
                    binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_keyboard_24,
                        0, 0, 0)
                    var drawable = resources.getDrawable(R.drawable.ic_baseline_keyboard_24, theme) //Your drawable image
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.colorDefault, theme)) // Set whatever color you want
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    binding.password.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_keyboard_24, theme),
                        null, null, null
                    )
                }
            }
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

      /*  binding.rememberPassword.setOnClickListener(View.OnClickListener {

            if (!(binding.rememberPassword.isSelected)) {
                binding.rememberPassword.isChecked = true
                binding.rememberPassword.isSelected = true
            } else {
                binding.rememberPassword.isChecked = false
                binding.rememberPassword.isSelected = false
            }
        })*/
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
            val mUsername: String = binding.email.text.toString().trim()
            val mPassword: String = binding.password.text.toString().trim()
            //Log.d("user", mUsername)
          //  Log.d("user", mPassword)
            val t = mUsername.isNotEmpty() && mPassword.isNotEmpty()
            if (t)
            {
                binding.loginButton.setBackgroundResource(R.color.colordarkblue)
            }
            else
            {
                binding.loginButton.setBackgroundResource(R.color.colorwhiteblueshade)
            }

        }
    }

    override fun onStart()
    {
        super.onStart()
        val mUsername: String = binding.email.text.toString().trim()
        val mPassword: String = binding.password.text.toString().trim()
        val t = mUsername.isNotEmpty() && mPassword.isNotEmpty()
        if (t)
        {
            binding.loginButton.setBackgroundResource(R.color.colordarkblue)
        }
        else
        {
            binding.loginButton.setBackgroundResource(R.color.colorwhiteblueshade)
        }
    }
}