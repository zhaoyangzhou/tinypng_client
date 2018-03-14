package com.zzy.learn.swing.tabs.tinypng.model.ia;

public interface Callback {
	/**单个任务准备开始执行
	 * @param data
	 */
	void onPreExecute(Object data);
	/**单个任务更新进度
	 * @param data
	 */
	void onProgressUpdate(Object data);
	void onPostSuccess(Object data);
	/**单个任务出现错误
	 * @param e
	 * @param data
	 */
	void onPostError(Exception e, Object data);
	/**完成所有任务
	 * 
	 */
	void onComplete();
}
