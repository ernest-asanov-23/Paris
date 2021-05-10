package com.learning.paris.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.paris.R
import com.learning.paris.data.Bot

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatView: RecyclerView
    private lateinit var dialogAdapter: DialogAdapter

    private val args: ChatFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        chatViewModel =
                ViewModelProvider(this).get(ChatViewModel::class.java)
        val root = inflater.inflate(R.layout.chat_fragment, container, false)
        chatView = root.findViewById(R.id.chatView)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        chatViewModel.data.observe(viewLifecycleOwner, { chatData ->
            // CHAT DATA LOADED
            dialogAdapter = DialogAdapter(chatData.bot, chatData.dialog, chatData.responseOptions) { responseOption ->
                // RESPONSE OPTION SELECTED
                lifecycleScope.launchWhenCreated {
                    chatViewModel.sendResponse(args.botId, responseOption)
                }

            }
            chatView.adapter = dialogAdapter

        })
        lifecycleScope.launchWhenCreated {
            loadChat(args.botId, args.botName)
        }
    }

    private suspend fun loadChat(botId: Int, botName: String) {
        chatViewModel.loadData(Bot(botId, botName))
    }
}