package uz.shokirov.obhavopro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.shokirov.adapter.OnCLick
import uz.shokirov.adapter.RvAdapter
import uz.shokirov.models.Weather
import uz.shokirov.obhavopro.databinding.ActivitySettingsBinding
import uz.shokirov.obhavopro.databinding.ItemCustomDialogBinding
import uz.shokirov.utils.MySharedPreferences

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    lateinit var rvAdapter: RvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        MySharedPreferences.init(this)
        var list = MySharedPreferences.obektString
        rvAdapter = RvAdapter(list, object : OnCLick {

            override fun click(weather: Weather) {
                var intent = Intent(this@SettingsActivity, AboutActivity::class.java)
                intent.putExtra("lat", weather.coord?.lat)
                intent.putExtra("lon", weather.coord?.lon)
                startActivity(intent)
            }


            override fun delete(position: Int, weather: Weather) {
                val alertDialog = AlertDialog.Builder(this@SettingsActivity).create()
                var dialogView = ItemCustomDialogBinding.inflate(layoutInflater)
                //  val dialogView = layoutInflater.inflate(R.layout.item_custom_dialog, null, false)
                alertDialog.setView(dialogView.root)

                dialogView.adress.text = weather.name

                dialogView.ha.setOnClickListener {
                    alertDialog.cancel()
                    list.removeAt(position)
                    MySharedPreferences.obektString = list
                    rvAdapter.notifyItemRemoved(position)
                }
                dialogView.yuq.setOnClickListener {
                    alertDialog.cancel()
                }

                alertDialog.show()
            }
        })
        binding.rv.adapter = rvAdapter
    }
}