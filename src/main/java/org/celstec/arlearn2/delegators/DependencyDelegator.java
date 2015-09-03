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
 * Contributors: Roland Klemke
 ******************************************************************************/
package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.dependencies.*;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.tasks.beans.GenericBean;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class serves as base class for GeneralItemDelegator and VariableDelegator
 * in order to unify the dependency analysis procedures.
 * <p/>
 * User: klemke
 * Date: 23.07.13
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class DependencyDelegator extends GoogleDelegator {

    public DependencyDelegator(String authToken) {
        super(authToken);
    }

    public DependencyDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public DependencyDelegator(Account account, String token) {
        super(account, token);
    }

    public DependencyDelegator(Service service) {
        super(service);
    }

    public DependencyDelegator(GenericBean bean) {
        super(bean);
    }

    protected boolean influencedBy(Dependency dependsOn, Action action) {
        if (dependsOn == null)
            return false;
        if (dependsOn instanceof ActionDependency) {
            if (((ActionDependency) dependsOn).getAction() == null && action.getAction() != null)
                return false;
            return ((ActionDependency) dependsOn).getAction().equals(action.getAction());
        }
        if (dependsOn instanceof TimeDependency)
            return influencedBy(((TimeDependency) dependsOn).getOffset(), action);
        if (dependsOn instanceof BooleanDependency) {
            BooleanDependency bd = (BooleanDependency) dependsOn;
            boolean result = false;
            for (Dependency d : bd.getDependencies()) {
                result = result || influencedBy(d, action);
            }
            return result;
        }
        return false;
    }

    private boolean hasRole(User u, String role) {
        if (u.getRoles() == null)
            return false;
        for (String r : u.getRoles()) {
            if (role.equals(r))
                return true;
        }
        return false;
    }

    public long checkActions(Dependency dep, ActionList al, User u, HashMap<String, User> uMap) {
        if (dep instanceof ActionDependency) {
            return checkActions(((ActionDependency) dep), al, u, uMap);
        }
        if (dep instanceof AndDependency) {
            return checkActions(((AndDependency) dep), al, u, uMap);
        }
        if (dep instanceof OrDependency) {
            return checkActions(((OrDependency) dep), al, u, uMap);
        }
        if (dep instanceof TimeDependency) {
            return checkActions(((TimeDependency) dep), al, u, uMap);
        }
        return -1;
    }

    public long checkActions(ActionDependency dOn, ActionList al, User u, HashMap<String, User> uMap) {
        Iterator<Action> it = al.getActions().iterator();
        long minTime = -1;
        while (it.hasNext()) {
            long actionCheck = checkAction(dOn, (Action) it.next(), u, uMap);
            if (actionCheck != -1) {
                if (minTime == -1) {
                    minTime = actionCheck;
                } else {
                    minTime = Math.min(minTime, actionCheck);
                }
            }
        }
        return minTime;
    }

    public long checkActions(TimeDependency dOn, ActionList al, User u, HashMap<String, User> uMap) {
        if (dOn.getOffset() == null || dOn.getTimeDelta() == null)
            return -1;
        long time = checkActions(((ActionDependency) dOn.getOffset()), al, u, uMap);
        if (time == -1)
            return -1;
        return checkActions(((ActionDependency) dOn.getOffset()), al, u, uMap) + dOn.getTimeDelta();
    }

    public long checkActions(AndDependency andDep, ActionList al, User u, HashMap<String, User> uMap) {
        boolean result = true;
        long maxTime = -1;
        for (Dependency dOn : andDep.getDependencies()) {
            long time = checkActions(dOn, al, u, uMap);
            result = result && (time != -1);
            if (!result)
                return -1;
            if (maxTime == -1) {
                maxTime = time;
            } else {
                maxTime = Math.max(maxTime, time);
            }
        }
        return maxTime;
    }

    public long checkActions(OrDependency orDep, ActionList al, User u, HashMap<String, User> uMap) {
        boolean result = false;
        long minTime = -1;
        for (Dependency dOn : orDep.getDependencies()) {
            long time = checkActions(dOn, al, u, uMap);
            if (time != -1) {
                result = true;
                if (minTime == -1) {
                    minTime = time;
                } else {
                    minTime = Math.min(minTime, time);
                }
            }
        }
        return minTime;
    }

    public long checkAction(ActionDependency dOn, Action a, User u, HashMap<String, User> uMap) {
        if (a == null)
            return -1;
        // UsersDelegator ud = new UsersDelegator(this);
        // User actionUser = ud.getUserByEmail(u.getRunId(), a.getUserEmail());
        Integer scope = dOn.getScope();
        if (scope == null)
            scope = Dependency.USER_SCOPE;
        switch (scope) {
            case Dependency.USER_SCOPE:
                if (!a.getUserEmail().equals(u.getFullId()))
                    return -1;
                break;
            case Dependency.TEAM_SCOPE:
                if (!uMap.get(a.getUserEmail()).getTeamId().equals(u.getTeamId()))
                    return -1;
                break;

            default:
                break;
        }
        String role = dOn.getRole();
        if (role != null && !hasRole(uMap.get(a.getUserEmail()), role))
            return -1;

        if (dOn.getAction() != null && !dOn.getAction().equals(a.getAction()))
            return -1;
        if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(a.getGeneralItemId()))
            return -1;
        if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(a.getGeneralItemType()))
            return -1;
        if (a.getTime() == null)
            return 0;
        return a.getTime();
    }
}
