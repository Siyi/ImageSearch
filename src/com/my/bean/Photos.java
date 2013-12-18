package com.my.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Photos implements Serializable{
	private List<Photo> photo = new ArrayList<Photo>();

	public List<Photo> getPhoto() {
		return photo;
	}

	public void setPhoto(List<Photo> photo) {
		this.photo = photo;
	}
	
}
