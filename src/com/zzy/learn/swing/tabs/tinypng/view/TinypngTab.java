package com.zzy.learn.swing.tabs.tinypng.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.zzy.learn.swing.tabs.tinypng.model.Model;
import com.zzy.learn.swing.tabs.tinypng.pojo.TaskStatus;
import com.zzy.learn.swing.tabs.tinypng.presenter.Presenter;
import com.zzy.learn.swing.tabs.tinypng.view.ia.ViewIA;

public class TinypngTab extends Container implements ActionListener, ViewIA, KeyListener, ChangeListener, MouseListener {

	private JLabel jLabelFile = new JLabel("选择文件");
	private JButton jBtnChooser = new JButton("···");
	private JFileChooser jFileChooser = new JFileChooser();
	private JButton jBtnStart = new JButton("开始");
	private JButton jBtnClear = new JButton("清空");
	private JTextArea jTextFiledir = new JTextArea();
	private JLabel jLabelWidth = new JLabel("宽");
	private JLabel jLabelHeight = new JLabel("高");
	private JTextField jTextWidth = new JTextField();
	private JTextField jTextHeight = new JTextField();
	private JCheckBox jCBShowIcon = new JCheckBox("显示缩略图", true);
	private JWindow pupwindow = null;
	
	private Vector<Vector> dataList = new Vector<>();
	private Vector columnData = new Vector() {
		{
			add("缩略图");
			add("文件名");
			add("状态");
			add("原始大小");
			add("处理后大小");
			add("压缩比");
		}
	};
	private JTable jTable = new JTable(new DefaultTableModel(dataList, columnData) {
		@Override  
        public boolean isCellEditable(int row,int column){  
            return false;  
        }  
	});
	private TaskTableCellRenderer render = new TaskTableCellRenderer(jCBShowIcon.isSelected());
	
	private Presenter presenter;

