package com.example.recipefinder.api.listeners;

import com.example.recipefinder.api.models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void onSuccess(RandomRecipeApiResponse response, String message);
    void onError(String message);
}
