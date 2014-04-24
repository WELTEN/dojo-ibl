package org.celstec.arlearn2.portal.client.resultDisplay.ui.slideShow;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.media.client.Video;
import com.google.gwt.media.dom.client.MediaError;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.tile.TileGrid;

public class SlideShow extends PopupPanel {

	private static final int STATUS_BAR_ITEMS = 3;

	private static SlideShow instance;
	
	private VerticalPanel mainPanel;
	private HorizontalPanel statusBar;
	
	private static RecordList widgetsSelected;
	
	private static int currentPosition;
	private static Widget currentElement;
	
	private static String __width_max_poppanel = "85%";
	
	private boolean playingVideo;
	
		
	public static SlideShow getInstance(TileGrid resultSelected, Record current) {
				
		widgetsSelected = resultSelected.getRecordList();
		
		if (instance == null){
			currentPosition = widgetsSelected.indexOf(current);
			instance = new SlideShow();
		}else{
			currentPosition = widgetsSelected.indexOf(current);
			instance.updateCurrentView(currentPosition);
		}
	
		return instance;
	}
	
	private SlideShow() {
		
		setTitle("SlideShow ARLearn");
		setAnimationEnabled(true);
		ensureDebugId("cwBasicPopup-imagePopup");
		
		getElement().getStyle().setZIndex(1000000);
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		getElement().getStyle().setPadding(0, Unit.PX);
		getElement().getStyle().setProperty("marginLeft", "auto");
		getElement().getStyle().setProperty("marginRight", "auto");
		getElement().getStyle().setProperty("width", __width_max_poppanel);
		
		getElement().getStyle().setProperty("right", "0");
		getElement().setId("popup-main");
				
		mainPanel = new VerticalPanel();
		
		//currentPosition = 1;
		
		updateCurrentView(currentPosition);
		mainPanel.add(statusBar);


		setAutoHideEnabled(true);
		setGlassEnabled(true);
		getGlassElement().getStyle().setZIndex(1000000);
		getGlassElement().getStyle().setOpacity(0.7);

		setWidget(mainPanel);		
	}
	
