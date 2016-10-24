angular.module('DojoIBL')

    .service('GameService', function ($q, $sce, Game, CacheFactory, RunService, ActivityService) {

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
                            console.log(data)
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
            }
        }
    }
);