angular.module('DojoIBL')

    .service('GameService', function ($q, Game, CacheFactory) {

        CacheFactory('gamesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.

        });

        return {
            getGameById: function(id) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('gamesCache');
                if (dataCache.get(id)) {
                    deferred.resolve(dataCache.get(id));
                } else {

                    Game.getGameById({id:id}).$promise.then(
                        function(data){

                            var rol = [];
                            angular.forEach(data.config.roles, function(d){
                                rol.push(JSON.parse(d));
                            });

                            data.config.roles = rol;
                            dataCache.put(id, data);
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
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
            getGames: function(){
                var dataCache = CacheFactory.get('gamesCache');
                return dataCache.keySet();
            },
            deleteGame: function(gameId){
                var dataCache = CacheFactory.get('gamesCache');
                dataCache.remove(gameId);
                return Game.deleteGame({ gameId: gameId });
            }
        }
    }
);