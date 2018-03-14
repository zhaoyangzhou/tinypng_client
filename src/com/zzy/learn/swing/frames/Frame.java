package com.zzy.learn.swing.frames;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.zzy.learn.swing.tabs.tinypng.view.TinypngTab;
import com.zzy.learn.swing.tabs.tinypng.view.ia.ViewIA;

public class Frame extends JFrame {
	// 布局卡选项
	private JTabbedPane tabPane = new JTabbedPane();
	private JWindow layeredPane = new JWindow();

	public Frame() {
		super("图片压缩助手");
		setLocation(400, 150);
		
		JMenuBar menuBar = new JMenuBar();  
        JMenu exitMenu = new JMenu("退出");  
        menuBar.add(exitMenu);
        setJMenuBar(menuBar);
        
		setContentPane(tabPane);
		setVisible(true);
		setSize(800, 420);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {  
			  
			public void windowClosing(WindowEvent e) {  
				super.windowClosing(e);
				for (int i = 0, len = tabPane.getComponentCount(); i < len; i++) {
					((ViewIA) tabPane.getComponentAt(i)).doCancel();
				}
			}  
			  
		});
		
		exitMenu.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {
				System.exit(0);
			}
			
			@Override
			public void menuCanceled(MenuEvent e) {
				
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				
			}
		});
		
		tabPane.add("tab1", new TinypngTab(layeredPane));
	}
	

	public static void main(String[] args) {
		new Frame();
	}
}
