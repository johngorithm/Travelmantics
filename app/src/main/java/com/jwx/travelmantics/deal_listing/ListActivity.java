package com.jwx.travelmantics.deal_listing;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jwx.travelmantics.R;
import com.jwx.travelmantics.common.BaseActivity;
import com.jwx.travelmantics.deal_creation.InsertActivity;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_deal_option:
                showInsertDealActivity();
                break;
        }
        return true;
    }

    private void showInsertDealActivity() {
        Intent intent = new Intent(ListActivity.this, InsertActivity.class);
        startActivity(intent);
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
