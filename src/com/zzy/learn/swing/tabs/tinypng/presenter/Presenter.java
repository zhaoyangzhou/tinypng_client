package com.zzy.learn.swing.tabs.tinypng.presenter;

import java.util.Vector;

import com.zzy.learn.swing.tabs.tinypng.model.Model;
import com.zzy.learn.swing.tabs.tinypng.model.ia.Callback;
import com.zzy.learn.swing.tabs.tinypng.model.ia.ModelIA;
import com.zzy.learn.swing.tabs.tinypng.view.ia.ViewIA;

public class Presenter {
	private ViewIA viewIA;
	private ModelIA modelIA;
	public Presenter(ViewIA viewIA) {
        this.modelIA = new Model();
        this.viewIA = viewIA;
    }

    public void doRequest(Vector data, int width, int height) {
    	modelIA.doRequest(data, width, height, new Callback() {
			
			@Override
			public void onProgressUpdate(Object data) {
				viewIA.onProgressUpdate(data);
			}
			
			@Override
			public void onPreExecute(Object data) {
				viewIA.onPreExecute(data);
			}
			
			@Override
			public void onPostSuccess(Object data) {
				viewIA.onPostSuccess(data);
			}
			
			@Override
			public void onPostError(Exception e, Object data) {
				viewIA.onPostError(e, data);
			}

			@Override
			public void onComplete() {
				viewIA.onComplete();
			}
		});
    }
    
    public void cancelAll() {
    	modelIA.cancelAll();
    }
}
