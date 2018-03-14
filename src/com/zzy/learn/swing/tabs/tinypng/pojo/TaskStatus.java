package com.zzy.learn.swing.tabs.tinypng.pojo;

public enum TaskStatus {
	WAITING("等待"),
	DOING("处理中"),
	SUCCESS("成功"),
	ERROR("失败");
	
	private final String value;
    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
