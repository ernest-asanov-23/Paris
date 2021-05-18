package com.learning.paris.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private val dialogAdapter: DialogAdapter = DialogAdapter() { responseOption ->
        // RESPONSE OPTION SELECTED
        lifecycleScope.launchWhenCreated {
            chatViewModel.sendResponse(args.botId, responseOption)
        }
    }

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
        chatViewModel.data.observe(viewLifecycleOwner, { chatResult ->
            when (chatResult) {
                is ChatViewModel.ChatLoadResult.Success -> {
                    val chatData = chatResult.data
                    dialogAdapter.bot = chatData.bot
                    dialogAdapter.submitList(
                        chatData.dialog.map { DialogItem.MessageItem(it) } +
                                chatData.responseOptions.map { DialogItem.ResponseItem(it) }
                    )
                }
                is ChatViewModel.ChatLoadResult.Fail -> {
                    dialogAdapter.submitList(emptyList())
                    Toast.makeText(context, chatResult.exception.localizedMessage, Toast.LENGTH_LONG).show()
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