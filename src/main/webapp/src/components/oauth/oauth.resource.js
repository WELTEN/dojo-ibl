angular.module('DojoIBL')

    .factory('Oauth', function GameFactory($resource, $http, config) {
        return $resource(config.server+'/rest/oauth/', {}, {
            'info': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/oauth/getOauthInfo/'
            },
            'authenticate': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/oauth/authenticate'
            }
        });
    }

);
