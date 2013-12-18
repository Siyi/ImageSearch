package com.my.imagesearch;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.bean.Data;
import com.my.bean.Photo;
import com.my.bean.Photos;

import android.os.AsyncTask;
import android.util.Log;


public class CallApi extends AsyncTask<String, Void, Photos> {
	private static final String TAG = "CallApi";
	private WeakReference<CallBack> callback;

	public CallApi(CallBack callback) {
		this.callback = new WeakReference<CallBack>(callback);
	}

	@Override
	protected Photos doInBackground(String... params) {
		String query = params[0];
		HttpGet get = null;
		try {
			get = new HttpGet(new URI(query));
		} catch (Exception e) {
			System.out.println(e);
		}
		if(get==null) return null;
		get.addHeader("Accept", "application/json");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		Data data = null;
		InputStream stream=null;
		try {
			response = httpClient.execute(get);
			int code = response.getStatusLine().getStatusCode();
			stream = response.getEntity().getContent();
			if (code >= 200 && code < 300) {
				ObjectMapper om = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false).configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
				data = om.readValue(stream, Data.class);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}finally{
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}	
		Photos ps= data.getPhotos();
		return ps;
	}

	@Override
	protected void onPostExecute(Photos result) {
		if (callback != null && result != null) {
			CallBack cb = callback.get();
			if (cb != null) {
				cb.setPhoto(result.getPhoto());
			}
		}
	}
	
	public interface CallBack{
		void setPhoto(List<Photo> photoList);
	}
}
