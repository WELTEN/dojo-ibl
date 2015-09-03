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
package org.celstec.arlearn2.jdo.manager;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.beans.oauth.OauthInfo;
import org.celstec.arlearn2.beans.oauth.OauthInfoList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.OauthConfigurationJDO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


public class OauthKeyManager {

	public static void addKey(int oauthProviderId, String client_id, String client_secret, String redirect_uri) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		OauthConfigurationJDO conf = new OauthConfigurationJDO();
		conf.setOauthProviderId(oauthProviderId);
		conf.setClient_id(client_id);
		conf.setClient_secret(client_secret);
		conf.setRedirect_uri(redirect_uri);
		try {
			pm.makePersistent(conf);
		} finally {
			pm.close();
		}
	}

	public static OauthConfigurationJDO getConfigurationObject(int authProviderId) {
		Key key = KeyFactory.createKey(OauthConfigurationJDO.class.getSimpleName(), authProviderId);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			OauthConfigurationJDO confJDO = pm.getObjectById(OauthConfigurationJDO.class, key);
			return confJDO;
		} finally {
			pm.close();
		}
	}

	public static OauthInfoList getClientInformation() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			javax.jdo.Query query = pm.newQuery(OauthConfigurationJDO.class);

			List<OauthConfigurationJDO> list = (List<OauthConfigurationJDO>) query.execute();
			OauthInfoList resultList = new OauthInfoList();
			for (OauthConfigurationJDO conf : list) {
				OauthInfo info = new OauthInfo();
				info.setClientId(conf.getClient_id());
				info.setProviderId(conf.getOauthProviderId());
				info.setRedirectUri(conf.getRedirect_uri());
				resultList.addOauthInfo(info);
			}
			return resultList;
		} finally {
			pm.close();
		}
	}
}
