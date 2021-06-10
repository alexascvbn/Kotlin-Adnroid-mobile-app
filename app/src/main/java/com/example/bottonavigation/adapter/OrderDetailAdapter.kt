package com.example.bottonavigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bottonavigation.R
import com.example.bottonavigation.model.BaseParts

class OrderDetailAdapter(
    var mContext: Context,
    var bPartsList: ArrayList<BaseParts>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mItemClickListener: OnItemClickListener

    val storage_url = mContext.getString(R.string.storage_url)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.item_order_detail_list, parent, false
        )
        return ViewHolder(view)
    }

    fun updateList(bPartsList: ArrayList<BaseParts>) {
        this.bPartsList = bPartsList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bPartsList.size
    }
    fun getItem(pos: Int) : BaseParts { return bPartsList[pos] }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ViewHolder) {
            val model: BaseParts = getItem(position)
            val holder = viewHolder as ViewHolder
            holder.bind(model)
        }
    }

    public interface OnItemClickListener {
        fun onItemClick(
            view: View, position: Int, model: BaseParts
        )
    }


    fun setOnItemClickListener(mItemClickListener: OrderDetailAdapter.OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }


    inner class ViewHolder(mitemView: View) : RecyclerView.ViewHolder(mitemView), View.OnClickListener {

        private val itemTxtTitle = itemView.findViewById<TextView>(R.id.part_txt_name);
        private val itemTxtMessage = itemView.findViewById<TextView>(R.id.item_txt_type);
        private val itemTxtId = itemView.findViewById<TextView>(R.id.item_txt_id);
        private val itemTxtQty = itemView.findViewById<TextView>(R.id.item_txt_qty);
        private val itemTxtPrice = itemView.findViewById<TextView>(R.id.item_txt_price);
        private val imgProduct = itemView.findViewById<ImageView>(R.id.img_product);

        fun bind(model: BaseParts) {
            itemTxtTitle.text = model.name
            itemTxtMessage.text = String.format(mContext.getString(R.string.order_partstype), model.categoryId)
            itemTxtQty.text = model.qty.toString()
            itemTxtId.text = model.id
            itemTxtPrice.text = String.format(mContext.getString(R.string.price), model.price)
            val img_url = String.format(storage_url, "parts", "%2F", model.id + ".png")
            Glide.with(mContext)
                .load(img_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(ContextCompat.getDrawable(mContext, R.drawable.inchcape_mobility))
                .into(imgProduct)
        }

        override fun onClick(v: View?) {
            val bp = getItem(adapterPosition)
            if (v?.id == itemView.id) {
                mItemClickListener.onItemClick(itemView, adapterPosition, bp)
            }
        }

    }

}