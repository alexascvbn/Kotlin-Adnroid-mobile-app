package com.example.bottonavigation.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bottonavigation.R;
import com.example.bottonavigation.model.NewsModel;

import java.util.ArrayList;

public class SingleNewsListAdapter extends RecyclerView.Adapter<SingleNewsListAdapter.SingleNewsRowHolder>{

    private ArrayList<NewsModel> itemsList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public SingleNewsListAdapter(Context context, ArrayList<NewsModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleNewsRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_single_news, viewGroup, false);

        return new SingleNewsRowHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleNewsRowHolder holder, int i) {

        NewsModel singleItem = itemsList.get(i);

        holder.itemNewsTitle.setText(singleItem.getTitle());


        Glide.with(mContext)
            .load(Uri.parse(singleItem.getImage_url()))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(R.drawable.car1)
            .into(holder.itemNewsImg);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int itemPosition, NewsModel model);
    }

    public class SingleNewsRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemNewsTitle;
        protected ImageView itemNewsImg;

        public SingleNewsRowHolder(View view) {
            super(view);

            this.itemNewsTitle = (TextView) view.findViewById(R.id.item_card_txt_title);
            this.itemNewsImg = (ImageView) view.findViewById(R.id.item_card_img);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, getAdapterPosition(), itemsList.get(getAdapterPosition()));
                }
            });

        }
    }
}
