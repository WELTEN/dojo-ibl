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

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.run.Run;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MailDelegator extends GoogleDelegator {

    private static final Logger logger = Logger.getLogger(ActionDelegator.class.getName());
    private static final long MINUTES =  1; // 1 minute
    private static final long MILLIS_PER_DAY =  1 * MINUTES * 60 * 1000L;

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

    public void sendReminders(org.celstec.arlearn2.beans.run.Message message, String list_email) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        logger.log(Level.SEVERE, dateFormat.format(date));

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        AccountDelegator ad = new AccountDelegator(this);
        RunDelegator rd = new RunDelegator(this);

        Account account = ad.getContactDetails(message.getSenderProviderId() + ":" + message.getSenderId());
        Run run = rd.getRun(message.getRunId());

        String content = "You have missed <strong>a message</strong> in "+run.getTitle()+" in the last "+MINUTES+" minutes.";
        String message_missed = "<strong>"+account.getName()+"</strong>: "+message.getBody();
        String button = "<a href=\"http://dojo-ibl.appspot.com/main.html#/inquiry/"+run.getRunId()+"\" class=\"btn-primary\">Catch up now!</a>";
        String email_header = "Message missed - DojoIBL";
        String subject = "("+run.getTitle()+") Someone sent a message in DojoIBL";

        String from = "titogelo@gmail.com";
        String fromName = account.getGivenName()+" (DojoIBL)";

        String msgBody = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        msgBody += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
        msgBody += "<head>";
        msgBody += "<meta name=\"viewport\" content=\"width=device-width\" />";
        msgBody += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />";
        msgBody += "<title>Weekle digest DojoIBL</title>";

        msgBody += "<style>/* -------------------------------------";
        msgBody += "GLOBAL";
        msgBody += "A very basic CSS reset";
        msgBody += "------------------------------------- */";
        msgBody += "* {";
        msgBody += "margin: 0;";
        msgBody += "padding: 0;";
        msgBody += "font-family: \"Helvetica Neue\", \"Helvetica\", Helvetica, Arial, sans-serif;";
        msgBody += "box-sizing: border-box;";
        msgBody += "font-size: 14px;";
        msgBody += "}";
        msgBody += "";
        msgBody += "img {";
        msgBody += "max-width: 100%;";
        msgBody += "}";
        msgBody += "";
        msgBody += "body {";
        msgBody += "-webkit-font-smoothing: antialiased;";
        msgBody += "-webkit-text-size-adjust: none;";
        msgBody += "width: 100% !important;";
        msgBody += "height: 100%;";
        msgBody += "line-height: 1.6;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* Let's make sure all tables have defaults */";
        msgBody += "table td {";
        msgBody += "vertical-align: top;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "BODY & CONTAINER";
        msgBody += "------------------------------------- */";
        msgBody += "body {";
        msgBody += "background-color: #f6f6f6;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".body-wrap {";
        msgBody += "background-color: #f6f6f6;";
        msgBody += "width: 100%;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".container {";
        msgBody += "display: block !important;";
        msgBody += "max-width: 600px !important;";
        msgBody += "margin: 0 auto !important;";
        msgBody += "/* makes it centered */";
        msgBody += "clear: both !important;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".content {";
        msgBody += "max-width: 600px;";
        msgBody += "margin: 0 auto;";
        msgBody += "display: block;";
        msgBody += "padding: 20px;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "HEADER, FOOTER, MAIN";
        msgBody += "------------------------------------- */";
        msgBody += ".main {";
        msgBody += "background: #fff;";
        msgBody += "border: 1px solid #e9e9e9;";
        msgBody += "border-radius: 3px;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".content-wrap {";
        msgBody += "padding: 20px;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".content-block {";
        msgBody += "padding: 0 0 20px;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".header {";
        msgBody += "width: 100%;";
        msgBody += "margin-bottom: 20px;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".footer {";
        msgBody += "width: 100%;";
        msgBody += "clear: both;";
        msgBody += "color: #999;";
        msgBody += "padding: 20px;";
        msgBody += "}";
        msgBody += ".footer a {";
        msgBody += "color: #999;";
        msgBody += "}";
        msgBody += ".footer p, .footer a, .footer unsubscribe, .footer td {";
        msgBody += "font-size: 12px;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "TYPOGRAPHY";
        msgBody += "------------------------------------- */";
        msgBody += "h1, h2, h3 {";
        msgBody += "font-family: \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;";
        msgBody += "color: #000;";
        msgBody += "margin: 40px 0 0;";
        msgBody += "line-height: 1.2;";
        msgBody += "font-weight: 400;";
        msgBody += "}";
        msgBody += "";
        msgBody += "h1 {";
        msgBody += "font-size: 32px;";
        msgBody += "font-weight: 500;";
        msgBody += "}";
        msgBody += "";
        msgBody += "h2 {";
        msgBody += "font-size: 24px;";
        msgBody += "}";
        msgBody += "";
        msgBody += "h3 {";
        msgBody += "font-size: 18px;";
        msgBody += "}";
        msgBody += "";
        msgBody += "h4 {";
        msgBody += "font-size: 14px;";
        msgBody += "font-weight: 600;";
        msgBody += "}";
        msgBody += "";
        msgBody += "p, ul, ol {";
        msgBody += "margin-bottom: 10px;";
        msgBody += "font-weight: normal;";
        msgBody += "}";
        msgBody += "p li, ul li, ol li {";
        msgBody += "margin-left: 5px;";
        msgBody += "list-style-position: inside;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "LINKS & BUTTONS";
        msgBody += "------------------------------------- */";
        msgBody += "a {";
        msgBody += "color: #1ab394;";
        msgBody += "text-decoration: underline;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".btn-primary {";
        msgBody += "text-decoration: none;";
        msgBody += "color: #FFF;";
        msgBody += "background-color: #1ab394;";
        msgBody += "border: solid #1ab394;";
        msgBody += "border-width: 5px 10px;";
        msgBody += "line-height: 2;";
        msgBody += "font-weight: bold;";
        msgBody += "text-align: center;";
        msgBody += "cursor: pointer;";
        msgBody += "display: inline-block;";
        msgBody += "border-radius: 5px;";
        msgBody += "text-transform: capitalize;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "OTHER STYLES THAT MIGHT BE USEFUL";
        msgBody += "------------------------------------- */";
        msgBody += ".last {";
        msgBody += "margin-bottom: 0;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".first {";
        msgBody += "margin-top: 0;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".aligncenter {";
        msgBody += "text-align: center;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".alignright {";
        msgBody += "text-align: right;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".alignleft {";
        msgBody += "text-align: left;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".clear {";
        msgBody += "clear: both;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "ALERTS";
        msgBody += "Change the class depending on warning email, good email or bad email";
        msgBody += "------------------------------------- */";
        msgBody += ".alert {";
        msgBody += "font-size: 16px;";
        msgBody += "color: #fff;";
        msgBody += "font-weight: 500;";
        msgBody += "padding: 20px;";
        msgBody += "text-align: center;";
        msgBody += "border-radius: 3px 3px 0 0;";
        msgBody += "}";
        msgBody += ".alert a {";
        msgBody += "color: #fff;";
        msgBody += "text-decoration: none;";
        msgBody += "font-weight: 500;";
        msgBody += "font-size: 16px;";
        msgBody += "}";
        msgBody += ".alert.alert-warning {";
        msgBody += "background: #f8ac59;";
        msgBody += "}";
        msgBody += ".alert.alert-bad {";
        msgBody += "background: #ed5565;";
        msgBody += "}";
        msgBody += ".alert.alert-good {";
        msgBody += "background: #1ab394;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "INVOICE";
        msgBody += "Styles for the billing table";
        msgBody += "------------------------------------- */";
        msgBody += ".invoice {";
        msgBody += "margin: 40px auto;";
        msgBody += "text-align: left;";
        msgBody += "width: 80%;";
        msgBody += "}";
        msgBody += ".invoice td {";
        msgBody += "padding: 5px 0;";
        msgBody += "}";
        msgBody += ".invoice .invoice-items {";
        msgBody += "width: 100%;";
        msgBody += "}";
        msgBody += ".invoice .invoice-items td {";
        msgBody += "border-top: #eee 1px solid;";
        msgBody += "}";
        msgBody += ".invoice .invoice-items .total td {";
        msgBody += "border-top: 2px solid #333;";
        msgBody += "border-bottom: 2px solid #333;";
        msgBody += "font-weight: 700;";
        msgBody += "}";
        msgBody += "";
        msgBody += "/* -------------------------------------";
        msgBody += "RESPONSIVE AND MOBILE FRIENDLY STYLES";
        msgBody += "------------------------------------- */";
        msgBody += "@media only screen and (max-width: 640px) {";
        msgBody += "h1, h2, h3, h4 {";
        msgBody += "font-weight: 600 !important;";
        msgBody += "margin: 20px 0 5px !important;";
        msgBody += "}";
        msgBody += "";
        msgBody += "h1 {";
        msgBody += "font-size: 22px !important;";
        msgBody += "}";
        msgBody += "";
        msgBody += "h2 {";
        msgBody += "font-size: 18px !important;";
        msgBody += "}";
        msgBody += "";
        msgBody += "h3 {";
        msgBody += "font-size: 16px !important;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".container {";
        msgBody += "width: 100% !important;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".content, .content-wrap {";
        msgBody += "padding: 10px !important;";
        msgBody += "}";
        msgBody += "";
        msgBody += ".invoice {";
        msgBody += "width: 100% !important;";
        msgBody += "}";
        msgBody += "} </style>";


        msgBody += "</head>";
        msgBody += "";
        msgBody += "<body>";
        msgBody += "";
        msgBody += "<table class=\"body-wrap\">";
        msgBody += "<tr>";
        msgBody += "<td></td>";
        msgBody += "<td class=\"container\" width=\"600\">";
        msgBody += "<div class=\"content\">";
        msgBody += "<table class=\"main\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">";
        msgBody += "<tr>";
        msgBody += "<td class=\"alert alert-good\">";
        msgBody += email_header;
        msgBody += "</td>";
        msgBody += "</tr>";
        msgBody += "<tr>";
        msgBody += "<td class=\"content-wrap\">";
        msgBody += "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">";
        msgBody += "<tr>";
        msgBody += "<td class=\"content-block\">";

//        MessageDelegator messageDelegator = new MessageDelegator();
//        RunDelegator runDelegator = new RunDelegator();
//
//        RunList r = runDelegator.getParticipateRuns();
//
//        messageDelegator.getMessagesForDefaultThread();

        msgBody += content;
        msgBody += "</td>";
        msgBody += "</tr>";
        msgBody += "<tr>";
        msgBody += "<td class=\"content-block\">";
        msgBody += message_missed;
        msgBody += "</td>";
        msgBody += "</tr>";
        msgBody += "<tr>";
        msgBody += "<td class=\"content-block\">";
        msgBody += button;
        msgBody += "</td>";
        msgBody += "</tr>";
        msgBody += "<tr>";
        msgBody += "<td class=\"content-block\">";
        msgBody += "Thanks for using DojoIBL";
        msgBody += "</td>";
        msgBody += "</tr>";
        msgBody += "</table>";
        msgBody += "</td>";
        msgBody += "</tr>";
        msgBody += "</table>";
        msgBody += "<div class=\"footer\">";
        msgBody += "<table width=\"100%\">";
        msgBody += "<tr>";
        msgBody += "<td class=\"aligncenter content-block\"><a href=\"http://dojo-ibl.appspot.com/main.html#/home\">http://dojo-ibl.appspot.com</a> - Welten Institute</td>";
        msgBody += "</tr>";
        msgBody += "</table>";
        msgBody += "</div></div>";
        msgBody += "</td>";
        msgBody += "<td></td>";
        msgBody += "</tr>";
        msgBody += "</table>";
        msgBody += "";
        msgBody += "</body>";
        msgBody += "</html>";


//        String msgBody = "<html><body>";
//        msgBody += "Hi,<br>";
//        msgBody += "<p>";
//        msgBody += "Welcome to ARLearn. We hope you'll find this a useful tool.";
//        msgBody += "</p>";
//        msgBody += "<p>";
//        msgBody += "ARLearn content is organized in games and runs. Both can be created and managed with the <a href=\"http://streetlearn.appspot.com/\">ARLearn authoring tool</a>.";
//        msgBody += "</p>";
//        msgBody += "<p>";
//        msgBody += "For more information on using ARLearn, visit our <a href=\"http://ou.nl/arlearn\">get started page</a>.";
//        msgBody += "</p>";
//        msgBody += "<p>";
//        msgBody += "Have fun<br>";
//        msgBody += "The ARLearn team.";
//        msgBody += "</p>";
//        msgBody += "</body></html>";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, fromName));

            String[] parts = list_email.split(";");

            for (int i = 0; i < parts.length; i++) {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(parts[i]));
            }

            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
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
}
