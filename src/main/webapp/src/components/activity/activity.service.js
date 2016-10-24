angular.module('DojoIBL')

    .service('ActivityService', function ($q, Activity, CacheFactory) {

        CacheFactory('activitiesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        CacheFactory('activitiesStatusCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        return {
            newActivity: function(activityAsJson){
                var dataCache = CacheFactory.get('activitiesCache');

                var newActivity = new Activity(activityAsJson);

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

                                //var rol = [];
                                //angular.forEach(data.generalItems[i].roles, function(d){
                                //    rol.push(JSON.parse(d));
                                //});

                                //data.generalItems[i].roles = rol;

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
            getActivities: function (gameId) {
                var service = this;
                var dataCache = CacheFactory.get('activitiesCache');
                var deferred = $q.defer();
                Activity.getActivities({ gameId: gameId }).$promise.then(
                    function (data) {
                        var generalItems={};
                        generalItems[gameId] = generalItems[gameId] || {};
                        for (i = 0; i < data.generalItems.length; i++) {
                            if (!data.generalItems[i].deleted) {
                                dataCache.put(data.generalItems[i].id, data.generalItems[i]);
                                generalItems[gameId][data.generalItems[i].id] = data.generalItems[i];
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
            },
            changeActivityStatus: function(runId, gItemId, status){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesStatusCache');

                Activity.changeActivityStatus({ runId:runId, generalItemId: gItemId, status: status}).$promise.then(function(data){
                        dataCache.put(runId+"_"+gItemId, data);
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            getActivityStatus: function(runId, gItemId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesStatusCache');

                if (dataCache.get(runId+"_"+gItemId)) {
                    deferred.resolve(dataCache.get(runId+"_"+gItemId));
                } else {
                    Activity.getActivityStatus({ runId:runId, generalItemId: gItemId, status: status}).$promise.then(function(data){
                            dataCache.put(runId+"_"+gItemId, data);
                            deferred.resolve(data);
                        }
                    );
                }

                return deferred.promise;
            }
        }
    }
);