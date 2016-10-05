angular.module('DojoIBL')

    .service('GameService', function ($q, Game, CacheFactory) {

        CacheFactory('gamesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
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
            getGameById: function(id) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('gamesCache');
                if (dataCache.get(id)) {
                    deferred.resolve(dataCache.get(id));
                } else {

                    Game.getGameById({id:id}).$promise.then(
                        function(data){

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
                                deferred.resolve(data);
                            }
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

                console.log(gameAsJson);
                gameAsJson.lastModificationDate = new Date();

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
                return Game.deleteGame({ gameId: gameId });
            }
        }
    }
);