	public TinypngTab(JWindow pupwindow) {
		this.pupwindow = pupwindow;
		jLabelFile.setBounds(10, 10, 60, 20);
		jTextFiledir.setBounds(70, 10, 200, 20);
		jBtnChooser.setBounds(280, 10, 50, 20);
		jLabelWidth.setBounds(340, 10, 20, 20);
		jTextWidth.setBounds(360, 10, 40, 20);
		jLabelHeight.setBounds(410, 10, 20, 20);
		jTextHeight.setBounds(430, 10, 40, 20);
		jCBShowIcon.setBounds(480, 10, 90, 20);
		jBtnStart.setBounds(580, 10, 90, 20);
		jBtnClear.setBounds(680, 10, 90, 20);
		jTextWidth.setText(String.valueOf(Model.DEFAULT_WIDTH));
		jTextHeight.setText(String.valueOf(Model.DEFAULT_HEIGHT));
		jTextWidth.addKeyListener(this);  
		jTextHeight.addKeyListener(this);  
		jTextWidth.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (jTextWidth.getText() == null || jTextWidth.getText().isEmpty()) {
					jTextWidth.setText(String.valueOf(Model.DEFAULT_WIDTH));
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				jTextWidth.setText("");
			}
		});
		jTextHeight.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (jTextHeight.getText() == null || jTextHeight.getText().isEmpty()) {
					jTextHeight.setText(String.valueOf(Model.DEFAULT_HEIGHT));
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				jTextHeight.setText("");
			}
		});
		add(jLabelFile);
		add(jTextFiledir);
		add(jLabelWidth);
		add(jTextWidth);
		add(jLabelHeight);
		add(jTextHeight);
		add(jCBShowIcon);
		add(jBtnChooser);
		add(jBtnStart);
		add(jBtnClear);

		render.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		for (int i = 0, len = jTable.getColumnCount(); i < len; i++) {
			jTable.getColumnModel().getColumn(i).setCellRenderer(render);
		}
		jTable.getTableHeader().setReorderingAllowed(false);//禁止拖动表头
		jTable.setRowHeight(60);//设置行高
		jTable.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
		JScrollPane jScrollPane = new JScrollPane(jTable);
		jScrollPane.setBounds(10, 40, 770, 295);
		add(jScrollPane);

		jBtnChooser.addActionListener(this);
		jBtnStart.addActionListener(this);
		jBtnClear.addActionListener(this);
		jCBShowIcon.addChangeListener(this);
		jTable.addMouseListener(this);
		presenter = new Presenter(this);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == jBtnChooser) {
			jFileChooser
					.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int status = jFileChooser.showOpenDialog(null);
			if (status == 1) {
				return;
			} else {
				File file = jFileChooser.getSelectedFile();
				jTextFiledir.setText(file.getAbsolutePath());
			}
		} else if (action.getSource() == jBtnStart) {
			jBtnStart.setEnabled(false);
			jBtnStart.updateUI();
			String path = jTextFiledir.getText();
			File pathFile = new File(path);
			doRequest(pathFile);
		} else if (action.getSource() == jBtnClear) {
			dataList.clear();
			((DefaultTableModel) jTable.getModel()).setRowCount(0);
			jTable.updateUI();
		}
	}

	@Override
	public void doRequest(File file) {
		int width = Integer.parseInt(jTextWidth.getText()), height = Integer.parseInt(jTextHeight.getText());
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			for (File childFile : childFiles) {
				if (childFile.isDirectory()) {
					if (childFile.getName().equals(Model.DIR_NAME)) continue;
					doRequest(childFile);
				} else {
					presenter.doRequest(createTask(childFile), width, height);
				}
			}
		} else {
			presenter.doRequest(createTask(file), width, height);
		}
	}

	@Override
	public void onPreExecute(Object data) {
		jTable.updateUI();
	}

	@Override
	public void onProgressUpdate(Object data) {
		Vector taskInfo = (Vector) data;
		taskInfo.set(2, TaskStatus.DOING);
		jTable.updateUI();
	}

	@Override
	public void onPostSuccess(Object data) {
		Vector taskInfo = (Vector) data;
		taskInfo.set(2, TaskStatus.SUCCESS);
		jTable.updateUI();
	}

	@Override
	public void onPostError(Exception e, Object data) {
		Vector taskInfo = (Vector) data;
		taskInfo.set(2, TaskStatus.ERROR);
		jTable.updateUI();
	}

	@Override
	public void onComplete() {
		jBtnStart.setEnabled(true);
		jBtnStart.updateUI();
	}

	@Override
	public void doCancel() {
		presenter.cancelAll();
		jBtnStart.setEnabled(true);
		jBtnStart.updateUI();
	}

	private Vector createTask(File file) {
		Vector taskInfo = new Vector();
		taskInfo.add(file.getAbsolutePath());
		taskInfo.add(file.getName());
		taskInfo.add(TaskStatus.WAITING);
		taskInfo.add(file.length());
		taskInfo.add(0L);
		taskInfo.add("0");
		dataList.add(taskInfo);
		return taskInfo;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int keyChar = e.getKeyChar();                 
		if(!(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)){  
            e.consume(); //关键，屏蔽掉非法输入  
        }  
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// 获取事件源（即复选框本身）
        JCheckBox checkBox = (JCheckBox) e.getSource();
        render.setShowIcon(checkBox.isSelected());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			pupwindow.setLocation(290, e.getYOnScreen());
			int row = jTable.getSelectedRow();
			Vector data = dataList.get(row);
			String filePath = (String) (data.size() == 7 ? data.get(6) : data.get(0));
			ImageIcon icon = new ImageIcon(filePath);
			Image img = icon.getImage().getScaledInstance(100, 100,
					Image.SCALE_FAST);
			((JLabel) pupwindow.getContentPane().getComponent(0)).setIcon(new ImageIcon(img));
			pupwindow.setIconImage(img);
			pupwindow.setVisible(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JLabel label = new JLabel();
		pupwindow.getContentPane().add(label);
		pupwindow.setPreferredSize(new Dimension(100, 100));
		pupwindow.pack();
		pupwindow.setAlwaysOnTop(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		pupwindow.removeAll();
	}
}
