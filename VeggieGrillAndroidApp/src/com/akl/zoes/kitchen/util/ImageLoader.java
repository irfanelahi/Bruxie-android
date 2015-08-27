package com.akl.zoes.kitchen.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private static HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

	private static File cacheDir;

	public ImageLoader(Context context) {
		// Make the background thead low priority. This way it will not affect
		// the UI performance
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"LazyList");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	// final int stub_id = R.drawable.born_to_be_wild;

	public void DisplayImage(String url, Activity activity, ImageView imageView) {

		// if (url != null && cache.containsKey(url)) {
		// // imageView.setImageBitmap(cache.get(url));
		// createImages(cache.get(url), imageView);
		// cache.clear();
		// } else
		{
			queuePhoto(url, activity, imageView);
			// imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, Activity activity, ImageView imageView) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	public Bitmap getBitmap(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);

		// Log.e("urlgetBitmap",url);
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			InputStream is = new URL(url).openStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			stopThread();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(ImageView image) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image)
					photosToLoad.remove(j);
				else
					++j;
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						Bitmap bmp = getBitmap(photoToLoad.url);
						if (bmp != null) {
							cache.put(photoToLoad.url, bmp);
							Object tag = photoToLoad.imageView.getTag();
							if (tag != null
									&& ((String) tag).equals(photoToLoad.url)) {
								BitmapDisplayer bd = new BitmapDisplayer(bmp,
										photoToLoad.imageView);
								Activity a = (Activity) photoToLoad.imageView
										.getContext();
								a.runOnUiThread(bd);
							}
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null)
				// imageView.setImageBitmap(bitmap);y
				try {
					createImages(bitmap, imageView);
				} catch (Exception e) {
					// TODO: handle exception
				}
			// else
			// imageView.setImageResource(stub_id);
		}
	}

	private void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 2048;
		if (is != null) {
			try {
				byte[] bytes = new byte[buffer_size];
				for (;;) {
					int count = is.read(bytes, 0, buffer_size);
					if (count == -1)
						break;
					os.write(bytes, 0, count);
				}
			} catch (Exception ex) {

				Log.e("Exception", ex + "");
				stopThread();
			}
		}
	}

	public static void clearCache() {
		// clear memory cache
		if (cache != null)
			cache.clear();
		// clear SD cache
		File[] files = null;
		if (cacheDir != null)
			files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files)
				f.delete();
		}
	}

	public void createImages(Bitmap bitmapOrg, ImageView imageView) {
		try {
			imageView.setImageBitmap(bitmapOrg);
			InputStream input = new URL((String) imageView.getTag())
					.openStream();
			// InputStream input = BitmapFactory.Org.url.openStream();
			try {
				// The sdcard directory e.g. '/sdcard' can be used directly, or
				// more safely abstracted with getExternalStorageDirectory()
				File storagePath = Environment.getExternalStorageDirectory();
				String path = storagePath.getAbsolutePath() + "/Zoe";
				storagePath = new File(path);
				if (!storagePath.exists())
					storagePath.mkdir();
				OutputStream output = new FileOutputStream(new File(
						storagePath, "myImage.png"));
				try {
					byte[] buffer = new byte[1024];
					int bytesRead = 0;
					while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
						output.write(buffer, 0, bytesRead);
					}
				} finally {
					output.close();
				}
			} finally {
				input.close();
			}

			// int width = bitmapOrg.getWidth(); /* 140 */
			// int height = bitmapOrg.getHeight();
			//
			// Display display =
			// HomePage.getInstance().getWindowManager().getDefaultDisplay();
			// int newWidth = display.getWidth() - 25;
			// int newHeight = display.getHeight() - 25;
			//
			// double ratio = (double) width / height;
			// newHeight = (int) (newHeight / ratio);
			// float scaleWidth = ((float) newWidth) / width;
			// float scaleHeight = ((float) newHeight) / height;
			//
			// Matrix matrix = new Matrix();
			// matrix.postScale(scaleWidth, scaleHeight);
			// Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
			// width,
			// height, matrix, true);
			// imageView.setImageBitmap(resizedBitmap);
			// BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
			// imageView.setImageDrawable(bmd);
			// imageView.setScaleType(ScaleType.FIT_CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}