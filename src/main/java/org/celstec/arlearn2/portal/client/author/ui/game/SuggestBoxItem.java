package org.celstec.arlearn2.portal.client.author.ui.game;

import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.Canvas;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class SuggestBoxItem extends CanvasItem {

	public static final int HEIGHT = 20;
	private Canvas canvas = new Canvas();
	private SuggestBox suggestBoxField;

	public SuggestBoxItem(String s, String s1, SuggestOracle suggestOracle) {
		super(s, s1);

		suggestBoxField = new SuggestBox(suggestOracle);

		suggestBoxField.setStyleName("gwt-SuggestBox");
		suggestBoxField.setHeight(getHeight() + "px");

		canvas.setHeight(getHeight());
		canvas.setStyleName("gwt-SuggestBoxCanvas");
		canvas.addChild(suggestBoxField);

		setCanvas(canvas);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public int getHeight() {
		return HEIGHT;
	}
}
