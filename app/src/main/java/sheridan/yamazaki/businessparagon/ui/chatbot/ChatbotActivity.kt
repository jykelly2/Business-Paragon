package sheridan.yamazaki.businessparagon.ui.chatbot

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibm.cloud.sdk.core.http.HttpMediaType
import com.ibm.cloud.sdk.core.http.Response
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.assistant.v2.Assistant
import com.ibm.watson.assistant.v2.model.CreateSessionOptions
import com.ibm.watson.assistant.v2.model.MessageInput
import com.ibm.watson.assistant.v2.model.MessageOptions
import com.ibm.watson.assistant.v2.model.SessionResponse
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType
import com.ibm.watson.speech_to_text.v1.SpeechToText
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions
import sheridan.yamazaki.businessparagon.BusinessActivity
import sheridan.yamazaki.businessparagon.MainActivity
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.ui.business.SettingsFragment
import java.io.InputStream
import java.util.*

class ChatbotActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ChatAdapter? = null
    private var messageArrayList: ArrayList<Message>? = null
    private var inputMessage: EditText? = null
    private var btnSend: ImageButton? = null
    private var btnRecord: ImageButton? = null
    var streamPlayer = StreamPlayer()
    private var initialRequest = false
    private var permissionToRecordAccepted = false
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private val TAG = "ChatbotActivity"
    private val RECORD_REQUEST_CODE = 101
    private var listening = false
    private var capture: MicrophoneInputStream? = null
    private var mContext: Context? = null
    private var microphoneHelper: MicrophoneHelper? = null
    private var watsonAssistant: Assistant? = null
    private var watsonAssistantSession: Response<SessionResponse>? = null
    private var speechService: SpeechToText? = null
    private var textToSpeech: TextToSpeech? = null
    private fun createServices() {
        watsonAssistant = Assistant(
                "2019-02-28", IamAuthenticator(
                mContext!!.getString(R.string.assistant_apikey)
        )
        )
        watsonAssistant!!.serviceUrl = mContext!!.getString(R.string.assistant_url)
       // textToSpeech = TextToSpeech(IamAuthenticator(mContext!!.getString(R.string.TTS_apikey)))
        //textToSpeech!!.serviceUrl = mContext!!.getString(R.string.TTS_url)
        speechService = SpeechToText(IamAuthenticator(mContext!!.getString(R.string.STT_apikey)))
        speechService!!.serviceUrl = mContext!!.getString(R.string.STT_url)
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         supportActionBar?.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_chatbot)
        mContext = applicationContext
        inputMessage = findViewById(R.id.message)
        btnSend = findViewById(R.id.btn_send)
        btnRecord = findViewById(R.id.btn_record)
        val customFont = "Montserrat-Regular.ttf"
        val typeface = Typeface.createFromAsset(assets, customFont)
        inputMessage!!.setTypeface(typeface)
        recyclerView = findViewById(R.id.recycler_view)
        messageArrayList = ArrayList<Message>()
        mAdapter = ChatAdapter(messageArrayList as ArrayList<Message>)
        microphoneHelper = MicrophoneHelper(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
         recyclerView?.layoutManager = layoutManager
         recyclerView?.itemAnimator = DefaultItemAnimator()
         recyclerView?.adapter = mAdapter
        inputMessage!!.setText("")
        initialRequest = true
        val permission: Int = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(
                    sheridan.yamazaki.businessparagon.ui.chatbot.ChatbotActivity.Companion.TAG,
                    "Permission to record denied"
            )
            makeRequest()
        } else {
            Log.i(
                    sheridan.yamazaki.businessparagon.ui.chatbot.ChatbotActivity.Companion.TAG,
                    "Permission to record was already granted"
            )
        }
        recyclerView?.addOnItemTouchListener(
                RecyclerTouchListener(
                        applicationContext,
                        recyclerView!!,
                        object : ClickListener {
                            override fun onClick(view: View?, position: Int) {
                                val audioMessage: Message = (messageArrayList)?.get(position) as Message
                                if (audioMessage != null && !audioMessage.message?.isEmpty()!!) {
                                   // SayTask().execute(audioMessage.message)
                                }
                            }

                            override fun onLongClick(view: View?, position: Int) {
                                recordMessage()
                            }
                        })
        )
        btnSend!!.setOnClickListener {
            if (checkInternetConnection()) {
                sendMessage()
            }
        }
        btnRecord!!.setOnClickListener { recordMessage() }
        createServices()
        sendMessage()
    }
    override fun onSupportNavigateUp(): Boolean {
        startFragment()
        return true
    }

    private fun startFragment() {
        val intent = Intent(this, BusinessActivity::class.java)
        intent.putExtra("chatbot","explore")
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Speech-to-Text Record Audio permission
    public fun onRequestPermissionsResult(
            requestCode: Int,
            @NonNull permissions: Array<String?>?,
            @NonNull grantResults: IntArray
    ) : Void? {
        if (permissions != null) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        when (requestCode) {
            sheridan.yamazaki.businessparagon.ui.chatbot.ChatbotActivity.Companion.REQUEST_RECORD_AUDIO_PERMISSION -> permissionToRecordAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
            sheridan.yamazaki.businessparagon.ui.chatbot.ChatbotActivity.Companion.RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty()
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i(
                            sheridan.yamazaki.businessparagon.ui.chatbot.ChatbotActivity.Companion.TAG,
                            "Permission has been denied by user"
                    )
                } else {
                    Log.i(
                            sheridan.yamazaki.businessparagon.ui.chatbot.ChatbotActivity.Companion.TAG,
                            "Permission has been granted by user"
                    )
                }
                return null
            }
            MicrophoneHelper.REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission to record audio denied", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
        return null
        // if (!permissionToRecordAccepted ) finish();
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                MicrophoneHelper.REQUEST_PERMISSION
        )
    }

    // Sending a message to Watson Assistant Service
    private fun sendMessage() {
        val inputmessage = inputMessage!!.text.toString().trim { it <= ' ' }
        if (!initialRequest) {
            val inputMessage = Message()
            inputMessage.message = inputmessage
            inputMessage.id = "1"
            messageArrayList!!.add(inputMessage)
        } else {
            val inputMessage = Message()
            inputMessage.message = inputmessage
            inputMessage.id = "100"
            initialRequest = false
            Toast.makeText(
                    applicationContext,
                    "Tap on the message for Voice",
                    Toast.LENGTH_LONG
            ).show()
        }
        inputMessage!!.setText("")
        mAdapter?.notifyDataSetChanged()
        val thread = Thread {
            try {
                if (watsonAssistantSession == null) {
                    val call = watsonAssistant!!.createSession(
                            CreateSessionOptions.Builder().assistantId(
                                    mContext!!.getString(R.string.assistant_id)
                            ).build()
                    )
                    watsonAssistantSession = call.execute()
                }
                val input = MessageInput.Builder()
                    .text(inputmessage)
                    .build()
                val options = MessageOptions.Builder()
                    .assistantId(mContext!!.getString(R.string.assistant_id))
                    .input(input)
                    .sessionId(watsonAssistantSession!!.result.sessionId)
                    .build()
                val response = watsonAssistant!!.message(options).execute()
                Log.d(
                        "chatbot response",
                        "run: " + response!!.result
                )
                if (response != null && response.result.output != null &&
                        response.result.output.generic.isNotEmpty()
                ) {
                    val responses = response.result.output.generic
                    for (r in responses) {
                        var outMessage: Message
                        when (r.responseType()) {
                            "text" -> {
                                outMessage = Message()
                                outMessage.message = r.text()
                                outMessage.id = "2"
                                messageArrayList!!.add(outMessage)

                                // speak the message
                                //SayTask().execute(outMessage.message)
                            }
                            "option" -> {
                                outMessage = Message()
                                val title = r.title()
                                var OptionsOutput = ""
                                var i = 0
                                while (i < r.options().size) {
                                    val option = r.options()[i]
                                    OptionsOutput = """
                                          $OptionsOutput${option.label}
                                          
                                          """.trimIndent()
                                    i++
                                }
                                outMessage.message =
                                        """
                                          $title
                                          $OptionsOutput
                                          """.trimIndent()

                                outMessage.id = "2"
                                messageArrayList!!.add(outMessage)

                                // speak the message
                                //SayTask().execute(outMessage.message)
                            }
                            "image" -> {
                                outMessage = Message(r)
                                messageArrayList!!.add(outMessage)

                                // speak the description
                               // SayTask().execute("You received an image: " + outMessage.title + outMessage.description)
                            }
                            else -> Log.e("Error", "Unhandled message type")
                        }
                    }
                    runOnUiThread(Runnable {
                        mAdapter?.notifyDataSetChanged()
                        if (mAdapter?.itemCount!! > 1) {
                            recyclerView?.layoutManager?.smoothScrollToPosition(
                                    recyclerView,
                                    null,
                                    mAdapter!!.itemCount - 1
                            )
                        }
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    //Record a message via Watson Speech to Text
    private fun recordMessage() {
        if (!listening) {
            capture = microphoneHelper!!.getInputStream(true)
            Thread {
                try {
                    speechService!!.recognizeUsingWebSocket(
                            getRecognizeOptions(capture),
                            MicrophoneRecognizeDelegate()
                    )
                } catch (e: Exception) {
                    showError(e)
                }
            }.start()
            listening = true
            Toast.makeText(this@ChatbotActivity, "Listening....Click to Stop", Toast.LENGTH_LONG)
                .show()
            Log.d("jugga", capture.toString())
        } else {
            try {
                microphoneHelper!!.closeInputStream()
                listening = false
                Toast.makeText(
                        this@ChatbotActivity,
                        "Stopped Listening....Click to Start",
                        Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Check Internet Connection
     *
     * @return
     */
    private fun checkInternetConnection(): Boolean {
        // get Connectivity Manager object to check connection
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting

        // Check for network connections
        return if (isConnected) {
            true
        } else {
            Toast.makeText(this, " No Internet Connection available ", Toast.LENGTH_LONG).show()
            false
        }
    }

    //Private Methods - Speech to Text
    private fun getRecognizeOptions(audio: InputStream?): RecognizeOptions {
        return RecognizeOptions.Builder()
            .audio(audio)
            .contentType(ContentType.OPUS.toString())
            .model("en-US_BroadbandModel")
            .interimResults(true)
            .inactivityTimeout(2000)
            .build()
    }

    private fun showMicText(text: String) {
        runOnUiThread(Runnable { inputMessage!!.setText(text) })
    }

    private fun enableMicButton() {
        runOnUiThread(Runnable { btnRecord!!.isEnabled = true })
    }

    private fun showError(e: Exception) {
        runOnUiThread(Runnable {
            Toast.makeText(this@ChatbotActivity, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        })
    }

//    private inner class SayTask :
//        AsyncTask<String?, Void?, String>() {
//        protected override fun doInBackground(vararg params: String?): String? {
//            streamPlayer.playStream(
//                    textToSpeech!!.synthesize(
//                            SynthesizeOptions.Builder()
//                                    .text(params[0])
//                                    .voice(SynthesizeOptions.Voice.EN_US_OLIVIAV3VOICE)
//                                    .accept(HttpMediaType.AUDIO_WAV)
//                                    .build()
//                    ).execute().result
//            )
//            return "Did synthesize"
//        }
//    }

    //Watson Speech to Text Methods.
    private inner class MicrophoneRecognizeDelegate : BaseRecognizeCallback() {
        override fun onTranscription(speechResults: SpeechRecognitionResults) {
            if (speechResults.results != null && speechResults.results.isNotEmpty()) {
                val text = speechResults.results[0].alternatives[0].transcript
                showMicText(text)
            }
        }

        override fun onError(e: Exception) {
            showError(e)
            enableMicButton()
        }

        override fun onDisconnected() {
            enableMicButton()
        }
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private const val TAG = "ChatbotActivity"
        private const val RECORD_REQUEST_CODE = 101
    }
}