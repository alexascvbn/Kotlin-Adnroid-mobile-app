package com.example.bottonavigation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bottonavigation.R;
import com.example.bottonavigation.model.BranchModel;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

public class BranchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<BranchModel> modelList;
    private String storage_url;

    private OnItemClickListener mItemClickListener;

    public BranchListAdapter(Context context, ArrayList<BranchModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        storage_url = mContext.getString(R.string.storage_url);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<BranchModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.branch_filp_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final BranchModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.itemTxtTitle.setText(model.getAddress());
            genericViewHolder.itemTxtOpenHour.setText(model.getOpenHour());
            String contact = mContext.getString(R.string.item_txt_phone) + model.getPhoneNo();
            genericViewHolder.itemTxtContact.setText(contact);

            int btype;
            switch (model.getBType()) {
                case 0:
                    btype = R.string.brand_type_exh;
                    break;
                case 1:
                    btype = R.string.brand_type_main;
                    break;
                case 2:
                    btype = R.string.brand_type_parts;
                    break;
                case 3:
                    btype = R.string.brand_type_rental;
                    break;
                default:
                    btype = R.string.brand_type_unclassifiy;
                    break;
            }
            genericViewHolder.itemTxtBType.setText(mContext.getString(btype));
            String img_url = String.format(storage_url, "branch", "%2F", model.getImgPath());
            Glide.with(mContext)
                    .load(img_url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(ContextCompat.getDrawable(mContext, R.drawable.inchcape_mobility))
                    .into(genericViewHolder.imgBranch);
//            String slogn_url = storage_url + model.getSlogn() + "?alt=media";
            String slogn_url = String.format(storage_url, "branch", "%2F", model.getSlogn());
            if (model.getSlogn() != null) {

                Glide.with(mContext)
                        .load(slogn_url)
                        .into(genericViewHolder.imgSlogn);
            }
            else {

                genericViewHolder.imgSlogn.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.dot_background));
            }
//                Log.d("johnson***", model.getSlogn());
//            if (!Objects.equals(model.getSlogn(), "null"))
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private BranchModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, BranchModel model);
        void onContactClick(View view, int position, BranchModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imgBranch;
        private ImageView imgSlogn;
        private TextView itemTxtTitle;
        private TextView itemTxtOpenHour;
        private TextView itemTxtContact;
        private EasyFlipView myEasyFlipView;
        private Button btnLocation;
        private Button btnContact;
        private TextView itemTxtBType;

        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.imgBranch = (ImageView) itemView.findViewById(R.id.item_branch_img);
            this.imgSlogn = (ImageView) itemView.findViewById(R.id.item_slogn_img);
            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.part_txt_name);
            this.itemTxtOpenHour = (TextView) itemView.findViewById(R.id.item_txt_type);
            this.itemTxtContact = (TextView) itemView.findViewById(R.id.item_txt_contact);
            this.myEasyFlipView = (EasyFlipView) itemView.findViewById(R.id.myEasyFlipView);
            this.btnLocation = (Button) itemView.findViewById(R.id.item_map_button);
            this.btnContact = (Button) itemView.findViewById(R.id.item_contact_button);
            this.itemTxtBType = (TextView) itemView.findViewById(R.id.item_txt_bType);


            itemView.setOnClickListener(this);
            this.btnContact.setOnClickListener(this);
            this.btnLocation.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v.getId() == btnContact.getId()) {
                    mItemClickListener.onContactClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
                else if (v.getId() == btnLocation.getId()) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
                else {
                    myEasyFlipView.flipTheView();
                }
            }
        }
    }

}
