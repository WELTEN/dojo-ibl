angular.module('DojoIBL')

    .service('ResponseService', function ($q, Response, CacheFactory) {

        CacheFactory('responsesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        var responses = {};
        var dataCache = CacheFactory.get('responsesCache');
        var responsesId = dataCache.keys();
        for (var i=0; i < responsesId.length; i++) {
            var response = dataCache.get(responsesId[i]);
            responses[response.runId+"_"+response.generalItemId] = responses[response.runId] || {};
            responses[response.runId+"_"+response.generalItemId][response.responseId] = response;
        }

        //console.log(responses);

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

                return newResponse.$save(function(data){
                    responses[data.runId+"_"+data.generalItemId][data.responseId] = data;
                });
            },
            deleteResponse: function(responseId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('responsesCache');
                dataCache.remove(responseId);

                Response.deleteResponse({ responseId: responseId }).$promise.then(
                    function (data) {
                        deferred.resolve(data);
                        delete responses[data.runId+"_"+data.generalItemId][data.responseId]
                    }
                );

                return deferred.promise;
            },
            getResponsesByInquiryActivity: function(runId, itemId){
                return responses[runId+"_"+itemId];
            },
            addResponse: function(response, runId, itemId){
                var dataCache = CacheFactory.get('responsesCache');
                dataCache.put(response.responseId, response);
                responses[runId+"_"+itemId][response.responseId] = response;
            },
            getResponses: function (runId, itemId, from, resumptionToken) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('responsesCache');

                responses[runId+"_"+itemId] = responses[runId+"_"+itemId] || {};
                if (!from) {
                    from = 0;
                }

                var service = this;

                if(resumptionToken){
                    Response.getResponsesInquiryActivity({ runId:runId, itemId:itemId, resumptionToken: resumptionToken, from: from }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (i = 0; i < data.responses.length; i++) {
                                if (!data.responses[i].deleted) {
                                    dataCache.put(data.responses[i].responseId, data.responses[i]);
                                    responses[runId+"_"+itemId][data.responses[i].responseId] = data.responses[i];
                                }else{
                                    delete [runId+"_"+itemId][data.responses[i].responseId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getResponses(runId, itemId, from, data.resumptionToken);
                            }
                        }
                    });
                }else{
                    Response.getResponsesInquiryActivity({ runId:runId, itemId:itemId, from: 0 }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (i = 0; i < data.responses.length; i++) {
                                if (!data.responses[i].deleted) {
                                    dataCache.put(data.responses[i].responseId, data.responses[i]);
                                    responses[runId+"_"+itemId][data.responses[i].responseId] = data.responses[i];
                                }else{
                                    delete [runId+"_"+itemId][data.responses[i].responseId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getResponses(runId, itemId, from, data.resumptionToken);
                            }
                        }
                    });
                }

                return responses[runId+"_"+itemId];
            },
            uploadUrl: function(runId, account, key) {
                return Response.uploadUrl({ runId:runId, account:account, key:key });
            }
        }
    }
);