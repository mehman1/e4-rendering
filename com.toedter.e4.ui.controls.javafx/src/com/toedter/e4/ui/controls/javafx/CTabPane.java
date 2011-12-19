package com.toedter.e4.ui.controls.javafx;

import javafx.scene.control.TabPane;

import com.toedter.e4.ui.controls.javafx.skin.CTabPaneSkin;

public class CTabPane extends TabPane {
	private boolean isMaximized;
	private boolean isMinimized;

	private final CTabPaneSkin skin;

	public CTabPane() {
		skin = new CTabPaneSkin(this);
		setSkin(skin);
	}

	public void setMinimizeVisible(boolean isMinimizeVisible) {
		skin.setMinimizeVisible(isMinimizeVisible);
	}

	public void setMaximizeVisible(boolean isMaximizeVisible) {
		skin.setMaximizeVisible(isMaximizeVisible);
	}

	public void setMaximized(boolean isMaximized) {
		this.isMaximized = isMaximized;
	}

	public void setMinimized(boolean isMinimized) {
		this.isMinimized = isMinimized;
	}

	public void setMaximizedHandler(Runnable handler) {
		skin.setMaximizedHandler(handler);
		skin.setMaximizeVisible(true);
	}

	public void setMinimizedHandler(Runnable handler) {
		skin.setMinimizedHandler(handler);
		skin.setMinimizeVisible(true);
	}
}