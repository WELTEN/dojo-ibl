angular.module('DojoIBL')

    .factory('Game', function GameFactory($resource, $http, config) {
        return $resource(config.server+'/rest/myGames/:id', {}, {
            access: {
                params: {id: 'gameAccess'},
                method: 'GET',
                isArray: false
            },
            'query': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames'
            },
            'resume': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames?resumptionToken=:resumptionToken'
            },
            'getGameById': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames/gameId/:id'
            }
        });
    }
);