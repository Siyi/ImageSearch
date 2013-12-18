package com.my.imagesearch;

import java.util.ArrayList;
import java.util.List;

import com.example.imagesearch.R;
import com.my.bean.Photo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdpater extends BaseAdapter {
	protected final List<Photo> items;
	private LruCache<String, Bitmap> cache;
	private LayoutInflater inflater;

	
	public ImageAdpater(Activity context) {
		super();
		this.items = new ArrayList<Photo>();	
		this.inflater= LayoutInflater.from(context);
		
	}
	
	void initCache(){
		cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()) / 8) {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
					return bitmap.getByteCount();
				else
					return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};	
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Photo getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<Photo> getItems() {
		return items;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.image_info, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Photo photo = getItem(position);
		viewHolder.textView.setText(photo.getTitle());
		String imageUrl = "http://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_t.jpg";
		viewHolder.imageView.setTag(imageUrl);
		if (cache.get(imageUrl) != null) {
			viewHolder.imageView.setImageBitmap(cache.get(imageUrl));
		} else {
			DownloadImage task = new DownloadImage(viewHolder.imageView, cache, imageUrl);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
			else
				task.execute(imageUrl);
		}
		return convertView;
	}

	
	public void replaceAll(List<Photo> newItems) {
		items.clear();
		items.addAll(newItems);
		notifyDataSetChanged();
	}
	
   private static class ViewHolder{
		ImageView imageView;
		TextView textView;
	}
}
