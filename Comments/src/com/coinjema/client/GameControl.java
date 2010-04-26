package com.coinjema.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GameControl implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get("GameControlCenter").add(
				new Label("Game Control Center"));

	}

}
