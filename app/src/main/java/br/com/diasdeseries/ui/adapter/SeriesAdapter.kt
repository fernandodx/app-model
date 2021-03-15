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
import br.com.diasdeseries.data.pojo.SerieData
import br.com.diasdeseries.data.pojo.SerieNowData
import br.com.diasdeseries.databinding.SerieItemBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation


class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {

    private var list: List<SerieNowData> = emptyList()
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        this.context = parent.context
        return SeriesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.serie_item, parent, false)
        )
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {

        val serie :SerieNowData = list[position]

        holder.viewHolderBinding?.let {
            it.nameSerie.text = serie.show?.name
            it.thumbImageView.setImageResource(R.drawable.ic_launcher_foreground)
            it.labelChannelTextview.text = "${serie.show?.network?.name} | ${serie.airtime}h"
            it.episodesTextview.text =  context.getString(R.string.label_season_episode, serie.season.toString(), serie.number)

            serie.show?.image?.let { image ->
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

    fun updateSeries(listNew: List<SerieNowData>) {
        this.list = listNew
        notifyDataSetChanged()
    }

    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val viewHolderBinding = DataBindingUtil.bind<SerieItemBinding>(itemView)
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

    fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = 100f
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }



}