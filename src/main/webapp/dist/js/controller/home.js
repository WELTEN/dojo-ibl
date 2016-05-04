angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, config, Session) {

        Game.access({ }).$promise.then(function (data) {

            //console.log(data.gamesAccess);

            angular.forEach(data.gamesAccess, function (gameAccess) {

                GameService.getGameById(gameAccess.gameId)
                    .then(function (data) {

                        $scope.games.games = $scope.games.games.concat(data);
                    });
            });
        });

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