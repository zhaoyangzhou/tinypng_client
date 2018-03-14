package com.zzy.learn.swing.tabs.tinypng.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.tinify.Options;
import com.tinify.Source;
import com.tinify.TLSContext;
import com.tinify.Tinify;
import com.zzy.learn.swing.tabs.tinypng.model.ia.Callback;
import com.zzy.learn.swing.tabs.tinypng.model.ia.ModelIA;
import com.zzy.learn.swing.tabs.tinypng.pojo.TaskStatus;

public class Model implements ModelIA {
	public static final String DIR_NAME = "tinypng";
	public static final int DEFAULT_WIDTH = 0;
	public static final int DEFAULT_HEIGHT = 0;
	protected Vector<SwingWorker> workerList = new Vector<>();

	@Override
	public void doRequest(final Vector data, final int width, final int height, final Callback callback) {
		if (callback != null) {
			callback.onPreExecute(data);
		}
		SwingWorker worker = new SwingWorker<Object, Integer>() {
		    @Override
		    public Object doInBackground() throws Exception {
		    	publish(0);
		    	Vector taskInfo = (Vector) data;
		    	File sourceFile = new File((String) taskInfo.get(0));
				File tempDir = new File(sourceFile.getParent() + File.separator
						+ DIR_NAME);
				if (!tempDir.exists()) {
					tempDir.mkdirs();
				}
				String destFilePath = sourceFile.getParent() + File.separator
						+ DIR_NAME + File.separator + sourceFile.getName();

				Tinify.setKey(getAPIKey());
				// Tinify.setProxy("http://user:pass@192.168.0.1:8080");
				System.out.println(sourceFile.getAbsolutePath());
				Source source = Tinify.fromFile(sourceFile.getAbsolutePath());
				if (width > 0 && height > 0) {
					Options options = new Options().with("method", "fit");
					options.with("width", width).with("height", height);
					Source resized = source.resize(options);
					resized.toFile(destFilePath);
				} else {
					source.toFile(destFilePath);
				}

				File destFile = new File(destFilePath);
				if (destFile.exists()) {
					taskInfo.set(4, destFile.length());
					taskInfo.set(5, String.format("%.2f", (sourceFile.length() - destFile.length())
							/ (double) sourceFile.length() * 100));
					taskInfo.add(destFile.getAbsolutePath());
				}
				return taskInfo;
		    }

		    @Override
		    public void done() {
		    	try {
		    		if (!isCancelled()) {
		    			workerList.remove(this);
						Vector taskInfo = (Vector) get();
						taskInfo.set(2, TaskStatus.SUCCESS);
						if (callback != null) {
							callback.onPostSuccess(taskInfo);
							if (workerList.isEmpty()) {
								callback.onComplete();
							}
						}
		    		}
				} catch (InterruptedException e) {
					e.printStackTrace();
					Vector taskInfo = (Vector) data;
					taskInfo.set(2, TaskStatus.ERROR);
					if (callback != null) {
						callback.onPostError(e, taskInfo);
					}
				} catch (ExecutionException e) {
					e.printStackTrace();
					Vector taskInfo = (Vector) data;
					taskInfo.set(2, TaskStatus.ERROR);
					if (callback != null) {
						callback.onPostError(e, taskInfo);
					}
				} catch (CancellationException e) {
					e.printStackTrace();
					Vector taskInfo = (Vector) data;
					taskInfo.set(2, TaskStatus.ERROR);
					if (callback != null) {
						callback.onPostError(e, taskInfo);
					}
				}
		    }

			@Override
			protected void process(List<Integer> chunks) {
				super.process(chunks);
				Vector taskInfo = (Vector) data;
				taskInfo.set(2, TaskStatus.DOING);
				if (callback != null) {
					callback.onProgressUpdate(taskInfo);
				}
			}
		};
		worker.execute();
		workerList.add(worker);
	}

	@Override
	public void cancelAll() {
		for (SwingWorker worker : workerList) {
			if (!worker.isCancelled()) {
				worker.cancel(true);
			}
		}
	}
	
	private String getAPIKey() throws IOException {
		String filePath = System.getProperty("user.dir") + "/api_key.properties";    
		InputStream in = new BufferedInputStream(new FileInputStream(filePath));  
		Properties prop = new Properties();  
		prop.load(in);  
		in.close();
		return prop.getProperty("key");
	}
}
