package com.zzy.learn.swing.tabs.tinypng.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.zzy.learn.swing.tabs.tinypng.pojo.TaskStatus;

public class TaskTableCellRenderer extends DefaultTableCellRenderer {
	private boolean showIcon = true;
	
	public TaskTableCellRenderer(boolean showIcon) {
		this.showIcon = showIcon;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setIcon(null);
		setText("");
		switch (column) {
		case 0: {
			if (showIcon) {
				setAlignmentX(CENTER_ALIGNMENT);
				String cellValue = (String) value;
				ImageIcon icon = new ImageIcon(cellValue);
				Image img = icon.getImage().getScaledInstance(50, 50,
						Image.SCALE_FAST);
				setIcon(new ImageIcon(img));
				setIconTextGap(30);
			}
			break;
		}
		case 1: {
			String cellValue = (String) value;
			setText(cellValue);
			break;
		}
		case 2: {
			TaskStatus cellValue = (TaskStatus) value;
			setText(cellValue.getValue());
			break;
		}
		case 3: {
			long cellValue = (long) value;
			setText(getByteUnit(cellValue));
			break;
		}
		case 4: {
			long cellValue = (long) value;
			setText(getByteUnit(cellValue));
			break;
		}
		case 5: {
			String cellValue = (String) value;
			setText(String.format("%s%%", cellValue));
			break;
		}
		}
		
		if (row % 2 == 0) {
			setBackground(Color.decode("#FFFFFF")); // 设置奇数行底色
		} else if (row % 2 == 1) {
			setBackground(Color.decode("#F0F0F0")); // 设置奇数行底色
		}
		return this;
	}

	public void setShowIcon(boolean showIcon) {
		this.showIcon = showIcon;
	}
	
	private String getByteUnit(long val) {
		if (val >= 0 && val < 1024) {
			return String.format("%sB", val);
		} else if (val >= 1024 && val < 1024 * 1024) {
			return String.format("%.2fKB", (float) val / 1024);
		} else if (val >= 1024 && val < 1024 * 1024 * 1024) {
			return String.format("%.2fMB", (float) val / 1024 / 1024);
		} else {
			return String.format("%.2fGB", (float) val / 1024 / 1024 / 1024);
		}
	}
}
