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
            getParticipateRuns: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/participate'
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
            'update': {
                params: { runId: '@runId' },
                method: 'PUT',
                isArray: false,
                url: config.server + '/rest/myRuns/runId/:runId'
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
            removeAccess: {
                params: { runId: '@runId', accountId: '@accountId' },
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/removeAccess/runId/:runId/account/:accountId'
            },
            addUserToRun: {
                method: 'POST',
                isArray: false,
                url: config.server+'/rest/users'
            },
            removeUserRun: {
                method: 'DELETE',
                params: { runId: '@runId', email: '@email' },
                isArray: false,
                url: config.server+'/rest/users/runId/:runId/email/:email'
            }
        });
    }
);