angular.module('DojoIBL')

    .service('UserService', function ($q, User, CacheFactory) {

        CacheFactory('usersCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        return {
            getUsersForRun: function (runId) {
                var dataCache = CacheFactory.get('usersCache');
                var deferred = $q.defer();
                User.getUsersRun({id: runId}).$promise.then(
                    function (data) {

                        angular.forEach(data.users, function(user){
                            dataCache.put(user.localId, user);
                        });

                        deferred.resolve(data.users);
                    }
                );
                return deferred.promise;
            },
            getUserFromCache: function(accountId) {
                var dataCache = CacheFactory.get('usersCache');
                return dataCache.get(accountId);
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
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            }
        }
    }
);