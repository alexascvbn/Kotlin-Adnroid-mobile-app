package com.example.bottonavigation.adapter

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.R
import com.example.bottonavigation.model.ChatMsgModel
import com.example.bottonavigation.util.Util
import io.opencensus.internal.Utils

class MessageListAdapter(mContext : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2
    private val mContext = mContext
    private lateinit var messageList : ArrayList<ChatMsgModel>
    init {
        this.messageList = ArrayList()
    }

    constructor(mContext : Context, messageList : ArrayList<ChatMsgModel>) : this(mContext) {
        this.messageList = messageList
        notifyDataSetChanged()
    }

    override fun getItemViewType(pos: Int): Int {
        val message = messageList[pos]
        return if (message.sender.userId.equals(Util.getCurrentUser().userId)) {
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_SENT
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    fun updateList(messageList : ArrayList<ChatMsgModel>) {
        this.messageList = messageList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
            return  SentMessageHolder(view)
        } else /* if (viewType == VIEW_TYPE_MESSAGE_RECEIVED)*/ {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
            return  ReceivedMessageHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val message : ChatMsgModel = messageList[pos]
        when(holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                if (holder is SentMessageHolder)
                    holder.bind(message)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                if (holder is ReceivedMessageHolder)
                    holder.bind(message)
            }
        }
    }

    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var messageText = itemView.findViewById<TextView>(R.id.text_message_body)
        private var timeText = itemView.findViewById<TextView>(R.id.text_message_time)
        private var nameText = itemView.findViewById<TextView>(R.id.text_message_name)
        private var profileImg = itemView.findViewById<ImageView>(R.id.image_message_profile)

        fun bind(message : ChatMsgModel) {
            messageText.text = message.message
            nameText.text = message.sender.userId
            timeText.text = DateUtils.formatDateTime(mContext, message.createdAt, DateUtils.FORMAT_SHOW_TIME)

//            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }

    }

    inner class SentMessageHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var messageText = itemView.findViewById<TextView>(R.id.message_txt_body)
        private var timeText = itemView.findViewById<TextView>(R.id.message_txt_time)

        fun bind(message : ChatMsgModel) {
            messageText.text = message.message
            timeText.text = DateUtils.formatDateTime(mContext, message.createdAt, DateUtils.FORMAT_SHOW_TIME)
        }
    }
}