package android.kotlin.quran.adapter

import android.kotlin.quran.R
import android.kotlin.quran.data.Ayah
import android.kotlin.quran.data.Surah
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_ayah.view.*
import kotlinx.android.synthetic.main.cell_surah.view.*

class AyahAdapter() :
    RecyclerView.Adapter<AyahAdapter.AyahViewHolder>() {

    var data: ArrayList<Ayah> = ArrayList()
        set(value) {
            Log.e("DownloadedAyahs" , data.size.toString())
            field = value
            notifyDataSetChanged()
        }

    var onClickListener : (ayah : Ayah) -> Unit = {}
        set(value){
            field = value
            notifyDataSetChanged()
        }





    override fun getItemCount() = data.size

    override fun onBindViewHolder(holderAyah: AyahViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holderAyah.bindAyah(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_ayah, parent, false)
        return AyahViewHolder(view)
    }

    inner class AyahViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindAyah(ayah: Ayah) {
            itemView.text.text = ayah.text

            itemView.setOnClickListener {
                onClickListener(ayah)
            }

        }
    }


}
