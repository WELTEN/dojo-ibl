angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, config, Session, RunService, ChannelService, AccountService) {

        $scope.games = [];

        Game.access({ }).$promise.then(function (data) {

            angular.forEach(data.gamesAccess, function (gameAccess) {

                GameService.getGameById(gameAccess.gameId).then(function (data) {

                    var data_extended = angular.extend({}, data, gameAccess);

                    console.log(data.description);

                    //data.description = $sce.trustAsHtml(data.description);
                    //
                    //$scope.des = data.description
                    //$scope.deliberatelyTrustDangerousSnippet = function() {
                    //    return $sce.trustAsHtml($scope.des);
                    //};

                    $scope.games = $scope.games.concat(data_extended);

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

            swal({
                title: "Are you sure?",
                text: "You will not be able to recover this inquiry!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                closeOnConfirm: false
            }, function () {
                swal("Deleted!", "Your inquiry has been deleted.", "success");

                var idx = arrayObjectIndexOf($scope.games, GameService.getGameFromCache(id).gameId, "gameId");

                // is currently selected
                if (idx > -1) {
                    $scope.games.splice(idx, 1);
                }

                GameService.deleteGame(id);

            });

        };

        $scope.config = {
            itemsPerPage: 5,
            fillLastPage: true
        };

        $scope.isLoggedIn = function () {
            if (Session.getAccessToken() ) return true;
            return false;
        };

        AccountService.myDetails().then(function(data){
            var socket = new ChannelService.SocketHandler(data);
            socket.onMessage(function (data) {
                $scope.$apply(function () {
                    console.log(data)
                    switch (data.type) {
                        case 'org.celstec.arlearn2.beans.game.Game':
                            Game.access({ }).$promise.then(function (gameAccesses) {

                                angular.forEach(gameAccesses.gamesAccess, function (gameAccess) {
                                    if(data.gameId == gameAccess.gameId){
                                        GameService.refreshGame(data.gameId).then(function (data) {
                                            var idx = arrayObjectIndexOf($scope.games, data.gameId, "gameId");

                                            // is currently selected
                                            if (idx == -1) {

                                                var data_extended = angular.extend({}, data, gameAccess);
                                                $scope.games.push(data_extended)
                                            }
                                            console.info("[Notification][Game]");
                                        });
                                    }
                                });
                            });

                            break;
                    }
                });
            });
        });

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    }
);