package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * ****************************************************************************
 * Copyright (C) 2017 Open Universiteit Nederland
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
 * Contributors: Angel Suarez
 * Date: 09/10/17
 * ****************************************************************************
 */

public class DojoAnalyticsDelegator extends GoogleDelegator {
    protected static final Logger logger = Logger.getLogger(DojoAnalyticsDelegator.class.getName());

    private static DojoAnalyticsDelegator dojoAnalyticsDelegator;


    public DojoAnalyticsDelegator(String authToken) {
        super(authToken);
    }

    public DojoAnalyticsDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public DojoAnalyticsDelegator(Service service) {
        super(service);
    }

    public DojoAnalyticsDelegator(Account account, String token) {
        super(account, token);
    }
    public DojoAnalyticsDelegator() {
        super();
    }

    private static final Class[] dbTypes = new Class[]{
            StefLaDelegator.class,
    };

    public void registerStatement(String jsonStatement) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        for (Class c : dbTypes) {
            Class<?> clazz = Class.forName(c.getCanonicalName());
            Object lrs = clazz.newInstance();
            Class[] cArg = new Class[1];
            cArg[0] = String.class;
            Method registerStatement = clazz.getDeclaredMethod("submitStatement",cArg);
            registerStatement.setAccessible(true);
            registerStatement.invoke(lrs, jsonStatement);
        }
    }
}