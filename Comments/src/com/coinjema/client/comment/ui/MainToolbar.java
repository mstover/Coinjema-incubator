/**
 * 
 */
package com.coinjema.client.comment.ui;

import com.coinjema.client.ClientCommentSystem;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author mstover
 * 
 */
public class MainToolbar extends FlowPanel {

	ClientCommentSystem system;
	NewStreamBox dialog;

	/**
	 * 
	 */
	public MainToolbar(ClientCommentSystem s) {
		system = s;
		initialize();
	}

	private void initialize() {
		addStyleName("main-toolbar");

		add(new Button("View Source", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert(getSource());
			}
		}));

		if (!system.loggedIn()) {
			add(makeLoginButton());
			add(makeRegisterButton());
		} else {
			add(makeNewStreamButton());
			add(makeLogoutButton());
		}
	}

	private Button makeLogoutButton() {
		Button logoutButton = new Button("Logout");
		logoutButton.addStyleName("logout-button");
		logoutButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				system.logout();
			}
		});
		return logoutButton;
	}

	private Button makeNewStreamButton() {
		Button newStreamButton = new Button("Create Stream");
		newStreamButton.addStyleName("create-stream-button");
		newStreamButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final DialogBox popup = new DialogBox();
				dialog = new NewStreamBox(newStreamHandler(popup),
						cancelNewStreamHandler(popup));
				popup.setText("Create New Stream");
				popup.add(dialog);
				popup.show();
				dialog.setFocus(true);
			}
		});
		return newStreamButton;
	}

	protected ClickHandler cancelNewStreamHandler(final DialogBox popup) {
		// TODO Auto-generated method stub
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		};
	}

	protected ClickHandler newStreamHandler(final DialogBox popup) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
				system.createStream(dialog.getStreamTitle(), dialog
						.getStreamText(), dialog.getLink(), dialog
						.getHomepage());
			}
		};
	}

	private Button makeLoginButton() {
		Button loginButton = new Button("Login");
		loginButton.addStyleName("login-button");
		loginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final DialogBox loginDialog = new DialogBox();
				final TextBox loginBox = new TextBox();
				final PasswordTextBox passwordBox = new PasswordTextBox();
				Label loginLabel = new Label("Username");
				Label passwordLabel = new Label("Password");
				Button submitButton = new Button("Login");
				submitButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						system.login(loginBox.getText(), passwordBox
								.getText());
						loginDialog.hide();

					}
				});
				Button cancelButton = new Button("Cancel");
				cancelButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						loginDialog.hide();
					}
				});
				FlowPanel loginPanel = new FlowPanel();
				loginPanel.add(loginLabel);
				loginPanel.add(loginBox);
				loginPanel.add(passwordLabel);
				loginPanel.add(passwordBox);
				loginPanel.add(submitButton);
				loginPanel.add(cancelButton);
				loginDialog.add(loginPanel);
				loginDialog.setText("Please Login");
				loginDialog.show();
				loginBox.setFocus(true);
			}
		});
		return loginButton;
	}

	private Button makeRegisterButton() {
		Button registerButton = new Button("Register");
		registerButton.addStyleName("register-button");
		registerButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final DialogBox registerDialog = new DialogBox();
				final TextBox loginBox = new TextBox();
				final PasswordTextBox passwordBox = new PasswordTextBox();
				final PasswordTextBox passwordConfirmBox = new PasswordTextBox();
				Label loginLabel = new Label("Username");
				Label passwordLabel = new Label("Password");
				Label passwordConfirmLabel = new Label("Confirm Password");
				Button submitButton = new Button("Register");
				submitButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (passwordBox.getText().equals(
								passwordConfirmBox.getText())) {
							system.registerUser(loginBox.getText(),
									passwordBox.getText());
						}
						registerDialog.hide();
					}
				});
				Button cancelButton = new Button("Cancel");
				cancelButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						registerDialog.hide();
					}
				});
				FlowPanel loginPanel = new FlowPanel();
				loginPanel.add(loginLabel);
				loginPanel.add(loginBox);
				loginPanel.add(passwordLabel);
				loginPanel.add(passwordBox);
				loginPanel.add(passwordConfirmLabel);
				loginPanel.add(passwordConfirmBox);
				loginPanel.add(submitButton);
				loginPanel.add(cancelButton);

				registerDialog.add(loginPanel);
				registerDialog.setText("Please Register a new username");
				registerDialog.show();
				loginBox.setFocus(true);
			}
		});
		return registerButton;
	}

	private String getSource() {
		return system.getSource();
	}

}
