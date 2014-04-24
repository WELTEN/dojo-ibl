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
package org.celstec.arlearn2.delegators.inventory;

import java.io.IOException;
import java.util.logging.Level;

import org.celstec.arlearn2.beans.run.InventoryRecord;
import org.celstec.arlearn2.cache.InventoryRecordCache;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.jdo.manager.InventoryRecordManager;


public class CreateInventoryRecord extends GoogleDelegator {

	public CreateInventoryRecord(String authToken)  {
		super(authToken);
	}

	public CreateInventoryRecord(GoogleDelegator gd) {
		super(gd);
	}

	public InventoryRecord createInventoryRecord(InventoryRecord ir) {
		if (ir.getRunId() == null) {
			ir.setError("No run identifier specified");
			return ir;
		}
//		InventoryRecordCache.getInstance().removeInventoryRecords(ir.getRunId(), ir.getScope());
		ir = InventoryRecordManager.addInventoryRecord(ir.getRunId(), ir.getGeneralItemId(), ir.getScope(), ir.getEmail(), ir.getTeamId(), ir.getLat(), ir.getLng(), ir.getStatus(), ir.getTimestamp());
		InventoryRecordCache.getInstance().putInventoryRecordList(ir, ir.getRunId(), ir.getScope(), ir.getGeneralItemId(), ir.getEmail(), ir.getTeamId());
		return ir;
	}
	
	public void deleteInventryRecords(long runId) {
		InventoryRecordManager.deleteInventoryRecords(runId);
		//TODO empty cache, after migrate of cachemanager
	}


}
