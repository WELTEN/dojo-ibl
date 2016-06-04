angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, config, Session, RunService, ChannelService) {

        $scope.games = [];

        Game.access({ }).$promise.then(function (data) {

            angular.forEach(data.gamesAccess, function (gameAccess) {

                GameService.getGameById(gameAccess.gameId).then(function (data) {
                        $scope.games = $scope.games.concat(data);
                        //for (i = 0; i < data.length; i++) {
                        //    //GameService.storeInCache(data.games[i]);
                        //    data[i].description = $sce.trustAsHtml(data[i].description);
                        //
                        //}
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
        };

        $scope.deleteInquiry = function (id) {
            GameService.deleteGame(id)
        };

        $scope.isLoggedIn = function () {
            if (Session.getAccessToken() ) return true;
            return false;
        };

        //var socket = new ChannelService.SocketHandler();
        //socket.onMessage(function (data) {
        //    $scope.$apply(function () {
        //        console.log(data);
        //        console.log(data.type);
        //        $scope.notifications.push({
        //            sort: new Date(),
        //            time: new Date().toISOString(),
        //            json: JSON.stringify(data, undefined, 2)
        //        });
        //        switch (data.type) {
        //            //case 'org.celstec.arlearn2.beans.notification.GeneralItemModification':
        //            //    //GeneralItemService.handleNotification(data);
        //            //    console.log("Received ")
        //            //    break;
        //        }
        //
        //    });
        //    //jQuery("time.timeago").timeago();
        //});
        //
        //$scope.notifications = [];
        //$scope.waitingForData = function () {
        //    $scope.notifications.length == 0;
        //}
    }
);