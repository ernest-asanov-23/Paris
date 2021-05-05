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
import com.learning.paris.R
import com.learning.paris.ui.MainViewModel

class BotsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainViewModel =
                ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.data.observe(viewLifecycleOwner, { bots ->
            // UI thread - refresh bots list
            navigateToChat(1)
        })
        lifecycleScope.launchWhenCreated {
            mainViewModel.loadData()
            // background thread - coroutine work
        }
    }

    private fun navigateToChat(botId: Int) {
        val navController = requireView().findNavController()
        val action = BotsFragmentDirections.actionNavBotsToNavChat(botId, "Name")
        navController.navigate(action)
    }
}