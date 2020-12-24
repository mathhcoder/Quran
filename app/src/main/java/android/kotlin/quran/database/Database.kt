//@file:Suppress("LABEL_NAME_CLASH")

package android.kotlin.quran.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.kotlin.quran.BOOLEAN
import android.kotlin.quran.INTEGER
import android.kotlin.quran.TEXT
import android.kotlin.quran.data.Ayah
import android.kotlin.quran.data.DataAyahs
import android.kotlin.quran.data.Surah
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL


const val DATABASE_AYAH_VERSION = 1
const val AYAHTABLE = "ayahTable"


const val AYAH_TEXT = "text"
const val SURAH_ID = "surah_id"
const val NUMBERINSURAH = "numberInSurah"
const val JUZ = "juz"
const val MANZIL = "manzil"
const val PAGE = "page"
const val RUKU = "ruku"
const val HIZBQUARTER = "hizbQuarter"


const val SURAHTABLE = "surah"

const val NUMBER = "number"
const val NAME = "name"
const val ENGLISHNAME = "englishName"
const val ENGLISHNAMETRANSLATION = "englishNameTranslation"
const val NUMBEROFAYAHS = "numberOfAyahs"
const val REVALUTIONTYPE = "revalutionType"


val AYAHTABLECOLUMNS = arrayOf(
    NUMBER,
    AYAH_TEXT,
    SURAH_ID,
    NUMBERINSURAH,
    JUZ,
    MANZIL,
    PAGE,
    RUKU,
    HIZBQUARTER,
)


