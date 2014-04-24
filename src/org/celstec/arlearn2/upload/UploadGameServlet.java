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
package org.celstec.arlearn2.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.RunPackage;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.OpenUrl;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.ScoreDefinitionDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;

import org.codehaus.jettison.json.JSONObject;


public class UploadGameServlet extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		long gameId = 0l;
		String auth = null;
		try {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(req);

			String json = "";
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				InputStream stream = item.openStream();
				if (item.isFormField()) {
					String value = Streams.asString(stream);
					if ("gameId".equals(name)) gameId = Long.parseLong(value);
					if ("auth".equals(name)) auth = value;
					
				} else {
					json = Streams.asString(stream);

				}
			}

			res.setContentType("text/plain");
			JSONObject jObject = new JSONObject(json);
			Object deserialized = JsonBeanDeserializer.deserialize(json);
			
			if (deserialized instanceof GamePackage && ((GamePackage) deserialized).getGame() != null)
				unpackGame((GamePackage) deserialized, req, auth);
			if (deserialized instanceof RunPackage && ((RunPackage) deserialized ).getRun() != null)
				unpackRun((RunPackage) deserialized, req, gameId, auth);

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	private void unpackRun(RunPackage runPackage, HttpServletRequest req,
			long gameId, String auth)  {
		auth = auth == null?req.getHeader("Authorization"):auth;
		RunUnpacker ru = new RunUnpacker(runPackage, auth, gameId);
		
		ru.unpack();

	}

	private void unpackGame(GamePackage arlPackage, HttpServletRequest req, String auth) {
		auth = auth == null?req.getHeader("Authorization"):auth;
		GameUnpacker gu = new GameUnpacker(arlPackage, auth);
		gu.unpack();
	}
//	private void unpackGame_old(GamePackage arlPackage, HttpServletRequest req, String auth)
//			 {
//		Game game = arlPackage.getGame();
//		if (game != null) {
//			GameDelegator gd = new GameDelegator(auth == null?req.getHeader("Authorization"):auth);
//			game.setGameId(null);
//			game = gd.createGame(game);
//			Long gameId = game.getGameId();
//			Iterator<GeneralItem> it = arlPackage.getGeneralItems().iterator();
//			GeneralItemDelegator gid = new GeneralItemDelegator(auth == null?req.getHeader("Authorization"):auth);
//
////			CreateGeneralItems cr = new CreateGeneralItems(
////					auth == null?req.getHeader("Authorization"):auth);
//			while (it.hasNext()) {
//				GeneralItem generalItem = (GeneralItem) it.next();
//				generalItem.setGameId(gameId);
//
//				gid.createGeneralItem(generalItem);
//			}
//			if (arlPackage.getScoreDefinitions() != null) {
//				Iterator<ScoreDefinition> scoreIterator = arlPackage
//						.getScoreDefinitions().iterator();
//				ScoreDefinitionDelegator sdd = new ScoreDefinitionDelegator(gid);
//				while (scoreIterator.hasNext()) {
//					ScoreDefinition scoreDefinition = (ScoreDefinition) scoreIterator
//							.next();
//					scoreDefinition.setGameId(gameId);
//					sdd.createScoreDefinition(scoreDefinition);
//
//				}
//			}
//		}
//
//	}

	public static String slurp(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
}
