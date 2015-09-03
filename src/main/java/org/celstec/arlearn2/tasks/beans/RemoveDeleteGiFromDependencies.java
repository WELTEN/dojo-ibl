package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.dependencies.*;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;

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
public class RemoveDeleteGiFromDependencies extends GenericBean {
    private long gameId;
    private long generalItemId;

    public RemoveDeleteGiFromDependencies() {

    }

    public RemoveDeleteGiFromDependencies(String token, long gameId, long generalItemId) {
        super(token);
        this.gameId = gameId;
        this.generalItemId = generalItemId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGeneralItemId() {
        return generalItemId;
    }

    public void setGeneralItemId(long generalItemId) {
        this.generalItemId = generalItemId;
    }

    @Override
    public void run() {
        GeneralItemDelegator gid = new GeneralItemDelegator("auth=" + getToken());
        GeneralItemList gil = gid.getGeneralItems(gameId);
        for (GeneralItem gi : gil.getGeneralItems()) {
            if (!gi.getDeleted()) {
                Dependency dep = gi.getDependsOn();
                Dependency dis = gi.getDisappearOn();
                boolean save = false;
                if (dep != null) {
                    Dependency newDep = cleanDep(dep);

                    System.out.println("dep out " + newDep);
                    if (newDep == null || !newDep.equals(dep)) {
                        gi.setDependsOn(newDep);
                        save = true;
                    }
                }
                if (save) {
                    gid.createGeneralItem(gi);
                }
            }
        }


    }

    private Dependency cleanDep(Dependency dep) {
        System.out.println("dep in " + dep);

        if (dep instanceof ActionDependency) {
            ActionDependency ad = (ActionDependency) dep;
            if (ad.getGeneralItemId() != null && ad.getGeneralItemId() == generalItemId) {
                return null;
            }
        }
        if (dep instanceof AndDependency) {
            AndDependency andDep = (AndDependency) dep;
            AndDependency newAndDep = new AndDependency();
            for (Dependency aDep : andDep.getDependencies()) {
                Dependency cleanDep = cleanDep(aDep);
                if (cleanDep != null) newAndDep.addDependecy(cleanDep);
            }
            if (newAndDep.getDependencies() == null) return null;
            if (andDep.equals(newAndDep)) {
                return andDep;
            } else {
                if (newAndDep.getDependencies().isEmpty()) return null;
                return newAndDep;
            }
        }
        if (dep instanceof OrDependency) {
            OrDependency orDependency = (OrDependency) dep;
            OrDependency orNewDep = new OrDependency();
            for (Dependency oDep : orDependency.getDependencies()) {
                Dependency cleanDep = cleanDep(oDep);
                if (cleanDep != null) orNewDep.addDependecy(cleanDep);
            }
            if (orDependency.getDependencies() == null) return null;
            if (orDependency.equals(orNewDep)) {
                return orDependency;
            } else {
                if (orNewDep.getDependencies().isEmpty()) return null;
                return orNewDep;
            }
        }
        if (dep instanceof TimeDependency) {
            TimeDependency td = (TimeDependency) dep;
            if (td.getOffset() != null) {
                Dependency cleanDep = cleanDep(td.getOffset());
                if (cleanDep == null) return null;
                if (cleanDep.equals(td.getOffset())) {
                    return td;
                } else {

                    td.setOffset(cleanDep);
                    return td;
                }
            } else {
                return null;
            }
        }

        return null;
    }
}
