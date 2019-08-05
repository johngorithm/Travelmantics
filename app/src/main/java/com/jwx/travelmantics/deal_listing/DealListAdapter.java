package com.jwx.travelmantics.deal_listing;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jwx.travelmantics.R;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.deal_creation.InsertActivity;
import com.jwx.travelmantics.models.TravelDeal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.DealViewHolder>{
    private List<TravelDeal> travelDeals = new ArrayList<>();
    private Context mContext;
    private static final String TAG = "DealListAdapter";

    DealListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemRow = inflater.inflate(R.layout.deal_item_row, parent, false);
        return new DealViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        final TravelDeal travelDeal = travelDeals.get(position);

        holder.titleView.setText(travelDeal.getTitle());
        holder.priceView.setText(travelDeal.getPrice());
        holder.descView.setText(travelDeal.getDescription());

        showImage(travelDeal.getImage(), holder.dealImageView);

        holder.dealCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InsertActivity.class);
                intent.putExtra(Constants.DEAL_DATA_ID, travelDeal);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return travelDeals.size();
    }

    class DealViewHolder extends RecyclerView.ViewHolder{
        private ImageView dealImageView;
        private TextView titleView, priceView, descView;
        private CardView dealCardView;

        DealViewHolder(@NonNull View itemView) {
            super(itemView);
            dealImageView = itemView.findViewById(R.id.iv_deal_image);
            titleView = itemView.findViewById(R.id.tv_deal_title);
            priceView = itemView.findViewById(R.id.tv_price);
            descView = itemView.findViewById(R.id.tv_deal_desc);
            dealCardView = itemView.findViewById(R.id.deal_card);
        }
    }

    void setDeal(TravelDeal deal) {
        travelDeals.add(deal);
        notifyItemInserted(travelDeals.size() - 1);
    }

    public void showImage(String imageUrl, ImageView dealImageView) {
        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .resize(100, 100)
                    .centerCrop()
                    .into(dealImageView);
        }
    }
}
