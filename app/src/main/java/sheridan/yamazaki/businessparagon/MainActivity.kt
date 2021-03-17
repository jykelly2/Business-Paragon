package sheridan.yamazaki.businessparagon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.firestore.FirestoreActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setSupportActionBar(findViewById(R.id.toolbar))
    }
}

   // private lateinit var toolbar: Toolbar

  /*
   private lateinit var binding: ActivityMainBinding
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
        val signUpFragment = SignUpFragment()

        val signUpClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                //findNavController().navigate(R.id.action_input_to_output)
                if(savedInstanceState == null) {
                    // initial transaction should be wrapped like this
                    binding.loginButton.isEnabled = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.root_container, signUpFragment)
                        .commitAllowingStateLoss()
                }
              //  Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
            }
        }
        spannableString.setSpan(signUpClickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signUp.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.signUp.movementMethod = LinkMovementMethod.getInstance()

        binding.loginButton.setOnClickListener {loginClicked()}

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
                binding.loginButton.isEnabled = true
            }
            else
            {
                binding.loginButton.isEnabled = false
                binding.loginButton.setBackgroundResource(R.color.colorwhiteblueshade)
            }
        }
    }

    private fun loginClicked(){
        Log.d("userjj", "here")
        if (binding.email.text.toString().trim() == "test" && binding.password.text.toString().trim() == "test"){
            val intent = Intent(this, BrowseActivity::class.java)
          //  intent.putExtra(DOG_INFO, dogInfo)
            startActivity(intent)
        }else{
            Toast.makeText(this@MainActivity, "User doesn't exist! Please change username or password", Toast.LENGTH_SHORT).show()
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
}*/