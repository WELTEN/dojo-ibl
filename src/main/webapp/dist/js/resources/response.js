angular.module('DojoIBL')

    .factory('Response', function ResponseFactory($resource, $http, config) {
        return $resource(config.server+'/rest/response', {}, {
            getResponsesForActivity: {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/response/runId/:runId/itemId/:itemId?from=:from&resumptionToken=:resumptionToken&orderByLastModificationDate=true'
            },
            'getResponsesInquiryActivity': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/response/runId/:runId/itemId/:itemId?resumptionToken=:resumptionToken&from=:from&orderByLastModificationDate=true'
            },
            getResponsesForInquiry: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/response/runId/:runId'
            },
            'resume': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/response/runId/:runId?from=:from&resumptionToken=:resumptionToken&orderByLastModificationDate=true'
            },
            deleteResponse: {
                params: { responseId: '@responseId' },
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/response/responseId/:responseId'
            }
        });
    }
);