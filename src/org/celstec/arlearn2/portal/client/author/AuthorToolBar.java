package org.celstec.arlearn2.portal.client.author;

import com.smartgwt.client.data.Record;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.game.ImportGame;
import org.celstec.arlearn2.portal.client.author.ui.game.ManageGameFiles;
import org.celstec.arlearn2.portal.client.author.ui.run.ExportUsers;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

public class AuthorToolBar extends ToolBar {

	private AuthorPage authorPage;
	private static AuthorConstants constants = GWT.create(AuthorConstants.class);

	public AuthorToolBar(AuthorPage authorPage) {
		super(true);
		this.authorPage = authorPage;
	}

	protected void addButtons() {
		if (AccountManager.getInstance().isAdministrator()) {
			addMenuButton(addAdminOptions());
		}
		addMenuButton(addGameMenuButtons());
        if (AccountManager.getInstance().isAdministrator()) {
            addMenuButton(addRunMenuButtons());
        }
		addMenuButton(addContactMenuButtons());
	}

    private ToolStripMenuButton addRunMenuButtons() {
        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(3);
        MenuItem importGame = new MenuItem("Export users");
        importGame.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                (new ExportUsers()).show();
            }
        });
        menu.setItems(importGame);
        ToolStripMenuButton menuButton = new ToolStripMenuButton("Run", menu);
        menuButton.setWidth(100);

        menuButton.hideContextMenu();
        return menuButton;

    }

	private ToolStripMenuButton addGameMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);
		MenuItem gameAccess = new MenuItem(constants.newGame());
		gameAccess.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				SC.askforValue(constants.newGame(),
						constants.provideGameName(),
						new ValueCallback() {

							@Override
							public void execute(String title) {
								if (title != null) {
									Game newGame = new Game();
									newGame.setTitle(title);
									GameClient.getInstance().createGame(newGame, new JsonCallback(){
										
										public void onJsonReceived(JSONValue jsonValue) {
											GameDataSource.getInstance().loadDataFromWeb();
										}
									});

							}
						}
				});
			}
		});
		MenuItem importGame = new MenuItem(constants.importGame());
		importGame.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				(new ImportGame()).show();
			}
		});

        final MenuItem manageFiles = new MenuItem("Manage Files");
        manageFiles.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                new ManageGameFiles().show();
            }
        });

        final MenuItem viewSections = new MenuItem("View") ;
        final Menu viewSubMenu = new Menu();
        final MenuItem aboutMenuItem = new MenuItem("Show about menu");

        aboutMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                System.out.println("checked "+aboutMenuItem.getChecked());
                aboutMenuItem.setChecked(true);

            }
        });

        viewSubMenu.setItems(aboutMenuItem);

        viewSections.setSubmenu(viewSubMenu);

        if (AccountManager.getInstance().isAdvancedUser()) {
            menu.setItems(gameAccess, importGame, manageFiles, viewSections);
        } else {
            menu.setItems(gameAccess);
        }


        ToolStripMenuButton menuButton = new ToolStripMenuButton(constants.game(), menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		menuButton.hideContextMenu();
		return menuButton;
	}
	
	private ToolStripMenuButton addContactMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);
		MenuItem gameAccess = new MenuItem(constants.inviteContact());
		gameAccess.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				SC.askforValue(constants.inviteContact(),
						constants.emailForContact(),
						new ValueCallback() {

							@Override
							public void execute(String value) {
								if (value != null)
									CollaborationClient.getInstance()
											.addContactViaEmail(value,
													new JsonCallback());

							}
						});
			}
		});

		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(gameAccess, separator);

		ToolStripMenuButton menuButton = new ToolStripMenuButton(constants.contacts(),
				menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		menuButton.hideContextMenu();
		return menuButton;
	}

	private ToolStripMenuButton addAdminOptions() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItem gameAccess = new MenuItem("Load game");
		gameAccess.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				// authorPage.pastePath("myGames/gameAccess");
				SC.askforValue("What gameId do you want to load?",
						new ValueCallback() {

							@Override
							public void execute(String value) {
								GameDataSource.getInstance().loadGame(Long.parseLong(value));
							}
						});
			}
		});
		
		MenuItem runAccess = new MenuItem("Load run");
		runAccess.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				SC.askforValue("What runId do you want to load?",
						new ValueCallback() {

							@Override
							public void execute(String value) {
								RunDataSource.getInstance().loadRun(Long.parseLong(value));
							}
						});
			}
		});

		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(gameAccess, runAccess, separator);

		ToolStripMenuButton menuButton = new ToolStripMenuButton(
				"Admin options", menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		menuButton.hideContextMenu();
		return menuButton;
	}

}
