package com.zzy.learn.swing.tabs.tinypng.model.ia;

import java.util.Vector;

public interface ModelIA {
	void doRequest(Vector data, int width, int height, Callback callback);
	void cancelAll();
}
