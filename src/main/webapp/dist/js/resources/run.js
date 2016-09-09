angular.module('DojoIBL')

    .factory('Run', function RunFactory($resource, $http, config) {
        return $resource(config.server+'/rest/myRuns', {}, {
            getRun: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/runId/:id'
            },
            getRunByCode: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/code/:code'
            },
            getParticipateRunsForGame: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/participate/gameId/:id'
            },
            getOwnedRunsForGame: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/runAccess/gameId/:id'
            },
            'create': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myRuns'
            },
            'delete': {
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/myRuns/runId/:id'
            },
            giveAccess: {
                params: { runId: '@runId', accountId: '@accountId', accessRight: '@accessRight' },
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/access/runId/:runId/account/:accountId/accessRight/:accessRight'
            },
            addUserToRun: {
                method: 'POST',
                isArray: false,
                url: config.server+'/rest/users'
            }
        });
    }
);