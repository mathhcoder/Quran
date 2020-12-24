package android.kotlin.quran

import android.annotation.SuppressLint
import android.content.Intent
//import android.kotlin.quran.database.SurahDatabase
import android.kotlin.quran.adapter.SurahAdapter
import android.kotlin.quran.data.Surah
import android.kotlin.quran.database.Database
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var surah = ArrayList<Surah>()
        set(value){
            field = value
            surahAdapter.data = surah
        }
    private val surahAdapter by lazy{
        SurahAdapter({
            var id = it.number
            var intent: Intent = Intent(applicationContext, AyahActivity::class.java)
            intent.putExtra(POSITION, id)
            startActivity(intent)
        })
    }


    var db = Database(this)


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val internal = filesDir
        val cache = cacheDir
        val quran = File(filesDir, "quran")

        if (!quran.exists()) {
            quran.mkdir()
        }



        getSurah()


        GlobalScope.launch(Dispatchers.IO) {
            db.addAllSurah(surah)
        }
        recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayout.VERTICAL,
            false
        )
        recyclerView.adapter = surahAdapter
    }

    fun getSurah(){

        var dwn = GlobalScope.launch(Dispatchers.IO) {
            var db = Database(applicationContext)
            var res = db.getAllSurah()
            GlobalScope.launch(Dispatchers.Main) {
                surah = res
            }

        }
        return
    }



}