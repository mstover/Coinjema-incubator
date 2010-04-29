package com.coinjema.client.comment.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class NewStreamBox extends FlowPanel {

	Label titleLabel;
	TextBox title;
	Label textLabel;
	TextArea text;
	List<TextBox> links;
	Label linkLabel;
	TextBox linkBox;
	Label homepageLabel;
	TextBox homepageBox;
	Button submit;
	Button cancel;

	/**
	 * 
	 */
	public NewStreamBox(ClickHandler submitter, ClickHandler closer) {
		titleLabel = new Label("Stream Name", false);
		title = new TextBox();
		textLabel = new Label("Stream Writeup", false);
		text = new TextArea();
		links = new ArrayList<TextBox>();
		linkLabel = new Label("Base URL of stream stories", false);
		linkBox = new TextBox();
		homepageLabel = new Label("Homepage URL of blog or site");
		homepageBox = new TextBox();
		submit = new Button("Create Stream", submitter);
		submit.addClickHandler(closer);
		cancel = new Button("Cancel", closer);
		initialize();
	}

	private void initialize() {
		addStyleName("new-stream-dialog");
		titleLabel.addStyleName("title-label");
		add(titleLabel);
		title.addStyleName("title-box");
		add(title);
		textLabel.addStyleName("text-label");
		add(textLabel);
		text.addStyleName("text-box");
		add(text);
		linkLabel.addStyleName("link-label");
		add(linkLabel);
		linkBox.addStyleName("link-box");
		add(linkBox);
		homepageLabel.addStyleName("homepage-label");
		add(homepageLabel);
		homepageBox.addStyleName("homepage-box");
		add(homepageBox);
		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.addStyleName("button-panel");
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		add(buttonPanel);
		title.setFocus(true);
	}

	public String getStreamTitle() {
		return title.getText();
	}

	public String getStreamText() {
		return text.getText();
	}

	public String getLink() {
		return linkBox.getText();
	}

	public void setFocus(boolean b) {
		title.setFocus(true);
	}

	public String getHomepage() {
		return homepageBox.getText();
	}

}