	private HorizontalPanel setStatusBar(int newPos, int numberElem) {

		HTML counter = new HTML();
		counter.setHTML((newPos+1) + " of " + numberElem);
		counter.getElement().getStyle().setProperty("textAlign", "center");
		counter.getElement().getStyle().setProperty("position", "absolute");
		counter.getElement().getStyle().setProperty("bottom", "2px");
		
		if (statusBar == null || statusBar.getWidgetCount() < STATUS_BAR_ITEMS) {
			
			statusBar = new HorizontalPanel();
			
			statusBar.getElement().getStyle().setPosition(Position.ABSOLUTE);
			statusBar.getElement().getStyle().setProperty("width", "100%");
			statusBar.getElement().getStyle().setProperty("bottom", "0");
			statusBar.addStyleName("my-mouse-out");
			statusBar.getElement().setId("statusBar");

			/**
			 * DEPRECATED
			 * TODO 
			 * Is using MyMouseEventHandler to manage over and out mouse event.
			 * 
			 * Not using it at this moment
			 * */
			//statusBar.addDomHandler(new MyMouseEventHandler(), MouseOverEvent.getType());
			//statusBar.addDomHandler(new MyMouseEventHandler(), MouseOutEvent.getType());
						
			ImgButton next = new ImgButton();
			
			next.setSrc("btn-next.gif");
			next.getElement().setId("slide_right");
			next.getElement().getStyle().setFloat(Float.RIGHT);
			next.getElement().getStyle().setProperty("position", "relative");
			next.getElement().getStyle().setProperty("bottom", "100px");
			
			
			ImgButton previous = new ImgButton();  
			previous.getElement().setId("slide_left");
			previous.getElement().getStyle().setFloat(Float.LEFT);
			previous.getElement().getStyle().setProperty("position", "relative");
			previous.getElement().getStyle().setProperty("bottom", "100px");
			
			previous.setSrc("btn-previous.gif");
			statusBar.add(previous);
			statusBar.add(counter);
			statusBar.add(next);
			
			next.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					instance.updateCurrentView(currentPosition++);
				}
			});
			
			previous.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					instance.updateCurrentView(currentPosition--);
				}
			});
		}
		else{
			statusBar.remove(1);
			statusBar.insert(counter, 1);
		}
		
		return statusBar;
	}
	
	private void updateCurrentView(int position){
		
		sms("Elements in Record[] to show:"+widgetsSelected.getLength()+" CurrentPosition:"+currentPosition);
		
		if (currentPosition >= widgetsSelected.getLength()) {
			currentPosition = 0;
		} else if (currentPosition < 0) {
			currentPosition = widgetsSelected.getLength() - 1;
		}
		
		setStatusBar(currentPosition, widgetsSelected.getLength());

		
		if (mainPanel.getWidgetCount() > 1) {
			mainPanel.remove(0);
			mainPanel.insert(getNextElement(), 0);
		}
		else{
			mainPanel.add(getNextElement());
		}
	}
	
	private Widget getNextElement() {
		Record wid = widgetsSelected.get(currentPosition);
		
		String auxPic = wid.getAttribute("picture");
		String auxAud = wid.getAttribute("audio");
		String auxVid = wid.getAttribute("video");
		String auxText = wid.getAttribute("text");

		if (!auxAud.equals("")) {
			if (!auxPic.equals("")) {
				// Audio & Image
				
				System.out.println("Audio:"+auxAud);
				System.out.println("Image:"+auxPic);
				
				VerticalPanel vPanel = new VerticalPanel();
				
				vPanel.add(createImage(auxPic, Long.parseLong(wid.getAttribute("width")), Long.parseLong(wid.getAttribute("height")), false));
				vPanel.add(createAudio(auxAud));
				
				currentElement = vPanel;
				
			}else if (!auxVid.equals("")) {
				// Audio & Video
				System.out.println("Audio:"+auxAud);
				System.out.println("Video:"+auxVid);

				VerticalPanel vPanel = new VerticalPanel();
				
				vPanel.add(createVideo(auxVid, Long.parseLong(wid.getAttribute("width")), Long.parseLong(wid.getAttribute("height"))));
				vPanel.add(createAudio(auxAud));
				
				currentElement = vPanel;
				
			}else {
				// Audio
				System.out.println("Audio:"+auxAud);
				
				currentElement = createAudio(auxAud);
				
			}
		}else if (!auxText.equals("")) {
			if (!auxPic.equals("")) {
				// Text & Image
				System.out.println("Text:"+auxText);
				System.out.println("Image:"+auxPic);
				
				VerticalPanel vPanel = new VerticalPanel();
				
				vPanel.add(createText(auxText));
//				vPanel.add(createImage(auxPic, Long.parseLong(wid.getAttribute("width")), Long.parseLong(wid.getAttribute("height"))));				
				
				currentElement = vPanel;
			
			}else if (!auxVid.equals("")) {
				// Text & Video
				System.out.println("Text:"+auxText);
				System.out.println("Video:"+auxVid);
			
				VerticalPanel vPanel = new VerticalPanel();
				
				vPanel.add(createText(auxText));
				vPanel.add(createVideo(auxVid, Long.parseLong(wid.getAttribute("width")), Long.parseLong(wid.getAttribute("height"))));
				
				currentElement = vPanel;
				
			}else {
				// Text
				System.out.println("Text:"+auxText);
			
				currentElement = createText(auxText);
				currentElement = addFocusElement(currentElement);
			}
		}else if (!auxVid.equals("")) {
			// Video
			System.out.println("Video:"+auxVid+" "+Long.parseLong(wid.getAttribute("width"))+" "+Long.parseLong(wid.getAttribute("height")));
			
//			currentElement = new Image("images/loading.jpg");
			

			currentElement = createVideo(auxVid, Long.parseLong(wid.getAttribute("width")), Long.parseLong(wid.getAttribute("height")));
			currentElement = addFocusElementForVideo((Video)currentElement);			
			
		}else if (!auxPic.equals("")) {
			// Image
			System.out.println("Image:"+auxPic);
			
			currentElement = createImage(auxPic, Long.parseLong(wid.getAttribute("width")), Long.parseLong(wid.getAttribute("height")), true);
			currentElement = addFocusElement(currentElement);
		
		}else {
			// None
			System.out.println("None");
			
			currentElement = noneElement();
			currentElement = addFocusElement(currentElement);
		}
		
		return currentElement;
		
	}

	private Widget addFocusElementForVideo(final Video element) {
		FocusPanel pFocusPanel = new FocusPanel(element);

		playingVideo = false;
		
        pFocusPanel.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			
			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (playingVideo) {
					element.play();	
				} else {
					element.pause();
				}
				playingVideo = ! playingVideo;
				System.out.println(playingVideo);
				
			}
		});

		return pFocusPanel;
	}
	
	private static Widget addFocusElement(Widget element) {
		FocusPanel pFocusPanel = new FocusPanel(element);
		
		pFocusPanel.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_RIGHT:
                	System.out.println("Derecha");
					instance.updateCurrentView(currentPosition++);
                    break;
                case KeyCodes.KEY_LEFT:
                	System.out.println("Izquierda");
					instance.updateCurrentView(currentPosition--);
                    break;
                case KeyCodes.KEY_ESCAPE:
                	System.out.println("Escape");
                	instance.hide();
                    break;
                }
			}
        });
		
		final Element aux = element.getElement();
		
		/**
		 * DEPRECATED
		 * Is using MyMouseEventHandler to manage over and out mouse event.
		 * 
		 * Not using it at this moment
		 * */
		//pFocusPanel.addDomHandler(new MyMouseEventHandler(), MouseOverEvent.getType());
		//pFocusPanel.addDomHandler(new MyMouseEventHandler(), MouseOutEvent.getType());
       
        pFocusPanel.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			
			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (event.getRelativeX(aux) >= aux.getOffsetWidth() / 2) {
					instance.updateCurrentView(currentPosition++);

				} else {
					instance.updateCurrentView(currentPosition--);
				}
			}
		});

		return pFocusPanel;

	}

	private Widget createVideo(String auxVid, long w, long h) {
		
		final Video video = Video.createIfSupported();
		if (video == null) {
			return null;
		}
		
		video.load();
		
		video.addSource(auxVid);
		
		
		
		if (w >= h) {
			// TODO with this we use all width of the screen
			getElement().getStyle().setProperty("width", __width_max_poppanel);
			
			// TODO with this we set image, video, etc to width of its father (popupanel)
			video.getElement().getStyle().setProperty("width", "100%");
		}else if(w < h){
			
			double scaleFactor = getScaleFactor(w, h);
			
			final int width = (int) (w * scaleFactor);
			final int height = (int) (h * scaleFactor*0.90);
			video.setPixelSize(width, height);
			
			getElement().getStyle().setWidth(width, Unit.PX);
			
			video.getElement().getStyle().setProperty("width", "100%");
		}
		
		video.setControls(true);		
		video.setAutoplay(true);
		

		return video;
	}

	
	private Widget createVideo1(String auxVid, long w, long h) {
		
		HTML video =  new HTML("<embed src='"+auxVid+"'>");
		
		if (w >= h) {
			// TODO with this we use all width of the screen
			getElement().getStyle().setProperty("width", __width_max_poppanel);
			
			// TODO with this we set image, video, etc to width of its father (popupanel)
			video.getElement().getStyle().setProperty("width", "100%");
		}else if(w < h){
			
			double scaleFactor = getScaleFactor(w, h);
			
			final int width = (int) (w * scaleFactor);
			final int height = (int) (h * scaleFactor*0.90);
			video.setPixelSize(width, height);
			
			getElement().getStyle().setWidth(width, Unit.PX);
			
			video.getElement().getStyle().setProperty("width", "100%");
		}
	
		return video;
	}

	private Widget createAudio1(String auxAud) {
		Audio audio = Audio.createIfSupported();
		if (audio == null) {
			return null;
		}

		audio.load();

		audio.addSource(auxAud);			
		audio.setControls(true);
		return audio;
	}

	private Widget createAudio(String auxAud) {
		HTML audio =  new HTML("<embed height='50' width='80%' src='"+auxAud+"'>");
		return audio;
	}
	
	private HTML noneElement() {
		HTML noneElement = new HTML();
		
		noneElement.setHTML("<b>There is no element to show</b>");
		noneElement.getElement().getStyle().setProperty("height", "400px");
		
		getElement().getStyle().setProperty("width", __width_max_poppanel);
		noneElement.getElement().getStyle().setProperty("width", "100%");
		
		return noneElement;
	}

	/*
	private HTML createDoc(String auxDoc) {
		
		HTML visorDocuments = new HTML();
		
		double scaleFactor = getScaleFactor(600,
				700);

		final int width = ((int) (600* scaleFactor))-20;
		final int height = ((int) (700 * scaleFactor))-20;
		visorDocuments.setPixelSize(width, height);
		
		visorDocuments.setHTML("<iframe src='http://docs.google.com/viewer?url="+auxDoc+"&embedded=true' width='"+width+"' height='"+height+"' style='border: none;'></iframe>");
		
		return visorDocuments;
	}
	*/
	
	private HTML createText(String auxDoc) {
		
		HTML richText = new HTML();
		
		richText.setHTML("<p>"+auxDoc+"</p>");
		
		//richText.getElement().getStyle().setProperty("height", "400px");
		
		getElement().getStyle().setProperty("width", __width_max_poppanel);
		richText.getElement().getStyle().setProperty("width", "100%");
		
		return richText;
	}

	private Image createImage(String auxPic, long w, long h, boolean option) {
		Image image;
		image = new Image(auxPic);

		if (w >= h) {
			
			if (w == 0 & h == 0) {
				System.out.println("Original size. Images are not resized.");
			}
			else{
				getElement().getStyle().setProperty("width", __width_max_poppanel);
				
				if (option) {
					image.getElement().getStyle().setProperty("width", "100%");
				}
			}
			

			
			
		}else if(image.getWidth() < image.getHeight()){
			
			double scaleFactor = getScaleFactor(w, h);
			
			final int width = (int) (w * scaleFactor);
			final int height = (int) (h * scaleFactor*0.90);
			image.setPixelSize(width, height);
			getElement().getStyle().setWidth(width, Unit.PX);
			
			if (option) {
				image.getElement().getStyle().setProperty("width", "100%");
			}
		}
		else{
			System.out.println("Width:"+w+" Height: "+h);
		}
		
		return image;
	}

	
	private double getScaleFactor(long width, long height) {
		
		return Math.min(Window.getClientWidth() / (double) width,
				Window.getClientHeight() / (double) height);

		
	}
	
	public void sms(String msg){
		System.out.println(msg);
	}
	
	
	
}
