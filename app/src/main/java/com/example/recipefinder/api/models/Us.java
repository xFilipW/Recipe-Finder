package com.example.recipefinder.api.models;

import com.google.gson.annotations.SerializedName;

public class Us{

	@SerializedName("amount")
	private Object amount;

	@SerializedName("unitShort")
	private String unitShort;

	@SerializedName("unitLong")
	private String unitLong;

	public void setAmount(Object amount){
		this.amount = amount;
	}

	public Object getAmount(){
		return amount;
	}

	public void setUnitShort(String unitShort){
		this.unitShort = unitShort;
	}

	public String getUnitShort(){
		return unitShort;
	}

	public void setUnitLong(String unitLong){
		this.unitLong = unitLong;
	}

	public String getUnitLong(){
		return unitLong;
	}
}