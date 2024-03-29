package com.jwx.travelmantics.deal_creation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jwx.travelmantics.R;
import com.jwx.travelmantics.common.BaseActivity;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.models.TravelDeal;
import com.jwx.travelmantics.services.FirebaseApiService;
import com.squareup.picasso.Picasso;

public class InsertActivity extends BaseActivity implements InsertView {
    private DealPresenter dealPresenter;
    private TravelDeal deal;
    private static final int PICTURE_REQUEST_CODE = 19;

    private EditText titleInput, priceInput, descInput;
    private ImageView dealImageView;
    private Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);


        initProperties();
    }

    private void initProperties() {

        titleInput = findViewById(R.id.deal_name_input);
        priceInput = findViewById(R.id.price_input);
        descInput = findViewById(R.id.description_input);
        dealImageView = findViewById(R.id.deal_image_view);
        uploadButton = findViewById(R.id.upload_btn);

        dealPresenter = new DealPresenter(this);

        if (getIntent().hasExtra(Constants.DEAL_DATA_ID)) {
            deal = (TravelDeal) getIntent().getSerializableExtra(Constants.DEAL_DATA_ID);
        } else  {
            deal = new TravelDeal();
        }

        titleInput.setText(deal.getTitle());
        priceInput.setText(deal.getPrice());
        descInput.setText(deal.getDescription());

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");

                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Select Picture"), PICTURE_REQUEST_CODE);
            }
        });

        if (deal.getImage() != null) {
            showImage(deal.getImage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);

        MenuItem saveOption = menu.findItem(R.id.menu_save_option);
        MenuItem deleteOption = menu.findItem(R.id.delete_deal_option);

        if (FirebaseApiService.isAdmin) {
            saveOption.setVisible(true);
            deleteOption.setVisible(true);
            enableEditFields(true);
            uploadButton.setEnabled(true);
        } else {
            saveOption.setVisible(false);
            deleteOption.setVisible(false);
            enableEditFields(false);
            uploadButton.setEnabled(false);
        }
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
    }

    private void showListActivity(){
        onBackPressed();
    }


    private void enableEditFields(boolean isEnabled) {
        titleInput.setEnabled(isEnabled);
        priceInput.setEnabled(isEnabled);
        descInput.setEnabled(isEnabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            super.showProgressDialog("Uploading ...");
            dealPresenter.uploadImage(imageUri);
        }
    }

    @Override
    public void onImageUploadSuccess(String imageUrl, String imageName) {
        deal.setImage(imageUrl);
        deal.setImageName(imageName);
        showImage(imageUrl);
        super.hideProgressDialog();
    }

    public void showImage(String imageUrl) {
        if (imageUrl != null) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(imageUrl)
                    .resize(width, width * 2/3)
                    .centerCrop()
                    .into(dealImageView);
        }
    }


}
