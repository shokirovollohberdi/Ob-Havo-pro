package uz.shokirov.obhavopro

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.airbnb.lottie.model.Marker
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.shokirov.models.WeatherModel
import uz.shokirov.obhavopro.databinding.BottomSheetBinding
import uz.shokirov.utils.MySharedPreferences
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.hide()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    var marker: Marker? = null
    private val TAG = "MapsActivity"
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        MySharedPreferences.init(this)
        var list = MySharedPreferences.obektString

        /*try {
            loaddate()
        } catch (e: Exception) {
            Log.d("FATAL", "onMapReady: $e")
        }*/

        mMap.setOnMapLongClickListener { latlong ->
            val dialog = BottomSheetDialog(this)
            var item = BottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(item.root)
            item.adress.setText(getAddressFromLatLng(this, latlong))

            item.ha.setOnClickListener {
                list.add(WeatherModel(latlong.latitude, latlong.longitude))
                MySharedPreferences.obektString = list
                dialog.dismiss()
                finish()
            }
            item.yuq.setOnClickListener {
                dialog.cancel()
            }

            dialog.show()
        }
    }

    fun getAddressFromLatLng(context: Context?, latLng: LatLng): String? {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(context, Locale.getDefault())
        return try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses[0].getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    @SuppressLint("MissingPermission", "VisibleForTests")
    private fun loaddate() {
        MySharedPreferences.init(this)
        var list = MySharedPreferences.obektString
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener { it: Location ->
            Log.d(TAG, "loaddate: Qoshildi")
        }
    }
}