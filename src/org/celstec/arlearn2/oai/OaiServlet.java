package org.celstec.arlearn2.oai;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

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
public class OaiServlet extends HttpServlet {

    public static XMLOutputter out;
    static {
        out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/xml;  charset=UTF-8");
        OaiParameters op = new OaiParameters(req);
        String verb = req.getParameter("verb");
        Document feed = null;
        if (op.isIdentify()) {
            resp.getWriter().write(Identify.getXmlAsString(op));
        }
        else if (op.isListRecords()) {
            resp.getWriter().write(ListRecords.getXmlAsString(op));
        }
//        else if (op.isListMetadataFormats()) {
//            resp.getWriter().write(ListMetadataFormats.getXmlAsString(op));
//        } else if (op.isListIdentifiers()) {
//            resp.getWriter().write(ListIdentifiers.getXmlAsString(op));
//        } else if (op.isGetRecord()) {
//            resp.getWriter().write(GetRecord.getXmlAsString(op));
//
//        }

        if (feed != null) {
            resp.getWriter().write(out.outputString(feed));
        }
    }
}
