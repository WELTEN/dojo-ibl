angular.module('DojoIBL')

    .controller('ToolbarController', function ($scope, $state, $stateParams,RunService, ActivityService, Session, GameService) {
        $scope.test = "foo";

        $scope.name = "your name";

        $scope.state = $state;

        if ($stateParams.runId) {
            RunService.getRunById($stateParams.runId).then(function(data){
                $scope.run = data;

                if ($stateParams.phase) {
                    //ActivityService.getActivitiesForPhase(data.game.gameId, $stateParams.phase).then(function (data) {
                    //    $scope.activities = data;
                    //    console.log(data);
                    //});
                }

            });
        }

        if (Session.getAccessToken() ) {
            //Account.accountDetails().$promise.then(
            //    function(data){
            //        $scope.pictureUrl = data.picture;
            //        $scope.name = data.name;
            //    }
            //);
        }
    }
);