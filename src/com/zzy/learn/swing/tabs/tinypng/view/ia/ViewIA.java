package com.zzy.learn.swing.tabs.tinypng.view.ia;

import java.io.File;
import java.util.Vector;

import com.zzy.learn.swing.tabs.tinypng.model.ia.Callback;

public interface ViewIA extends Callback {
	void doRequest(File file);
	void doCancel();
}
