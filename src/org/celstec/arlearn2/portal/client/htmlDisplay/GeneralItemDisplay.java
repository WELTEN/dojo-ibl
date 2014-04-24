package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Action;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Response;

import java.util.HashMap;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
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
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public abstract class GeneralItemDisplay {

    protected HashMap<String, Account> accountMap = new HashMap<String, Account>();

    public abstract Canvas getCanvas();
    public abstract void handleResponse(Response response);
    public abstract void handleAction(Action action);

    public void putAccount(Account account) {
        accountMap.put(account.getFullId(), account);
    }


}
