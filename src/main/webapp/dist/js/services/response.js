angular.module('DojoIBL')

    .service('ResponseService', function ($q, Response, CacheFactory) {

        CacheFactory('responsesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

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
            getResponsesByInquiry: function (runId) {
                var service = this;
                var dataCache = CacheFactory.get('responsesCache');
                var deferred = $q.defer();
                Response.getResponsesForInquiry({ runId: runId }).$promise.then(
                    function (data) {
                        console.log(data);
                        //var generalItems={};
                        //generalItems[gameId] = generalItems[gameId] || {};
                        //for (i = 0; i < data.generalItems.length; i++) {
                        //    if (!data.generalItems[i].deleted) {
                        //        dataCache.put(data.generalItems[i].id, data.generalItems[i]);
                        //        generalItems[gameId][data.generalItems[i].id] = data.generalItems[i];
                        //        // returnElements.push(data.generalItems[i])
                        //    }
                        //}
                        deferred.resolve(generalItems[gameId]);
                    }
                );

                return deferred.promise;
            },
            deleteResponse: function(responseId){
                var dataCache = CacheFactory.get('responsesCache');
                dataCache.remove(responseId);
                return Response.deleteResponse({ responseId: responseId });
            }
        }
    }
);