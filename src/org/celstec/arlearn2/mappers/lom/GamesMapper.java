package org.celstec.arlearn2.mappers.lom;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.Mapper;
import org.celstec.arlearn2.jdo.manager.LomManager;
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
    public static XMLOutputter out;
    static {
        out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
    }

    @Override
    public void map(Entity entity) {
        log.log(Level.SEVERE, ""+entity.toString());

        String languageString = ""+ entity.getProperty("language");


        Object property = entity.getProperty("gameId");

        Document lom = new Document();
        Element lomElement = new Element("lom", lomNS);
        lom.setRootElement(lomElement);

        Element general = new Element("general", lomNS);
        lomElement.addContent(general);

        Element identifier = new Element("identifier", lomNS);
        general.addContent(identifier);

        Element catalog = new Element("catalog", lomNS);
        catalog.setText("ARLearn");
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
        lomElement.addContent(getTechnical(entity));

        String lomString =out.outputString(lom).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        long gameId = entity.getKey().getId();
        Long lastModificationDate = (Long) entity.getProperty("lastModificationDate");
        Boolean deleted = (Boolean) entity.getProperty("deleted");
        if (deleted == null) deleted = false;
        if (lastModificationDate == null) {
            lastModificationDate = System.currentTimeMillis();
        }
        LomManager.addGame(gameId, lomString, lastModificationDate, deleted);

//        emit("" + entity.getKey().getId(), lomString);
    }

    private static Element getTechnical(Entity entity) {
        Element technical = new Element("technical", lomNS);


            Element location = new Element("location", lomNS);
            location.setText("http://streetlearn.appspot.com/?"+entity.getKey().getId());
            technical.addContent(location);


        return technical;

    }


    
}
