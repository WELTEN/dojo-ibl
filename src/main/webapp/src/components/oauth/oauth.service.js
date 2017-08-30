    angular.module('DojoIBL')

    .factory('Session', function SessionFactory($q, $http, Oauth, CacheFactory,
                                                ActivityService, GameService, AccountService, ActivityStatusService, ResponseService, RunService, UserService, MessageService) {
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
                $http.defaults.headers.common['Authorization'] = value;
                return localStorage.setItem('accessToken', value)
            },
            reset: function(){

                GameService.emptyGamesCache();
                AccountService.emptyAccountsCache();
                ActivityService.emptyActivitiesCache();
                ActivityStatusService.emptyActivityStatusCache();
                ResponseService.emptyResponsesCache();
                RunService.emptyRunsCache();
                UserService.emptyUsersCache();
                MessageService.emptyMessagesCache();

                localStorage.removeItem('oauth');
                localStorage.removeItem('accessToken');
            },
            authenticate: function(){
                var service = this;

                var deferred = $q.defer();
                Oauth.authenticate().$promise.then(function(data){
                    deferred.resolve(data.toJSON().token);
                });

                return deferred.promise;
            }
        }
    }
);