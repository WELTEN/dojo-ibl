angular.module('DojoIBL')

    .service('ActivityService', function ($q, Activity, CacheFactory) {

        CacheFactory('activitiesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });


        return {
            newActivity: function(activityAsJson){
                var dataCache = CacheFactory.get('activitiesCache');

                ////////////////////////////////////////
                // Only put in cache when we are editing
                ////////////////////////////////////////
                //if(activityAsJson.activity)
                //    dataCache.put(activityAsJson.gameId, gameAsJson);

                console.log(activityAsJson);

                var newActivity = new Activity(activityAsJson);

                console.log(newActivity);


                return newActivity.$save();
            },
            getItemFromCache: function(id) {
                var dataCache = CacheFactory.get('activitiesCache');
                return dataCache.get(id);
            },
            getActivitiesForPhase: function (gameId, phaseId) {
                var service = this;
                var dataCache = CacheFactory.get('activitiesCache');
                var deferred = $q.defer();
                Activity.getActivitiesForPhase({ gameId: gameId, phase: phaseId }).$promise.then(
                    function (data) {
                        var generalItems={};
                        generalItems[gameId] = generalItems[gameId] || {};
                        for (i = 0; i < data.generalItems.length; i++) {
                            if (!data.generalItems[i].deleted) {
                                dataCache.put(data.generalItems[i].id, data.generalItems[i]);
                                generalItems[gameId][data.generalItems[i].id] = data.generalItems[i];
                                // returnElements.push(data.generalItems[i])
                            }
                        }
                        deferred.resolve(generalItems[gameId]);
                    }
                );

                return deferred.promise;
            },
            getActivityById: function(itemId, gameId) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesCache');
                if (dataCache.get(itemId)) {
                    deferred.resolve(dataCache.get(itemId));
                } else {

                    Activity.getActivity({ itemId:itemId, gameId: gameId}).$promise.then(
                        function(data){
                            dataCache.put(itemId, data);
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            },
            deleteActivity: function(gameId, itemId){
                var dataCache = CacheFactory.get('activitiesCache');
                dataCache.remove(itemId);
                return Activity.delete({ gameId: gameId, itemId: itemId});
            }
        }
    }
);