angular.module('DojoIBL')

    .service('UserService', function ($q, User, CacheFactory) {

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
            users[user.accountType+":"+user.localId] = user || {} ;
        }

        return {
            getUsersForRun: function (runId) {
                var dataCache = CacheFactory.get('usersCache');
                var deferred = $q.defer();
                var self = this;
                User.getUsersRun({id: runId}).$promise.then(
                    function (data) {

                        var filtered_users =[];

                        angular.forEach(data.users, function(user){
                            if(!user.deleted){
                                console.log(user)
                                users[user.accountType+":"+user.localId] = user;
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
            getUser: function(fullAccountId) {
              return users[fullAccountId];
            },
            getUserByAccount: function(runId, accountId) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('usersCache');
                if (dataCache.get(accountId)) {
                    deferred.resolve(dataCache.get(accountId));
                } else {
                    User.getUserByAccount({ runId: runId, accountId:accountId }).$promise.then(
                        function(data){
                            dataCache.put(accountId, data);
                            users[data.accountType+":"+data.localId] = data;
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
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