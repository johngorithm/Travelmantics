package com.jwx.travelmantics.deal_creation;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jwx.travelmantics.R;
import com.jwx.travelmantics.common.BaseActivity;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.deal_listing.ListActivity;
import com.jwx.travelmantics.models.TravelDeal;

public class InsertActivity extends BaseActivity implements InsertView {
    private DealPresenter dealPresenter;
    private TravelDeal deal;

    private EditText titleInput, priceInput, descInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initProperties();
    }

    private void initProperties() {

        titleInput = findViewById(R.id.deal_name_input);
        priceInput = findViewById(R.id.price_input);
        descInput = findViewById(R.id.description_input);

        dealPresenter = new DealPresenter(this);

        if (getIntent().hasExtra(Constants.DEAL_DATA_ID)) {
            deal = (TravelDeal) getIntent().getSerializableExtra(Constants.DEAL_DATA_ID);
        } else  {
            deal = new TravelDeal();
        }

        titleInput.setText(deal.getTitle());
        priceInput.setText(deal.getPrice());
        descInput.setText(deal.getDescription());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_option:
                saveDealData();
                break;
            case R.id.delete_deal_option:
                deleteDeal();
                break;
        }
        return true;
    }

    private void clearInputs() {
        titleInput.setText("");
        priceInput.setText("");
        descInput.setText("");

        titleInput.requestFocus();
    }

    private void saveDealData() {
        super.showProgressDialog("Saving ...");

        deal.setTitle(titleInput.getText().toString());
        deal.setPrice(priceInput.getText().toString());
        deal.setDescription(descInput.getText().toString());

        // TODO: VALIDATE USER INPUT;

        if (deal.getUid() != null) {
            dealPresenter.updateDeal(deal);
            return;
        }
        dealPresenter.saveTravelDealData(deal);
    }

    private void deleteDeal() {
        if (deal.getUid() == null) {
            Toast.makeText(this, "Deletion Failed. Have you saved the deal?", Toast.LENGTH_LONG).show();
            return;
        }

        dealPresenter.deleteDeal(deal);
    }

    @Override
    public void onSaveComplete(String message) {
        super.hideProgressDialog();
        clearInputs();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        showListActivity();
    }

    @Override
    public void onSaveError(String message) {
        super.hideProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showListActivity();
    }

    @Override
    public void onDeleteDeal(String message) {
        super.hideProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showListActivity();
    }

    @Override
    public void onDeleteDealError(String message) {
        super.hideProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showListActivity();
    }

    private void showListActivity(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }


}
