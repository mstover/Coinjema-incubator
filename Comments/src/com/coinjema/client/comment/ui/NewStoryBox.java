/**
 * 
 */
package com.coinjema.client.comment.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author mstover
 *
 */
public class NewStoryBox extends FlowPanel {
	
	Label titleLabel;
	TextBox title;
	Label textLabel;
	TextArea text;
	List<TextBox> links;
	Label linkLabel;
	FlowPanel linkPanel;
	Button submit;
	Button cancel;
	LinkTextChangeHandler linkChanger;

	/**
	 * 
	 */
	public NewStoryBox(ClickHandler submitter,ClickHandler closer) {
		titleLabel = new Label("Story Title",false);
		title = new TextBox();
		textLabel = new Label("Story Writeup",false);
		text = new TextArea();
		links = new ArrayList<TextBox>();
		linkLabel = new Label("Story Links (Add as many as you like)",false);
		linkPanel = new FlowPanel();
		submit = new Button("Submit Story",submitter);
		submit.addClickHandler(closer);
		cancel = new Button("Cancel",closer);
		linkChanger = new LinkTextChangeHandler();
		initLinkPanel();
		initialize();
	}
	
	private void initLinkPanel() {
		TextBox linkBox = new TextBox();
		linkBox.addStyleName("link-box");
		linkPanel.add(linkBox);
		links.add(linkBox);
		linkBox.addChangeHandler(linkChanger);		
		linkBox.addKeyUpHandler(linkChanger);
	}
	
	private class LinkTextChangeHandler implements ChangeHandler,KeyUpHandler {
		@Override
		public void onChange(ChangeEvent event) {
			checkLinkBoxes();			
		}
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				checkLinkBoxes();
			}
		}
	}
	
	private void checkLinkBoxes() {
		TextBox lastLink = links.get(links.size()-1);
		if(lastLink.getText().length() > 0) {
			TextBox newLastLink = new TextBox();
			links.add(newLastLink);
			newLastLink.addStyleName("link-box");
			newLastLink.addChangeHandler(linkChanger);
			newLastLink.addKeyUpHandler(linkChanger);
			linkPanel.add(newLastLink);
			newLastLink.setFocus(true);
		}
		Set<TextBox> remove = new HashSet<TextBox>();
		for(int i = 0;i < links.size()-1;i++) {
			TextBox box = links.get(i);
			if(box.getText().length() == 0) {
				linkPanel.remove(box);
				remove.add(box);
			}
		}
		for(TextBox box : remove) {
			links.remove(box);
		}
	}
	
	private void initialize() {
		addStyleName("new-story-dialog");
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
		linkPanel.addStyleName("link-panel");
		add(linkPanel);
		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.addStyleName("button-panel");
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		add(buttonPanel);
		title.setFocus(true);
	}

	public String getStoryTitle() {
		return title.getText();
	}

	public String getStoryText() {
		return text.getText();
	}

	public String[] getLinks() {
		String[] l = new String[links.size()-1];
		for(int i = 0;i < l.length;i++) {
			l[i] = links.get(i).getText();
		}
		return l;
	}

	public void setFocus(boolean b) {
		title.setFocus(true);
	}

}
