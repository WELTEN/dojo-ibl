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
package org.celstec.arlearn2.api;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class CacheFilterFactory implements ResourceFilterFactory {
    private static final List<ResourceFilter> NO_CACHE_FILTER = Collections.<ResourceFilter>singletonList(new CacheResponseFilter("no-cache"));

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        CacheControlHeader cch = am.getAnnotation(CacheControlHeader.class);
        if (cch == null) {
            return NO_CACHE_FILTER;
        } else {
            return Collections.<ResourceFilter>singletonList(new CacheResponseFilter(cch.value()));
        }
    }

    private static class CacheResponseFilter implements ResourceFilter, ContainerResponseFilter {
        private final String headerValue;

        CacheResponseFilter(String headerValue) {
            this.headerValue = headerValue;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return null;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return this;
        }

        @Override
        public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
            // attache Cache Control header to each response based on the annotation value
            response.getHttpHeaders().putSingle(HttpHeaders.CACHE_CONTROL, headerValue);
            return response;
        }
    }
}
