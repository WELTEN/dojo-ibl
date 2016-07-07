angular.module('DojoIBL')

    .factory('Message', function RunFactory($resource, $http, config) {
        return $resource(config.server+'/rest/messages/message', {}, {
            'resume': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/messages/runId/:runId/default?from=:from&resumptionToken=:resumptionToken'
            },
            'getMessageById': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/messages/messageId/:messageId'
            }
        });
    }
);