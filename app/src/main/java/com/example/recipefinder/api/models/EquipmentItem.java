package com.example.recipefinder.api.models;

import com.google.gson.annotations.SerializedName;

public class EquipmentItem{

	@SerializedName("image")
	private String image;

	@SerializedName("localizedName")
	private String localizedName;

	@SerializedName("name")
	private String name;

	@SerializedName("temperature")
	private Temperature temperature;

	@SerializedName("id")
	private int id;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setLocalizedName(String localizedName){
		this.localizedName = localizedName;
	}

	public String getLocalizedName(){
		return localizedName;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setTemperature(Temperature temperature){
		this.temperature = temperature;
	}

	public Temperature getTemperature(){
		return temperature;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}