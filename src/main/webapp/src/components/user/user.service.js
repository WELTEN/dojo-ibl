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
            checkAccess: function(runId) {
                var self = this;
                AccountService.myDetails().then(function(me){
                    self.getUsersForRun(runId).then(function(data){
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
            }
        }
    }
);