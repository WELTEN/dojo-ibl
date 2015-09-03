package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.cache.UsersCache;
import org.celstec.arlearn2.delegators.NotificationDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.jdo.manager.UserManager;

import java.util.List;

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
public class DeleteUserAfterDeleteRun extends GenericBean {

    private Long runId;


    public DeleteUserAfterDeleteRun() {
        super();
    }

    public DeleteUserAfterDeleteRun(String token, Long runId) {
        super(token);
        this.runId = runId;

    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    @Override
    public void run() {
            UsersDelegator ud = new UsersDelegator(getToken());

            if (getRunId() != null) {
                List<User> userList = ud.getUserList(runId, null, null, null);
                for (User u : userList) {

                    User user = ud.getUserByEmail(runId, u.getFullId());
                    // UserManager.deleteUser(runId, email);
                    UserManager.setStatusDeleted(runId, u.getFullId());
                    UsersCache.getInstance().removeUser(runId); // removing because user
                    if (user.getEmail() != null) {
                        User notificationUser = new User();
                        notificationUser.setRunId(user.getRunId());
                        notificationUser.setFullIdentifier(user.getFullId());
                        new NotificationDelegator(getToken()).broadcast(notificationUser, user.getFullId());
                    }
                }
            }

    }
}
