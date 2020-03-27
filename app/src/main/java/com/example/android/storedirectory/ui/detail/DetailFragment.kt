package com.example.android.storedirectory.ui.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.storedirectory.R
import com.example.android.storedirectory.databinding.DetailFragmentBinding
import com.example.android.storedirectory.model.Store
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: DetailViewModel

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setStoreData(args.store)

        viewModel.store.observe(viewLifecycleOwner, Observer {
            setupUI(it)
        })
    }

    private fun setupUI(store: Store) {
        binding.toolbar.title = store.name

        binding.storeAddress.text = String.format(
            resources.getString(R.string.address_formatted),
            store.address,
            store.city,
            store.state,
            store.zipcode,
            store.phone
        )

        with(binding.mapView) {
            onCreate(null)
            getMapAsync { googleMap ->
                MapsInitializer.initialize(requireContext())
                setMapLocation(googleMap, store.latitude, store.longitude)
            }
        }

        binding.call.setOnClickListener {
            makeCall(store.phone)
        }
        binding.navigate.setOnClickListener {
            launchMaps(store)
        }

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }

    private fun launchMaps(store: Store) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("google.navigation:q=" + store.latitude + "," + store.longitude)
        )
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun setMapLocation(map: GoogleMap, lat: String, long: String) {
        with(map) {
            val location = LatLng(lat.toDouble(), long.toDouble())
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
            addMarker(MarkerOptions().position(location))
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private fun makeCall(phoneNumber: String) {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CALL_PHONE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                )
            ) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Permission to access Phone is required to make this call.")
                builder.setTitle("Permission required")
                builder.setPositiveButton("OK") { _, _ ->
                    makePermissionRequest()
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                makePermissionRequest()
            }
        } else {
            startActivity(
                Intent(
                    Intent.ACTION_CALL,
                    Uri.parse(
                        String.format(
                            resources.getString(R.string.telephone_uri),
                            phoneNumber
                        )
                    )
                )
            )
        }
    }

    private fun makePermissionRequest() {
        requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 101)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty()
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    val store = viewModel.store.value
                    store?.let {
                        makeCall(it.phone)
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        "Permission denied to make call",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}