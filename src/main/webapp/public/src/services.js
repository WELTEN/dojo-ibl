angular.module('DojoIBL')

    .service('AccountService', ["$q", "Account", "CacheFactory", function ($q, Account, CacheFactory) {

        CacheFactory('accountCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        var dataCache = CacheFactory.get('accountCache');
        var usersId = dataCache.keys();
        var me = dataCache.get("me");

        return {
            myDetails: function () {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('accountCache');
                if (dataCache.get('me')) {
                    deferred.resolve(dataCache.get('me'));
                } else {
                    Account.accountDetails().$promise.then(
                        function (accountData) {
                            dataCache.put('me', accountData);
                            me = accountData;
                            deferred.resolve(accountData);
                        }
                    );
                }
                return deferred.promise;
            },
            myDetailsCache: function(){
                return me;
            },
            accountDetailsById: function(fullId) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('accountCache');
                if (dataCache.get(fullId)) {
                    deferred.resolve(dataCache.get(fullId));
                } else {
                    Account.accountDetailsById({ fullId:fullId }).$promise.then(
                        function (accountData) {
                            console.log(accountData)
                            dataCache.put(fullId, accountData);
                            //dataCache.put("me", accountData);
                            //me = accountData;
                            deferred.resolve(accountData);
                        }
                    );
                }
                return deferred.promise;
            },
            uploadUrl: function(account, key) {
                return Account.uploadUrl({ account:account, key:key });
            },
            update: function(accountAsJson){
                var service = this;
                Account.update(accountAsJson).$promise.then(function(data){
                    service.refreshAccount(data.email);
                });
            },
            searchAccount: function(query) {
                var deferred = $q.defer();
                Account.search(query).$promise.then(function (accountData) {
                    deferred.resolve(accountData);
                });
                return deferred.promise;

            },
            sendReminder: function(account) {
                Account.reminder({ account:account });
            },
            refreshAccount: function(id) {
                var dataCache = CacheFactory.get('accountCache');
                if (dataCache.get(id)) {
                    dataCache.remove(id);
                    //delete me;
                    //dataCache.remove("me");
                }
                return this.accountDetailsById(id);
            }
        }

    }]);;angular.module('DojoIBL')

    .service('ChannelService', ["$q", "$http", "CacheFactory", "ChannelApi", "$rootScope", function ($q, $http, CacheFactory, ChannelApi, $rootScope) {

        CacheFactory('channelAPICache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var dataCache = CacheFactory.get('channelAPICache');

        var SocketHandler = function () {
            this.messageCallback = function () {
            };

            this.onMessage = function (callback) {
                var theCallback = function (message) {
                    callback(JSON.parse(message.data));
                }

                if (this.channelSocket == undefined) {
                    this.messageCallback = theCallback;
                } else {
                    this.channelSocket.onmessage = theCallback;
                }
            }

            var context = this;
            this.socketCreationCallback = function (channelData) {
                var channel = new goog.appengine.Channel(channelData.token);
                dataCache.put("tokenChannelApi", channelData);
                context.channelId = channelData.channelId;
                var socket = channel.open();
                socket.onerror = function () {
                    console.log("Channel error");
                };
                socket.onclose = function () {
                    console.log("Channel closed, reopening");
                    //We reopen the channel
                    context.messageCallback = context.channelSocket.onmessage;
                    context.channelSocket = undefined;
                    $.getJSON("chats/channel", context.socketCreationCallback);
                    dataCache.remove("tokenChannelApi");
                    swal({
                        title: "Timeout!",
                        text: "The session has timed out. Refresh!",
                        type: "warning",
                        showCancelButton: false,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Refresh!!",
                        closeOnConfirm: false
                    }, function () {
                        //window.location.replace(window.location.href);
                        location.reload();
                        //swal("Timeout!", "Refresh this page to continue.", "success");
                    });
                };
                context.channelSocket = socket;
                context.channelSocket.onmessage = context.messageCallback;
            };

            //if (!dataCache.get("tokenChannelApi")) {
                ChannelApi.getToken().$promise.then(
                    this.socketCreationCallback
                );
                //console.log("Invoke tokenChannelApi")
            //}else {
            //    console.log("Hit cache tokenChannelApi");
            //}
        };

        var callBackFunctions = {};
        var socket = new SocketHandler();
        socket.onMessage(function (data) {
            $rootScope.$apply(function () {
                console.info(data.type);

                if (callBackFunctions[data.type]) callBackFunctions[data.type](data);

            });

        });
        return {
            register: function (type, callBackFunction) {
                callBackFunctions[type] = callBackFunction;
            }

        };

        //return {
        //    SocketHandler: function(user){
        //
        //        var dataCache = CacheFactory.get('channelAPICache');
        //
        //        this.messageCallback = function () {
        //
        //        };
        //
        //        this.onMessage = function (callback) {
        //            var theCallback = function (message) {
        //                callback(JSON.parse(message.data));
        //            };
        //
        //            if (this.channelSocket == undefined) {
        //                this.messageCallback = theCallback;
        //            } else {
        //                this.channelSocket.onmessage = theCallback;
        //            }
        //        };
        //
        //        var context = this;
        //
        //        this.setSocketChannel = function (channelData) {
        //            var channel = new goog.appengine.Channel(channelData.token);
        //
        //            dataCache.put(user.accountType+":"+user.localId, channelData);
        //
        //            context.channelId = channelData.channelId;
        //            var socket = channel.open();
        //
        //            socket.onopen = function(e) {
        //                console.info("Channel opened");
        //            };
        //
        //            socket.onerror = function () {
        //                console.error("Channel error");
        //            };
        //
        //            socket.onmessage = function(message) {
        //                console.log(message);
        //            };
        //
        //            socket.onclose = function () {
        //
        //                //We reopen the channel
        //                context.messageCallback = context.channelSocket.onmessage;
        //                context.channelSocket = undefined;
        //                $.getJSON("chats/channel", context.socketCreationCallback);
        //
        //                dataCache.remove(user.accountType+":"+user.localId);
        //
        //                console.warn('Channel closed');
        //
        //                swal({
        //                    title: "Timeout!",
        //                    text: "The session has timed out. Refresh!",
        //                    type: "warning",
        //                    showCancelButton: false,
        //                    confirmButtonColor: "#DD6B55",
        //                    confirmButtonText: "Refresh!!",
        //                    closeOnConfirm: false
        //                }, function () {
        //                    //window.location.replace(window.location.href);
        //                    location.reload();
        //                    //swal("Timeout!", "Refresh this page to continue.", "success");
        //                });
        //            };
        //
        //            socket.onmessage('test');
        //
        //            context.channelSocket = socket;
        //            //console.info("Channel info received");
        //            context.channelSocket.onmessage = context.messageCallback;
        //        };
        //
        //        if (!dataCache.get(user.accountType+":"+user.localId)) {
        //            $http({url: "/rest/channelAPI/token", method: 'GET'}).success(function(channelData){
        //                context.setSocketChannel(channelData);
        //            });
        //        }else {
        //            var channelData = dataCache.get(user.accountType + ":" + user.localId);
        //            context.setSocketChannel(channelData);
        //        }
        //    }
        //}
    }]
);;angular.module('DojoIBL')

    .service('ActivityService', ["$q", "Activity", "CacheFactory", function ($q, Activity, CacheFactory) {

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
                console.log(generalItems)
                return generalItems;
            },
            getCalendarActivities: function () {
                return calendarItems;
            },
            deleteActivity: function(gameId, item){
                var dataCache = CacheFactory.get('activitiesCache');
                dataCache.remove(item.id);
                var service = this;

                //console.log(generalItems[gameId][item.section][item.id]);

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
            }
        }
    }])
    .service('ActivityStatusService', ["$q", "Activity", "CacheFactory", function($q, Activity, CacheFactory){

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
            refreshActivityStatus: function(id, gameId, runId, phase) {
                var dataCache = CacheFactory.get('activitiesStatusCache');
                if (dataCache.get(runId+"_"+id)) {
                    delete generalItemsStatus[runId][phase][id];
                    dataCache.remove(runId+"_"+id);
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
                                dataCache.put(runId+"_"+generalItemsStatus[runId][gi.section][gi.id].id, generalItemsStatus[runId][gi.section][gi.id]);
                            });
                        });
                }
                return deferred.promise;
            }
        };
    }])