class Database(context: Context) :
    SQLiteOpenHelper(context, AYAHTABLE, null, DATABASE_AYAH_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {

        var CREATION_AYAH_TABLE = "CREATE TABLE $AYAHTABLE ( " +
                "$NUMBER $INTEGER , " +
                "$AYAH_TEXT  $TEXT , " +
                "$SURAH_ID $INTEGER , " +
                "$NUMBERINSURAH $INTEGER ," +
                "$JUZ $INTEGER , " +
                "$MANZIL $INTEGER , " +
                "$PAGE $INTEGER ," +
                "$RUKU $INTEGER ," +
                "$HIZBQUARTER $INTEGER )"

        db.execSQL(CREATION_AYAH_TABLE)

        var CREATION_TABLE = "CREATE TABLE $SURAHTABLE ( " +
                "$NUMBER $INTEGER, " +
                "$NAME $TEXT, " +
                "$ENGLISHNAME $TEXT, " +
                "$ENGLISHNAMETRANSLATION $TEXT, " +
                "$REVALUTIONTYPE $TEXT, " +
                "$NUMBEROFAYAHS $INTEGER )"

        db.execSQL(CREATION_TABLE)


        Log.e("DATABASECREATE", "suggesfull")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun addSurah(surah: Surah) {

        var wdb = this.writableDatabase
        val values = ContentValues()

        values.put(NUMBER, surah.number)
        values.put(NAME, surah.name)
        values.put(ENGLISHNAMETRANSLATION, surah.englishNameTranslation)
        values.put(ENGLISHNAME, surah.englishName)
        values.put(REVALUTIONTYPE, surah.revelationType)
        values.put(NUMBEROFAYAHS, surah.numberOfAyahs)

        wdb.insert(SURAHTABLE, null, values)
        wdb.close()
    }

    fun getAllSurah(): ArrayList<Surah> {


        var surahs = ArrayList<Surah>()
        val query = "SELECT * FROM " + SURAHTABLE

        val rdb = this@Database.readableDatabase
        var cursor: Cursor = rdb.rawQuery(query, null)


        if (cursor.moveToFirst())
            do {
                var number = cursor.getString(0).toInt()
                var name = cursor.getString(1)


                var englishName = cursor.getString(2)
                var englishTransName = cursor.getString(3)
                var revalutionType = cursor.getString(4)
                var numberOfAyahs = cursor.getString(5).toInt()

                var surah = Surah(
                    number, name, englishName, englishTransName,
                    revalutionType, numberOfAyahs
                )
                surahs.add(surah)

            } while (cursor.moveToNext())
        else {
            var surahs = dowloadSurahs()
        }
        rdb.close()
        cursor.close()
        return surahs
    }


    fun setAyah(ayah: Ayah) {

        val wdb = this.writableDatabase
        val values = ContentValues()
        values.put(NUMBER, ayah.number)
        values.put(AYAH_TEXT, ayah.text)
        values.put(SURAH_ID, ayah.surah_id)
        values.put(NUMBERINSURAH, ayah.numberInSurah)
        values.put(JUZ, ayah.juz)
        values.put(MANZIL, ayah.manzil)
        values.put(PAGE, ayah.page)
        values.put(RUKU, ayah.ruku)
        values.put(HIZBQUARTER, ayah.hizbQuarter)


        wdb.insert(AYAHTABLE, null, values)
        wdb.close()
    }

    fun getAyahsInSurah(surah: Surah): ArrayList<Ayah> {
        var wdb = this.writableDatabase
        var rdb = this.readableDatabase


        var ayahs = ArrayList<Ayah>()

        var cursor = rdb.rawQuery(
            "SELECT * FROM $AYAHTABLE WHERE $SURAH_ID = ${surah.number}", null
        )
        if (cursor.moveToFirst()) {
            do {
                var values = Ayah(
                    cursor.getString(0).toInt(),
                    cursor.getString(1),
                    cursor.getString(2).toInt(),
                    cursor.getString(3).toInt(),
                    cursor.getString(4).toInt(),
                    cursor.getString(5).toInt(),
                    cursor.getString(6).toInt(),
                    cursor.getString(7).toInt(),
                    cursor.getString(8).toInt()
                )
                ayahs.add(values)
                Log.e("getAyahs", values.toString())

            } while (cursor.moveToNext())
        } else {
            ayahs = downloadAyahs(surah)
        }
        cursor.close()
        rdb.close()

        return ayahs
    }


    fun downloadAyahs(surah: Surah): ArrayList<Ayah> {

        var ayahs = ArrayList<Ayah>()
        val api = "http://api.alquran.cloud/v1/surah/${surah.number}"

        var x = 0

        try {
            val res = URL(api).readText();
            x++
            val jsonData = Gson().fromJson(res, DataAyahs::class.java)
            x++
            ayahs = jsonData.data.ayahs
            x++

        } catch (e: Exception) {
            Log.e("catch_dw", "catch download ayahs $x")
        }

        ayahs.forEach {
            it.surah_id = surah.number
            setAyah(it)
        }


        return ayahs
    }

    fun dowloadSurahs(): ArrayList<Surah> {
        var api = "http://api.alquran.cloud/v1/surah"
        var res = URL(api).readText()
        res = res.substring(res.indexOf('[', 0, false), res.length - 1)

        var surahs = GsonBuilder().create().fromJson(res, Array<Surah>::class.java)
            .toList() as ArrayList<Surah>
        surahs.forEach {
            addSurah(it)
        }
        return surahs
    }

    fun getSurah(id: Int): Surah {

        var rdb = this.readableDatabase
        var cursor = rdb.rawQuery("SELECT * FROM  $SURAHTABLE WHERE $NUMBER = $id", null)
        var cnt = "SELECT COUNT($NUMBER) FROM $SURAHTABLE WHERE $NUMBER = ${id}"

        cursor.moveToFirst()

        var result = Surah(
            cursor.getString(0).toInt(),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getString(5).toInt()
        )

        cursor.close()
        rdb.close()

        return result

    }

    fun addAllSurah(surahs: ArrayList<Surah>) {

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM  $SURAHTABLE", null)

        val t = Log.e("JAMAS", cursor.count.toString())

        if (cursor.moveToFirst()) {
            return
        } else {
            surahs.forEach {
                Log.e("STARTT", it.toString())
                addSurah(it)
            }
        }
        cursor.close()
        return
    }


}