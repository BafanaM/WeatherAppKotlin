package com.mankahla.weatherappkotlin.ui.onboarding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mankahla.weatherappkotlin.R
import com.mankahla.weatherappkotlin.ui.MainActivity
import com.mankahla.weatherappkotlin.utils.isConnected
import im.delight.android.location.SimpleLocation


class SplashFragment : Fragment() {

    var location: SimpleLocation? = null
    var latitude: String? = null
    var longitude: String? = null
    private var isLocationGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer()
    }

    private fun setupLocation() {
        location = SimpleLocation(requireContext())
        if (!location!!.hasLocationEnabled()) {
            SimpleLocation.openSettings(requireContext())
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
            } else {
                location = SimpleLocation(requireContext())
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                isLocationGranted = true
                navigateToNextScreen()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                isLocationGranted = true
            } else {
                isLocationGranted = false
            }
        }
        navigateToNextScreen()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun timer() {
        object : CountDownTimer(DURATION, INTERVAL) {

            override fun onFinish() {
                setupLocation()
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }

    private fun navigateToNextScreen() {

        if (!isConnected(context)) {
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToNetworkErrorFragment()
            )
        }else if (!isLocationGranted) {
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToLocationErrorFragment()
            )
        } else{
            startActivity(MainActivity.getStartIntent(requireActivity(), latitude, longitude))
        }
    }


    companion object {
        const val REQUEST_CODE = 1
        const val DURATION = 2000L
        const val INTERVAL = 1000L
    }
}
