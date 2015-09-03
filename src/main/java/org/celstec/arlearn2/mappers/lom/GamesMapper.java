package org.celstec.arlearn2.mappers.lom;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.Mapper;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.GameAccessManager;
import org.celstec.arlearn2.jdo.manager.LomManager;
import org.celstec.arlearn2.oai.Configuration;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.appengine.api.datastore.Text;


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
public class GamesMapper extends Mapper<Entity, String, String> {
    private static final Logger log = Logger.getLogger(GamesMapper.class
            .getName());

    private static Namespace oaiNS = Namespace.getNamespace("oai",
            "http://www.openarchives.org/OAI/2.0/");
    private static Namespace lomNS = Namespace.getNamespace("lom",
            "http://ltsc.ieee.org/xsd/LOM");
    private static Namespace ecoNS = Namespace.getNamespace("eco",
            "http://www.ecolearning.eu/xsd/LOM");
    public static XMLOutputter out;
    static {
        out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
    }

    @Override
    public void map(Entity entity) {
        log.log(Level.SEVERE, ""+entity.toString());


        String languageString = ""+ entity.getProperty("language");
        if (languageString == null || languageString.equals("null")) languageString = "en";



        Document lom = new Document();
        Element lomElement = new Element("lom", lomNS);
        lom.setRootElement(lomElement);

        Element general = new Element("general", lomNS);
        lomElement.addContent(general);

        Element identifier = new Element("identifier", lomNS);
        general.addContent(identifier);

        Element catalog = new Element("catalog", lomNS);
        catalog.setText("org.celstec.arlearn2");
        identifier.addContent(catalog);
        Element entry = new Element("entry", lomNS);
        entry.setText(entity.getKey().getId()+"");
        identifier.addContent(entry);


        Element title = new Element("title", lomNS);
        general.addContent(title);

        Element titleLangString = new Element("string", lomNS);
        titleLangString.setText("" + entity.getProperty("title"));
        titleLangString.setAttribute("language", languageString);
        title.addContent(titleLangString);

        Text descText = (Text) entity.getProperty("description");
        if (descText != null) {
            Element description = new Element("description", lomNS);
            general.addContent(description);
            Element descriptionLangString = new Element("string", lomNS);
            descriptionLangString.setText(descText.getValue());
            descriptionLangString.setAttribute("language", languageString);

            System.out.println(entity.getProperty("description").getClass());
            description.addContent(descriptionLangString);
        }

        Element language = new Element("language", lomNS);
        language.setText(languageString);
        general.addContent(language);

        Element nrOfUnits = new Element("nrOfUnits", ecoNS);
        nrOfUnits.setText("1");
        general.addContent(nrOfUnits);

        lomElement.addContent(getLifeCyle(entity));
        lomElement.addContent(getTechnical(entity));
        lomElement.addContent(getEducational(entity));
        lomElement.addContent(getClassification(entity));


        String lomString =out.outputString(lom).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        long gameId = entity.getKey().getId();
        Long lastModificationDate = (Long) entity.getProperty("lastModificationDate");
        Boolean deleted = (Boolean) entity.getProperty("deleted");
        if (deleted == null) deleted = false;
        Long sharing = (Long) entity.getProperty("sharing");
        if (sharing == null || sharing.intValue() != 3) {
            deleted = true;
        }
        if (descText == null || descText.toString().trim() == null) {
            deleted = true;
        }
        if (lastModificationDate == null) {
            lastModificationDate = System.currentTimeMillis();
        }
        LomManager.addGame(gameId, lomString, lastModificationDate, deleted);

//        emit("" + entity.getKey().getId(), lomString);
    }

    private static Element getTechnical(Entity entity) {
        Element technical = new Element("technical", lomNS);


            Element location = new Element("location", lomNS);
            location.setText(Configuration.getBaseUrl()+"/resolve/"+entity.getKey().getId());
            technical.addContent(location);


        return technical;

    }

