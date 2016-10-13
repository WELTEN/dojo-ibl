angular.module('DojoIBL')

    .controller('TimelineController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService) {

        $scope.games = {};
        $scope.games.games = [];

        $scope.disableGameLoading = false;

        var old_item_id = 0;
        var old_side_left = true;

        ChannelService.register('org.celstec.arlearn2.beans.run.Response', function (data) {
            if(data.runId == $stateParams.runId){
                var responses = [];

                if(data.generalItemId != old_item_id){
                    if(old_side_left){
                        data.move_right = false;
                    }else{
                        data.move_right = true;
                    }
                }

                old_item_id = data.generalItemId;
                old_side_left = data.move_right;

                RunService.getRunById($stateParams.runId).then(function (run) {
                    ActivityService.getActivityById(data.generalItemId, run.gameId).then(function(act){
                        data.activity = act;
                    });
                });

                //UserService.getUserByAccount($stateParams.runId, data.userEmail.split(':')[1]).then(function(user){
                //    data.user = user;
                //});
                data.user = UserService.getUser(data.userEmail);


                responses.push(data);
                $scope.games.games = responses.concat($scope.games.games)
                //$scope.responses.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);
                //if((user.localId != data.userEmail.split(':')[1]) && data.generalItemId == $stateParams.activityId && data.runId == $stateParams.runId)
                //    //ResponseService.addResponse(data, $stateParams.runId, $stateParams.activityId);
                //console.info("[Notification][Response]", data, $stateParams.runId, $stateParams.activityId);
            }
        });


        $scope.loadMoreGames = function () {

            $scope.disableGameLoading = true;

            Response.resume({resumptionToken: $scope.games.resumptionToken, runId: $stateParams.runId, from: 0 })
                .$promise.then(function (data) {

                    var responses = [];

                    angular.forEach(data.responses, function(resp){

                        // TODO check this is not working well
                        if(resp.generalItemId != old_item_id){
                            if(old_side_left){
                                resp.move_right = false;
                            }else{
                                resp.move_right = true;
                            }
                        }

                        old_item_id = resp.generalItemId;
                        old_side_left = resp.move_right;

                        RunService.getRunById($stateParams.runId).then(function (run) {
                            ActivityService.getActivityById(resp.generalItemId, run.gameId).then(function(data){
                                resp.activity = data;
                            });
                        });

                        //UserService.getUserByAccount($stateParams.runId, resp.userEmail.split(':')[1]).then(function(data){
                        //    resp.user = data;
                        //});

                        resp.user = UserService.getUser(resp.userEmail);

                        responses.push(resp);
                    });

                    $scope.games.games = $scope.games.games.concat(responses);
                    $scope.games.resumptionToken = data.resumptionToken;
                    $scope.games.serverTime = data.serverTime;

                    if (data.resumptionToken) {
                        $scope.disableGameLoading = false
                    } else {
                        $scope.disableGameLoading = true
                    }
                });
        };


        UserService.getUsersForRun($stateParams.runId).then(function(data){

            $scope.usersRun = data;


        });

        $scope.filter = {};

        // Functions - Definitions
        $scope.filterByCategory = function(response) {
            return $scope.filter[response.user.localId] || noFilter($scope.filter);
        };

        function noFilter(filterObj) {
            return Object.
                keys(filterObj).
                every(function (key) { return !filterObj[key]; });
        }

    }
);