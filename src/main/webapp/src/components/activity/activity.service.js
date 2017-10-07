angular.module('DojoIBL')

    .service('ActivityService', function ($q, Activity, CacheFactory) {

        CacheFactory('activitiesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        CacheFactory('activitiesTempCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var generalItems = {};
        var calendarItems = {};
        var dataCache = CacheFactory.get('activitiesCache');
        var generalItemsId = dataCache.keys();
        for (var i=0; i < generalItemsId.length; i++) {
            var generalItem = dataCache.get(generalItemsId[i]);
            generalItems[generalItem.gameId] = generalItems[generalItem.gameId] || {};
            generalItems[generalItem.gameId][generalItem.section] = generalItems[generalItem.gameId][generalItem.section] || {};
            generalItems[generalItem.gameId][generalItem.section][generalItem.id] = generalItem;
            calendarItems[generalItem.gameId] = calendarItems[generalItem.gameId] || [];
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
                                    calendarItems[gameId] = [];
                                }

                                if(angular.isUndefined(generalItems[gameId][data.generalItems[i].section])){
                                    generalItems[gameId][data.generalItems[i].section] = {};
                                }

                                generalItems[gameId][data.generalItems[i].section][data.generalItems[i].id] = data.generalItems[i];

                                var obj ={
                                    _id: data.generalItems[i].id,
                                    title: data.generalItems[i].section+") "+data.generalItems[i].name,
                                    start: new Date(data.generalItems[i].timestamp),
                                    activity: data.generalItems[i]
                                }

                                var index = service.arrayObjectIndexOf(calendarItems[gameId], data.generalItems[i].id, "_id");

                                if (index == -1) {
                                    calendarItems[gameId].push(obj);
                                }

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
            getCalendarActivities: function () {
                return calendarItems;
            },
            deleteActivity: function(gameId, item){
                var dataCache = CacheFactory.get('activitiesCache');
                dataCache.remove(item.id);
                var service = this;

                delete generalItems[gameId][item.section][item.id];

                var index = service.arrayObjectIndexOf(calendarItems[gameId], item.id, "_id");
                if (index > -1) {
                    calendarItems[gameId].splice(index, 1);
                }
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
                if(Date.parse(activityAsJson.timestamp)){
                    activityAsJson.timestamp = Date.parse(activityAsJson.timestamp);
                }

                activityAsJson.timeStamp = activityAsJson.timestamp;

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

                            if (!data.deleted) {

                                if (!data.error) {

                                    if (angular.isUndefined(generalItems[gameId])) {
                                        generalItems[gameId] = {};
                                    }
                                    if (angular.isUndefined(calendarItems[gameId])) {
                                        calendarItems[gameId] = [];
                                    }

                                    if (angular.isUndefined(generalItems[gameId][data.section])) {
                                        generalItems[gameId][data.section] = {};
                                    }
                                    generalItems[gameId][data.section][itemId] = data;

                                    var obj = {
                                        _id: data.id,
                                        title: data.section + ") " + data.name,
                                        start: new Date(data.timestamp),
                                        activity: data
                                    };

                                    var index = service.arrayObjectIndexOf(calendarItems[gameId], data.id, "_id");
                                    if (index == -1) {
                                        calendarItems[gameId].push(obj);
                                    }

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
                var service = this;
                var act = dataCache.get(id);
                if (act) {
                    delete generalItems[gameId][act.section][id];

                    var obj ={
                        id: act.id,
                        title: act.section+") "+act.name,
                        start: new Date(act.timestamp),
                        activity: act
                    };

                    var index = service.arrayObjectIndexOf(calendarItems[gameId], act.id, "_id");
                    if (index > -1) {
                        calendarItems[gameId].splice(index, 1);
                    }
                    dataCache.remove(id);
                }
                return this.getActivityById(id, gameId);
            },
            uploadUrl: function(gameId, itemId, key) {
                return Activity.uploadUrl({ gameId:gameId, itemId:itemId, key:key });
            },
            arrayObjectIndexOf: function (myArray, searchTerm, property) {
                for(var i = 0, len = myArray.length; i < len; i++) {
                    if (myArray[i][property] === searchTerm){
                        return i;
                    }
                }
                return -1;
            },
            getNextActivity: function(itemId, gameId, phase) {
                if(this.getLengthActivity(itemId, gameId, phase) + 1 != this.getCurrentPositionActivity(itemId, gameId, phase)){
                    var aux = 1 + this.getCurrentPositionActivity(itemId, gameId, phase);
                    return generalItems[gameId][phase][Object.keys(generalItems[gameId][phase])[aux]];
                }
            },
            getPrevActivity: function(itemId, gameId, phase) {
                if(this.getCurrentPositionActivity(itemId, gameId, phase) > 0){
                    var aux = this.getCurrentPositionActivity(itemId, gameId, phase) - 1;
                    return generalItems[gameId][phase][Object.keys(generalItems[gameId][phase])[aux]];
                }
            },
            getCurrentPositionActivity: function(itemId, gameId, phase){
                return Object.keys(generalItems[gameId][phase]).indexOf(itemId);
            },
            getLengthActivity: function(itemId, gameId, phase){
                return Object.keys(generalItems[gameId][phase]).length;
            },
            getLengthPhase: function(itemId, gameId){
                return Object.keys(generalItems[gameId]).length;
            },
            saveActivityInCache: function(activityAsJson){
                var dataCache = CacheFactory.get('activitiesTempCache');
                dataCache.put("cachedActivity", activityAsJson);
            },
            getActivityInCached: function(){
                var dataCache = CacheFactory.get('activitiesTempCache');
                return dataCache.get("cachedActivity");
            },
            removeCachedActivity: function(){
                var dataCache = CacheFactory.get('activitiesTempCache');
                dataCache.remove("cachedActivity");
            },
            emptyActivitiesCache: function(){
                var dataCache = CacheFactory.get('activitiesCache');
                if(dataCache) dataCache.removeAll();
                generalItems = {};
                calendarItems = {};
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
                                    dataCache.remove(runId+"_"+activity.id);

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
                                    var object = {
                                        type: "org.celstec.arlearn2.beans.run.GeneralItemsStatus",
                                        runId: runId,
                                        generalItemId: gi.id,
                                        status: 0
                                    };

                                    Activity.changeActivityStatus(object).$promise.then(function(data){
                                            generalItemsStatus[runId][phase][gi.id]["status"] = data;

                                            dataCache.put(runId+"_"+gi.id, generalItemsStatus[runId][phase][gi.id]);

                                            deferred.resolve(data);
                                        }
                                    );
                                }else{
                                    generalItemsStatus[runId][gi.section][gi.id]["status"] = status;
                                }
                                dataCache.put(runId+"_"+gi.id, generalItemsStatus[runId][gi.section][gi.id]);
                            });
                        });
                    });
                return deferred.promise;
            },
            getActivitiesStatus: function () {
                return generalItemsStatus;
            },
            refreshActivityStatus: function(gItemId, gameId, runId, phase) {
                var dataCache = CacheFactory.get('activitiesStatusCache');
                if (dataCache.get(runId+"_"+gItemId)) {
                    delete generalItemsStatus[runId][phase][gItemId];
                    dataCache.remove(runId+"_"+gItemId);
                }
                return this.getActivityByIdRun(gItemId, gameId, runId);
            },
            changeActivityStatus: function(runId, gItemId, status, phase, id){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesStatusCache');

                var object = {
                    type: "org.celstec.arlearn2.beans.run.GeneralItemsStatus",
                    runId: runId,
                    generalItemId: gItemId,
                    status: status,
                    id: id
                };

                Activity.changeActivityStatus(object).$promise.then(function(data){
                        generalItemsStatus[runId][phase][gItemId]["status"] = data;

                        dataCache.put(runId+"_"+gItemId, generalItemsStatus[runId][phase][gItemId]);

                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            getActivityStatus: function(runId, gItemId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('activitiesStatusCache');

                if (dataCache.get(runId+"_"+gItemId)) {

                    deferred.resolve(dataCache.get(runId+"_"+gItemId)["status"]);
                } else {
                    Activity.getActivityStatus({ runId:runId, generalItemId: gItemId}).$promise.then(function(data){
                            deferred.resolve(data);
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
                                    dataCache.remove(runId+"_"+itemId);

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
                                dataCache.put(runId+"_"+itemId, generalItemsStatus[runId][gi.section][gi.id]);
                            });
                        });
                }
                return deferred.promise;
            },
            emptyActivityStatusCache: function(){
                var dataCache = CacheFactory.get('activitiesStatusCache');
                if(dataCache) dataCache.removeAll();
                generalItemsStatus = {};
            }
        };
    })
;