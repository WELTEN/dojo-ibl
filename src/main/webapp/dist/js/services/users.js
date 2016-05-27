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
                        deferred.resolve(data.users);
                    }
                );
                return deferred.promise;
            }
        }
    }
);