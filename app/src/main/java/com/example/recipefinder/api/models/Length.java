package com.example.recipefinder.api.models;

import com.google.gson.annotations.SerializedName;

public class Length{

	@SerializedName("number")
	private int number;

	@SerializedName("unit")
	private String unit;

	public void setNumber(int number){
		this.number = number;
	}

	public int getNumber(){
		return number;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}
}