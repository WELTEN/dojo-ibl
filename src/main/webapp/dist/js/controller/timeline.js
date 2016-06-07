angular.module('DojoIBL')

    .controller('TimelineController', function ($scope, $sce, $stateParams, $state, Response, ActivityService, UserService) {

        console.log($stateParams)
        $scope.games = {};
        $scope.games.games = [];

        $scope.disableGameLoading = false;

        var old_item_id = 0;
        var old_side_left = true;

        $scope.loadMoreGames = function () {

            $scope.disableGameLoading = true;

            Response.resume({resumptionToken: $scope.games.resumptionToken, runId: $stateParams.runId, from: 0 })
                .$promise.then(function (data) {

                    var responses = [];

                    angular.forEach(data.responses, function(resp){

                        if(resp.generalItemId != old_item_id){
                            if(old_side_left){
                                resp.move_right = false;
                            }else{
                                resp.move_right = true;
                            }
                        }

                        old_item_id = resp.generalItemId;
                        old_side_left = resp.move_right;

                        resp.activity = ActivityService.getItemFromCache(resp.generalItemId)
                        resp.user = UserService.getUserFromCache(resp.userEmail.split(':')[1]);

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
    }
);