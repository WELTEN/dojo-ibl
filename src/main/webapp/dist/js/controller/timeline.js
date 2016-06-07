angular.module('DojoIBL')

    .controller('TimelineController', function ($scope, $sce, $stateParams, $state, Response, ActivityService) {

        console.log($stateParams)
        $scope.games = {};
        $scope.games.games = [];

        $scope.disableGameLoading = false;

        $scope.loadMoreGames = function () {

            $scope.disableGameLoading = true;

            Response.resume({resumptionToken: $scope.games.resumptionToken, runId: $stateParams.runId, from: 0 })
                .$promise.then(function (data) {




                    var responses = [];

                    angular.forEach(data.responses, function(resp){
                        resp.activity = ActivityService.getItemFromCache(resp.generalItemId)
                        responses.push(resp);
                    });

                    $scope.games.games = $scope.games.games.concat(responses);

                    console.log(responses);

                    //for (i = 0; i < data.responses.length; i++) {
                    //    //GameService.storeInCache(data.games[i]);
                    //    data.responses[i].description = $sce.trustAsHtml(data.games[i].description);
                    //
                    //}
                    //$scope.games = GameService.getGames();
                    $scope.games.resumptionToken = data.resumptionToken;
                    $scope.games.serverTime = data.serverTime;

                    //console.log(data.resumptionToken, "HOLA");

                    //if (data.resumptionToken) {
                    if (data.resumptionToken) {
                        $scope.disableGameLoading = false
                    } else {
                        $scope.disableGameLoading = true
                    }
                });
        };
    }
);