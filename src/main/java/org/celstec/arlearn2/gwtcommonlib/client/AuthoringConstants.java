package org.celstec.arlearn2.gwtcommonlib.client;

import com.google.gwt.i18n.client.Constants;

public interface AuthoringConstants extends Constants {

	@DefaultStringValue("Login")
	String login();
	
	@DefaultStringValue("Logout")
	String logout();
	
	@DefaultStringValue("Authentication")
	String authentication();
	
	@DefaultStringValue("Google account")
	String googleAccount();
	
	@DefaultStringValue("Password")
	String password();
	
	@DefaultStringValue("Either your account or password is incorrect")
	String passwordOrAccountIncorrect();
	
	//edit games overview
	@DefaultStringValue("Create new game")
	String createNewGame();
	
	@DefaultStringValue("Game name")
	String gameName();
	
	@DefaultStringValue("Author")
	String author();

	@DefaultStringValue("With map")
	String withMap();
	
	@DefaultStringValue("Maps type")
	String mapsType();
	
	@DefaultStringValue("Offline maps")
	String offlineMaps();
	
	@DefaultStringValue("Define an offline region")
	String defineOfflineRegion();
	
	@DefaultStringValue("Highest lattitude")
	String latUp();
	
	@DefaultStringValue("Lowest lattitude")
	String latDown();
	
	@DefaultStringValue("Left most longitude")
	String lngLeft();
	
	@DefaultStringValue("Right most longitude")
	String lngRight();
	
	@DefaultStringValue("Minimum zoomlevel")
	String minZoom();
	
	@DefaultStringValue("Maximum zoomlevel")
	String maxZoom();
	
	@DefaultStringValue("Submit")
	String submit();
	
	@DefaultStringValue("Upload Game")
	String uploadGame();
	
	@DefaultStringValue("Choose Game")
	String chooseAGame();
	
	@DefaultStringValue("Upload")
	String upload();
	
	@DefaultStringValue("Game")
	String game();
	
	@DefaultStringValue("Creator")
	String creator();
	
	// edit runs overview
	
	@DefaultStringValue("Create new run")
	String createNewRun();
	
	@DefaultStringValue("Run name")
	String runName();
	
	@DefaultStringValue("Upload Run")
	String uploadRun();
	
	@DefaultStringValue("Choose Run")
	String chooseARun();
		
	@DefaultStringValue("Run")
	String run();
	
	@DefaultStringValue("Owner account")
	String ownerAccount();
	
	
	// edit game
	
	@DefaultStringValue("Create new general item")
	String createNewGeneralItem();
	
	@DefaultStringValue("Select Type")
	String selectType();
	
	@DefaultStringValue("Title")
	String title();
	
	@DefaultStringValue("Type")
	String type();
	
	@DefaultStringValue("Roles")
	String roles();
	
	@DefaultStringValue("Edit Item")
	String editItem();
	
	@DefaultStringValue("Delete Item")
	String deleteItem();
	
	@DefaultStringValue("Map Item")
	String mapItem();
	
	@DefaultStringValue("Download Item")
	String downloadItem();
	
	@DefaultStringValue("Browse Item")
	String browseItem();
	
	@DefaultStringValue("New Role")
	String newRole();
	
	@DefaultStringValue("Narrator Item")
	String narratorItem();
	
	@DefaultStringValue("Multiple Choice")
	String multipleChoice();
	
	@DefaultStringValue("Video Object")
	String videoObject();
	
	@DefaultStringValue("Audio Object")
	String audioObject();
	
	@DefaultStringValue("Item name")
	String itemName();
	
	@DefaultStringValue("Description")
	String description();
	
	@DefaultStringValue("Edit")
	String edit();
	
	@DefaultStringValue("Radius")
	String radius();
	
	@DefaultStringValue("Latitude")
	String latitude();
	
	@DefaultStringValue("Longitude")
	String longitude();
	
	@DefaultStringValue("Select Role")
	String selectRole();
	
	@DefaultStringValue("Appear condition")
	String simpleDep();
	
	@DefaultStringValue("Filling out the four fields below will make the item only appear if all specified conditions are met.")
	String simpleDepExplain();
	
	@DefaultStringValue("Action")
	String actionDep();
	
	@DefaultStringValue("Scope")
	String scopeDep();
	
	@DefaultStringValue("Role")
	String roleDep();
	
	@DefaultStringValue("Show Count Down")
	String showCountDown();
	
	@DefaultStringValue("Time delay")
	String disTime();
	
	@DefaultStringValue("General Item")
	String generalItemDep();
	
	@DefaultStringValue("Disappear condition")
	String disappearDep();
	
	@DefaultStringValue("Filling out the four fields below will make the item disappear from the user's screen if all specified conditions are met.") 
	String disappearDepExplain();
	
