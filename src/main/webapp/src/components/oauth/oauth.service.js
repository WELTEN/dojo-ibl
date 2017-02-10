    angular.module('DojoIBL')

    .factory('Session', function SessionFactory($http, CacheFactory) {
        function getCookie(name) {
            var value = "; " + document.cookie;
            var parts = value.split("; " + name + "=");
            if (parts.length == 2) return parts.pop().split(";").shift();
        } //temp function

        return {
            getOauthType : function(){
                if (getCookie('arlearn.OauthType')) {
                    this.setOauthType(getCookie('arlearn.OauthType'));
                }
                return localStorage.getItem('oauth')
            },
            setOauthType : function(value){
                return localStorage.setItem('oauth', value)
            },
            getAccessToken: function() {
                var service = this;
                if (getCookie('arlearn.AccessToken')) {
                    service.setAccessToken(getCookie('arlearn.AccessToken'));
                }
                return localStorage.getItem('accessToken')
            },
            setAccessToken: function(value) {
                $http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+value;
                return localStorage.setItem('accessToken', value)
            },
            reset: function(){
                var accounts = CacheFactory.get('accountCache');
                var activities = CacheFactory.get('activitiesCache');
                var activitiesStatus = CacheFactory.get('activitiesStatusCache');
                var games = CacheFactory.get('gamesCache');
                var responses = CacheFactory.get('responsesCache');
                var runs = CacheFactory.get('runsCache');
                var users = CacheFactory.get('usersCache');
                var messages = CacheFactory.get('messagesCache');

                if(accounts) accounts.removeAll();
                if(activities) activities.removeAll();
                if(activitiesStatus) activitiesStatus.removeAll();
                if(games) games.removeAll();
                if(responses) responses.removeAll();
                if(runs) runs.removeAll();
                if(users) users.removeAll();
                if(messages) messages.removeAll();
                localStorage.removeItem('oauth');
                localStorage.removeItem('accessToken');
            }
        }
    }
);