package org.celstec.arlearn2.portal.client.author.ui.generic.canvas;


import org.celstec.arlearn2.gwtcommonlib.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class RichTextCanvas extends VLayout{

	private HtmlSaver saver;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	private boolean htmlEditingmode = false;
	private TextAreaItem textAreaItem;
	private RichTextEditor richTextEditor;
    private final DynamicForm form = new DynamicForm();
	
	private IButton submitButton;

    public RichTextCanvas(String html, String title, HtmlSaver sa) {
        this(html, title, sa, null);
    }
	public RichTextCanvas(String html, String title, HtmlSaver sa, Integer formWidth) {
		this.saver = sa;
//		final DynamicForm form = new DynamicForm();
		
		textAreaItem = new TextAreaItem();  
        textAreaItem.setTitle("TextArea"); 
        textAreaItem.setShowTitle(false);
        textAreaItem.setColSpan(3);
        textAreaItem.setWidth("100%");
        textAreaItem.setValue(html);

        form.setFields( textAreaItem);
        if (formWidth == null) {
            form.setWidth100();
        } else {
            form.setWidth(formWidth);
        }
//        form.setWidth100();
        form.setVisibility(Visibility.HIDDEN);
        
		richTextEditor = new RichTextEditor();
		richTextEditor.setValue(html);
		richTextEditor.setHeight("*");
//		richTextEditor.setWidth(440);
		richTextEditor.setWidth100();
//		richTextEditor.setShowEdges(true);
		richTextEditor.setBorder("1px  grey");

		richTextEditor.setControlGroups(new String[] { "fontControls",
				"formatControls", "styleControls" });
		
		
		submitButton = new IButton(constants.save());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (htmlEditingmode) {
					saver.htmlReady(textAreaItem.getValueAsString());
				} else{
					saver.htmlReady(richTextEditor.getValue());	
				}
			}
		});
		
		final IButton toggleHtmlButton = new IButton(constants.htmlFormatting());
		
		HLayout buttonLayout = new HLayout();
//		buttonLayout.setBorder("1px solid grey");

		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.setHeight(30);
		buttonLayout.addMember(toggleHtmlButton);
		buttonLayout.addMember(submitButton);
		
		toggleHtmlButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (htmlEditingmode) {
					htmlEditingmode = false;
					richTextEditor.setVisibility(Visibility.INHERIT);
					richTextEditor.setValue(textAreaItem.getValueAsString());
					form.setVisibility(Visibility.HIDDEN);
					toggleHtmlButton.setTitle(constants.htmlFormatting());
				} else {
					htmlEditingmode = true;
					richTextEditor.setVisibility(Visibility.HIDDEN);
					textAreaItem.setValue(richTextEditor.getValue());
					toggleHtmlButton.setTitle(constants.richFormatting());
					form.setVisibility(Visibility.INHERIT);
				}
			}
		});
		addMember(form);
		addMember(richTextEditor);
		addMember(buttonLayout);
		
	}
	
	public interface HtmlSaver {
		public void htmlReady(String html);
	}
	
	public void updateHtml(String html) {
        textAreaItem.setValue(html);
		richTextEditor.setValue(html);

	}

    public DynamicForm getForm() {
        return form;
    }
}
