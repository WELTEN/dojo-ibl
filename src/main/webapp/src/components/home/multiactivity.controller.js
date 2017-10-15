angular.module('DojoIBL')

    .controller('MultiactivityController', function ($scope, ActivityService) {
        $scope.activities = ActivityService.getActivities();

        $scope.list = [];

        angular.forEach($scope.activities[$scope.activity.gameId][$scope.activity.section], function(_b){
            $scope.list.push(_b)
        });


    }
);