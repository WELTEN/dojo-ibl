package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.run.GeneralItemsStatus;
import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.jdo.manager.GeneralItemStatusManager;

import java.util.List;

/**
 * ****************************************************************************
 * Copyright (C) 2016 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Angel Suarez
 * Date: 07/10/16
 * ****************************************************************************
 */

public class GeneralItemStatusDelegator extends GoogleDelegator {

    public GeneralItemStatusDelegator(String authToken) {
        super(authToken);
    }

    public GeneralItemStatusDelegator(Service service) {
        super(service);
    }

    public GeneralItemStatusDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public GeneralItemsStatus changeItemStatus(Long runId, Long generalItemId, Integer status) {

        GeneralItemsStatus generalItemsStatus = GeneralItemStatusManager.addGeneralItemStatus(runId, generalItemId, status);

        RunAccessDelegator rad = new RunAccessDelegator(this);
        NotificationDelegator nd = new NotificationDelegator(this);
        for (RunAccess ra : rad.getRunAccess(runId).getRunAccess()) {
            nd.broadcast(generalItemsStatus, ra.getAccount());
        }

        return generalItemsStatus;
    }

    public GeneralItemsStatus getItemStatus(Long runId, Long generalItemId) {
        GeneralItemsStatus generalItemsStatus = null;
        List<GeneralItemsStatus> generalItemsStatusList = GeneralItemStatusManager.getGeneralitemsStatusFromUntil(runId, generalItemId, null, null);
        if (generalItemsStatusList.isEmpty())
            return null;
        generalItemsStatus = generalItemsStatusList.get(0);

        return generalItemsStatus;
    }
}