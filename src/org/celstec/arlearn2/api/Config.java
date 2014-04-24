package org.celstec.arlearn2.api;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameJDO;
import org.celstec.arlearn2.jdo.classes.GeneralItemJDO;
import org.celstec.arlearn2.jdo.classes.GeneralItemVisibilityJDO;
import org.celstec.arlearn2.jdo.classes.UserJDO;


@Path("/config")
public class Config {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getInfo(
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			 {
		String email = "arlearn1";
		Long gameId = 100l;
		Long from = System.currentTimeMillis() - 60000l;
		Long until = System.currentTimeMillis() + 60000l;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(UserJDO.class);
		String filter = "email == emailParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
		String params = "String emailParam, Long fromParam, Long untilParam";
		Object args[] = new Object[] { email, from, until };
		query.setFilter(filter);
		query.declareParameters(params);
		Iterator<UserJDO> it = ((List<UserJDO>) query.executeWithArray(args))
				.iterator();

		filter = "email == emailParam & lastModificationDateGame >= fromParam & lastModificationDateGame <= untilParam";
		query.setFilter(filter);
		query.declareParameters(params);
		it = ((List<UserJDO>) query.executeWithArray(args)).iterator();

		query = pm.newQuery(GeneralItemJDO.class);
		args = new Object[] { gameId, from, until };
		params = "String gameParam, Long fromParam, Long untilParam";
		filter = "gameId == gameParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
		query.setFilter(filter);
		query.declareParameters(params);
		it = ((List<UserJDO>) query.executeWithArray(args)).iterator();
		
		query = pm.newQuery(GeneralItemVisibilityJDO.class);
		args = new Object[] { email, gameId, from, until };
		params = "String emailParam, String gameParam, Long fromParam, Long untilParam";
		filter = "email == emailParam & runId == gameParam & lastModificationDate >= fromParam & lastModificationDate <= untilParam";
		query.setFilter(filter);
		query.declareParameters(params);
		it = ((List<UserJDO>) query.executeWithArray(args)).iterator();

		query = pm.newQuery(GameJDO.class);
		args = new Object[] { email, from, until };
		params = "String emailParam, Long fromParam, Long untilParam";
		filter = "owner == emailParam &lastModificationDate >= fromParam & lastModificationDate <= untilParam";
		query.setFilter(filter);
		query.declareParameters(params);
		it = ((List<UserJDO>) query.executeWithArray(args)).iterator();
		return "{}";
	}
}
