package com.my.imagesearch;


import com.example.imagesearch.R;
import com.my.bean.Photo;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	private static final String MESSAGE = "message";
	private EditText editText;
	private ImageAdpater imageAdapter;
	private ListView listView;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.editText);
		listView = (ListView) findViewById(R.id.listview);
		imageAdapter = new ImageAdpater(this);
		imageAdapter.initCache();
		listView.setAdapter(imageAdapter);
		listView.setOnItemClickListener(new ListViewListener());
		editText.addTextChangedListener(new SearchTextWater());
		handler = new TextHandler(this.getResources().getString(R.string.query_api), imageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {	
		super.onPause();
	}
	private class SearchTextWater implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			handler.removeMessages(0);
			handler.sendMessageDelayed(handler.obtainMessage(0, s.toString().trim()), 900);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
	}

	private class ListViewListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			Photo photo = imageAdapter.getItem(position);
			String imageUrl = "http://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_m.jpg";
			Intent intent = new Intent(MainActivity.this, ImageActivity.class);
			intent.putExtra(MESSAGE, imageUrl);
			startActivity(intent);
		}
	}

}
