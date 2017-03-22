/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.tasks;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.delegators.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TimerSendEmail extends HttpServlet {

	private static final long MILLIS_PER_DAY =  1 * 1 * 60 * 1000L; // 1 minute
    private static String token;
    private static Long runId;
	private static final Logger logger = Logger.getLogger(TimerSendEmail.class.getName());


	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		try {

			token = request.getParameter("token");
			runId = Long.parseLong(request.getParameter("name"));

			RunAccessDelegator rad = new RunAccessDelegator(token);
			AccountDelegator ad = new AccountDelegator(token);
			NotificationDelegator nd = new NotificationDelegator(token);
			MailDelegator md = new MailDelegator(token);

			for(RunAccess ra: rad.getRunAccess(runId).getRunAccess()) {

				Account account1 = ad.getContactDetails(ra.getAccount());

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

				md.sendReminders();

				//				if(Math.abs(account1.getLastModificationDate() -  date.getTime()) < MILLIS_PER_DAY){
				//					System.out.print("["+account1.getEmail()+"] "+account1.getName()+" login less than 1 minutes ago");
				//				}else{
				//					System.out.print("["+account1.getEmail()+"] "+account1.getName()+" login more than 1 minutes ago");
				//				}
			}


		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}



	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
