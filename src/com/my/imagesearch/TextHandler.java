package com.my.imagesearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import com.my.bean.Photo;
import com.my.imagesearch.CallApi.CallBack;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TextHandler extends Handler {
	private static final String TAG = "TextHandler";
	private String query;
	private CallBack cb;

	TextHandler(String query, final ImageAdpater imageAdapter) {
		this.query = query;
		cb = new CallBack() {
			@Override
			public void setPhoto(List<Photo> photoList) {
				imageAdapter.replaceAll(photoList);
			}
		};
	}

	public void handleMessage(Message msg) {
		String input = (String) msg.obj;
		String request = null;
		if (input != null && input.length() != 0) {
			try {
				request = query + (URLEncoder.encode(input, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, e.getMessage());
			}
			if (request != null) {
				CallApi call = new CallApi(cb);
				call.execute(request);
			}
		}
	}
}
