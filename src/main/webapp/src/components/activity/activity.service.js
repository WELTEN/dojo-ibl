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

        var generalItemsStatus = {};
        var statusCache = CacheFactory.get('activitiesStatusCache');
        var generalItemsStatusId = statusCache.keys();
        for (var i=0; i < generalItemsStatusId.length; i++) {

            var giStatus = statusCache.get(generalItemsStatusId[i]);

            generalItemsStatus[giStatus.runId] = generalItemsStatus[giStatus.runId] || {};
            generalItemsStatus[giStatus.runId][giStatus.section] = generalItemsStatus[giStatus.runId][giStatus.section] || {};
            //generalItemsStatus[generalItemStatus.runId][generalItemStatus.section][generalItemStatus.status] = generalItemsStatus[generalItemStatus.gameId][generalItemStatus.section] || {};
            generalItemsStatus[giStatus.runId][giStatus.section][giStatus.id] = giStatus;
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
            getActivities: function () {
                return generalItems;
            },
            getActivitiesStatus: function (runId) {
                console.log(generalItemsStatus, runId)
                return generalItemsStatus[runId];
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
            getActivityById: function(itemId, gameId) {
                var deferred = $q.defer();
                var service = this;

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
            getActivityByIdRun: function(itemId, gameId, runId) {
                var deferred = $q.defer();
                var service = this;

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
                                    //generalItems[gameId][data.section][itemId].status = 0;
                                    console.log(generalItems[gameId]);
                                    service.getActivityStatus(runId, data.id).then(function(status){

                                        if(Object.keys(status).length != 0){
                                            service.getActivityById(status.generalItemId, gameId).then(function(activity){
                                                generalItems[gameId][activity.section][activity.id].status = status;
                                            });
                                        }
                                    });

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
            },
            refreshActivityStatus: function(id, gameId, runId) {
                var dataCache = CacheFactory.get('activitiesCache');
                var act = dataCache.get(id);
                if (act) {
                    delete generalItems[gameId][act.section][id];
                    dataCache.remove(id);
                }
                return this.getActivityByIdRun(id, gameId, runId);
            },
            getActivitiesServerStatus: function(gameId, runId){
                var dataCache = CacheFactory.get('activitiesStatusCache');
                var deferred = $q.defer();
                var service = this;
                Activity.getActivities({ gameId: gameId }).$promise.then(
                    function (data) {
                        for (var i = 0; i < data.generalItems.length; i++) {

                            var activity = data.generalItems[i];


                            if (!activity.deleted) {

                                if(angular.isUndefined(generalItemsStatus[runId])){
                                    generalItemsStatus[runId] = {};
                                }

                                if(angular.isUndefined(generalItemsStatus[runId][activity.section])){
                                    generalItemsStatus[runId][activity.section] = {};
                                }

                                activity.status = {};

                                activity.status = service.getActivityStatus(runId, activity.id);
                                generalItemsStatus[runId][activity.section][activity.id] = activity;
                                dataCache.put(activity.id, activity);
                            }
                        }

                        console.log(generalItemsStatus);

                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            }
        }
    }
);