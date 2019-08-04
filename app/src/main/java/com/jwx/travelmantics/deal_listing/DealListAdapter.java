package com.jwx.travelmantics.deal_listing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jwx.travelmantics.R;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.deal_creation.InsertActivity;
import com.jwx.travelmantics.models.TravelDeal;

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
        TravelDeal travelDeal = travelDeals.get(position);

        holder.titleView.setText(travelDeal.getTitle());
        holder.priceView.setText(travelDeal.getPrice());
        holder.descView.setText(travelDeal.getDescription());
    }

    @Override
    public int getItemCount() {
        return travelDeals.size();
    }

    class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView dealImage;
        private TextView titleView, priceView, descView;

        DealViewHolder(@NonNull View itemView) {
            super(itemView);
            dealImage = itemView.findViewById(R.id.iv_deal_image);
            titleView = itemView.findViewById(R.id.tv_deal_title);
            priceView = itemView.findViewById(R.id.tv_price);
            descView = itemView.findViewById(R.id.tv_deal_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.i(TAG, "onClick: "+position);

            TravelDeal deal = travelDeals.get(position);
            Intent intent = new Intent(mContext, InsertActivity.class);
            intent.putExtra(Constants.DEAL_DATA_ID, deal);
            view.getContext().startActivity(intent);
        }
    }

    void setDeal(TravelDeal deal) {
        travelDeals.add(deal);
        notifyItemInserted(travelDeals.size() - 1);
    }
}
