package com.example.recipefinder.api.listeners;

import com.example.recipefinder.api.models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
