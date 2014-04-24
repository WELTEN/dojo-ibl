package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.ContactJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.ContactManager;

import com.google.appengine.api.utils.SystemProperty;

public class CollaborationDelegator  extends GoogleDelegator {

	public CollaborationDelegator(String authtoken)  {
		super(authtoken);
	}

	public CollaborationDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public Account getContactDetails(String addContactToken) {
		Account myAccount = ContactManager.getContactViaId(addContactToken);
		if (myAccount == null) return null;
		AccountDelegator ad = new AccountDelegator(this);
		myAccount =  ad.getAccountInfo(myAccount);
		if (myAccount == null) return null;
		return myAccount;
	}
	
	public void addContactViaEmail(String toEmail) {
		com.google.apphosting.api.ApiProxy.getCurrentEnvironment();
//		String version = SystemProperty.version.get();
//		String applicationVersion = 
		Account fullAccount = getMyAccount();
		if (fullAccount == null) return;
		ContactJDO jdo = ContactManager.addContactInvitation(fullAccount.getLocalId(), fullAccount.getAccountType(), toEmail);
		
		String msgBody = "<html><body>";
		msgBody += "Hi,<br>";
		msgBody += "<p>";
		msgBody += fullAccount.getName()+" has invited you to become his ARLearn contact";
		msgBody += "</p>";
		msgBody += "<p>";
		msgBody += "Click  <a href=\"http://"+SystemProperty.applicationId.get()+".appspot.com/contact.html?id="+jdo.getUniqueId()+"\">here</a> to accept this invitation.";
		msgBody += "</p>";
		msgBody += "</body></html>";
		
		MailDelegator md;

			System.out.println("sending mail");
			System.out.println("from  "+fullAccount.getEmail());
			System.out.println("name  "+fullAccount.getName());
			System.out.println("to  "+toEmail);
			System.out.println("body  "+msgBody);
			md = new MailDelegator(getAuthToken());
			md.sendMail("no-reply@"+SystemProperty.applicationId.get()+".appspotmail.com", fullAccount.getName(), toEmail, "Pending contact request", msgBody);


	}

	public String confirmAddContact(String addContactToken) {
		Account fullAccount = getMyAccount();
		if (fullAccount == null) return null;
		Account targetAccount = ContactManager.getContactViaId(addContactToken);
		ContactManager.addContact(fullAccount, targetAccount, addContactToken);
		return "{}";
	}
	
	public Account getMyAccount() {
		UsersDelegator qu = new UsersDelegator(this);
		Account myAccount = qu.getCurrentUserAccountObject();
		if (myAccount == null) {
			return null;
		}
		return AccountManager.getAccount(myAccount);
	}

	public AccountList getContacts(Long from, Long until, String cursor) {
		UsersDelegator qu = new UsersDelegator(this);
		Account myAccount = qu.getCurrentUserAccountObject();
		return ContactManager.getContacts(myAccount, from , until, cursor);
	}

	

}
