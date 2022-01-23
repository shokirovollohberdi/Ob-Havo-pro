package uz.shokirov.obhavopro

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonObject
import uz.shokirov.adapter.PagerAdpter
import uz.shokirov.models.WeatherModel
import uz.shokirov.obhavopro.databinding.ActivityMainBinding
import uz.shokirov.transformer.ZoomOutPageTransformer
import uz.shokirov.utils.MySharedPreferences

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var requestQueue: RequestQueue
    var model: WeatherModel? = null
    lateinit var pagerAdpter: PagerAdpter
    lateinit var fusedLocatedProviderClient: FusedLocationProviderClient
    var MY_API_KEY = "appid=26e9eff74c11286112319f821ccb303a"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        getPermission()

        binding.listWeather.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }


    }

    override fun onResume() {
        super.onResume()
        requestQueue = Volley.newRequestQueue(this)
        MySharedPreferences.init(this)
        var list = MySharedPreferences.obektString
        Log.d("List", "onCreate: $list")
        if (list.isEmpty()) {
            Toast.makeText(this, "bo'sh", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "good", Toast.LENGTH_SHORT).show()
        }
        pagerAdpter = PagerAdpter(supportFragmentManager, list)
        binding.viewPager.adapter = pagerAdpter
        binding.dotsIndicator.setViewPager(binding.viewPager)
        binding.viewPager.setPageTransformer(true, ZoomOutPageTransformer())

        binding.addImage.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }


    private fun getPermission() {
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
            //all permissions already granted or just granted

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }


/*
    private fun loading(lat: String, long: String, unit: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${MY_API_KEY}&units=${unit}",
            null,
            { response ->
                if (response != null) {

                    model = Gson().fromJson(response.toString(), WeatherModel::class.java)
                    Log.d("Murodhonov", model.toString())

                    val list = ArrayList<WeatherModel>()

                    list.add(model!!)

                    MySharedPreferences.init(this)
                    val listMain = MySharedPreferences.obektString

                    for (i in listMain) {
                        list.add(i)
                    }

                    val sectionsPagerAdapter = PagerAdpter(supportFragmentManager, list)
                    val viewPager: ViewPager = binding.viewPager
                    viewPager.adapter = sectionsPagerAdapter
                    binding.dotsIndicator.setViewPager(viewPager)


                }
            }
        ) {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(jsonObjectRequest)
    }
*/


}