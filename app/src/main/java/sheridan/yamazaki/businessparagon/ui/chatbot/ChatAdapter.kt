package sheridan.yamazaki.businessparagon.ui.chatbot

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.chat_item_watson.view.*
import sheridan.yamazaki.businessparagon.R
import java.util.*

/**
 * Created by VMac on 17/11/16.
 */

class ChatAdapter(messageArrayList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var activity: Activity? = null
    private val SELF = 100
    private val messageArrayList: ArrayList<Message> = messageArrayList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // view type is to identify where to render the chat message
        // left or right
        val itemView: View = if (viewType == SELF) {
            // self message
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_self, parent, false)
        } else {
            // WatBot message
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_watson, parent, false)
        }
        return ViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        val message: Message = messageArrayList[position]

        return if (message.id != null && message.id.equals("1")) {
            SELF
        } else position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Message = messageArrayList[position]
        when (message.type) {
            Message.Type.TEXT -> (holder as sheridan.yamazaki.businessparagon.ui.chatbot.ChatAdapter.ViewHolder).message.setText(
                    message.message
            )
            Message.Type.IMAGE -> {
                (holder as sheridan.yamazaki.businessparagon.ui.chatbot.ChatAdapter.ViewHolder).message.setVisibility(
                        View.GONE
                )
                val iv: ImageView = (holder as sheridan.yamazaki.businessparagon.ui.chatbot.ChatAdapter.ViewHolder).image
                Glide
                        .with(iv.context)
                        .load(message.url)
                        .into(iv)
            }
        }
    }

     override fun getItemCount(): Int {
        return messageArrayList.size
    }
    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var message: TextView = itemView.findViewById(R.id.message) as TextView
        var image: ImageView = itemView?.image

        init {
            //TODO: Uncomment this if you want to use a custom Font
            /*String customFont = "Montserrat-Regular.ttf";
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), customFont);
            message.setTypeface(typeface);*/
        }
    }
}