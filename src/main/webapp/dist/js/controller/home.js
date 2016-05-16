angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, config, Session, RunService) {

        $scope.games = [];

        Game.access({ }).$promise.then(function (data) {

            angular.forEach(data.gamesAccess, function (gameAccess) {

                GameService.getGameById(gameAccess.gameId).then(function (data) {
                        $scope.games = $scope.games.concat(data);
                });
            });
        });

        $scope.thumbnailUrl = function(gameId) {

            return config.server+'/game/'+gameId+'/gameThumbnail';
        };

        $scope.showRunsValue = true;
        $scope.showRuns = function (id) {

            RunService.getParticipateRunsForGame(id).then(function(data){
                $scope.runs = data;
            });

            this.showRunsValue = !this.showRunsValue;
        };
    }
);