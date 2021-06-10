package com.example.bottonavigation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bottonavigation.R;
import com.example.bottonavigation.model.OrderParts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class OrderPartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<OrderParts> modelList;

    private OnItemClickListener mItemClickListener;
    private OnCheckedListener mOnCheckedListener;

    private Set<Integer> checkSet = new HashSet<>();
    private Boolean isSelectedAll = false;
    private String storage_url;

    public OrderPartListAdapter(Context context, ArrayList<OrderParts> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        this.storage_url = mContext.getString(R.string.storage_url);
    }

    public void updateList(ArrayList<OrderParts> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public void selectAll() {
        isSelectedAll = true;
        notifyDataSetChanged();
    }
    public void unSelectAll() {
        isSelectedAll = false;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_checkbox_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, final int position) {

        //Here you can fill your row view
        if (viewHolder instanceof ViewHolder) {
            final OrderParts model = getItem(position);
            ViewHolder holder = (ViewHolder) viewHolder;

            holder.bind(model);

            String img_url = String.format(storage_url, "parts", "%2F", model.getImgPath());

            Glide.with(mContext)
                    .load(img_url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(ContextCompat.getDrawable(mContext, R.drawable.inchcape_mobility))
                    .into(holder.imgProduct);

            //if true, your checkbox will be selected, else unselected
            holder.itemCheckList.setChecked(checkSet.contains(position));

            holder.itemCheckList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        checkSet.add(position);
                    } else {
                        checkSet.remove(position);
                    }

                    mOnCheckedListener.onChecked(buttonView, isChecked, position, modelList.get(position));

                }
            });

            if (!isSelectedAll) {
                holder.itemCheckList.setChecked(false);
            }
            else {
                holder.itemCheckList.setChecked(true);
            }
        }
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setOnCheckedListener(final OnCheckedListener onCheckedListener) {
        this.mOnCheckedListener = onCheckedListener;

    }

    private OrderParts getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, OrderParts model);
        void onAddBtnClick(View view, boolean isChecked, int position, OrderParts model);
        void onSubBtnClick(View view, boolean isChecked, int position, OrderParts model);
    }


    public interface OnCheckedListener {
        void onChecked(View view, boolean isChecked, int position, OrderParts model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // name type price qty

        private ImageView imgProduct;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private TextView itemTxtId;
        private Button addBtn, subtractBtn;
        private TextView itemTxtQty;
        private TextView itemTxtPrice;
//        private ImageView itemImage;


        private CheckBox itemCheckList;

        public ViewHolder(final View itemView) {
            super(itemView);


            // ButterKnife.bind(this, itemView);

            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.part_txt_name);
            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_type);
            this.itemCheckList = (CheckBox) itemView.findViewById(R.id.check_list);
            this.itemTxtId = (TextView) itemView.findViewById(R.id.item_txt_id);
            this.itemTxtQty = (TextView) itemView.findViewById(R.id.item_txt_qty);
            this.subtractBtn = (Button) itemView.findViewById(R.id.item_btn_sub);
            this.addBtn = (Button) itemView.findViewById(R.id.item_btn_add);
            this.itemTxtPrice = (TextView) itemView.findViewById(R.id.item_txt_price);
            this.imgProduct = (ImageView) itemView.findViewById(R.id.img_product);

            itemView.setOnClickListener(this);
            addBtn.setOnClickListener(this);
            subtractBtn.setOnClickListener(this);


        }
        public void bind(OrderParts model) {
            this.itemTxtTitle.setText(model.getName());
            this.itemTxtMessage.setText(String.valueOf(model.getCategoryName()));
            this.itemTxtQty.setText(String.valueOf(model.getQty()));
            this.itemTxtId.setText(model.getId());
            this.itemTxtPrice.setText(String.format(mContext.getString(R.string.price), model.getPrice()));
            //in some cases, it will prevent unwanted situations
            this.itemCheckList.setOnCheckedChangeListener(null);
//            String imgPath = model.getImgPath();
//            if (imgPath.equals("")) {
//                imgPath = "default";
//            }
        }

        @Override
        public void onClick(View v) {
            OrderParts opm = getItem(getAdapterPosition());
            if (v.getId() == itemView.getId()) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition(), opm);
            }
            else if (v.getId() == addBtn.getId()) {
//                modelList.get(getAdapterPosition())
                mItemClickListener.onAddBtnClick(itemTxtQty, checkSet.contains(getAdapterPosition()), getAdapterPosition(), opm);
            }
            else if (v.getId() == subtractBtn.getId()) {
                mItemClickListener.onSubBtnClick(itemTxtQty, checkSet.contains(getAdapterPosition()), getAdapterPosition(), opm);
            }
        }
    }

}

