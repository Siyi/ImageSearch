package com.my.bean;

import java.io.Serializable;


public class Data implements Serializable {
	private Photos photos;

	public Photos getPhotos() {
		return photos;
	}

	public void setPhotos(Photos photos) {
		this.photos = photos;
	}
}