;;angular.module('DojoIBL')

    .service('NotificationService', ["$q", "$sce", "Game", "CacheFactory", "RunService", "ActivityService", function ($q, $sce, Game, CacheFactory, RunService, ActivityService) {

        CacheFactory('userNotificationsCache', {
            maxAge: 3 * 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 3 * 24 * 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var numberNotifications = 0;

        return {
            setNumberNotification: function(notifications){
                numberNotifications = notifications;
                var dataCache = CacheFactory.get('userNotificationsCache');
                dataCache.put("notifications", notifications);
            },
            getNumberNotification: function(){
                var dataCache = CacheFactory.get('userNotificationsCache');

                if (dataCache.get("notifications")) {
                    return dataCache.get("notifications");
                } else {
                    return 0;
                }
            }
        }
    }]
);;angular.module('DojoIBL')

    .constant('config', {
        //"server": "http://dojo-ibl.appspot.com"
        //"server": "http://localhost:8888"
        "server": ""
    }

);;angular.module('DojoIBL')

    .service('GameService', ["$q", "$sce", "Game", "CacheFactory", "RunService", "ActivityService", function ($q, $sce, Game, CacheFactory, RunService, ActivityService) {

        CacheFactory('gamesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 12 * 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        var games = {};
        var dataCache = CacheFactory.get('gamesCache');
        var gameIds = dataCache.keys();
        for (var i=0; i < gameIds.length; i++) {
            games[gameIds[i]] = dataCache.get(gameIds[i]);
        }

        var resumptionToken;
        var serverTime= 0;
        var serverTimeFirstInvocation;

        return {
            resumeLoadingGames: function(){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('gamesCache');
                var service = this;
                Game.resume({resumptionToken: resumptionToken, from:serverTime})
                    .$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);

                        } else {
                            for (i = 0; i < data.games.length; i++) {
                                if (data.games[i].deleted) {
                                    delete games[data.games[i].gameId];
                                } else {
                                    dataCache.put(data.games[i].gameId, data.games[i]);
                                    games[data.games[i].gameId] = data.games[i];
                                    games[data.games[i].gameId].description = $sce.trustAsHtml(data.games[i].description);

                                    games[data.games[i].gameId]["access"] = {};
                                    service.getGameAccesses(data.games[i].gameId).then(function(data){
                                        angular.forEach(data.gamesAccess, function(gameAccess) {
                                            games[gameAccess.gameId]["access"][gameAccess.account] = gameAccess;
                                        });
                                    });
                                }
                            }
                            resumptionToken = data.resumptionToken;
                            serverTimeFirstInvocation = serverTimeFirstInvocation || data.serverTime;
                            if (!data.resumptionToken){
                                serverTime = serverTimeFirstInvocation;
                                serverTimeFirstInvocation = undefined;
                            }

                            deferred.resolve(data);
                        }
                    });
                return deferred.promise;

            },
            getGameById: function (id) {
                var deferred = $q.defer();
                var service = this;

                var dataCache = CacheFactory.get('gamesCache');
                if (dataCache.get(id)) {
                    deferred.resolve(dataCache.get(id));
                } else {
                    Game.getGameById({id: id}).$promise.then(
                        function (data) {
                            if (!data.error){
                                if (data.deleted) {
                                    delete games[id];
                                    dataCache.remove(id);

                                } else {
                                    var rol = [];
                                    angular.forEach(data.config.roles, function(d){
                                        rol.push(JSON.parse(d));
                                    });

                                    data.config.roles = rol;

                                    dataCache.put(id, data);
                                    games[id] = data;

                                    games[id]["access"] = {};
                                    service.getGameAccesses(id).then(function(data){
                                        angular.forEach(data.gamesAccess, function(gameAccess) {
                                            games[gameAccess.gameId]["access"][gameAccess.account] = gameAccess;
                                        });
                                    });
                                }

                            }

                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            },
            refreshGame: function(id) {
                var dataCache = CacheFactory.get('gamesCache');
                if (dataCache.get(id)) {
                    delete games[id];
                    dataCache.remove(id);
                }
                return this.getGameById(id);
            },
            getGameFromCache: function(id) {
                var dataCache = CacheFactory.get('gamesCache');
                return dataCache.get(id);
            },
            storeInCache:function(game){
                var dataCache = CacheFactory.get('gamesCache');
                dataCache.put(game.gameId, game);
            },
            newGame: function(gameAsJson){
                var dataCache = CacheFactory.get('gamesCache');

                ////////////////////////////////////////
                // Only put in cache when we are editing
                ////////////////////////////////////////
                if(gameAsJson.gameId)
                    dataCache.put(gameAsJson.gameId, gameAsJson);

                var newGame = new Game(gameAsJson);

                return newGame.$save();
            },
            giveAccess: function(id, accountId, accessRight){
                Game.giveAccess({ gameId: id, accountId: accountId, accessRight: accessRight});
            },
            getGameAccesses: function(gameId){
                var deferred = $q.defer();
                Game.getGameAccesses({ gameId: gameId }).$promise.then(function(data){
                    deferred.resolve(data);
                });
                return deferred.promise;
            },
            getGames: function(){
                return games;
            },
            deleteGame: function(gameId){
                var dataCache = CacheFactory.get('gamesCache');
                dataCache.remove(gameId);
                delete games[gameId];
                return Game.deleteGame({ gameId: gameId });
            },
            removePhase: function(game, index){
                game.phases.splice(index, 1);

                this.newGame(game).then(function(updatedGame){
                    RunService.getParticipateRunsForGame(game.gameId).then(function(gameRuns){
                        angular.forEach(gameRuns, function(value, key) {
                            value.game = updatedGame;
                            RunService.storeInCache(value);
                        });
                    });
                });

                ActivityService.getActivitiesForPhase(game.gameId, index).then(function(data){
                    angular.forEach(data, function(i, a){
                        ActivityService.deleteActivity(i.gameId, i.id);
                    });
                });
            },
            addRole: function(gameId, roleAsJson, roleAsJsonOld){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('gamesCache');

                Game.addRole({ gameId: gameId }, roleAsJson, roleAsJsonOld).$promise.then(function(data){
                        dataCache.put(gameId, data);
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            removeRole: function(gameId, roleAsJson){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('gamesCache');

                Game.removeRole({ gameId: gameId }, roleAsJson).$promise.then(function(data){
                        dataCache.put(gameId, data);
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            editRole: function(gameId, roleAsJson, index){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('gamesCache');

                Game.editRole({ gameId: gameId, index: index }, roleAsJson).$promise.then(function(data){
                        dataCache.put(gameId, data);
                        deferred.resolve(data);
                    }
                );
                return deferred.promise;
            },
            cloneInquiry: function(gameId, title){

                var service = this;

                this.getGameById(gameId).then(function (data) {
                    var cloned_inquiry = {};
                    cloned_inquiry.title = title;
                    cloned_inquiry.phases = data.phases;
                    cloned_inquiry.description = data.description;
                    service.newGame(cloned_inquiry).then(function(new_inq){

                        ActivityService.getActivitiesServer(gameId).then(function (activities) {

                            angular.forEach(activities.generalItems, function(activity){

                                if(!activity.deleted){
                                    var object = {
                                        type: activity.type,
                                        section: activity.section,
                                        gameId: new_inq.gameId,
                                        deleted: activity.deleted,
                                        name: activity.name,
                                        description: activity.description,
                                        autoLaunch: activity.autoLaunch,
                                        fileReferences: activity.fileReferences,
                                        sortKey: activity.sortKey,
                                        richText: activity.richText
                                    };

                                    if(activity.type == "org.celstec.arlearn2.beans.generalItem.VideoObject"){
                                        object.videoFeed = activity.videoFeed;
                                    }else if(activity.type == "org.celstec.arlearn2.beans.generalItem.AudioObject") {
                                        object.audioFeed = activity.audioFeed;
                                    }

                                    ActivityService.newActivity(object);
                                }



                            });
                        });
                    });
                });
            },
            getGameAssets: function(gameId) {
                var deferred = $q.defer();
                Game.gameAssets({gameId:gameId}).$promise.then(
                    function (data) {
                        deferred.resolve(data.gameFiles);
                    }
                );
                return deferred.promise;
            }
        }
    }]
);;angular.module('DojoIBL')

    .service('MessageService', ["$q", "Message", "CacheFactory", "UserService", function ($q, Message, CacheFactory, UserService) {

        CacheFactory('messagesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var messages = {};
        var dataCache = CacheFactory.get('messagesCache');
        var messagesId = dataCache.keys();
        for (var i=0; i < messagesId.length; i++) {
            var message = dataCache.get(messagesId[i]);
            messages[message.runId] = messages[message.runId] || {};
            messages[message.runId][message.messageId] = message;
        }

        var resumptionToken;
        var serverTime= 0;
        var serverTimeFirstInvocation;

        return {
            getMessagesDefaultByRun: function(runId){
                //console.log(messages[runId]);
                return messages[runId];
            },
            newMessage: function(jsonMessage){
                var newMessage = new Message(jsonMessage);
                var dataCache = CacheFactory.get('messagesCache');
                return newMessage.$save();
            },
            refreshMessage: function(runId, messageId){
                var dataCache = CacheFactory.get('messagesCache');
                if(dataCache.get(messageId)){
                    delete messages[runId][messageId];
                    dataCache.remove(messageId);
                }
                return this.getMessageById(messageId);
            },
            getMessages: function (runId, from, resumptionToken) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('messagesCache');

                messages[runId] = messages[runId] || {};
                if (!from) {
                    from = 0;
                }

                var service = this;

                if(resumptionToken){
                    Message.resume({ runId:runId, resumptionToken: resumptionToken, from:from }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (i = 0; i < data.messages.length; i++) {
                                if (!data.messages[i].deleted) {
                                    dataCache.put(data.messages[i].messageId, data.messages[i]);
                                    messages[runId][data.messages[i].messageId] = data.messages[i];

                                    var message = data.messages[i];

                                    messages[runId][message.messageId].user = UserService.getUser(runId, data.messages[i].senderProviderId+":"+data.messages[i].senderId);
                                }else{
                                    delete messages[runId][data.messages[i].messageId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getMessages(runId, from, data.resumptionToken);
                            }
                        }
                    });
                }else{
                    Message.resume({ runId:runId, from:0 }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (var i = 0; i < data.messages.length; i++) {
                                if (!data.messages[i].deleted) {
                                    dataCache.put(data.messages[i].messageId, data.messages[i]);
                                    messages[runId][data.messages[i].messageId] = data.messages[i];

                                    var message = data.messages[i];

                                    messages[runId][message.messageId].user = UserService.getUser(runId, data.messages[i].senderProviderId+":"+data.messages[i].senderId);
                                }else{
                                    delete messages[runId][data.messages[i].messageId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getMessages(runId, from, data.resumptionToken);
                            }
                        }
                    });
                }

                return messages[runId];
            },
            getMessageById: function(messageId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('messagesCache');
                if (dataCache.get(messageId)) {
                    deferred.resolve(dataCache.get(messageId));
                } else {

                    Message.getMessageById({ messageId:messageId }).$promise.then(
                        function(data){
                            dataCache.put(messageId, data);
                            messages[data.runId][data.messageId] = data;
                            messages[data.runId][data.messageId].user = UserService.getUser(data.runId, data.senderProviderId+":"+data.senderId);
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            }
        }
    }]
);;    angular.module('DojoIBL')

    .factory('Session', ["$http", "CacheFactory", function SessionFactory($http, CacheFactory) {
        function getCookie(name) {
            var value = "; " + document.cookie;
            var parts = value.split("; " + name + "=");
            if (parts.length == 2) return parts.pop().split(";").shift();
        } //temp function

        return {
            getOauthType : function(){
                if (getCookie('arlearn.OauthType')) {
                    this.setOauthType(getCookie('arlearn.OauthType'));
                }
                return localStorage.getItem('oauth')
            },
            setOauthType : function(value){
                return localStorage.setItem('oauth', value)
            },
            getAccessToken: function() {
                var service = this;
                if (getCookie('arlearn.AccessToken')) {
                    service.setAccessToken(getCookie('arlearn.AccessToken'));
                }
                return localStorage.getItem('accessToken')
            },
            setAccessToken: function(value) {
                $http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+value;
                return localStorage.setItem('accessToken', value)
            },
            reset: function(){
                var accounts = CacheFactory.get('accountCache');
                var activities = CacheFactory.get('activitiesCache');
                var activitiesStatus = CacheFactory.get('activitiesStatusCache');
                var games = CacheFactory.get('gamesCache');
                var responses = CacheFactory.get('responsesCache');
                var runs = CacheFactory.get('runsCache');
                var users = CacheFactory.get('usersCache');
                var messages = CacheFactory.get('messagesCache');

                if(accounts) accounts.removeAll();
                if(activities) activities.removeAll();
                if(activitiesStatus) activitiesStatus.removeAll();
                if(games) games.removeAll();
                if(responses) responses.removeAll();
                if(runs) runs.removeAll();
                if(users) users.removeAll();
                if(messages) messages.removeAll();
                localStorage.removeItem('oauth');
                localStorage.removeItem('accessToken');
            }
        }
    }]
);;angular.module('DojoIBL')

    .service('ResponseService', ["$q", "Response", "CacheFactory", "UserService", "$filter", function ($q, Response, CacheFactory, UserService, $filter) {

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
            }
        }
    }]
);;angular.module('DojoIBL')

    .service('UserService', ["$q", "User", "CacheFactory", "AccountService", "toaster", "$location", "$stateParams", function ($q, User, CacheFactory, AccountService, toaster, $location, $stateParams) {

        CacheFactory('usersCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var users = {};
        var dataCache = CacheFactory.get('usersCache');
        var usersId = dataCache.keys();
        for (var i=0; i < usersId.length; i++) {
            var user = dataCache.get(usersId[i]);
            users[user.runId] = users[user.runId] || {} ;
            users[user.runId][user.accountType+":"+user.localId] = user;
        }

        return {
            getUsersForRun: function (runId) {
                var dataCache = CacheFactory.get('usersCache');
                var deferred = $q.defer();
                var self = this;

                users[runId] = users[runId] || {};

                User.getUsersRun({id: runId}).$promise.then(
                    function (data) {

                        var filtered_users =[];

                        angular.forEach(data.users, function(user){
                            if(!user.deleted){

                                user.runId = runId;

                                users[runId][user.accountType+":"+user.localId] = user;
                                filtered_users.push(user)
                            }
                        });

                        deferred.resolve(filtered_users);
                    }
                );
                return deferred.promise;
            },
            getUserFromCache: function(accountId) {
                var dataCache = CacheFactory.get('usersCache');
                return dataCache.get(accountId);
            },
            getUser: function(runId, fullAccountId) {
              return users[runId][fullAccountId];
            },
            getUserByAccount: function(runId, accountId) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('usersCache');
                if (dataCache.get(accountId)) {
                    deferred.resolve(dataCache.get(accountId));
                } else {
                    User.getUserByAccount({ runId: runId, accountId:accountId }).$promise.then(
                        function(data){

                            if (data.deleted) {
                                delete users[runId][data.accountType+":"+data.localId];
                                dataCache.remove(accountId);
                            }else{

                                if(angular.isUndefined(users[runId])){
                                    users[runId] = {};
                                }

                                data.runId = runId;

                                dataCache.put(accountId, data);
                                users[runId][data.accountType+":"+data.localId] = data;
                                deferred.resolve(data);
                            }
                        }
                    );
                }
                return deferred.promise;
            },
            checkAccess: function() {
                var runId = $stateParams.runId;
                var self = this;
                AccountService.myDetails().then(function(me){
                    console.log(runId, me);
                    self.getUsersForRun(runId).then(function(data){
                        console.log(arrayObjectIndexOf(data, me.localId, "localId"));
                        if(arrayObjectIndexOf(data, me.localId, "localId") == -1){
                            $location.path('home');
                            toaster.error({
                                title: 'No access ',
                                body: 'You do not have access to this inquiry.'
                            });
                        }
                    });
                });

                function arrayObjectIndexOf(myArray, searchTerm, property) {
                    for(var i = 0, len = myArray.length; i < len; i++) {
                        if (myArray[i][property] === searchTerm) return i;
                    }
                    return -1;
                }


                //console.log(users);
                //
                //if(angular.isUndefined(users[runId])){
                //    console.log("user runId undefined")
                //
                //    users[runId] = {};
                //    this.getUsersForRun(runId).$promise.then(function (results) {
                //        if(angular.isUndefined(users[runId][data.accountType+":"+data.localId])){
                //            console.log("user runId was undefined y user undefined")
                //
                //            //console.log("salida 2")
                //            //return !angular.isUndefined(users[runId][data.accountType+":"+data.localId]);
                //            return false;
                //        }else{
                //            console.log("user runId was undefined y user exists")
                //
                //            return true;
                //        }
                //    });
                //}else{
                //    console.log("user runId exists", angular.isUndefined(users[runId][data.accountType+":"+data.localId]), users[runId], data.accountType+":"+data.localId, users[runId][data.accountType+":"+data.localId])
                //
                //    if(angular.isUndefined(users[runId][data.accountType+":"+data.localId])){
                //        console.log("user runId exists y user undefined")
                //
                //        return false;
                //    }else{
                //        console.log("user runId exists y user exists")
                //
                //        return true;
                //    }
                //}
                //
                //if(angular.isUndefined(users[runId])){
                //    users[runId] = {};
                //    this.getUsersForRun(runId).$promise.then(function (results) {
                //        console.log("salida 2")
                //        return !angular.isUndefined(users[runId][data.accountType+":"+data.localId]);
                //    });
                //}else{
                //    console.log("salida 1")
                //    return !angular.isUndefined(users[runId][data.accountType+":"+data.localId]);
                //}
                //
                //
                //
                //return !angular.isUndefined(users[runId]) && !angular.isUndefined(users[runId][data.accountType+":"+data.localId])
            }
            //refreshAccount: function(account) {
            //    console.log(account)
            //    var dataCache = CacheFactory.get('usersCache');
            //    if (dataCache.get(account.email)) {
            //        console.log(dataCache.get(account.email));
            //        dataCache.remove(account.email);
            //    }
            //    //return this.getUserByAccount(account.email);
            //}
        }
    }]
);;angular.module('DojoIBL')

    .service('RunService', ["$q", "Run", "CacheFactory", function ($q, Run, CacheFactory) {

        CacheFactory('runsCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        return {
            getRunById: function (id) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('runsCache');
                if (dataCache.get(id)) {
                    deferred.resolve(dataCache.get(id));
                } else {

                    Run.getRun({id: id}).$promise.then(
                        function (data) {

                            if(!angular.isUndefined(data.game.config.roles)){
                                var roles = [];

                                for (var i=0; i < data.game.config.roles.length; i++){
                                    roles.push(JSON.parse(data.game.config.roles[i]))
                                }
                                data.game.config.roles = roles;
                            }


                            dataCache.put(id, data);
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            },
            getRunByCode: function (code) {
                var deferred = $q.defer();

                Run.getRunByCode({ code: code }).$promise.then(
                    function (data) {
                        deferred.resolve(data);
                    }
                );

                return deferred.promise;
            },
            getOwnedRunsForGame: function (gameId) {
                var service = this;
                var deferred = $q.defer();
                Run.getOwnedRunsForGame({id: gameId}).$promise.then(
                    function (data) {
                        var runAccess={};
                        for (var i = 0; i < data.runAccess.length; i++) {
                            runAccess['id'+data.runAccess[i].runId] = data.runAccess[i];
                            service.getRunById(data.runAccess[i].runId).then(function (runObject) {
                                runAccess['id'+runObject.runId].run = runObject;
                            });
                        }
                        deferred.resolve(data);
                    }
                );

                return deferred.promise;
            },
            getParticipateRunsForGame: function (gameId) {
                var service = this;
                var dataCache = CacheFactory.get('runsCache');
                var deferred = $q.defer();
                Run.getParticipateRunsForGame({id: gameId}).$promise.then(
                    function (data) {

                        var runAccess={};
                        for (var i = 0; i < data.runs.length; i++) {
                            if(!data.runs[i].deleted){
                                dataCache.put(data.runs[i].runId, data.runs[i]);
                                runAccess['id'+data.runs[i].runId] = data.runs[i];
                            }
                        }
                        deferred.resolve(runAccess);
                    }
                );

                return deferred.promise;
            },
            newRun: function(runAsJson){
                var newrun = new Run(runAsJson);
                var dataCache = CacheFactory.get('runsCache');

                ////////////////////////////////////////
                // Only put in cache when we are editing
                ////////////////////////////////////////
                if(runAsJson.runId)
                    dataCache.put(runAsJson.runId, runAsJson);
                return newrun.$save();
            },
            updateRun: function(runAsJson){
                Run.update({ runId: runAsJson.runId }, runAsJson);
            },
            giveAccess: function(runId, accountId, accessRight){
                Run.giveAccess({ runId: runId, accountId: accountId, accessRight: accessRight});
            },
            removeAccessToInquiryRun: function(runId, account){

                Run.removeAccess({ runId: runId, accountId: account.accountType+":"+account.localId });

                Run.removeUserRun({
                    runId: runId,
                    email: account.accountType+":"+account.localId
                });

            },
            addUserToRun: function(json){
                return Run.addUserToRun(json);
            },
            deleteRun: function(runId){
                var dataCache = CacheFactory.get('runsCache');
                dataCache.remove(runId);
                return Run.delete({ id: runId });
            },
            storeInCache:function(run){
                var dataCache = CacheFactory.get('runsCache');
                if (dataCache.get(run.runId)) {
                    dataCache.remove(run.runId);
                }
                dataCache.put(run.runId, run);
            }
        }
    }]
);