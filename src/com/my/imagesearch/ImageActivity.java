package com.my.imagesearch;

import com.example.imagesearch.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;

public class ImageActivity extends Activity {
	private static final String MESSAGE = "message";
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		String imageUrl = getIntent().getStringExtra(MESSAGE);
		imageView = (ImageView) findViewById(R.id.largeImage);
		makeApiCall(imageUrl);
	}

	private void makeApiCall(String imageUrl) {
		imageView.setTag(imageUrl);
    	DownloadImage task = new DownloadImage(imageView, null, imageUrl);
		task.execute(imageUrl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
