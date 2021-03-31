package br.com.diasdeseries.ui.adapter

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.diasdeseries.R
import br.com.diasdeseries.data.pojo.SerieNowData
import br.com.diasdeseries.databinding.SerieItemBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation


class SeriesAdapter(
     private val onClickSerie : (SerieNowData) -> Unit
) : ListAdapter<SerieNowData,SeriesAdapter.SeriesViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SerieNowData>() {
            override fun areItemsTheSame(oldItem: SerieNowData, newItem: SerieNowData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SerieNowData, newItem: SerieNowData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {

        val serie = getItem(position)
        holder.bind(serie)
        holder.itemView.setOnClickListener {
            onClickSerie.invoke(serie)
        }

    }

    class SeriesViewHolder(private val itemBinding: SerieItemBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(serie: SerieNowData) {
            itemBinding.run {
                nameSerie.text = serie.show?.name
                thumbImageView.setImageResource(R.drawable.ic_launcher_foreground)
                labelChannelTextview.text = "${serie.show?.network?.name} | ${serie.airtime}h"
                episodesTextview.text =  itemBinding.root.context.getString(R.string.label_season_episode, serie.season.toString(), serie.number.toString())

                serie.show?.image?.let { image ->
                    Picasso.get()
                        .load(Uri.parse(image.original))
                        .transform(RoundedTransformation(16, 8))
                        .error(R.drawable.ic_launcher_background)
                        .into(thumbImageView)
                }

            }
        }

        companion object {
            fun create(parent: ViewGroup): SeriesViewHolder {
                val itemBinding = SerieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SeriesViewHolder(itemBinding)
            }
        }
    }

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