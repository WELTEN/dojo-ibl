angular.module('DojoIBL')

    .controller('ToolbarController', function ($scope, $state, $stateParams,RunService, ActivityService, Session, GameService, AccountService, config) {
        $scope.test = "foo";

        $scope.name = "your name";
        $scope.inquiryCode;

        $scope.state = $state;

        if ($stateParams.runId) {
            RunService.getRunById($stateParams.runId).then(function(data){
                $scope.run = data;

                if ($stateParams.phase) {


                    GameService.getGameById(data.gameId).then(function (data) {
                        $scope.namePhase = data.phases[$stateParams.phase];
                        //console.log(data.phases);
                    });

                    if ($stateParams.activityId) {

                        ActivityService.getActivityById($stateParams.activityId, data.gameId).then(function (data) {
                            $scope.activity = data;

                            //console.log(data);


                        });
                    }
                }

            });
        }

        $scope.findAndJoin = function(){

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    GameService.giveAccess(run.game.gameId, user.accountType+":"+user.localId,1);

                    RunService.giveAccess(run.runId, user.accountType+":"+user.localId,1);

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