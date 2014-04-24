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

import org.celstec.arlearn2.beans.run.InventoryRecord;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.jdo.manager.InventoryRecordManager;
import org.celstec.arlearn2.cache.InventoryRecordCache;


public class UpdateInventoryRecord extends GoogleDelegator {

	public UpdateInventoryRecord(String authToken)  {
		super(authToken);
	}

	public UpdateInventoryRecord(GoogleDelegator gd) {
		super(gd);
	}

	public InventoryRecord updateInventoryRecord(Long runId, InventoryRecord ir) {
		if (ir.getRunId() == null) {
			ir.setError("No run identifier specified");
			return ir;
		}
			QueryInventoryRecord qu = new QueryInventoryRecord(authToken);
			InventoryRecord storedInventoryRecord = qu.getInventoryRecord(runId, ir.getGeneralItemId(), ir.getScope(), ir.getEmail(), ir.getTeamId());

			String teamId = ir.getTeamId();
			String email = ir.getEmail();
			if ("all".equals(ir.getScope())) {
				teamId = null;
				email = null;
			} else if ("team".equals(ir.getScope())) {
				email = null;
			} else if ("user".equals(ir.getScope())) {
				teamId = null;
			}

			InventoryRecordManager.updateInventoryRecord(runId, ir.getGeneralItemId(), ir.getScope(), email, teamId, ir.getLat(), ir.getLng(), ir.getStatus());
			InventoryRecordCache.getInstance().putInventoryRecordList(ir, runId, ir.getScope(), ir.getGeneralItemId(),  email, teamId);

		return ir;
	}

	// public InventoryRecord updateInventoryRecord(Long runId, InventoryRecord
	// ir) {
	// try {
	// QueryInventoryRecord qu = new QueryInventoryRecord(authToken);
	// InventoryRecord storedInventoryRecord = qu.getInventoryRecord(runId,
	// ir.getGeneralItemId(), ir.getScope(), ir.getEmail(), ir.getTeamId());
	// if (ir.getRunId() == null) {
	// ir.setError("No run identifier specified");
	// return ir;
	// }
	// int tableId;
	// tableId = (new
	// QueryInventoryRecord(this)).getInventoryRecordTableIdForRun(ir.getRunId());
	// if (tableId == -1) throw new
	// ServiceException("table to update InventoryRecord "+ir.getEmail()+" does not exist.");
	// if (tableId != -1) {
	// updateRowInInventoryRecordTable(tableId, ir.getGeneralItemId(),
	// ir.getScope(), ir.getEmail(), ir.getTeamId(), ir.getLat(), ir.getLng(),
	// ir.getStatus());
	// InventoryRecordCache.getInstance().removeInventoryRecord(runId,
	// ir.getGeneralItemId(), ir.getScope(), ir.getEmail(), ir.getTeamId());
	// InventoryRecordCache.getInstance().putInventoryRecord(runId, ir);
	// }
	// } catch (IOException e) {
	// logger.log(Level.SEVERE, e.getMessage(), e);
	// ir.setError(e.getMessage());
	// } catch (ServiceException e) {
	// logger.log(Level.SEVERE, e.getMessage(), e);
	// ir.setError(e.getMessage());
	// }
	// return ir;
	// }

//	public InventoryRecord pickupItem(Long runIdentifier, String email, String teamId, Long generalItemId) {
//		QueryInventoryRecord qir = new QueryInventoryRecord(this);
//		QueryGeneralItems qgi = new QueryGeneralItems(this);
//		GeneralItem gi = qgi.getGeneralItem(runIdentifier, generalItemId);
//		InventoryRecord ir = qir.getInventoryRecord(runIdentifier, generalItemId, gi.getScope(), email, teamId);
//		if (ir != null && ("map".equals(ir.getStatus()) || "dropped".equals(ir.getStatus()))) {
//			ir.setStatus("picked");
//			ir.setScope(gi.getScope());
//			ir.setTeamId(teamId);
//			ir.setGeneralItemId(generalItemId);
//			ir.setEmail(email);
//			return updateInventoryRecord(runIdentifier, ir);
//		}
//		return ir;
//	}
//
//	public InventoryRecord dropItem(Long runIdentifier, String email, String teamId, Long generalItemId, Double lat, Double lng) {
//		QueryInventoryRecord qir = new QueryInventoryRecord(this);
//		QueryGeneralItems qgi = new QueryGeneralItems(this);
//		GeneralItem gi = qgi.getGeneralItem(runIdentifier, generalItemId);
//		InventoryRecord ir = qir.getInventoryRecord(runIdentifier, generalItemId, gi.getScope(), email, teamId);
//		if (ir != null && "picked".equals(ir.getStatus())) {
//			ir.setStatus("dropped");
//			ir.setScope(gi.getScope());
//			ir.setTeamId(teamId);
//			ir.setGeneralItemId(generalItemId);
//			ir.setEmail(email);
//			ir.setLat(lat);
//			ir.setLng(lng);
//			return updateInventoryRecord(runIdentifier, ir);
//		}
//		return ir;
//	}
//
//	public InventoryRecord dropAtDropZone(Long runIdentifier, String email, String teamId, Long generalItemId, Long dropZoneId) {
//		QueryInventoryRecord qir = new QueryInventoryRecord(this);
//		QueryGeneralItems qgi = new QueryGeneralItems(this);
//		GeneralItem gi = qgi.getGeneralItem(runIdentifier, generalItemId);
//		if (gi instanceof PickupItem && dropZoneId != null && dropZoneId.equals(((PickupItem) gi).getDropZoneId())) {
//			InventoryRecord ir = qir.getInventoryRecord(runIdentifier, generalItemId, gi.getScope(), email, teamId);
//			if (ir != null && "picked".equals(ir.getStatus())) {
//				GeneralItem dz = qgi.getGeneralItem(runIdentifier, dropZoneId);
//				if (dz != null && dz instanceof DropZone) {
//					ir.setStatus("droppedAtDropZone");
//					ir.setScope(gi.getScope());
//					ir.setTeamId(teamId);
//					ir.setGeneralItemId(generalItemId);
//					ir.setEmail(email);
//					ir.setLat(dz.getLat());
//					ir.setLng(dz.getLng());
//					return updateInventoryRecord(runIdentifier, ir);
//				}
//			}
//			return ir;
//		}
//		return null;
//	}

}
