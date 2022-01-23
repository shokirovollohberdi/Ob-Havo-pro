package uz.shokirov.obhavopro

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import uz.shokirov.models.Weather
import uz.shokirov.obhavopro.databinding.ActivityAboutBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class AboutActivity : AppCompatActivity() {
    private val TAG = "AboutActivity"
    lateinit var requestQueue: RequestQueue
    lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        requestQueue = Volley.newRequestQueue(binding.root.context)

    }

    override fun onResume() {
        super.onResume()
        var lat = intent.getDoubleExtra("lat", 0.1)
        var lon = intent.getDoubleExtra("lon", 0.2)
        Log.d(TAG, "onCreate: $lat $lon")
        loading(lat, lon)
    }

    @SuppressLint("SimpleDateFormat")
    private fun loading(lat: Double, lon: Double) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=26e9eff74c11286112319f821ccb303a&units=metric",
            null,
            { response ->
                if (response != null) {
                    val model = Gson().fromJson(response.toString(), Weather::class.java)

                    val updatedAt: Long = model.dt!!.toLong()

                    val sunrise: Long = model.sys!!.sunrise.toLong()
                    val sunset: Long = model.sys!!.sunset.toLong()

                    val txtUpdatedAt =
                        "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a").format(
                            Date(updatedAt * 1000)
                        )
                    val txtAddress = model.name
                    val txtStatus = model.weather!![0].description
                    val txtPressure = model.main?.pressure.toString()
                    val txtTemp = "${model.main?.temp?.roundToInt()}°C"
                    val txtHumidity = model.main?.humidity.toString() + " %"
                    val txtWindSpeed = model.wind!!.speed.roundToInt().toString() + " k/h"
                    val txtTempMin = "Min Temp: ${model.main!!.temp_min.roundToInt()}°C"
                    val txtTempMax = "Max Temp: ${model.main!!.temp_max.roundToInt()}°C"
                    val txtSunrise = SimpleDateFormat("hh:mm a").format(Date(sunrise * 1000))
                    val txtSunset = SimpleDateFormat("hh:mm a").format(Date(sunset * 1000))

                    binding.temp.text = txtTemp
                    binding.sunset.text = txtSunset
                    binding.status.text = txtStatus
                    binding.wind.text = txtWindSpeed
                    binding.sunrise.text = txtSunrise
                    binding.address.text = txtAddress
                    binding.tempMin.text = txtTempMin
                    binding.tempMax.text = txtTempMax
                    binding.pressure.text = txtPressure
                    binding.humidity.text = txtHumidity
                    binding.updatedAt.text = txtUpdatedAt

                    //openweather.org saytining maxsus icon list mavjud
                    //api orqali iconning faqat nomi keladi ushbu link orqali iconni picasso yordamida olishimiz mumkin
                    //http://openweathermap.org/img/wn/01d@2x.png - openweather api

                    //Mening 3d icon larim url manzili
                    Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/valyutalaruz.appspot.com/o/${model.weather!![0].icon}.png?alt=media&token=fa90bed4-56ed-4f3a-8bb1-898db954ffc9")
                        .into(binding.logotip)

                    /* val weatherView = binding.weatherView
                     when {
                         txtStatus.contains("rain") -> {
                             weatherView.setWeatherData(PrecipType.RAIN)
                         }
                         txtStatus.contains("snow") -> {
                             weatherView.setWeatherData(PrecipType.SNOW)
                         }
                     }*/

                }
            }
        ) {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(jsonObjectRequest)
    }
}