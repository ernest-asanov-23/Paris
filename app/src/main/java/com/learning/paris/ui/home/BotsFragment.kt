package com.learning.paris.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.paris.R
import com.learning.paris.data.Bot
import com.learning.paris.ui.MainViewModel
import com.learning.paris.ui.chat.ChatViewModel
import com.learning.paris.ui.chat.DialogAdapter

class BotsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var botsListRV: RecyclerView
    private lateinit var botsAdapter: BotsAdapter
    private var tryCounter = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainViewModel =
                ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        botsListRV = root.findViewById(R.id.botListRV)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botsListRV.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        mainViewModel.data.observe(viewLifecycleOwner, { list_b ->
            botsAdapter = BotsAdapter(list_b) { bot_ ->
                // On bot name selected navigate to chat
                navigateToChat(bot_)
            }
            botsListRV.adapter = botsAdapter
        })
        mainViewModel.error.observe(viewLifecycleOwner, { error ->
            if (tryCounter++ < 5) {
                loadBots()
            }
        })

        loadBots()
    }

    private fun loadBots() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.loadData()
        }
    }

    private fun navigateToChat(bot_: Bot) {
        val navController = requireView().findNavController()
        val action = BotsFragmentDirections.actionNavBotsToNavChat(bot_.id, bot_.name)
        navController.navigate(action)
    }
}
