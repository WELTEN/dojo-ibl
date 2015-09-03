package org.celstec.arlearn2.delegators;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;

import java.util.List;
import java.util.logging.Logger;

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
public class MigrationDelegator extends GoogleDelegator {

    private static final Logger logger = Logger.getLogger(GameDelegator.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public MigrationDelegator(String authToken) {
        super(authToken);
    }

    public MigrationDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public MigrationDelegator(Service service) {
        super(service);
    }

    public MigrationDelegator() {
        super();
    }

    public MigrationDelegator(Account account, String token) {
        super(account, token);
    }

    public String migrateGame(long gameId){
        List<GeneralItem> list =  GeneralItemManager.getGeneralitemsFromUntil(gameId, null, null);
        for (GeneralItem item : list) {
            migrateItem(item);
        }
        return "{'done'=1}";
    }

    private void migrateItem(GeneralItem item){
        if (item instanceof AudioObject) {
            migrateAudioObject((AudioObject)item);
        }
    }

    private void migrateAudioObject(AudioObject item) {
        if (!isCloudBase(item.getAudioUrl())){
//            blobstoreService.
        }

    }

    private static boolean isCloudBase(String url) {
        if (url.startsWith("http://streetlearn.appspot.com/")) return true;
        return false;
    }
}
