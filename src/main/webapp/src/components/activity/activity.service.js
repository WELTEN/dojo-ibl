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

        var generalItems = {};
        var dataCache = CacheFactory.get('activitiesCache');
        var generalItemsId = dataCache.keys();
        for (var i=0; i < generalItemsId.length; i++) {
            var generalItem = dataCache.get(generalItemsId[i]);
            generalItems[generalItem.gameId] = generalItems[generalItem.gameId] || {};
            generalItems[generalItem.gameId][generalItem.section] = generalItems[generalItem.gameId][generalItem.section] || {};
            generalItems[generalItem.gameId][generalItem.section][generalItem.id] = generalItem;
        }

        return {
            getActivitiesServer: function(gameId){
                var dataCache = CacheFactory.get('activitiesCache');
                var deferred = $q.defer();
                var service = this;
                Activity.getActivities({ gameId: gameId }).$promise.then(
                    function (data) {
                        for (i = 0; i < data.generalItems.length; i++) {

                            if (!data.generalItems[i].deleted) {

                                dataCache.put(data.generalItems[i].id, data.generalItems[i]);

                                if(angular.isUndefined(generalItems[gameId])){
                                    generalItems[gameId] = {};
                                }

                                if(angular.isUndefined(generalItems[gameId][data.generalItems[i].section])){
                                    generalItems[gameId][data.generalItems[i].section] = {};
                                }

                                generalItems[gameId][data.generalItems[i].section][data.generalItems[i].id] = data.generalItems[i];
                            }
                        }
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            getActivitiesServerStatus: function(gameId, runId){
                var dataCache = CacheFactory.get('activitiesCache');
                var deferred = $q.defer();
                var service = this;
                Activity.getActivities({ gameId: gameId }).$promise.then(
                    function (data) {
                        for (i = 0; i < data.generalItems.length; i++) {

                            if (!data.generalItems[i].deleted) {

                                if(angular.isUndefined(generalItems[gameId])){
                                    generalItems[gameId] = {};
                                }

                                if(angular.isUndefined(generalItems[gameId][data.generalItems[i].section])){
                                    generalItems[gameId][data.generalItems[i].section] = {};
                                }

                                generalItems[gameId][data.generalItems[i].section][data.generalItems[i].id] = data.generalItems[i];
                                generalItems[gameId][data.generalItems[i].section][data.generalItems[i].id].status = 0;

                                service.getActivityStatus(runId, data.generalItems[i].id).then(function(status){
                                    //console.log(data, i)
                                    //generalItems[gameId][data.generalItems[i].section][data.generalItems[i].id].status = status.status;
                                });
                                //
                                //console.log(generalItems);

                                dataCache.put(data.generalItems[i].id, generalItems[gameId][data.generalItems[i].section][data.generalItems[i].id]);

                            }
                        }
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            getActivities: function () {
                return generalItems;
            },
            deleteActivity: function(gameId, item){
                var dataCache = CacheFactory.get('activitiesCache');
                dataCache.remove(item.id);
                delete generalItems[gameId][item.section][item.id];
                return Activity.delete({ gameId: gameId, itemId: item.id});
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
            },
            addRole: function(generalItemId, roleAsJson, roleAsJsonOld){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesCache');

                Activity.addRole({ generalItemId: generalItemId }, roleAsJson, roleAsJsonOld).$promise.then(function(data){
                        dataCache.put(generalItemId, data);
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },

            newActivity: function(activityAsJson){
                var dataCache = CacheFactory.get('activitiesCache');

                var newActivity = new Activity(activityAsJson);
                return newActivity.$save();
            },
            getItemFromCache: function(id) {
                var dataCache = CacheFactory.get('activitiesCache');
                return dataCache.get(id);
            },
            //getActivitiesForPhase: function (gameId, phaseId) {
            //    var service = this;
            //    var dataCache = CacheFactory.get('activitiesCache');
            //    var deferred = $q.defer();
            //    Activity.getActivitiesForPhase({ gameId: gameId, phase: phaseId }).$promise.then(
            //        function (data) {
            //            //var generalItems={};
            //            //generalItems[gameId] = generalItems[gameId] || {};
            //            for (i = 0; i < data.generalItems.length; i++) {
            //                if (!data.generalItems[i].deleted) {
            //
            //                    dataCache.put(data.generalItems[i].id, data.generalItems[i]);
            //                    generalItems[gameId][data.generalItems[i].id] = data.generalItems[i];
            //                    // returnElements.push(data.generalItems[i])
            //                }
            //            }
            //            deferred.resolve(generalItems[gameId]);
            //        }
            //    );
            //
            //    return deferred.promise;
            //},
            //getActivities: function (gameId) {
            //    var dataCache = CacheFactory.get('activitiesCache');
            //    var deferred = $q.defer();
            //    Activity.getActivities({ gameId: gameId }).$promise.then(
            //        function (data) {
            //            for (i = 0; i < data.generalItems.length; i++) {
            //                if (!data.generalItems[i].deleted) {
            //                    dataCache.put(data.generalItems[i].id, data.generalItems[i]);
            //                    generalItems[gameId][data.generalItems[i].id] = data.generalItems[i];
            //                }
            //            }
            //            deferred.resolve(generalItems[gameId]);
            //        }
            //    );
            //    return deferred.promise;
            //},

            getActivityById: function(itemId, gameId) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesCache');
                if (dataCache.get(itemId)) {
                    deferred.resolve(dataCache.get(itemId));
                } else {

                    Activity.getActivity({ itemId:itemId, gameId: gameId}).$promise.then(
                        function(data){

                            if (!data.error){
                                if (data.deleted) {
                                    delete generalItems[gameId][data.section][itemId];
                                    dataCache.remove(itemId);

                                } else {
                                    if(angular.isUndefined(generalItems[gameId])){
                                        generalItems[gameId] = {};
                                    }
                                    if(angular.isUndefined(generalItems[gameId][data.section])){
                                        generalItems[gameId][data.section] = {};
                                    }
                                    generalItems[gameId][data.section][itemId] = data;
                                    dataCache.put(itemId, data);
                                    deferred.resolve(data);
                                }
                            }
                        }
                    );
                }
                return deferred.promise;
            },
            refreshActivity: function(id, gameId) {
                var dataCache = CacheFactory.get('activitiesCache');
                var act = dataCache.get(id);
                if (act) {
                    delete generalItems[gameId][act.section][id];
                    dataCache.remove(id);
                }
                return this.getActivityById(id, gameId);
            }
        }
    }
);