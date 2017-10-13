angular.module('DojoIBL')
    .factory('La', function GameFactory($resource, $http, config) {
        return $resource(config.server+'/rest/la/:id', {}, {
            'create': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/la'
            }
        });
    })
;