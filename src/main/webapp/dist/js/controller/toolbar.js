angular.module('DojoIBL')

    .controller('ToolbarController', function ($scope, $state, $stateParams,RunService, ActivityService, Session, Game, GameService, AccountService, config) {
        $scope.test = "foo";

        $scope.name = "your name";
        $scope.inquiryCode;

        $scope.state = $state;

        if ($stateParams.runId) {
            RunService.getRunById($stateParams.runId).then(function(run){
                $scope.run = run;

                console.log(run);

                Game.access({ }).$promise.then(function (data) {

                    angular.forEach(data.gamesAccess, function (gameAccess) {

                        if(run.gameId == gameAccess.gameId){
                            GameService.getGameById(gameAccess.gameId).then(function (aux) {


                                var data_extended = angular.extend({}, aux, gameAccess);

                                $scope.game = data_extended;

                                console.log(data_extended);

                                if ($stateParams.phase) {
                                    $scope.namePhase = aux.phases[$stateParams.phase];
                                }
                            });
                        }
                    });
                });



                if ($stateParams.activityId) {

                    ActivityService.getActivityById($stateParams.activityId, data.gameId).then(function (data) {
                        $scope.activity = data;
                    });
                }

            });
        }

        $scope.findAndJoin = function(){

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    //////
                    // AccessRight explanation
                    // 1: Editor
                    // 2: User

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

    }
);