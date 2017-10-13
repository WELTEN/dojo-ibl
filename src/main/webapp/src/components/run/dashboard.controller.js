angular.module('DojoIBL')

    .controller('DashboardController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService, $location) {


        $scope.$parent.toggle = true;

        //$scope.dashboard = "";

        $scope.closeActivity = function() {
            $scope.$parent.toggle = false;
            $location.path('inquiry/'+$stateParams.runId);
        };

    }
);