package com.learning.paris.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.learning.paris.R
import com.learning.paris.data.Bot
import com.learning.paris.data.ResponseOption
import com.learning.paris.ui.chat.BotMessageViewHolder


class BotNameViewHolder(itemView: ConstraintLayout, onClick: (Bot) -> Unit):
    RecyclerView.ViewHolder(itemView) {
    private val botNameTextView: TextView = itemView.findViewById(R.id.botNameTextView)
    private lateinit var data: Bot
    init {
        itemView.setOnClickListener {
            onClick(data)
        }
    }
    fun bind(bot: Bot) {
        botNameTextView.text = bot.name
    }
}


class BotsAdapter(
    private val listOfBots: List<Bot>,
    private val onBotSelected: (Bot) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.bot_message, parent, false)
        return BotNameViewHolder(itemView as ConstraintLayout, onBotSelected)

    }

    override fun getItemCount(): Int {
        return listOfBots.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BotNameViewHolder -> {
                val bot = listOfBots[position]
                holder.bind(bot)
            }
        }
    }
}