angular.module('DojoIBL')

    .controller('PhaseController', function ($scope, $sce, $stateParams, $state, Session, ActivityService, RunService) {
        $scope.runId = $stateParams.runId;

        RunService.getRunById($stateParams.runId).then(function(data){

            $scope.phase = data.game.phases[$stateParams.phase];

            ActivityService.getActivitiesForPhase(data.game.gameId, $stateParams.phase).then(function (data) {
                $scope.activities = data;
            });
        });

        $scope.sortableOptions = {
            connectWith: ".connectList"
        };

        $scope.getRoleName = function(roles){
            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(angular.fromJson(roles[0]))) {
                        if (!angular.isObject(roles[0])) {
                            return angular.fromJson(roles[0]).name;
                        }
                    }
                }catch(e){
                    return "-";
                }
            }
            return "-";
        };


        $scope.getRoleColor = function(roles){
            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(angular.fromJson(roles[0]))) {
                        if (!angular.isObject(roles[0])) {
                            return {
                                "border-left": "3px solid "+angular.fromJson(roles[0]).color
                            };
                        }
                    }
                }catch(e){
                    return {
                        "border-left": "3px solid #f8ac59"
                    };
                }
            }
            return {
                "border-left": "3px solid #f8ac59"
            };
        };
    }
);