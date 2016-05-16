angular.module('DojoIBL')

    .factory('Run', function RunFactory($resource, $http, config) {
        return $resource(config.server+'/rest/myRuns', {}, {
            getRun: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/runId/:id'

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
            }
        });
    }
);