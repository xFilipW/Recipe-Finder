package com.example.recipefinder.database;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeNutritionValue {
    @SerializedName("nutrients")
    private List<Nutrient> nutrients;

    public List<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public static class Nutrient {
        @SerializedName("name")
        private String name;

        @SerializedName("amount")
        private double amount;
        @SerializedName("unit")
        private String unit; //

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