    private static Element getEducational(Entity entity) {
        Element educational = new Element("educational", lomNS);


        Element typicalLearningTime = new Element("typicalLearningTime", lomNS);
        Element duration = new Element("duration", lomNS);
        duration.setText("PT30M");
        typicalLearningTime.addContent(duration);
        educational.addContent(typicalLearningTime);


        return educational;

    }

    private static Element getLifeCyle(Entity entity) {
        Element lifeCycle = new Element("lifeCycle", lomNS);

        for (GameAccess access: GameAccessManager.getGameList(entity.getKey().getId())){
            if (access.getAccessRights().equals(1) || access.getAccessRights().equals(2)) {
                Element contribute = new Element("contribute", lomNS);
                Element role = new Element("role", lomNS);

                Element source = new Element("source", lomNS);
                source.setText("LOMv1.0");
                Element value = new Element("value", lomNS);
                value.setText("author");
                role.addContent(source);
                role.addContent(value);

                Element entityElement = new Element("entity", lomNS);
                Account account = AccountManager.getAccount(access.getAccount());
                CDATA cdata = new CDATA("BEGIN:VCARD\r\n" +
                        "FN:" + account.getName() + "\r\n" +
                        "N:" + account.getFamilyName() + ";" + account.getGivenName() + "\r\n" +
                        "UID:urn:uuid:" + access.getAccount() + "\r\n" +
                        "VERSION:3.0\r\n" +
                        "END:VCARD");
                entityElement.addContent(cdata);

                contribute.addContent(role);
                contribute.addContent(entityElement);
                lifeCycle.addContent(contribute);
            }
        }
        Element contribute = new Element("contribute", lomNS);
        Element role = new Element("role", lomNS);

        Element source = new Element("source", lomNS);
        source.setText("LOMv1.0");
        Element value = new Element("value", lomNS);
        value.setText("content provider");
        role.addContent(source);
        role.addContent(value);

        Element entityElement = new Element("entity", lomNS);
//        Account account = AccountManager.getAccount(access.getAccount());
        CDATA cdata = new CDATA("BEGIN:VCARD\r\n" +
                "ORG: Open Universiteit Nederland"  + "\r\n" +
                "VERSION:3.0\r\n" +
                "END:VCARD");
        entityElement.addContent(cdata);

        contribute.addContent(role);
        contribute.addContent(entityElement);
        lifeCycle.addContent(contribute);

        return lifeCycle;

    }
//    <lom:classification>

//    <lom:taxonPath>
//    <lom:source>
//    <lom:string language="en">ECO Area of Interests</lom:string>
//    </lom:source>
//    <lom:taxon>
//    <lom:id>ECO:ES</lom:id>
//    <lom:entry>
//    <lom:string>Educational Science</lom:string>
//    </lom:entry>
//    </lom:taxon>
//    </lom:taxonPath>
//    </lom:classification>

    private static Element getClassification(Entity entity) {
        Element classification = new Element("classification", lomNS);
        Element purpose = new Element("purpose", lomNS);
        Element source = new Element("source", lomNS);
        source.setText("LOMv1.0");
        Element value = new Element("value", lomNS);
        value.setText("discipline");

        purpose.addContent(source);
        purpose.addContent(value);

        Element taxonPath = new Element("taxonPath", lomNS);
        Element sourceTaxon = new Element("source", lomNS);
        Element sourceLangstring = new Element("string", lomNS);
        sourceLangstring.setAttribute("language", "en");
        sourceLangstring.setText("ECO Area of Interests");

        Element taxon = new Element("taxon", lomNS);
        Element id = new Element("id", lomNS);
        id.setText("ECO:ES");
        Element entry = new Element("entry", lomNS);
        Element taxonstring = new Element("string", lomNS);
        taxonstring.setText("Educational Science");

        taxon.addContent(id);
        taxon.addContent(entry);
        entry.addContent(taxonstring);
        taxonPath.addContent(sourceTaxon);
        taxonPath.addContent(taxon);
        sourceTaxon.addContent(sourceLangstring);

        classification.addContent(purpose);
        classification.addContent(taxonPath);


        return classification;

    }
}
