package com.example.recipefinder.api.models;

import com.google.gson.annotations.SerializedName;

public class Temperature{

	@SerializedName("number")
	private Object number;

	@SerializedName("unit")
	private String unit;

	public void setNumber(Object number){
		this.number = number;
	}

	public Object getNumber(){
		return number;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}
}