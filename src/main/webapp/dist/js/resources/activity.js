angular.module('DojoIBL')

    .factory('Activity', function ActivityFactory($resource, $http, config) {
        return $resource(config.server+'/rest/generalItems', {}, {
            getActivitiesForPhase: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/generalItems/gameId/:gameId/section/:phase'
            },
            'create': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/generalItems'
            },
            'delete':{
                params: { gameId: '@gameId', itemId: '@itemId' },
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/generalItems/gameId/:gameId/generalItem/:itemId'
            }
        });
    }
);