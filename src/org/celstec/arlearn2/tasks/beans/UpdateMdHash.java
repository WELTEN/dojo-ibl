package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.generalItem.*;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
public class UpdateMdHash extends GenericBean {

//    private Long generalItemId;
//    private  String md5Hash;
//    private String localId;


    private String md5Hashes;

    public UpdateMdHash() {

    }

//    public UpdateMdHash(String token, Account accountAuth, Long generalItemId, String md5Hash, String localId) {
//        super(token, accountAuth);
//        this.generalItemId = generalItemId;
//        this.md5Hash = md5Hash;
//        this.localId = localId;
//    }

    public UpdateMdHash(String token, Account accountAuth, String md5Hashes) {
        super(token, accountAuth);
         this.md5Hashes = md5Hashes;
    }

    public String getMd5Hashes() {
        return md5Hashes;
    }

    public void setMd5Hashes(String md5Hashes) {
        this.md5Hashes = md5Hashes;
    }

    @Override
    public void run() {

        try {
            JSONArray array = new JSONArray(md5Hashes);
            for (int i = 0;i < array.length(); i++) {
                JSONObject object =array.getJSONObject(i);
                Long generalItemId = object.getLong("itemId");
                String md5Hash = object.getString("md5Hash");
                String localId =object.getString("localId");
                GeneralItemDelegator gid = new GeneralItemDelegator(this);
                GeneralItem item = gid.getGeneralItem(generalItemId);
                if (item != null && !item.getDeleted()) {
                    if ("icon".equals(localId)) {
                        item.setIconUrlMd5Hash(md5Hash);
                    } else if (item.getType().equals(SingleChoiceImageTest.class.getCanonicalName())) {
                        if ("audio".equals(localId)) {
                                SingleChoiceImageTest imageTest = (SingleChoiceImageTest) item;
                                imageTest.setMd5Hash(md5Hash);
                         } else {
                        SingleChoiceImageTest imageTest = (SingleChoiceImageTest) item;
                        for (MultipleChoiceAnswerItem imageAnswerItem2: imageTest.getAnswers()) {
                            MultipleChoiceImageAnswerItem imageAnswerItem = (MultipleChoiceImageAnswerItem) imageAnswerItem2;
                            if (localId.endsWith(":i") && localId.startsWith(imageAnswerItem.getId())) {
                                imageAnswerItem.setImageMd5Hash(md5Hash);
                            }
                            if (localId.endsWith(":a") && localId.startsWith(imageAnswerItem.getId())) {
                                imageAnswerItem.setAudioMd5Hash(md5Hash);
                            }
                        }
                        }
                    } else if (item.getType().equals(MultipleChoiceImageTest.class.getCanonicalName())) {
                        if ("audio".equals(localId)) {
                                MultipleChoiceImageTest imageTest = (MultipleChoiceImageTest) item;
                                imageTest.setMd5Hash(md5Hash);
                        } else {
                        MultipleChoiceImageTest imageTest = (MultipleChoiceImageTest) item;
                        for (MultipleChoiceAnswerItem imageAnswerItem2: imageTest.getAnswers()) {
                            MultipleChoiceImageAnswerItem imageAnswerItem = (MultipleChoiceImageAnswerItem) imageAnswerItem2;
                            if (localId.endsWith(":i") && localId.startsWith(imageAnswerItem.getId())) {
                                imageAnswerItem.setImageMd5Hash(md5Hash);
                            }
                            if (localId.endsWith(":a") && localId.startsWith(imageAnswerItem.getId())) {
                                imageAnswerItem.setAudioMd5Hash(md5Hash);
                            }
                        }
                        }
                    } else  if ("audio".equals(localId)) {
                        if (item.getType().equals(SingleChoiceImageTest.class.getCanonicalName())) {
                            SingleChoiceImageTest imageTest = (SingleChoiceImageTest) item;
                            imageTest.setMd5Hash(md5Hash);
                        } else if (item.getType().equals(MultipleChoiceImageTest.class.getCanonicalName())) {
                            MultipleChoiceImageTest imageTest = (MultipleChoiceImageTest) item;
                            imageTest.setMd5Hash(md5Hash);
                        } else {
                            ((AudioObject) item).setMd5Hash(md5Hash);
                        }

                    } else if ("video".equals(localId)) {
                        ((VideoObject) item).setMd5Hash(md5Hash);
                    } else {
                        List<FileReference> fileReferences =  item.getFileReferences();
                        if (fileReferences != null && !fileReferences.isEmpty()) {
                            for (FileReference ref : fileReferences) {
                                if (ref.getKey().equals(localId)) {
                                    ref.setMd5Hash(md5Hash);
                                }
                            }
                        }
                    }
                    gid.createGeneralItem(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }
}
