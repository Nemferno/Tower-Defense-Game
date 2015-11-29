package org.xodia.td.gui.custom;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;

public class Dialog extends ResizableFrame{

	private TextArea messageArea;
	private ScrollPane scrollPane;
	private Group hGroup, vGroup;
	private DialogLayout layout;
	
	public Dialog(String text){
		final HTMLTextAreaModel textAreaModel = new HTMLTextAreaModel(text);
		messageArea = new TextArea(textAreaModel);
		
		scrollPane = new ScrollPane(messageArea);
		scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
		
		layout = new DialogLayout();
		
		hGroup = layout.createSequentialGroup();
		hGroup.addGap();
		vGroup = layout.createParallelGroup();
		
		layout.setHorizontalGroup(layout.createParallelGroup().addWidget(scrollPane).addGroup(hGroup));
		layout.setVerticalGroup(layout.createSequentialGroup().addWidget(scrollPane).addGroup(vGroup));
		
		add(layout);
	}
	
	// the size will not be able to function since
	// the hGroup & vGroup objects are going to readjust the size automaticaclly
	// to the image size rather
	public void addButton(String text, Runnable call){
		Button button = new Button(text);
		button.setSize(300, 75);
		button.addCallback(call);
		hGroup.addWidget(button);
		vGroup.addWidget(button);
	}
	
	public void addWidget(Widget widget){
		hGroup.addWidget(widget);
		vGroup.addWidget(widget);
	}
	
	public void setText(String text){
		HTMLTextAreaModel model = new HTMLTextAreaModel(text);
		messageArea.setModel(model);
	}
	
}
