package org.celstec.arlearn2.portal.client.author.ui.gi.i18;

import com.google.gwt.i18n.client.Constants;

public interface GeneralItemConstants  extends Constants {

	
	@DefaultStringValue("Order")
	String order();
	
	@DefaultStringValue("Title")
	String title();

    @DefaultStringValue("Custom Icon")
    String customIcon();

    @DefaultStringValue("Roles")
    String roles();

    @DefaultStringValue("Section")
    String section();

    @DefaultStringValue("Tags")
    String tags();

	@DefaultStringValue("Message Type")
	String simpleName();
	
	@DefaultStringValue("Create")
	String create();
	
	@DefaultStringValue("Find")
	String find();
	
	@DefaultStringValue("Save")
	String save();
	
//Basic metadata editor

	@DefaultStringValue("Position on map")
	String drawOnMap();

	@DefaultStringValue("Automatically Launch")
	String automaticallyLaunch();
	
	@DefaultStringValue("Latitude")
	String latitude();
	
	@DefaultStringValue("Longitude")
	String longitude();
	
	@DefaultStringValue("Rich text description Editing")
	String richTextEditing();
	
	//Data collection
	
	@DefaultStringValue("Answer with audio")
	String answerWithAudio();
	
	@DefaultStringValue("Answer with text") 
	String answerWithText();
	
	@DefaultStringValue("Answer with number") 
	String answerWithValue(); //TODO translate
	
	@DefaultStringValue("Answer with picture")
	String answerWithPicture();
	
	@DefaultStringValue("Answer with video")
	String answerWithVideo();
	
	//Dependencies
	
	@DefaultStringValue("Advanced")
	String advanced();
	
	@DefaultStringValue("Simple")
	String simple();
	
	@DefaultStringValue("Action based Dependency")
	String actionBasedDependency();
	
	@DefaultStringValue("Has dependency")
	String hasDependency();
	
	@DefaultStringValue("Action")
	String action();
	
	@DefaultStringValue("Message")
	String item();
	
	@DefaultStringValue("Message Identifier")
	String itemId();
	
	@DefaultStringValue("Scope")
	String scope();
	
	@DefaultStringValue("Role")
	String role();
	
	@DefaultStringValue("if action performed by user")
	String user();
	
	@DefaultStringValue("if action performed by team member")
	String team();
	
	@DefaultStringValue("if action performed by any player")
	String all();
	
	@DefaultStringValue("When message is opened")
	String read();

	@DefaultStringValue("When an answer is given")
	String answerGiven();

    @DefaultStringValue("When correct answer is given")
    String correctAnswer();

    @DefaultStringValue("When wrong answer is given")
    String wrongAnswer();

    @DefaultStringValue("When audio completed playing")
    String completePlaying();

	@DefaultStringValue("Data collection")
	String dataCollection();
	
	@DefaultStringValue("When should this message appear")
	String appear();
	
	@DefaultStringValue("When should this message disappear")
	String disappear();
	
	//Extension editor
	
	@DefaultStringValue("URL Audio file")
	String audioURL();

    @DefaultStringValue("Start audio when message is opened")
    String autoPlayAudio();
	
	@DefaultStringValue("URL Video file")
	String videoURL();

    @DefaultStringValue("Start video when message is opened")
    String autoPlayVideo();


    @DefaultStringValue("Youtube URL")
	String youtubeURL();
	
	@DefaultStringValue("Enable data collection")
	String enableDataCollection();
	
	@DefaultStringValue("Automatically start QR reader")
	String automaticallyStartQRReader();
	
	@DefaultStringValue("Answer")
	String answer();
	
	@DefaultStringValue("Is correct")
	String isCorrect();
	
	@DefaultStringValue("Show Countdown")
	String showCountDown();
	
	@DefaultStringValue("Countdown")
	String countDown();
	
	//Metadata viewer

    @DefaultStringValue("Generic properties")
    String basicMetadata();

    @DefaultStringValue("Message dependent properties")
    String specificMetadata();

    //Modal Window
    @DefaultStringValue("Create Audio Object")
    String createAudioObject();

    @DefaultStringValue("Toggle HTML source")
    String toggleHtml();

    @DefaultStringValue("Create Single Choice Question with Images")
    String createSingleChoiceWithImages();

    @DefaultStringValue("Create Multiple Choice Question with Images")
    String createMultipleChoiceWithImages();

    @DefaultStringValue("Create Mozilla Open Badge Object")
    String createMozillaOpenBadge();

    @DefaultStringValue("Create Multiple Choice Question")
    String createMultipleChoice();

    @DefaultStringValue("Create Single Choice Question")
    String createSingleChoice();

    @DefaultStringValue("Create Narrator Item")
    String createNarratorItem();

    @Constants.DefaultStringValue("Create QR Scanner")
    String createQrScanner();

    @Constants.DefaultStringValue("Create Video Object")
    String createVideoObject();

    @Constants.DefaultStringValue("Create Youtube Object")
    String createYoutubeObject();
}
