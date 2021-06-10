package com.example.bottonavigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottonavigation.R;
import com.example.bottonavigation.model.NewsModel;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<NewsModel> modelList;

    private OnItemClickListener mItemClickListener;
    private OnMoreClickListener mMoreClickListener;

    public NewsListAdapter(Context context, ArrayList<NewsModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<NewsModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_news, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Here you can fill your row view

        final NewsModel model = getItem(position);


        ArrayList<NewsModel> singleSectionItems = model.getSingleItemList();

        holder.itemTxtTitle.setText(model.getTitle());

        SingleNewsListAdapter itemListDataAdapter = new SingleNewsListAdapter(mContext, singleSectionItems);

        holder.recyclerViewHorizontalList.setHasFixedSize(true);
        holder.recyclerViewHorizontalList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerViewHorizontalList.setAdapter(itemListDataAdapter);


        holder.recyclerViewHorizontalList.setNestedScrollingEnabled(false);


        itemListDataAdapter.SetOnItemClickListener(new SingleNewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemPosition, NewsModel model) {

                mItemClickListener.onItemClick(view, position, itemPosition, model);

            }
        });

        holder.itemTxtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMoreClickListener.onMoreClick(view, position, model);


            }
        });


    }

    @Override
    public int getItemCount() {

        return modelList.size();
    }


    private NewsModel getItem(int position) {
        return modelList.get(position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnMoreClickListener(final OnMoreClickListener onMoreClickListener) {
        this.mMoreClickListener = onMoreClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int absolutePosition, int relativePosition, NewsModel model);
    }


    public interface OnMoreClickListener {
        void onMoreClick(View view, int position, NewsModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected RecyclerView recyclerViewHorizontalList;
        protected TextView itemTxtMore;
        private TextView itemTxtTitle;


        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.item_txt_title);
            this.recyclerViewHorizontalList = (RecyclerView) itemView.findViewById(R.id.recycler_view_horizontal_list);
            this.itemTxtMore = (TextView) itemView.findViewById(R.id.item_txt_more);


        }
    }


}
