angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, ActivityService, config, Session, RunService, ChannelService, AccountService) {

        $scope.games = {};
        $scope.runs = {};

        function loadGames() {
            GameService.resumeLoadingGames().then(function (data) {
                if (data.error) {
                    $scope.showNoAccess = true;
                } else {
                    $scope.show = true;
                    if (data.resumptionToken) {
                        loadGames();
                    }
                }
            });
        }

        //if(isEmpty($scope.games)){
        loadGames();
        //}

        $scope.games = GameService.getGames();

        $scope.thumbnailUrl = function(gameId) {

            return config.server+'/game/'+gameId+'/gameThumbnail';
        };

        $scope.showRunsValue = true;

        $scope.showRuns = function (id) {

            RunService.getParticipateRunsForGame(id).then(function(data){
                console.log(data);
                $scope.runs[id] = {};
                $scope.runs[id] = data;
            });
        };

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

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
            //console.info("[Notification][Game]", notification);

            if(!angular.isUndefined(notification.gameId)){
                GameService.refreshGame(notification.gameId);
            }
        });

        $scope.findAndJoin = function(){

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    //////
                    // AccessRight explanation
                    // 1: Editor
                    // 2: User

                    console.log(user);

                    GameService.giveAccess(run.game.gameId, user.accountType+":"+user.localId,2);

                    RunService.giveAccess(run.runId, user.accountType+":"+user.localId,2);

                    RunService.addUserToRun({
                        runId: run.runId,
                        email: user.accountType+":"+user.localId,
                        accountType: user.accountType,
                        localId: user.localId,
                        gameId: run.game.gameId });

                    window.location.href=config.server+'/main.html#/inquiry/'+run.runId;
                });
            });

            $scope.inquiryCode = null;
        }

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    }
);