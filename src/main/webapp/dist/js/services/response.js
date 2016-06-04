angular.module('DojoIBL')

    .service('ResponseService', function ($q, Response, CacheFactory) {

        CacheFactory('responsesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        var responses = {};

        var resumptionToken;
        var serverTime= 0;
        var serverTimeFirstInvocation;

        return {
            newResponse: function(responseAsJson){
                var dataCache = CacheFactory.get('responsesCache');

                ////////////////////////////////////////
                // Only put in cache when we are editing
                ////////////////////////////////////////
                if(responseAsJson.responseId)
                    dataCache.put(responseAsJson.responseId, responseAsJson);

                var newResponse = new Response(responseAsJson);

                return newResponse.$save();
            },
            deleteResponse: function(responseId){
                var dataCache = CacheFactory.get('responsesCache');
                dataCache.remove(responseId);
                return Response.deleteResponse({ responseId: responseId });
            },
            getResponsesByInquiryActivity: function(runId, itemId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('responsesCache');

                Response.getResponsesInquiryActivity({ runId:runId, itemId:itemId, resumptionToken: resumptionToken, from:serverTime }).$promise.then(function (data) {
                    if (data.error) {
                        deferred.resolve(data);
                    } else {

                        for (var i = 0; i < data.responses.length; i++) {
                            if (data.responses[i].revoked || data.responses[i].deleted) {
                                delete responses[data.responses[i].responseId];
                            } else {

                                dataCache.put(data.responses[i].responseId, data.responses[i]);
                                responses[data.responses[i].responseId] = data.responses[i];
                            }
                        }

                        //dataCache.put("responses"+itemId, data);

                        resumptionToken = data.resumptionToken;
                        serverTimeFirstInvocation = serverTimeFirstInvocation || data.serverTime;
                        if (!data.resumptionToken){
                            serverTime = serverTimeFirstInvocation;
                            serverTimeFirstInvocation = undefined;
                        }

                        deferred.resolve(data);
                    }
                });
                return deferred.promise;
            },
            getResponsesFromCache: function(itemId){
                var dataCache = CacheFactory.get('responsesCache');
                return dataCache.get("responses"+itemId);
            }
        }
    }
);