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

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.jdo.manager.ResponseManager;

import java.lang.reflect.InvocationTargetException;


public class ResponseDelegator extends GoogleDelegator {

    public ResponseDelegator(Service service) {
        super(service);
    }

    public ResponseDelegator(String authtoken) {
        super(authtoken);
    }

    public ResponseDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public Response createResponse(Long runIdentifier, Response r) {
        RunDelegator rd = new RunDelegator(this);
        Run run = rd.getRun(runIdentifier);
        if (run == null) {
            r.setError("invalid run identifier");
            return r;
        }
        UsersDelegator qu = new UsersDelegator(this);
        AccountDelegator ad = new AccountDelegator(this);
        MailDelegator md = new MailDelegator(this);

        r.setUserEmail(qu.getCurrentUserAccount());

        long id = ResponseManager.addResponse(r.getGeneralItemId(), r.getResponseValue(), run.getRunId(), r.getUserEmail(), r.getTimestamp(), r.getLat(), r.getLng(), r.getParentId());
        r.setResponseId(id);
        r.setResponseValue(ResponseManager.normalizeValue(r.getResponseValue()));
        RunAccessDelegator rad = new RunAccessDelegator(this);
        NotificationDelegator nd = new NotificationDelegator(this);
        for (RunAccess ra : rad.getRunAccess(r.getRunId()).getRunAccess()) {
            nd.broadcast(r, ra.getAccount());
        }

        if(r.getParentId() != 0){
            Response parent = ResponseManager.getResponse(r.getParentId());
            Account account_quoted = ad.getContactDetails(parent.getUserEmail());

            md.commentReminder(r, account_quoted);

        }

        try {
            new DojoAnalyticsDelegator(this).registerStatement(qu.getCurrentAccount().getEmail(), "created", "http://localhost:8080/main.html#/group/"+r.getRunId()+"/activity/"+r.getGeneralItemId()+"/response/"+r.getResponseId());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return r;
    }

    public ResponseList getResponses(Long runId, Long itemId, String account) {
        ResponseList rl = new ResponseList();
        rl.setResponses(ResponseManager.getResponse(runId, itemId, account, null, false));
        return rl;
    }

    public ResponseList getResponsesFromUntil(Long runId, Long itemId, String fullId, Long from, Long until, String cursor) {
        return ResponseManager.getResponse(runId, itemId, fullId, from, until, cursor);
    }

    public ResponseList getResponsesFromUntil(Long runId, Long itemId, Long from, Long until, String cursor) {
        return ResponseManager.getResponse(runId, itemId, from, until, cursor);
    }

    public ResponseList getResponsesFromUntil(Long runId, Long from, Long until, String cursor, boolean orderByLastModificationDate) {
        return ResponseManager.getResponse(runId, from, until, cursor, orderByLastModificationDate);
    }

    public Response getResponse(Long responseId) {
        return ResponseManager.getResponse(responseId);
    }

    public Response revokeResponse(Long responseId) {
        UsersDelegator qu = new UsersDelegator(this);

        try {
            new DojoAnalyticsDelegator(this).registerStatement(qu.getCurrentAccount().getEmail(), "deleted", "http://localhost:8080/main.html#/group/"+getResponse(responseId).getRunId()+"/activity/"+getResponse(responseId).getGeneralItemId()+"/response/"+getResponse(responseId).getResponseId());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return ResponseManager.revokeResponse(responseId);
    }

    public Response revokeResponse(Response r) {
        UsersDelegator qu = new UsersDelegator(this);

        try {
            new DojoAnalyticsDelegator(this).registerStatement(qu.getCurrentAccount().getEmail(), "deleted", "http://localhost:8080/main.html#/group/"+r.getRunId()+"/activity/"+r.getGeneralItemId()+"/response/"+r.getResponseId());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return ResponseManager.revokeResponse(r.getRunId(), r.getGeneralItemId(), r.getUserEmail(), r.getTimestamp());
    }

    public void deleteResponses(Long runId) {
        ResponseManager.deleteResponses(runId, null);
    }

    public void deleteResponses(Long runId, String email) {
        ResponseManager.deleteResponses(runId, email);

    }

//    public CSVEntry csv(Long runIdentifier) {
//        return CSVCache.getInstance().getCSV(runIdentifier);
//    }
}
