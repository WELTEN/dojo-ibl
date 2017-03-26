angular.module('DojoIBL')
    .factory('InquiryTemplate', function($http) {
        return $http.get('/src/assets/resources/catalogue.json');
    })
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
                url: config.server + '/rest/myGames?resumptionToken=:resumptionToken&from=:from'
            },
            'getGameById': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames/gameId/:id'
            },
            'create': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames'
            },
            'addRole': {
                params: { gameId: '@gameId' },
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames/config/gameId/:gameId/role'
            },
            'removeRole': {
                params: { gameId: '@gameId' },
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames/config/gameId/:gameId/role/remove'
            },
            'editRole': {
                params: { gameId: '@gameId', index: '@index' },
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames/config/gameId/:gameId/role/:index/edit'
            },
            giveAccess: {
                params: { gameId: '@gameId', accountId: '@accountId', accessRight: '@accessRight' },
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames/access/gameId/:gameId/account/:accountId/accessRight/:accessRight'
            },
            getGameAccesses: {
                params: { gameId: '@gameId'},
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames/access/gameId/:gameId'
            },
            deleteGame: {
                params: { gameId: '@gameId' },
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/myGames/gameId/:gameId'
            },
            'gameAssets': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/myGames/gameContent/gameId/:gameId'
            }
        });
    })
;