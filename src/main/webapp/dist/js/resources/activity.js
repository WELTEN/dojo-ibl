angular.module('DojoIBL')

    .factory('Activity', function ActivityFactory($resource, $http, config) {
        return $resource(config.server+'/rest/generalItems', {}, {
            //getActivity: {
            //    method: 'GET',
            //    isArray: false,
            //    url: config.server+'/rest/myRuns/runId/:id'
            //
            //},
            getActivitiesForPhase: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/generalItems/gameId/:gameId/section/:phase'
            }
        });
    }
);