package com.learning.paris.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.learning.paris.R
import com.learning.paris.data.Bot


class BotNameViewHolder(itemView: FrameLayout, onClick: (Bot) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val cardView: CardView = itemView.findViewById(R.id.card1)
    private val botNameTextView: TextView = itemView.findViewById(R.id.botNameTextView)
    private lateinit var data: Bot

    init {
        cardView.setOnClickListener {
            onClick(data)
        }
    }

    fun bind(bot: Bot) {
        botNameTextView.text = bot.name
        data = bot
    }
}


class BotsAdapter(
    private val listOfBots: List<Bot>,
    private val onBotSelected: (Bot) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.bot_list_rv_item, parent, false)
        return BotNameViewHolder(itemView as FrameLayout, onBotSelected)
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