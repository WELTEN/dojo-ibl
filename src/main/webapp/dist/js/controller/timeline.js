angular.module('DojoIBL')

    .controller('TimelineController', function ($scope, $sce, $stateParams, $state, Response) {

        console.log($stateParams)
        $scope.games = {};
        $scope.games.games = [];

        $scope.disableGameLoading = false;

        $scope.loadMoreGames = function () {

            $scope.disableGameLoading = true;

            Response.resume({resumptionToken: $scope.games.resumptionToken, runId: $stateParams.runId, from: 0 })
                .$promise.then(function (data) {


                    $scope.games.games = $scope.games.games.concat(data.responses);
                    //for (i = 0; i < data.responses.length; i++) {
                    //    //GameService.storeInCache(data.games[i]);
                    //    data.responses[i].description = $sce.trustAsHtml(data.games[i].description);
                    //
                    //}
                    //$scope.games = GameService.getGames();
                    $scope.games.resumptionToken = data.resumptionToken;
                    $scope.games.serverTime = data.serverTime;

                    if (data.resumptionToken) {
                        $scope.disableGameLoading = false
                    } else {
                        $scope.disableGameLoading = true
                    }

                });

        };


        //$scope.games = [];
        //
        //Game.access({ }).$promise.then(function (data) {
        //
        //    angular.forEach(data.gamesAccess, function (gameAccess) {
        //
        //        GameService.getGameById(gameAccess.gameId).then(function (data) {
        //                $scope.games = $scope.games.concat(data);
        //        });
        //    });
        //});
        //
        //$scope.thumbnailUrl = function(gameId) {
        //
        //    return config.server+'/game/'+gameId+'/gameThumbnail';
        //};
        //
        //$scope.showRunsValue = true;
        //$scope.showRuns = function (id) {
        //
        //    RunService.getParticipateRunsForGame(id).then(function(data){
        //        $scope.runs = data;
        //    });
        //
        //    this.showRunsValue = !this.showRunsValue;
        //};

        //$scope.games = [];
        //
        //Store.getFeaturedByLanguage({lang:'nl'}).$promise.then(function(data){
        //    console.log(data);
        //    var localData = data;
        //    for (var i = 0; i< data.games.length;i++) {
        //
        //        mFunction = function localFct(game){
        //            Game.getGameById({id:game.gameId}).$promise.then(function(data){
        //
        //
        //                game.title = data.title;
        //                game.data = data;
        //                game.data.description = $sce.trustAsHtml(game.data.description);
        //
        //                $scope.games.push(game);
        //            });
        //        } ;
        //        mFunction((data.games[i]));
        //
        //    }
        //});
        //
        //$scope.thumbnailUrl = function(gameId) {
        //
        //    return config.server+'/game/'+gameId+'/gameThumbnail';
        //}
        //
        //
        //
        //$scope.isLoggedIn = function () {
        //    if (Session.getAccessToken() ) return true;
        //    return false;
        //};
    }
);