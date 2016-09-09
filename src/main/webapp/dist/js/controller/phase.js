angular.module('DojoIBL')

    .controller('PhaseController', function ($scope, $sce, $stateParams, $state, Session, ActivityService, RunService) {
        $scope.runId = $stateParams.runId;

        RunService.getRunById($stateParams.runId).then(function(data){

            $scope.phase = data.game.phases[$stateParams.phase];

            ActivityService.getActivitiesForPhase(data.game.gameId, $stateParams.phase).then(function (data) {
                $scope.activities = data;

                console.log(data);
            });
        });


        $scope.getRole = function(roles){

            return jQuery.parseJSON(roles[0]).color;


        };

    }
);