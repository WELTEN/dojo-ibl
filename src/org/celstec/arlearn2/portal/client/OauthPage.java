package org.celstec.arlearn2.portal.client;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import org.celstec.arlearn2.gwtcommonlib.client.auth.*;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailFormatter;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class OauthPage {
	ToolBar toolStrip;
	TileGrid tileGrid;
	
	public void loadPage() {
//		createToolstrip();
//		SC.say("Welcome", "Dear ARLearn author,<br><br> " +
//				"Welcome to the new ARLearn set of tools. Some of the new features include:"+
//				"<ol><li>	Collaborative authoring of games. Share your games with contacts." +
//				"<li>	License public games with a creative commons license." +
//				"<li>	Login with facebook, linkedin or google.</ol>" +
//				"<br> As you will see, most of the functionality is already implemented in this new environment... but in a slightly different manner<br>" +
//				"<br><br> Logging with google will automatically migrate your games and will render them unavailable in the <a href=\"Authoring.html\">old environment</a>. Logging in with Facebook or LinkedIn will not affect your old games and runs."+
//				"<br><br>If things are not clear, don't hesitate to contact me via <a href=\"skype:stefaanternier2?call\">Skype</a> or contact me via email: stefaan.ternier@ou.nl ");
		toolStrip = new ToolBar(false);

		
		VLayout vertical = new VLayout();
		vertical.setWidth100();
		vertical.setHeight100();
		vertical.addMember(toolStrip);
		createButtons();
		
		VLayout verticalGrid = new VLayout();
		
		verticalGrid.setLayoutAlign(Alignment.CENTER);
		verticalGrid.setAlign(Alignment.CENTER);  
		verticalGrid.setDefaultLayoutAlign(Alignment.CENTER);

		verticalGrid.setWidth100();
		verticalGrid.setHeight("*");
//		verticalGrid.setBorder("1px solid gray");
		verticalGrid.addMember(tileGrid);
		
		vertical.addMember(verticalGrid);
		RootPanel.get("oauth_new").add(vertical);
		
		if (Window.Location.getParameter("type") != null)
			Window.Location.replace("/index.html");

	}
	
	private void createButtons() {
		  tileGrid = new TileGrid();  
	        tileGrid.setTileWidth(250);
	        tileGrid.setTileHeight(250);  
	        tileGrid.setHeight(400);  
	        tileGrid.setWidth(1100);
	        tileGrid.setID("boundList");  
	        tileGrid.setCanReorderTiles(false);  
	        tileGrid.setShowAllRecords(false);  
	        
	        tileGrid.addRecordClickHandler(new RecordClickHandler() {
				
				@Override
				public void onRecordClick(RecordClickEvent event) {
					switch (event.getRecord().getAttributeAsInt("rec")) {
					case 1:
						Window.open((new OauthFbClient()).getLoginRedirectURL(), "_self", ""); 
						break;
					case 2:
						Window.open((new OauthGoogleClient()).getLoginRedirectURLWithGlass(), "_self", "");

						break;
					case 3:
						Window.open((new OauthLinkedIn()).getLoginRedirectURL(), "_self", ""); 
						break;
                    case 4:
                        Window.open((new OauthTwitter()).getLoginRedirectURL(), "_self", "");
                        break;
                    case 5:
                         Window.open((new OauthWespot()).getLoginRedirectURL(), "_self", "");
                         break;
					default:
						break;
					}
					
				}
			});
//	        tileGrid.setBorder("1px solid gray");
	        
	        
	        Record facebook = new Record();
	        facebook.setAttribute("picture", "facebook.png");
	        facebook.setAttribute("commonName", "Sign in with Facebook");
	        facebook.setAttribute("rec", 1);
	        
	        Record google = new Record();
	        google.setAttribute("picture", "google.png");
	        google.setAttribute("commonName", "Sign in with Google");
	        google.setAttribute("rec", 2);

	        Record linkedIn = new Record();
	        linkedIn.setAttribute("picture", "linked-in.png");
	        linkedIn.setAttribute("commonName", "Sign in with LinkedIn");
	        linkedIn.setAttribute("rec", 3);

            Record twitterRec = new Record();
            twitterRec.setAttribute("picture", "twitter.png");
            twitterRec.setAttribute("commonName", "Sign in with Twitter");
            twitterRec.setAttribute("rec", 4);

            Record wespot = new Record();
            wespot.setAttribute("picture", "wespot.png");
            wespot.setAttribute("commonName", "Sign in with weSPOT");
            wespot.setAttribute("rec", 5);

	        DataSource ds =new DataSource();
	        ds.setClientOnly(true);

            DataSourceField field = new DataSourceField();
            field.setName("rec");
            field.setHidden(true);
            field.setPrimaryKey(true);
            ds.addField(field);


	        ds.addData(facebook);
	        ds.addData(google);
	        ds.addData(linkedIn);
            ds.addData(twitterRec);
//            ds.addData(wespot);
	        
	        tileGrid.setDataSource(ds); 
	        tileGrid.setAutoFetchData(true);  

	        DetailViewerField pictureField = new DetailViewerField("picture"); 
			pictureField.setType("image");
			pictureField.setImageHeight(200);
			pictureField.setImageWidth(200);

	        DetailViewerField commonNameField = new DetailViewerField("commonName");  
//	        commonNameField.setCellStyle("commonName");
	        
	  
//	        DetailViewerField lifeSpanField = new DetailViewerField("lifeSpan");  
//	        lifeSpanField.setCellStyle("lifeSpan");  
	        commonNameField.setDetailFormatter(new DetailFormatter() {  
	            public String format(Object value, Record record, DetailViewerField field) {  
	                return "<h1> " + value +"</h1>";  
	            }  
	        });  
	  
	        DetailViewerField statusField = new DetailViewerField("status");  

	        tileGrid.setFields(pictureField, commonNameField);  
	  
	}
		
//	public void loadPage() {
//		CustomButton facebook_button = new CustomButton();
//		facebook_button.setText("Sign in with Facebook");
//		facebook_button.setResource("images/facebook.png");
//		facebook_button.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				Window.open((new OauthFbClient()).getLoginRedirectURL(), "_self", ""); 
//			}
//		});
//
//		CustomButton twitter_button = new CustomButton();
//		twitter_button.setText("Sign in with Google");
//		twitter_button.setResource("images/google.png");
//		twitter_button.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				Window.open((new OauthGoogleClient()).getLoginRedirectURL(), "_self", ""); 
//			}
//		});
//
//		CustomButton linkedin_button = new CustomButton();
//		linkedin_button.setText("Sign in with Linkedin");
//		linkedin_button.setResource("images/linked-in.png");
//		linkedin_button.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				Window.open((new OauthLinkedIn()).getLoginRedirectURL(), "_self", ""); 
//			}
//		});
//
//		RootPanel layout_button_facebook = RootPanel.get("button-facebook");
//		RootPanel layout_button_twitter = RootPanel.get("button-twitter");
//		RootPanel layout_button_linkedin = RootPanel.get("button-linkedin");
//
//		layout_button_facebook.add(facebook_button);
//		layout_button_twitter.add(twitter_button);
//		layout_button_linkedin.add(linkedin_button);
//		
//	}
}
