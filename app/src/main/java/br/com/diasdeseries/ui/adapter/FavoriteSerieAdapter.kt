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
import br.com.diasdeseries.data.db.entity.FavoriteSeriesEntity
import br.com.diasdeseries.data.pojo.EpisodesSerieData
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import br.com.diasdeseries.databinding.SeasonItemBinding


class FavoriteSerieAdapter(private val onClickFavorite : (FavoriteSeriesEntity) -> Unit) : RecyclerView.Adapter<FavoriteSerieAdapter.SeriesViewHolder>() {

    private var list: List<FavoriteSeriesEntity> = emptyList()
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        this.context = parent.context
        return SeriesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.favorite_serie_item, parent, false)
        )
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {

        val favorite :FavoriteSeriesEntity = list[position]

        holder.viewHolderBinding?.let {
            it.nameFavoriteTextview.text = favorite.nameSerie
            it.ratingTextview.text = "Nota: ${favorite.rating}"

            favorite.banner.let { image ->
               Picasso.get()
                    .load(Uri.parse(image))
                    .transform(RoundedTransformation(16, 8))
                    .error(R.drawable.ic_launcher_background)
                    .into(it.thumbImageView)
            }
        }

        holder.itemView.setOnClickListener {
            onClickFavorite.invoke(favorite)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateListFavorite(listNew: List<FavoriteSeriesEntity>) {
        this.list = listNew
        notifyDataSetChanged()
    }

    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val viewHolderBinding = DataBindingUtil.bind<br.com.diasdeseries.databinding.FavoriteSerieItemBinding>(itemView)
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