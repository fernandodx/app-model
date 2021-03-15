package br.com.diasdeseries.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.diasdeseries.R
import br.com.diasdeseries.data.pojo.EpisodesSerieData
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import br.com.diasdeseries.databinding.SeasonItemBinding


class SeasonAdapter : RecyclerView.Adapter<SeasonAdapter.SeriesViewHolder>() {

    private var list: List<EpisodesSerieData> = emptyList()
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        this.context = parent.context
        return SeriesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.season_item, parent, false)
        )
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {

        val serie :EpisodesSerieData = list[position]

        holder.viewHolderBinding?.let {
            it.labelSeasonTextview.text = context.getString(R.string.label_season_detail, serie.season.toString())
            it.episodesTextview.text = context.getString(R.string.label_episode_detail,  serie.countEpisodes)

            serie.image?.let { image ->
               Picasso.get()
                    .load(Uri.parse(image.original))
                    .transform(RoundedTransformation(16, 8))
                    .error(R.drawable.ic_launcher_background)
                    .into(it.thumbImageView)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateListEpisodesSeason(listNew: List<EpisodesSerieData>) {
        this.list = listNew
        notifyDataSetChanged()
    }

    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val viewHolderBinding = DataBindingUtil.bind<SeasonItemBinding>(itemView)
    }

    //Melhorar
    class RoundedTransformation(
        private val radius: Int, // dp
        private val margin: Int
    ) : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            canvas.drawRoundRect(
                RectF(
                    margin.toFloat(),
                    margin.toFloat(),
                    (source.width - margin).toFloat(),
                    (source.height - margin).toFloat()
                ), radius.toFloat(), radius.toFloat(), paint
            )
            if (source != output) {
                source.recycle()
            }
            return output
        }

        override fun key(): String {
            return "rounded"
        }

    }

}