angular.module('DojoIBL')

    .controller('ToolbarController', function ($scope, toaster, $rootScope, $state, $location, $stateParams,RunService, ActivityService, Session, Game, GameService, AccountService, config, ChannelService, UserService) {
        $scope.test = "foo";

        $scope.name = "your name";
        $scope.inquiryCode;


        $scope.showSpinerJoin = false;

        $scope.leftDisabled = false;

        $scope.state = $state;

        if ($stateParams.runId) {
            $scope.$on('inquiry-run', function(event, args) {
                RunService.getRunById(args.runId).then(function(run) {
                    $scope.run = run;
                });
            });

            RunService.getRunById($stateParams.runId).then(function(run){
                $scope.run = run;

                Game.access({ }).$promise.then(function (data) {

                    angular.forEach(data.gamesAccess, function (gameAccess) {

                        if(run.gameId == gameAccess.gameId){
                            GameService.getGameById(gameAccess.gameId).then(function (aux) {

                                var data_extended = angular.extend({}, aux, gameAccess);

                                $scope.game = data_extended;

                                //console.log(data_extended);

                                if ($stateParams.phase) {
                                    $scope.namePhase = aux.phases[$stateParams.phase];
                                }
                            });
                        }
                    });
                });

                RunService.getParticipateRunsForGame(run.gameId).then(function(data){
                    var aux = []
                    $scope.itemArray = aux;
                    angular.forEach(data, function(_b){
                        aux.push(_b)
                    });
                    $scope.selected = { value: $scope.itemArray[0] };
                });

                $scope.selectChange = function(run) {

                    $rootScope.$broadcast('inquiry-run', { 'runId':run.runId });

                    switch($state.current.name){
                        case "inquiry.home":
                            $state.go('inquiry.home', { 'runId':run.runId });
                            break;
                        case "inquiry.timeline":
                            $state.go('inquiry.timeline', { 'runId':run.runId });
                            break;
                        case "inquiry.phase":
                            $state.go('inquiry.phase', { 'runId':run.runId, phase: $state.params.phase });
                            break;
                        case "inquiry.activity":
                            $state.go('inquiry.activity', { 'runId':run.runId, phase: $state.params.phase, activityId: $state.params.activityId  });
                            break;
                    }
                };

                if ($stateParams.activityId) {

                    ActivityService.getActivityById($stateParams.activityId, run.gameId).then(function (data) {
                        $scope.activity = data;
                    });
                }

                $scope.goLeft = function(){
                    switch($state.current.name){
                        case "inquiry.home":

                            break;
                        case "inquiry.phase":

                            if($state.params.phase > 0){
                                var prevPhase = $state.params.phase - 1;
                                $state.go('inquiry.phase', { 'runId':run.runId, phase: prevPhase });
                            }
                            break;
                        case "inquiry.activity":
                            var prevActivity = ActivityService.getPrevActivity($stateParams.activityId, run.gameId, $state.params.phase);
                            if(!angular.isUndefined(prevActivity)){
                                $state.go('inquiry.activity', { 'runId':run.runId, phase: $state.params.phase, activityId: prevActivity.id  });
                            }
                            break;
                    }
                };

                $scope.goUp = function(){
                    switch($state.current.name){
                        case "inquiry.home":
                            $state.go('home', { });
                            break;
                        case "inquiry.phase":
                            $state.go('inquiry.home', { 'runId':run.runId });
                            break;
                        case "inquiry.activity":
                            $state.go('inquiry.phase', { 'runId':run.runId, phase: $state.params.phase });
                            break;
                    }
                };

                $scope.goRight = function(){
                    switch($state.current.name){
                        case "inquiry.home":

                            break;
                        case "inquiry.phase":
                            if($state.params.phase < ActivityService.getLengthPhase($stateParams.activityId, run.gameId) - 1){
                                var prevPhase = parseInt($state.params.phase) + 1;
                                $state.go('inquiry.phase', { 'runId':run.runId, phase: prevPhase });
                            }
                            break;
                        case "inquiry.activity":
                            var nextActivity = ActivityService.getNextActivity($stateParams.activityId, run.gameId, $state.params.phase);
                            if(!angular.isUndefined(nextActivity)){
                                $state.go('inquiry.activity', { 'runId':run.runId, phase: $state.params.phase, activityId: nextActivity.id  });
                            }
                            break;
                    }
                };



            });


        }

        $scope.findAndJoin = function(){
            $scope.showSpinerJoin = true;

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    GameService.giveAccess(run.game.gameId, user.accountType+":"+user.localId,2);

                    RunService.giveAccess(run.runId, user.accountType+":"+user.localId,2);

                    RunService.addUserToRun({
                        runId: run.runId,
                        email: user.accountType+":"+user.localId,
                        accountType: user.accountType,
                        localId: user.localId,
                        gameId: run.game.gameId }).then(function(){

                        window.location.href=config.server+'/#/inquiry/'+run.runId;

                        $scope.showSpinerJoin = false;
                        $scope.inquiryCode = null;
                    });


                });
            }, function(fallback) {

                $scope.showSpinerJoin = false;

                toaster.error({
                    title: 'Invalid code ',
                    body: 'Please provide a valid inquiry code in capital letters.'
                });
            });


        };

        //ChannelService.register('org.celstec.arlearn2.beans.run.User', function (notification) {
        //    //console.info("[Notification][User]", notification);
        //    UserService.getUserByAccount(notification.runId, notification.accountType+":"+notification.localId);
        //});
    }
);