angular.module('DojoIBL')

    .factory('ChannelApi', function ChannelFactory($resource, $http, config) {
        return $resource(config.server+'/rest/channelAPI', {}, {
            'getToken': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/channelAPI/token'
            }
        });
    });