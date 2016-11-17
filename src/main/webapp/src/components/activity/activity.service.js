angular.module('DojoIBL')

    .service('ActivityService', function ($q, Activity, CacheFactory) {

        CacheFactory('activitiesCache', {
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

                                if(angular.isUndefined(generalItems[gameId])){
                                    generalItems[gameId] = {};
                                }

                                if(angular.isUndefined(generalItems[gameId][data.generalItems[i].section])){
                                    generalItems[gameId][data.generalItems[i].section] = {};
                                }

                                generalItems[gameId][data.generalItems[i].section][data.generalItems[i].id] = data.generalItems[i];
                                dataCache.put(data.generalItems[i].id, data.generalItems[i]);
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

                console.log(newActivity, activityAsJson);

                //ActivityService.addRole(data.id, result.roles2[0]).then(function(data){
                //
                //});

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
                                //if (data.deleted) {
                                //    delete generalItems[gameId][data.section][itemId];
                                //    dataCache.remove(itemId);
                                //
                                //} else {
                                    if(angular.isUndefined(generalItems[gameId])){
                                        generalItems[gameId] = {};
                                    }
                                    if(angular.isUndefined(generalItems[gameId][data.section])){
                                        generalItems[gameId][data.section] = {};
                                    }
                                    generalItems[gameId][data.section][itemId] = data;

                                    dataCache.put(itemId, data);
                                    deferred.resolve(data);
                                //}
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
            uploadUrl: function(gameId, itemId, key) {
                return Activity.uploadUrl({ gameId:gameId, itemId:itemId, key:key });
            }
        }
    })
    .service('ActivityStatusService', function($q, Activity, CacheFactory){

        CacheFactory('activitiesStatusCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var generalItemsStatus = {};
        var statusCache = CacheFactory.get('activitiesStatusCache');
        var generalItemsStatusId = statusCache.keys();

        for (var i=0; i < generalItemsStatusId.length; i++) {
            var giStatus = statusCache.get(generalItemsStatusId[i]);

            if(angular.isUndefined(generalItemsStatus[giStatus.runId])){
                generalItemsStatus[giStatus.runId] = {};
            }else{
                generalItemsStatus[giStatus.runId] = generalItemsStatus[giStatus.runId];
            }

            if(angular.isUndefined(generalItemsStatus[giStatus.runId][giStatus.section])){
                generalItemsStatus[giStatus.runId][giStatus.section] = {};
            }else{
                generalItemsStatus[giStatus.runId][giStatus.section] = generalItemsStatus[giStatus.runId][giStatus.section];
            }

            generalItemsStatus[giStatus.runId][giStatus.section][giStatus.id] = giStatus;
        }

        return {
            getActivitiesServerStatus: function(gameId, runId, phase){
                var dataCache = CacheFactory.get('activitiesStatusCache');
                var deferred = $q.defer();
                var service = this;
                Activity.getActivitiesForPhase({ gameId: gameId, phase: phase }).$promise.then(
                    function (data) {
                        if (!data.error) {
                            for (var i = 0; i < data.generalItems.length; i++) {

                                var activity = data.generalItems[i];

                                if (activity.deleted) {
                                    if(!angular.isUndefined(generalItemsStatus[runId]) && !
                                            angular.isUndefined(generalItemsStatus[runId][activity.section])){
                                        delete generalItemsStatus[runId][activity.section][activity.id];
                                    }
                                    dataCache.remove(activity.id);

                                } else {
                                    if(angular.isUndefined(generalItemsStatus[runId])){
                                        generalItemsStatus[runId] = {};
                                    }
                                    if(angular.isUndefined(generalItemsStatus[runId][activity.section])){
                                        generalItemsStatus[runId][activity.section] = {};
                                    }

                                    generalItemsStatus[runId][activity.section][activity.id] = activity;
                                    generalItemsStatus[runId][activity.section][activity.id].runId = runId;
                                }
                            }
                            deferred.resolve(data);
                        }
                    }
                ).then(
                    function(){
                        angular.forEach(generalItemsStatus[runId][phase], function(gi){
                            service.getActivityStatus(runId, gi.id).then(function(status) {
                                if(angular.isUndefined(status)){
                                    generalItemsStatus[runId][gi.section][gi.id]["status"] = 0;
                                }else{
                                    generalItemsStatus[runId][gi.section][gi.id]["status"] = status;
                                }
                                dataCache.put(runId+"_"+generalItemsStatus[runId][gi.section][gi.id].id, generalItemsStatus[runId][gi.section][gi.id]);
                            });
                        });
                    });
                return deferred.promise;
            },
            getActivitiesStatus: function () {
                return generalItemsStatus;
            },
            refreshActivityStatus: function(id, gameId, runId) {
                var dataCache = CacheFactory.get('activitiesStatusCache');
                if (dataCache.get(runId+"_"+id)) {
                    dataCache.remove(id);
                }
                return this.getActivityByIdRun(id, gameId, runId);
            },
            changeActivityStatus: function(runId, gItemId, status, phase){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesStatusCache');

                Activity.changeActivityStatus({ runId:runId, generalItemId: gItemId, status: status}).$promise.then(function(data){
                        generalItemsStatus[runId][phase][gItemId]["status"] = data.status;
                        dataCache.put(runId+"_"+generalItemsStatus[runId][phase][gItemId].id, generalItemsStatus[runId][phase][gItemId])
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            getActivityStatus: function(runId, gItemId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesStatusCache');

                if (dataCache.get(runId+"_"+gItemId)) {
                    deferred.resolve(dataCache.get(runId+"_"+gItemId).status);
                } else {
                    Activity.getActivityStatus({ runId:runId, generalItemId: gItemId}).$promise.then(function(data){
                            deferred.resolve(data.status);
                        }
                    );
                }

                return deferred.promise;
            },
            getActivityByIdRun: function(itemId, gameId, runId) {
                var deferred = $q.defer();
                var gi;
                var service = this;

                var dataCache = CacheFactory.get('activitiesStatusCache');

                if (dataCache.get(runId+"_"+itemId)) {
                    deferred.resolve(dataCache.get(runId+"_"+itemId));
                } else {

                    Activity.getActivity({ itemId:itemId, gameId: gameId}).$promise.then(
                        function(data){

                            if (!data.error){
                                if (data.deleted) {
                                    if(!angular.isUndefined(generalItemsStatus[runId]) && !
                                            angular.isUndefined(generalItemsStatus[runId][data.section])){
                                        delete generalItemsStatus[runId][data.section][itemId];
                                    }
                                    dataCache.remove(itemId);

                                } else {
                                    if(angular.isUndefined(generalItemsStatus[runId])){
                                        generalItemsStatus[runId] = {};
                                    }
                                    if(angular.isUndefined(generalItemsStatus[runId][data.section])){
                                        generalItemsStatus[runId][data.section] = {};
                                    }

                                    generalItemsStatus[runId][data.section][itemId] = data;
                                    generalItemsStatus[runId][data.section][itemId].runId = runId;

                                    gi = generalItemsStatus[runId][data.section][itemId];
                                }
                            }
                            deferred.resolve(gi);
                        }
                    ).then(
                        function(){
                            service.getActivityStatus(runId, itemId).then(function(status) {
                                generalItemsStatus[runId][gi.section][gi.id]["status"] = status;
                                dataCache.put(runId+"_"+generalItemsStatus[runId][gi.section][gi.id].id, generalItemsStatus[runId][gi.section][gi.id]);
                            });
                        });
                }
                return deferred.promise;
            }
        };
    })
;