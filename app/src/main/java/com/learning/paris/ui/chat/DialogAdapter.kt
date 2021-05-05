package com.learning.paris.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.learning.paris.R
import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption

class BotMessageViewHolder(itemView: ConstraintLayout): RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById(R.id.titleView)
    private val bodyView: TextView = itemView.findViewById(R.id.bodyView)

    fun bind(bot: Bot, message: Message) {
        titleView.text = bot.name
        bodyView.text = message.text
    }
}

class PlayerMessageViewHolder(itemView: ConstraintLayout): RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById(R.id.titleView)
    private val bodyView: TextView = itemView.findViewById(R.id.bodyView)

    fun bind(message: Message) {
        titleView.text = titleView.context.getText(R.string.me)
        bodyView.text = message.text
    }

}

class ResponseOptionViewHolder(itemView: ConstraintLayout, onClick: (ResponseOption) -> Unit):
    RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById(R.id.titleView)
    private val bodyView: TextView = itemView.findViewById(R.id.bodyView)
    private lateinit var data: ResponseOption
    init {
        itemView.setOnClickListener {
            onClick(data)
        }
    }

    fun bind(responseOption: ResponseOption) {
        data = responseOption
        titleView.text = responseOption.index.toString()
        bodyView.text = responseOption.text
    }

}

class DialogAdapter(
    private val bot: Bot,
    private val messages: List<Message>,
    private val responseOptions: List<ResponseOption>,
    private val onResponseSelected: (ResponseOption) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val BOT_MESSAGE_TYPE = 0
        const val PLAYER_MESSAGE_TYPE = 1
        const val RESPONSE_OPTION_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            BOT_MESSAGE_TYPE -> {
                val itemView = inflater.inflate(R.layout.bot_message, parent, false)
                BotMessageViewHolder(itemView as ConstraintLayout)
            }
            PLAYER_MESSAGE_TYPE -> {
                val itemView = inflater.inflate(R.layout.player_message, parent, false)
                PlayerMessageViewHolder(itemView as ConstraintLayout)
            }
            RESPONSE_OPTION_TYPE -> {
                val itemView = inflater.inflate(R.layout.response_option, parent, false)
                ResponseOptionViewHolder(itemView as ConstraintLayout, onResponseSelected)
            }
            else -> throw IllegalStateException("Unknown item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BotMessageViewHolder -> {
                val message = messages[position]
                holder.bind(bot, message)
            }
            is PlayerMessageViewHolder -> {
                val message = messages[position]
                holder.bind(message)
            }
            is ResponseOptionViewHolder -> {
                holder.bind(responseOptions[position - messages.size])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= messages.size) {
            RESPONSE_OPTION_TYPE
        } else {
            if (messages[position].senderId == 0) {
                PLAYER_MESSAGE_TYPE
            } else {
                BOT_MESSAGE_TYPE
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size + responseOptions.size
    }
}