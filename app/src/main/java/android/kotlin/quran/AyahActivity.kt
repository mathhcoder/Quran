package android.kotlin.quran

import android.kotlin.quran.adapter.AyahAdapter
import android.kotlin.quran.data.Ayah
import android.kotlin.quran.data.Surah
import android.kotlin.quran.database.Database
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_ayah.*
import kotlinx.android.synthetic.main.cell_surah.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.URL
import java.net.URLConnection

class AyahActivity : AppCompatActivity() {
    val api = "http://api.alquran.cloud/v1/ayah/3:139/editions/quran-uthmani,en.asad,en.pickthall"

    private val ayahAdapter by lazy{
        AyahAdapter()
    }

    private var ayahs = ArrayList<Ayah>()
        set(value){
            field = value
            ayahAdapter.data = ayahs
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayah)
        Log.e("oncreate" , "AyahActivity")

        var db = Database(this)
        var position = intent.getIntExtra(POSITION, 0)
        Log.e(POSITION , position.toString())
        var surah = db.getSurah(position)

        var quran = File(filesDir, QURAN)

        recyclerViewAyahs.layoutManager = LinearLayoutManager(applicationContext , LinearLayoutManager.VERTICAL , false)
        recyclerViewAyahs.adapter = ayahAdapter

        GlobalScope.launch(Dispatchers.IO){
            val res = db.getAyahsInSurah(surah)
            Log.e("ayahd" , ayahs.toString())

            GlobalScope.launch(Dispatchers.Main) {
                ayahs = res
            }

        }



    }




    fun getAyahs(surah : Surah){
        GlobalScope.launch(Dispatchers.IO) {
            var db = Database(applicationContext)
            var res = db.getAyahsInSurah(surah)
            GlobalScope.launch(Dispatchers.Main) {
                ayahs = res
            }
        }
    }




//    }


//    fun downloadAyahsAudio(ayah : Ayah) :Boolean{
//
//
//        var surah = File(filesDir, "quran/${ayah.surah_id}")
//        if(!surah.exists())
//            surah.mkdir()
//        var ayahTxt = File(surah , "/${ ayah.numberInSurah}.txt")
//
//        if(ayahTxt.length()==0L) {
//            try {
////                var url = URL("https://cdn.alquran.cloud/media/audio/ayah/ar.alafasy/${startPosition + ayahPosition}")
////                val conection: URLConnection = url.openConnection()
//                conection.connect()
//                val input: InputStream = BufferedInputStream(
//                    url.openStream(),
//                    8192
//                )
//
//                val output = FileOutputStream(ayah)
//
//                var count = 0
//                var array = ArrayList<Byte>()
//                val data = ByteArray(1024)
//
//                while (input.read(data).also { count = it } !== -1) {
//                    output.write(data, 0, count)
//                }
//                output.flush()
//                output.close()
//                input.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.e("ERROR", e.message.toString())
//
//            }
//            return true
//        }
//        else{
//            return true
//        }
//
//    }

}