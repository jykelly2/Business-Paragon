package sheridan.yamazaki.businessparagon.ui.business.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.FragmentNearMeBinding
import sheridan.yamazaki.businessparagon.model.Business


@AndroidEntryPoint
class NearMeFragment : Fragment(), OnMapReadyCallback{
    private lateinit var binding: FragmentNearMeBinding
    private var mapView: MapView? = null
    private lateinit var mGoogleMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1

    private var searchDistance: Float = 10000F
    private var mapReady: Boolean = false

    private val viewModel: LocationViewModel by viewModels()
    private val businessList = ArrayList<Business>()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearMeBinding.inflate(inflater, container, false)
        initMap(binding.root, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.businesses.observe(viewLifecycleOwner){ businesses ->
            Log.d("busin98", businesses.size.toString())
           businessList.addAll(businesses)
        }
        return binding.root
    }

    private fun initMap(view: View?, savedInstanceState: Bundle?) {
        mapView = view?.findViewById(R.id.googleMap)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mGoogleMap = googleMap
            mGoogleMap.uiSettings?.isZoomControlsEnabled = true
            setUpMap()
        }
    }

    private fun placeMarkerOnMap(location: LatLng, businessName: String, businessCategory: String) {
        val markerOptions = MarkerOptions().position(location)
            .title(businessName)
            .icon(getMarkerIcon(businessCategory))

        mGoogleMap.addMarker(markerOptions)
    }

    private fun getMarkerIcon(businessCategory: String) : BitmapDescriptor?{
        return when (businessCategory) {
            "Grocery"-> bitmapDescriptorFromVector(requireActivity(), R.drawable.ic_baseline_local_grocery_store_24, R.color.teal_700)
            "Restaurant" -> bitmapDescriptorFromVector(requireActivity(), R.drawable.ic_baseline_restaurant_24, R.color.orange)
            else -> {
                bitmapDescriptorFromVector(requireActivity(), R.drawable.ic_baseline_electrical_services_24, R.color.orange)
            }
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mGoogleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10f))
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10f))
                addBusinessLocations()
                mapReady = true
            }
        }
    }

    private fun addBusinessLocations(){
        mGoogleMap.clear()
        businessList.forEach{
            val address = it.streetAddress + ", " + it.city + ", " + it.province + ", " + it.postalCode
            val businessLocation = convertAddress(address)
            Log.d("busin9", businessLocation.toString())
            if (businessLocation != null && it.name != null) {
                if (lastLocation.distanceTo(Location(LocationManager.GPS_PROVIDER).apply {
                        latitude = businessLocation.latitude
                        longitude = businessLocation.longitude
                            Log.d("busin98", businessLocation.longitude.toString())
                    }) <= searchDistance)
                         placeMarkerOnMap(businessLocation, it.name!!, it.category!!)
            }
        }
    }

    private fun convertAddress(address: String) : LatLng? {
        var location: LatLng? = null
        if (address.isNotEmpty()) {
            try {
                val geoCoder = Geocoder(context)
                val addressList: List<Address> = geoCoder.getFromLocationName(address, 2)
                Log.d("busin98", addressList.size.toString())
                if (addressList.isNotEmpty()) {
                    val lat: Double = addressList[0].latitude
                    val lng: Double = addressList[0].longitude
                    location = LatLng(lat, lng)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return  location
    }

    private fun backFragment() {
        val manager = (context as AppCompatActivity).supportFragmentManager
        manager.popBackStackImmediate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.distance_menu, menu)
        val item = menu.findItem(R.id.distance_spinner)
        val spinner = item.actionView as Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.distance_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    val selectedItem = parent.getItemAtPosition(position).toString().removeSuffix(" km")
                    searchDistance = selectedItem.toFloat() * 10000
                    Log.d("locatins", searchDistance.toString())
                    if (mapReady) {
                        addBusinessLocations()
                    }
                   // (parent.getChildAt(0) as TextView).setTextColor(Color.BLUE)
                    //(parent.getChildAt(0) as TextView).textSize = 5F
                }
            }
        }
    }

 /*   private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }*/

   // @SuppressLint("ResourceAsColor")
    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int, color:Int): BitmapDescriptor? {
        val background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24)
        background?.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        background?.setTint(color)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable?.setBounds(
            35,
            20,
            vectorDrawable.intrinsicWidth + 35,
            vectorDrawable.intrinsicHeight + 20
        )
        val bitmap = background?.intrinsicWidth?.let {
            Bitmap.createBitmap(it, background?.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = bitmap?.let { Canvas(it) }
        if (background != null) {
            if (canvas != null) {
                background.draw(canvas)
            }
        }
        if (vectorDrawable != null) {
            if (canvas != null) {
                vectorDrawable.draw(canvas)
            }
        }
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onResume() {
        mapView?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView?.onLowMemory()
        super.onLowMemory()
    }

}

/*private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.isMyLocationEnabled = true
        }
        else
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    mGoogleMap.isMyLocationEnabled = true
                    return
                }

            }
            else {
                Toast.makeText(requireActivity(), "User has not granted location access permission", Toast.LENGTH_LONG).show()
                backFragment()
            }
        }
    }*/