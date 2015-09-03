package org.celstec.arlearn2.facebook;

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

    //import org.apache.commons.codec.binary.Base64;
public class FacebookSignedRequest {

    private String algorithm;
    private Long expires;
    private Long issued_at;
    private String oauth_token;
    private Long user_id;
    private FacebookSignedRequestUser user;

    public static FacebookSignedRequest getFacebookSignedRequest(String signedRequest)
            throws Exception {

//        String payload = signedRequest.split("[.]", 2)[1];
//        payload = payload.replace("-", "+").replace("_", "/").trim();
//        String jsonString = new String(Base64.decodeBase64(payload.getBytes()));
//        return new ObjectMapper().readValue(jsonString, FacebookSignedRequest.class);
                                                    return null;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public Long getIssued_at() {
        return issued_at;
    }

    public void setIssued_at(Long issued_at) {
        this.issued_at = issued_at;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public FacebookSignedRequestUser getUser() {
        return user;
    }

    public void setUser(FacebookSignedRequestUser user) {
        this.user = user;
    }

    public static class FacebookSignedRequestUser {

        private String country;
        private String locale;
        private FacebookSignedRequestUserAge age;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public FacebookSignedRequestUserAge getAge() {
            return age;
        }

        public void setAge(FacebookSignedRequestUserAge age) {
            this.age = age;
        }

        public static class FacebookSignedRequestUserAge{
            private int min;
            private int max;

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }
        }
    }
}

