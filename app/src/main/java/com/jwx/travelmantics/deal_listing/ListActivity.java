package com.jwx.travelmantics.deal_listing;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jwx.travelmantics.R;
import com.jwx.travelmantics.common.BaseActivity;
import com.jwx.travelmantics.models.TravelDeal;

public class ListActivity extends BaseActivity implements DealListView {
    private DealListAdapter adapter;
    private DealListPresenter dealListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initProperties();
    }

    private void initProperties() {
        dealListPresenter = new DealListPresenter(this);

        RecyclerView dealRecyclerView = findViewById(R.id.deal_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dealRecyclerView.setLayoutManager(layoutManager);

        adapter = new DealListAdapter(this);
        dealRecyclerView.setAdapter(adapter);
    }



    @Override
    protected void onStart() {
        showProgressDialog("Loading ...");
        dealListPresenter.fetchDeals();
        super.onStart();
    }

    @Override
    public void onDealAdded(TravelDeal deal) {
        super.hideProgressDialog();
        adapter.setDeal(deal);
    }
}
