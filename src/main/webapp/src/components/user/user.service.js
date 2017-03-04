angular.module('DojoIBL')

    .service('UserService', function ($q, User, CacheFactory, AccountService, toaster, $location, $stateParams) {

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
                    //console.log(runId, me);
                    self.getUsersForRun(runId).then(function(data){
                        //console.log(arrayObjectIndexOf(data, me.localId, "localId"));
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
    }
);