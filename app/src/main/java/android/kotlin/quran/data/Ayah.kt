package android.kotlin.quran.data

data class Ayah(
    val number: Int,
    val text: String,
    var surah_id: Int,
    val numberInSurah: Int,
    val juz: Int,
    val manzil: Int,
    val page: Int,
    val ruku: Int,
    val hizbQuarter: Int
)