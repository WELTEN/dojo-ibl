package org.celstec.arlearn2.portal.client.author;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

public interface AuthorConstants extends Constants {
	
	@DefaultStringValue("Contacts")
	String contacts();
	
	@DefaultStringValue("Invite Contact")
	String inviteContact();
	
	@DefaultStringValue("Enter email address for contact?")
	String emailForContact();
	
	@DefaultStringValue("Game")
	String game();
	
	@DefaultStringValue("New")
	String newGame();
	
	@DefaultStringValue("Import Game")
	String importGame();
	
	@DefaultStringValue("Upload Game")
	String uploadGame();
	
	@DefaultStringValue("Choose Game")
	String chooseAGame();
	
	@DefaultStringValue("Upload")
	String upload();
	
	@DefaultStringValue("Provide a name for your game?<br>")
	String provideGameName();
	
}
