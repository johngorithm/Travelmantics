package com.jwx.travelmantics.deal_listing;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jwx.travelmantics.R;
import com.jwx.travelmantics.common.BaseActivity;
import com.jwx.travelmantics.deal_creation.InsertActivity;
import com.jwx.travelmantics.models.TravelDeal;
import com.jwx.travelmantics.services.FirebaseApiService;

public class ListActivity extends BaseActivity implements DealListView {
    private DealListAdapter adapter;
    private DealListPresenter dealListPresenter;
    private RecyclerView dealRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FirebaseApiService.initAuthState(this);

        initProperties();
    }

    private void initProperties() {
        dealListPresenter = new DealListPresenter(this);

        dealRecyclerView = findViewById(R.id.deal_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dealRecyclerView.setLayoutManager(layoutManager);



        showProgressDialog("Loading ...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);

        MenuItem insertMenu = menu.findItem(R.id.new_deal_option);
        if (FirebaseApiService.isAdmin) {
            insertMenu.setVisible(true);
        } else {
            insertMenu.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_deal_option:
                showInsertDealActivity();
                break;
            case R.id.logout_option:
                logout();
        }
        return true;
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ListActivity.this, "Logout Successful", Toast.LENGTH_LONG).show();
                            FirebaseApiService.addAuthListener();
                            return;
                        }

                        Toast.makeText(ListActivity.this, "Unable to logout. Mind trying again?", Toast.LENGTH_LONG).show();
                    }
                });
        FirebaseApiService.removeAuthListener();
    }

    private void showInsertDealActivity() {
        Intent intent = new Intent(ListActivity.this, InsertActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDealAdded(TravelDeal deal) {
        super.hideProgressDialog();
        adapter.setDeal(deal);
    }

    @Override
    protected void onResume() {
        adapter = new DealListAdapter(this);
        dealRecyclerView.setAdapter(adapter);
        FirebaseApiService.addAuthListener();
        dealListPresenter.fetchDeals();
        super.onResume();
    }

    @Override
    protected void onPause() {
        FirebaseApiService.removeAuthListener();
        super.onPause();
    }


    public void showMenu() {
        invalidateOptionsMenu();
    }
}
