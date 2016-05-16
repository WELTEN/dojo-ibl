angular.module('DojoIBL')

    .controller('ActivityController', function ($scope, $sce, $stateParams, Session, ActivityService) {
        $scope.activity = ActivityService.getItemFromCache($stateParams.activityId);
        console.log($scope.activity);
    }
);