	@DefaultStringValue("Automatically launch")
	String autoLaunch();
	
	@DefaultStringValue("Automatically start QR reader")
	String autoLaunchQR();
	
	@DefaultStringValue("Automatically launch means that the item will be started automatically on the client, once all dependencies are met")
	String autoLaunchHover();
	
	@DefaultStringValue("Automatically launch the QR code reader as soon as this item is selected")
	String autoLaunchQRCodeHover();
	
	@DefaultStringValue("T")
	String manualTrigger();
	
	@DefaultStringValue("Tigger manually means that the game facilitator will be able to make this item visible from withing the run management console")
	String manualTriggerHover();
	
	@DefaultStringValue("Open Question")
	String openQuestion();
	
	@DefaultStringValue("NFC Enabled")
	String isNfcEnabled();
	
	@DefaultStringValue("Answer with audio")
	String answerWithAudio();
	
	@DefaultStringValue("Answer with text") 
	String answerWithText();
	
	@DefaultStringValue("Answer with picture")
	String answerWithPicture();
	
	@DefaultStringValue("Answer with video")
	String answerWithVideo();
	
	@DefaultStringValue("Badge Url")
	String badgeUrl();
	
	@DefaultStringValue("Description")
	String badgeDescription();
	
	@DefaultStringValue("Evidence")
	String badgeEvidence();
	
	@DefaultStringValue("Badge Image")
	String badgeImage();
	
	@DefaultStringValue("Create")
	String create();
	
	@DefaultStringValue("Cancel")
	String cancel();
	
	@DefaultStringValue("Question")
	String question();
	
	@DefaultStringValue("Correct")
	String correct();
	
	@DefaultStringValue("Answer")
	String answer();
	
	@DefaultStringValue("Video URL")
	String videoUrl();
	
	@DefaultStringValue("Invalid URL. A valid URL must start with http:// ")
	String invalidUrl();
	
	@DefaultStringValue("An empty value is not allowed for this field ")
	String emptyValue();
	
	@DefaultStringValue("Incorrect Value ")
	String incorrectValue();
	
	@DefaultStringValue("Audio Url")
	String audioUrl();
	
	@DefaultStringValue("Formulate your question here")
	String formulateYourQuestionHere();
	
	@DefaultStringValue("Edit multiple choice question")
	String editMultipleChoiceQuestion();
	
	@DefaultStringValue("Edit description")
	String editDescription();
	
	//edit run
	@DefaultStringValue("Config")
	String config();
	
	@DefaultStringValue("Run options")
	String runOptions();
	
	@DefaultStringValue("Open Run")
	String openRun();
	
	@DefaultStringValue("Scan")
	String scan();
	
	@DefaultStringValue("Open run means, that the run is open to everyone and that everyone can register as a player for this run.")
	String openRunHover();
	
	@DefaultStringValue("Teams")
	String teams();
	
	@DefaultStringValue("New Team")
	String newTeam();
	
	@DefaultStringValue("Players")
	String players();
	
	@DefaultStringValue("Email")
	String email();
	
	@DefaultStringValue("New user")
	String newUser();
	
	@DefaultStringValue("Account")
	String account();
	
	@DefaultStringValue("Read")
	String read();
	
	@DefaultStringValue("Send")
	String send();
	
	
	// Modal windows
	
	@DefaultStringValue("Save")
	String save();
	
	@DefaultStringValue("Rich formatting")
	String richFormatting();
	
	@DefaultStringValue("Html formatting")
	String htmlFormatting();
	
	@DefaultStringValue("Discard")
	String discard();
	
	@DefaultStringValue("Add Role")
	String addRole();
	
	@DefaultStringValue("Role name")
	String roleName();
	
	@DefaultStringValue("Add")
	String add();
	
	@DefaultStringValue("Add Team")
	String addteam();
	
	@DefaultStringValue("Team name")
	String teamName();
	
	@DefaultStringValue("Add User")
	String addUser();
	
	@DefaultStringValue("Are you sure you want to remove *** ?")
	String confirmDeleteUser();
	
	@DefaultStringValue("User name")
	String userName();
	
	@DefaultStringValue("User email")
	String userEmail();
	
	@DefaultStringValue("Select Team")
	String selectTeam();
	
	@DefaultStringValue("search")
	String search();
	
	@DefaultStringValue("Time Out")
	String timeOut();
	
	@DefaultStringValue("Reload")
	String reload();
	
	@DefaultStringValue("Your session has timed out.")
	String sessionTimeout();
	
	//instruction
	
	@DefaultStringValue("Deselecting ''With map'' will result in the map view no longer being available on the ARLearn smartphone app. Only the messages window will be available.")
	String instructMap();
	
	
	
	
	
	
	
	
	
}
