package com.jwx.travelmantics.deal_creation;

public interface InsertView {
    void onSaveComplete(String message);
    void onSaveError(String message);
    void onDeleteDeal(String message);
    void onDeleteDealError(String message);
    void onImageUploadSuccess(String imageUrl);
}
