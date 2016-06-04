angular.module('DojoIBL')

    .factory('User', function RunFactory($resource, $http, config) {
        return $resource(config.server+'/rest/users', {}, {
            getUsersRun: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/users/runId/:id'
            },
            getUserByAccount: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/users/runId/:runId/account/:accountId'
            }
        });
    }
);