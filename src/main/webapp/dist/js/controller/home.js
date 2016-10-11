angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, ActivityService, config, Session, RunService, ChannelService, AccountService) {

        $scope.games = [];

        Game.access({ }).$promise.then(function (data) {

            angular.forEach(data.gamesAccess, function (gameAccess) {

                GameService.getGameById(gameAccess.gameId).then(function (data) {

                    var data_extended = angular.extend({}, data, gameAccess);

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

        $scope.cloneInquiry = function(id){
            GameService.getGameById(id).then(function (data) {
                var cloned_inquiry = {};
                cloned_inquiry.title = "Copy of "+data.title;
                cloned_inquiry.phases = data.phases;
                cloned_inquiry.description = data.description;
                GameService.newGame(cloned_inquiry).then(function(new_inq){
                    ActivityService.getActivities(data.gameId).then(function (activities) {
                        console.log(activities);

                        angular.forEach(activities, function(activity){
                            var object = {
                                type: activity.type,
                                section: activity.section,
                                gameId: new_inq.gameId,
                                deleted: activity.deleted,
                                name: activity.name,
                                description: activity.description,
                                autoLaunch: activity.autoLaunch,
                                fileReferences: activity.fileReferences,
                                sortKey: activity.sortKey,
                                richText: activity.richText
                            };

                            if(activity.type == "org.celstec.arlearn2.beans.generalItem.VideoObject"){
                                object.videoFeed = activity.videoFeed;
                            }else if(activity.type == "org.celstec.arlearn2.beans.generalItem.AudioObject") {
                                object.audioFeed = activity.audioFeed;
                            }

                            ActivityService.newActivity(object);

                        });
                    });
                });
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

        ChannelService.register('org.celstec.arlearn2.beans.game.Game', function (notification) {
            GameService.refreshGame(notification.gameId).then(function (data) {
            });
        });

        //AccountService.myDetails().then(function(data){
        //    var socket = new ChannelService.SocketHandler(data);
        //    socket.onMessage(function (data) {
        //        $scope.$apply(function () {
        //            console.log(data)
        //            switch (data.type) {
        //                case 'org.celstec.arlearn2.beans.game.Game':
        //                    Game.access({ }).$promise.then(function (gameAccesses) {
        //
        //                        angular.forEach(gameAccesses.gamesAccess, function (gameAccess) {
        //                            if(data.gameId == gameAccess.gameId){
        //                                GameService.refreshGame(data.gameId).then(function (data) {
        //                                    var idx = arrayObjectIndexOf($scope.games, data.gameId, "gameId");
        //
        //                                    // is currently selected
        //                                    if (idx == -1) {
        //
        //                                        var data_extended = angular.extend({}, data, gameAccess);
        //                                        $scope.games.push(data_extended)
        //                                    }
        //                                    console.info("[Notification][Game]");
        //                                });
        //                            }
        //                        });
        //                    });
        //
        //                    break;
        //            }
        //        });
        //    });
        //});

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    }
);