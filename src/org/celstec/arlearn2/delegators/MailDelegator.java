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
package org.celstec.arlearn2.delegators;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MailDelegator extends GoogleDelegator {

    private static final Logger logger = Logger.getLogger(ActionDelegator.class.getName());

    public MailDelegator(String authtoken) {
        super(authtoken);
    }

    public MailDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public void sendMail(String from, String fromName, String toMail, String subject, String msgBody) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, fromName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
//			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
            msg.setSubject(subject);

            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(msgBody, "text/html");
            final Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);

            msg.setContent(mp);
            Transport.send(msg);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void sendInstructionMail(String from, String fromName, String toMail) {
        if (!toMail.contains("@")) toMail += "@gmail.com";
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "<html><body>";
        msgBody += "Hi,<br>";
        msgBody += "<p>";
        msgBody += "Welcome to ARLearn. We hope you'll find this a useful tool.";
        msgBody += "</p>";
        msgBody += "<p>";
        msgBody += "ARLearn content is organized in games and runs. Both can be created and managed with the <a href=\"http://streetlearn.appspot.com/\">ARLearn authoring tool</a>.";
        msgBody += "</p>";
        msgBody += "<p>";
        msgBody += "For more information on using ARLearn, visit our <a href=\"http://ou.nl/arlearn\">get started page</a>.";
        msgBody += "</p>";
        msgBody += "<p>";
        msgBody += "Have fun<br>";
        msgBody += "The ARLearn team.";
        msgBody += "</p>";
        msgBody += "</body></html>";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, fromName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
            msg.setSubject("Instructions for using ARLearn");

            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(msgBody, "text/html");
            final Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);

            msg.setContent(mp);
            Transport.send(msg);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
