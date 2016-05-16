angular.module('DojoIBL')

    .service('ResponseService', function ($q, Response, CacheFactory) {

        CacheFactory('responsesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        return {
            //getGameById: function(id) {
            //    var deferred = $q.defer();
            //    var dataCache = CacheFactory.get('gamesCache');
            //    if (dataCache.get(id)) {
            //        deferred.resolve(dataCache.get(id));
            //    } else {
            //
            //        Game.getGameById({id:id}).$promise.then(
            //            function(data){
            //                dataCache.put(id, data);
            //                deferred.resolve(data);
            //            }
            //        );
            //    }
            //    return deferred.promise;
            //},
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
            }
            //getGameFromCache: function(id) {
            //    var dataCache = CacheFactory.get('gamesCache');
            //    return dataCache.get(id);
            //},
            //storeInCache:function(game){
            //    var dataCache = CacheFactory.get('gamesCache');
            //    dataCache.put(game.gameId, game);
            //
            //},
            //
            //getGames: function(){
            //    var dataCache = CacheFactory.get('gamesCache');
            //    return dataCache.keySet();
            //}
        }
    }
);