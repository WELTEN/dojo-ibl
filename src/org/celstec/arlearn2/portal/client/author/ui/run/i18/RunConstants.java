package org.celstec.arlearn2.portal.client.author.ui.run.i18;

import com.google.gwt.i18n.client.Constants;

public interface RunConstants extends Constants {

	
	@DefaultStringValue("Runs")
	String runs();
	
	@DefaultStringValue("Run Title")
	String runTitle();
	
	@DefaultStringValue("Game Title")
	String gameTitle();
	
	@DefaultStringValue("Change")
	String runAccess();
	
	@DefaultStringValue("Do you want to delete '***' <br> ")
	String deleteThisRun();
	
	@DefaultStringValue("Team and players")
	String teamAndUsers();
	
	@DefaultStringValue("Delete")
	String delete();
	
	@DefaultStringValue("Delete player")
	String deletePlayer();
	
	@DefaultStringValue("Delete user '***' from this run? <br>")
	String confirmDeletePlayer();
	
	@DefaultStringValue("Name")
	String name();
	
	@DefaultStringValue("Email")
	String email();
	
	@DefaultStringValue("Create QR login tokens")
	String createQRLoginTokens();
	
	@DefaultStringValue("Define name prefix")
	String defineNamePrefix();
	
	@DefaultStringValue("# amount")
	String amount();
	
	@DefaultStringValue("Create Tokens")
	String createTokens();
	
	@DefaultStringValue("Print Tokens")
	String printTokens();
	
	@DefaultStringValue("Add Players")
	String addPlayers();
	
	@DefaultStringValue("Select people")
	String selectAccounts();
	
	@DefaultStringValue("Submit Players")
	String submitPlayers();

	@DefaultStringValue("Create Team")
	String team();
	
	@DefaultStringValue("Team name")
	String teamName();
	
	@DefaultStringValue("Add teams")
	String addTeams();
	
	@DefaultStringValue("Submit team")
	String submitTeam();
	
	@DefaultStringValue("Delete team")
	String deleteTeam();
	
	@DefaultStringValue("Delete team '***' from this run? <br>")
	String confirmDeleteTeam();
}
