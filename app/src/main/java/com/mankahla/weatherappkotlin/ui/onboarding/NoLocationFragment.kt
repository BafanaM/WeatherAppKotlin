package com.mankahla.weatherappkotlin.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mankahla.weatherappkotlin.R
import com.mankahla.weatherappkotlin.databinding.FragmentNoLocationBinding

class NoLocationFragment : Fragment() {

    private lateinit var dataBinding: FragmentNoLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_no_location,
                container,
                false
            )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tryAgainButton = view.findViewById<Button>(R.id.try_again)
        tryAgainButton.setOnClickListener {
            findNavController().navigate(
                NoLocationFragmentDirections.actionLocationErrorFragmentToSplashFragment()
            )
        }
    }
}