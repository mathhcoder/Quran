package android.kotlin.quran.adapter

import android.kotlin.quran.R
import android.kotlin.quran.data.Surah
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_surah.view.*

class SurahAdapter(val onClickListener: (surah: Surah) -> Unit) :
    RecyclerView.Adapter<SurahAdapter.SurahViewHolder>() {

    var data: ArrayList<Surah> = ArrayList()
        set(value) {
            field = value
            Log.e("JAMA" , "change2")
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holderSurah: SurahViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holderSurah.bindSurah(it)

            Log.e("adapterrrr" , it.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_surah, parent, false)
        return SurahViewHolder(view)
    }

    inner class SurahViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindSurah(surah: Surah) {
            itemView.nameEnglish.text = surah.englishName
            itemView.title.text = "${ surah.numberOfAyahs } oyatdan iborat"
            itemView.number.text = surah.number.toString()
            itemView.setOnClickListener {
                onClickListener(surah)
            }

        }
    }


}
