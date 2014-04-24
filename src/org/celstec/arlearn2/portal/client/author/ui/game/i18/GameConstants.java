package org.celstec.arlearn2.portal.client.author.ui.game.i18;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

public interface GameConstants extends Constants {

	//Table Headings
	@DefaultStringValue("Games")
	String games();
	
	@DefaultStringValue("Title")
	String title();
	
	@DefaultStringValue("License")
	String license();
	
	@DefaultStringValue("Change")
	String gameAccess();
	
	@DefaultStringValue("Delete game")
	String deleteGame();
	
	@DefaultStringValue("Delete game '***'")
	String deleteThisGame();
	
	@DefaultStringValue("Remove your access rights from game '***'")
	String removeAccess();
	
	@DefaultStringValue("Create new run")
	String createRunMessageHeader();
	
	@DefaultStringValue("You are creating a run for '***'.<br> Define a name for this new run.<br>")
	String createRunMessage();
	
	@DefaultStringValue("Click here to create a run for this game")
	String createRunMessageHoover();
	
	@DefaultStringValue("Click here to edit messages for this game")
	String openGeneralItemsHoover();
	
	//Game Description section
	
	@DefaultStringValue("About this game")
	String aboutThisGame();
	
	@DefaultStringValue("Game description")
	String description();
	
	//Roles section
	
	@DefaultStringValue("Roles")
	String roles();
	
	@DefaultStringValue("Role")
	String role();
	
	@DefaultStringValue("New role")
	String newRole();
	
	@DefaultStringValue("Save role")
	String saveRole();
	
	@DefaultStringValue("Do you want to delete '***' <br>")
	String confirmDeleteRole();
	
	//Sharing
	
	@DefaultStringValue("Share")
	String share();
	
	@DefaultStringValue("Access")
	String access();
	
	@DefaultStringValue("Visibility options")
	String shareVisibilityOptions();
	
	@DefaultStringValue("Private")
	String privateSharing();
	
	@DefaultStringValue("Anyone with the link")
	String linkSharing();
	
	@DefaultStringValue("Public on the web")
	String publicSharing();
	
	//Collaborators
	@DefaultStringValue("Add people")
	String addCollaborators();
	
	@DefaultStringValue("Invite new contact")
	String inviteNewContact();
	
	@DefaultStringValue("Contact Email")
	String contactEmail();
	
	@DefaultStringValue("Invite")
	String invite();
	
	@DefaultStringValue("Name")
	String name();
	
	@DefaultStringValue("Can edit")
	String canEdit();
	
	@DefaultStringValue("Select people")
	String selectAccount();
	
	@DefaultStringValue("Submit")
	String submit();

    //Map, message lists

    @DefaultStringValue("Message views")
    String messageViews();

    @DefaultStringValue("Select message view")
    String selectMessageView();


    @DefaultStringValue("Show only messages list")
    String messagesOnly();

    @DefaultStringValue("Show both messages and map")
    String messagesAndMap();

    @DefaultStringValue("Show only map view")
    String mapViewOnly();

    @DefaultStringValue("Custom HTML")
    String customHtml();

    @DefaultStringValue("Select Map Type")
    String selectMapType();

    @DefaultStringValue("Map types")
    String mapTypes();

    @DefaultStringValue("Google Maps")
    String googleMaps();

    @DefaultStringValue("Open Street Maps")
    String osm();

}
