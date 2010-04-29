/**
 * 
 */
package com.coinjema.client.comment.ui;

import com.coinjema.client.comments.Story;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * @author mstover
 * 
 */
public class StoryWidget extends FlowPanel {

	FlowPanel links;
	Label title;
	Label text;
	Label author;
	Label submittedBy = new Label("Submitted By: ");
	FlowPanel authorPanel;

	/**
	 * 
	 */
	public StoryWidget() {
		// TODO Auto-generated constructor stub
	}

	public StoryWidget(Story result) {
		title = new Label(result.getTitle(), true);
		text = new Label(result.getText(), true);
		author = new Label(result.getAuthor(), false);
		links = createLinkPanel(result.getLinks());
		createAuthorPanel();
		initialize();
	}

	private FlowPanel createLinkPanel(String[] links2) {
		FlowPanel p = new FlowPanel();
		if (links2 != null) {
			for (String l : links2) {
				Anchor a = new Anchor(l, l);
				a.addStyleName("story-link");
				p.add(a);
			}
		}
		return p;
	}

	private void createAuthorPanel() {
		authorPanel = new FlowPanel();
		authorPanel.add(submittedBy);
		authorPanel.add(author);
	}

	private void initialize() {
		addStyleName("story-header");
		title.addStyleName("title");
		links.addStyleName("link-panel");
		text.addStyleName("text");
		authorPanel.addStyleName("author-panel");
		author.addStyleName("author");
		add(title);
		add(links);
		add(text);
		add(authorPanel);
	}

}
