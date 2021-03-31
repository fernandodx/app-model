package br.com.diasdeseries.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.diasdeseries.data.pojo.MarketingBanner
import br.com.diasdeseries.databinding.SerieMarketingBinding
import com.squareup.picasso.Picasso

class BannerAdapter : ListAdapter<MarketingBanner, BannerAdapter.BannerViewholder>(DIFF_UTIL)  {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<MarketingBanner>() {
            override fun areItemsTheSame(
                oldItem: MarketingBanner,
                newItem: MarketingBanner
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: MarketingBanner,
                newItem: MarketingBanner
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewholder {
        return BannerViewholder.create(parent)
    }

    override fun onBindViewHolder(holder: BannerViewholder, position: Int) {
       val marktingBanner = getItem(position)

        holder.bind(marktingBanner)
    }

    class BannerViewholder(
        private val itemBindig: SerieMarketingBinding
        ) : RecyclerView.ViewHolder(itemBindig.root) {


        fun bind(marktingBanner: MarketingBanner) {
            itemBindig.run {
                labelTitle.text = marktingBanner.title
                Picasso.get().load(Uri.parse(marktingBanner.image)).into(bannerImageview)
            }
        }

        companion object {
            fun create(parent: ViewGroup) : BannerViewholder {
                val viewBinding = SerieMarketingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                return BannerViewholder(viewBinding)
            }
        }

    }

}