angular.module('DojoIBL')

    .service('ResponseService', function ($q, Response, CacheFactory, UserService, $filter) {

        CacheFactory('responsesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        CacheFactory('responsesTempCache', {
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
            responses[response.runId+"_"+response.generalItemId] = responses[response.runId+"_"+response.generalItemId] || {};
            responses[response.runId+"_"+response.generalItemId][response.responseId] = response;

            //console.log("Num resul antes:",response.generalItemId,responses[response.runId+"_"+response.generalItemId]);

        }

        var resumptionToken;
        var serverTime= 0;
        var serverTimeFirstInvocation;

        return {
            newResponse: function(responseAsJson){
                var newResponse = new Response(responseAsJson);
                return newResponse.$save();
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
            getResponsesClosureById: function(id, runId, itemId){
                return responses[runId+"_"+itemId][id];
            },
            addResponse: function(response, runId, itemId){
                var dataCache = CacheFactory.get('responsesCache');
                if(angular.isUndefined(responses[runId+"_"+itemId])){
                    responses[runId+"_"+itemId] = {};
                }
                responses[runId+"_"+itemId][response.responseId] = response;
                responses[runId+"_"+itemId][response.responseId].user = UserService.getUser(runId, response.userEmail);
                dataCache.put(response.responseId, responses[runId+"_"+itemId][response.responseId]);
            },
            refreshResponse: function(response, runId, itemId) {
                var dataCache = CacheFactory.get('responsesCache');
                if (dataCache.get(response.responseId)) {
                    delete responses[runId+"_"+itemId][response.responseId];
                    dataCache.remove(response.responseId);
                }
                return this.getResponseById(response.responseId, runId, itemId);
            },
            getResponseById: function (id, runId, itemId) {
                var deferred = $q.defer();
                var service = this;

                var dataCache = CacheFactory.get('responsesCache');
                if (dataCache.get(id)) {
                    deferred.resolve(dataCache.get(id));
                } else {
                    Response.getResponse({id: id}).$promise.then(
                        function (data) {
                            if (!data.error){
                                if (data.deleted) {
                                    delete responses[runId+"_"+itemId][id];
                                    dataCache.remove(id);

                                } else {
                                    if(angular.isUndefined(responses[runId+"_"+itemId])){
                                        responses[runId+"_"+itemId] = {};
                                    }
                                    if(angular.isUndefined(responses[runId+"_"+itemId][id])){
                                        responses[runId+"_"+itemId][id] = {};
                                    }
                                    responses[runId+"_"+itemId][id] = data;
                                    responses[runId+"_"+itemId][id].user = UserService.getUser(runId, data.userEmail);
                                    dataCache.put(id, responses[runId+"_"+itemId][id]);
                                }
                            }

                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            },
            getResponses: function (runId, itemId) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('responsesCache');

                responses[runId+"_"+itemId] = responses[runId+"_"+itemId] || {};
                if (!serverTime) {
                    serverTime = 0;
                }

                var service = this;

                if(resumptionToken){
                    Response.getResponsesInquiryActivity({ runId:runId, itemId:itemId, resumptionToken: resumptionToken, from: serverTime }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (i = 0; i < data.responses.length; i++) {
                                if (!data.responses[i].deleted) {
                                    dataCache.put(data.responses[i].responseId, data.responses[i]);
                                    responses[runId+"_"+itemId][data.responses[i].responseId] = data.responses[i];
                                    responses[runId+"_"+itemId][data.responses[i].responseId].user = UserService.getUser(runId, data.responses[i].userEmail);
                                }else{
                                    delete [runId+"_"+itemId][data.responses[i].responseId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getResponses(runId, itemId, serverTime, data.resumptionToken);
                            }
                        }
                    });
                }else{
                    Response.getResponsesInquiryActivity({ runId:runId, itemId:itemId, serverTime: 0 }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (i = 0; i < data.responses.length; i++) {
                                if (!data.responses[i].deleted) {
                                    dataCache.put(data.responses[i].responseId, data.responses[i]);
                                    responses[runId+"_"+itemId][data.responses[i].responseId] = data.responses[i];
                                    //console.log(data.responses[i]);
                                    responses[runId+"_"+itemId][data.responses[i].responseId].user = UserService.getUser(runId, data.responses[i].userEmail);
                                }else{
                                    delete [runId+"_"+itemId][data.responses[i].responseId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getResponses(runId, itemId, serverTime , data.resumptionToken);
                            }
                        }
                    });
                }

                return responses[runId+"_"+itemId];
            },
            uploadUrl: function(runId, account, key) {
                return Response.uploadUrl({ runId:runId, account:account, key:key });
            },
            resumeLoadingResponses: function(runId, itemId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('responsesCache');

                responses[runId+"_"+itemId] = responses[runId+"_"+itemId] || {};
                //console.log("Num resul antes:", responses[runId+"_"+itemId])

                if(dataCache.get(runId+"_"+itemId)){
                    serverTime = dataCache.get(runId+"_"+itemId);
                    //console.log("Si habia comprobado antes y hay valor en Cache: ",$filter('date')(serverTime, "medium"));

                }else{
                    serverTime = 0;
                    //console.log("No se habia comprobado antes: ",$filter('date')(serverTime, "medium"));
                }

                Response.getResponsesInquiryActivity({runId:runId, itemId:itemId, resumptionToken: resumptionToken, from:0})
                    .$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);

                        } else {
                            for (i = 0; i < data.responses.length; i++) {
                                if (!data.responses[i].deleted) {
                                    if(angular.isUndefined(responses[runId+"_"+itemId])){
                                        responses[runId+"_"+itemId] = {};
                                    }
                                    responses[runId+"_"+itemId][data.responses[i].responseId] = data.responses[i];

                                    //if(data.responses[i].parentId){
                                    //    console.log("Es hijo de: ",data.responses[i].parentId)
                                    //}

                                    responses[runId+"_"+itemId][data.responses[i].responseId].user = UserService.getUser(runId, data.responses[i].userEmail);
                                    dataCache.put(data.responses[i].responseId, responses[runId+"_"+itemId][data.responses[i].responseId]);
                                }else{
                                    delete [runId+"_"+itemId][data.responses[i].responseId];
                                }
                            }

                            resumptionToken = data.resumptionToken;
                            serverTimeFirstInvocation = serverTimeFirstInvocation || data.serverTime;
                            dataCache.put(runId+"_"+itemId, serverTimeFirstInvocation);

                            //console.log("Comprobado server hasta "+$filter('date')(serverTime, "medium")+" hora para Run:"+runId+" y Act:"+itemId);
                            //console.log("Num resul despues:",responses[runId+"_"+itemId]);


                            if (!data.resumptionToken){
                                serverTime = serverTimeFirstInvocation;
                                serverTimeFirstInvocation = undefined;
                            }

                            deferred.resolve(data);
                        }
                    });
                return deferred.promise;
            },
            saveResponseInCache: function(responseAsJson, activityId){
                var dataCache = CacheFactory.get('responsesTempCache');
                dataCache.put("cachedResponse"+activityId, responseAsJson);
            },
            getResponseInCached: function(activityId){
                var dataCache = CacheFactory.get('responsesTempCache');
                return dataCache.get("cachedResponse"+activityId);
            },
            removeCachedResponse: function(activityId){
                var dataCache = CacheFactory.get('responsesTempCache');
                dataCache.remove("cachedResponse"+activityId);
            },
            emptyResponsesCache: function(){
                var dataCache = CacheFactory.get('responsesCache');
                if(dataCache) dataCache.removeAll();
                responses = {};
            }
        }
    }
);