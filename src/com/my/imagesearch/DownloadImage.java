package com.my.imagesearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImage extends AsyncTask<String, Void, Drawable> {
	private static final String TAG = "DownloadImage";
	private final WeakReference<ImageView> imageViewReference;
	private LruCache<String, Bitmap> cache;
	private String imageUrl;
	private File fileDir;

	DownloadImage(ImageView imageView, LruCache<String, Bitmap> cache, String imageUrl) {
		fileDir = imageView.getContext().getCacheDir();
		this.imageViewReference = new WeakReference<ImageView>(imageView);
		this.cache = cache;
		this.imageUrl = imageUrl;
	}

	@Override
	protected Drawable doInBackground(String... params) {
		String imageUrl = params[0];
		Drawable drawable = null;
		String fileName = "";
		if (imageUrl != null && imageUrl.length() != 0)
			fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		File file = new File(fileDir, fileName);
		if (!file.exists() && !file.isDirectory()) {
			InputStream stream = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				stream = new URL(imageUrl).openStream();
				int data = stream.read();
				while (data != -1) {
					fos.write(data);
					data = stream.read();
				}
				fos.close();
				stream.close();
				drawable = Drawable.createFromPath(file.toString());
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
		} else {
			drawable = Drawable.createFromPath(file.toString());
		}
		if (drawable != null && cache != null)
			cache.put(imageUrl, ((BitmapDrawable) drawable).getBitmap());
		return drawable;
	}

	@Override
	protected void onPostExecute(final Drawable drawable) {
		if (imageViewReference != null && drawable != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null && imageView.getTag() == imageUrl)
				imageView.setImageDrawable(drawable);
		}
	}
}
