package com.jwx.travelmantics.deal_creation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jwx.travelmantics.R;
import com.jwx.travelmantics.models.TravelDeal;

public class InsertActivity extends AppCompatActivity implements InsertView {
    private ProgressDialog progressDialog;
    private DealPresenter dealPresenter;

    private EditText titleInput, priceInput, descInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initProperties();
    }

    private void initProperties() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving ...");

        titleInput = findViewById(R.id.deal_name_input);
        priceInput = findViewById(R.id.price_input);
        descInput = findViewById(R.id.description_input);

        dealPresenter = new DealPresenter(this);
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
        progressDialog.show();

        String title = titleInput.getText().toString();
        String price = priceInput.getText().toString();
        String desc = descInput.getText().toString();

        // TODO: VALIDATE USER INPUT;

        TravelDeal travelDeal = new TravelDeal(title, price, desc, null);

        dealPresenter.saveTravelDealData(travelDeal);
    }

    @Override
    public void onSaveComplete() {
        progressDialog.hide();
        clearInputs();
        Toast.makeText(this, "Deal Saved!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveError() {
        progressDialog.hide();
        Toast.makeText(this, "Oops!... Unable to save data. Mind trying again?", Toast.LENGTH_SHORT).show();
    }
}
