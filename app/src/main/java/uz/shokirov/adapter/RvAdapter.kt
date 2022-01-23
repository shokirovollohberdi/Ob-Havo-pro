package uz.shokirov.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import uz.shokirov.models.Weather
import uz.shokirov.models.WeatherModel
import uz.shokirov.obhavopro.databinding.ItemRvBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class RvAdapter(var list: ArrayList<WeatherModel>, var onCLick: OnCLick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {
    lateinit var requestQueue: RequestQueue
    private val TAG = "RvAdapter"

    inner class Vh(var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(user: WeatherModel, position: Int) {
            requestQueue = Volley.newRequestQueue(itemRv.root.context)
            loading(list[position].lat!!, list[position].long!!)

        }

        private fun loading(lat: Double, long: Double) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=26e9eff74c11286112319f821ccb303a&units=metric",
                null,
                { response ->
                    if (response != null) {
                        val model = Gson().fromJson(response.toString(), Weather::class.java)

                        val updatedAt: Long = model.dt!!.toLong()

                        val sunrise: Long = model.sys!!.sunrise.toLong()
                        val sunset: Long = model.sys!!.sunset.toLong()

                        val txtUpdatedAt = SimpleDateFormat("dd/MM/yyyy \nhh:mm a").format(
                            Date(updatedAt * 1000)
                        )
                        val txtAddress = model.name
                        // val txtStatus = model!.weather[0].description
                        val txtStatus = model!!.weather!![0].description
                        val txtPressure = model.main?.pressure.toString()
                        val txtTemp = "${model.main?.temp?.roundToInt()}°C"
                        val txtHumidity = model.main?.humidity.toString() + " %"
                        val txtWindSpeed = model.wind?.speed?.roundToInt().toString() + " k/h"
                        val txtTempMin = "Min Temp: ${model.main?.temp_min?.roundToInt()}°C"
                        val txtTempMax = "Max Temp: ${model.main?.temp_max?.roundToInt()}°C"
                        val txtSunrise = SimpleDateFormat("hh:mm a").format(Date(sunrise * 1000))
                        val txtSunset = SimpleDateFormat("hh:mm a").format(Date(sunset * 1000))

                        itemRv.itemTvTemp.text = txtTemp
                        itemRv.status.text = txtStatus
                        itemRv.tvLocationName.text = txtAddress
                        itemRv.tvTime.text = txtUpdatedAt
                        //http://openweathermap.org/img/wn/01d@2x.png - openweather api
                        //Mening 3d icon larim url manzili
                        Picasso.get()
                            .load("https://firebasestorage.googleapis.com/v0/b/valyutalaruz.appspot.com/o/${model!!.weather!![0].icon}.png?alt=media&token=fa90bed4-56ed-4f3a-8bb1-898db954ffc9")
                            .into(itemRv.itemImageWeather)

                        itemRv.root.setOnClickListener {
                            onCLick.click(model)
                        }
                        itemRv.root.setOnLongClickListener {
                            onCLick.delete(position,model)
                            true
                        }

                    }
                }
            ) {
                Log.d(TAG, "loading: ${it.message}")
            }
            requestQueue.add(jsonObjectRequest)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}

interface OnCLick {
    fun click(weather: Weather)
    fun delete(position: Int,weather: Weather)
}