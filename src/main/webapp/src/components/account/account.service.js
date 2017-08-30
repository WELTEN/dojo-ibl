angular.module('DojoIBL')

    .service('AccountService', function ($q, Account, CacheFactory) {

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
                            //console.log(accountData)
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
            },
            emptyAccountsCache: function(){
                var dataCache = CacheFactory.get('accountCache');
                if(dataCache) dataCache.removeAll();
            }
        }

    });