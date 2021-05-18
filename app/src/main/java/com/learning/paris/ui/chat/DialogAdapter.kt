package com.learning.paris.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.learning.paris.R
import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption

class BotMessageViewHolder(itemView: CardView): RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById(R.id.titleView)
    private val bodyView: TextView = itemView.findViewById(R.id.bodyView)

    fun bind(bot: Bot, message: Message) {
        titleView.text = bot.name
        bodyView.text = message.text
    }
}

class PlayerMessageViewHolder(itemView: CardView): RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById(R.id.titleView)
    private val bodyView: TextView = itemView.findViewById(R.id.bodyView)

    fun bind(message: Message) {
        titleView.text = titleView.context.getText(R.string.me)
        bodyView.text = message.text
    }
}

class ResponseOptionViewHolder(itemView: CardView, onClick: (ResponseOption) -> Unit):
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

sealed class DialogItem {
    class MessageItem(val message: Message) : DialogItem()
    class ResponseItem(val response: ResponseOption) : DialogItem()
}

class DialogAdapter(
    private val onResponseSelected: (ResponseOption) -> Unit
) : ListAdapter<DialogItem, RecyclerView.ViewHolder>(diffCallback) {
    lateinit var bot: Bot
    companion object {
        const val BOT_MESSAGE_TYPE = 0
        const val PLAYER_MESSAGE_TYPE = 1
        const val RESPONSE_OPTION_TYPE = 2
        val diffCallback = object : DiffUtil.ItemCallback<DialogItem>() {
            override fun areItemsTheSame(oldItem: DialogItem, newItem: DialogItem): Boolean {
                return when (oldItem) {
                    is DialogItem.MessageItem -> {
                        when (newItem) {
                            is DialogItem.ResponseItem -> false
                            is DialogItem.MessageItem -> oldItem.message.id == newItem.message.id
                        }
                    }
                    is DialogItem.ResponseItem -> {
                        when (newItem) {
                            is DialogItem.MessageItem -> false
                            is DialogItem.ResponseItem -> oldItem.response.id == newItem.response.id
                        }
                    }
                }
            }

            override fun areContentsTheSame(oldItem: DialogItem, newItem: DialogItem): Boolean {
                return when (oldItem) {
                    is DialogItem.MessageItem -> {
                        when (newItem) {
                            is DialogItem.ResponseItem -> false
                            is DialogItem.MessageItem -> oldItem.message == newItem.message
                        }
                    }
                    is DialogItem.ResponseItem -> {
                        when (newItem) {
                            is DialogItem.MessageItem -> false
                            is DialogItem.ResponseItem -> oldItem.response == newItem.response
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            BOT_MESSAGE_TYPE -> {
                val itemView = inflater.inflate(R.layout.bot_message, parent, false)
                BotMessageViewHolder(itemView as CardView)
            }
            PLAYER_MESSAGE_TYPE -> {
                val itemView = inflater.inflate(R.layout.player_message, parent, false)
                PlayerMessageViewHolder(itemView as CardView)
            }
            RESPONSE_OPTION_TYPE -> {
                val itemView = inflater.inflate(R.layout.response_option, parent, false)
                ResponseOptionViewHolder(itemView as CardView, onResponseSelected)
            }
            else -> throw IllegalStateException("Unknown item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BotMessageViewHolder -> {
                val message = getItem(position) as DialogItem.MessageItem
                holder.bind(bot, message.message)
            }
            is PlayerMessageViewHolder -> {
                val message = getItem(position) as DialogItem.MessageItem
                holder.bind(message.message)
            }
            is ResponseOptionViewHolder -> {
                val response = getItem(position) as DialogItem.ResponseItem
                holder.bind(response.response)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is DialogItem.ResponseItem -> RESPONSE_OPTION_TYPE
            is DialogItem.MessageItem -> if (item.message.senderId == 1) {
                PLAYER_MESSAGE_TYPE
            } else {
                BOT_MESSAGE_TYPE
            }
        }
    }
}