package br.com.diasdeseries.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.diasdeseries.R
import br.com.diasdeseries.data.pojo.EpisodesSerieData
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import br.com.diasdeseries.databinding.SeasonItemBinding
import br.com.diasdeseries.databinding.SerieItemEpisodeBinding


class EpisodeAdapter : RecyclerView.Adapter<EpisodeAdapter.SeriesViewHolder>() {

    private var list: List<EpisodesSerieData> = emptyList()
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        this.context = parent.context
        return SeriesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.serie_item_episode, parent, false)
        )
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {

        val serie :EpisodesSerieData = list[position]

        holder.viewHolderBinding?.let {
            it.nameEpisodeTextview.text = serie.name
            it.labelDetailEpisodeTextview.text = "${serie.season}. Episódio ${serie.number}"
            it.detailEpisodeTextview.text = HtmlCompat.fromHtml(serie.summary?:"", HtmlCompat.FROM_HTML_MODE_COMPACT)

            serie.image?.let { image ->
                Picasso.get()
                    .load(Uri.parse(image.medium))
                    .transform(SeasonAdapter.RoundedTransformation(16, 8))
                    .error(R.drawable.ic_launcher_background)
                    .into(it.thumbImageView)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateListEpisodes(listNew: List<EpisodesSerieData>) {
        this.list = listNew
        notifyDataSetChanged()
    }

    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val viewHolderBinding = DataBindingUtil.bind<SerieItemEpisodeBinding>(itemView)
    }


}