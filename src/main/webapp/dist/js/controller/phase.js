angular.module('DojoIBL')

    .controller('PhaseController', function ($scope, $sce, $stateParams, $state, Session, ActivityService, RunService) {

        console.log($stateParams)

        $scope.runId = $stateParams.runId;

        RunService.getRunById($stateParams.runId).then(function(data){

            $scope.phase = data.game.phases[$stateParams.phase];

            console.log(data);

            ActivityService.getActivitiesForPhase(data.game.gameId, $stateParams.phase).then(function (data) {
                $scope.activities = data;
            });

        });


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