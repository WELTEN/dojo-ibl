angular.module('DojoIBL')

    .controller('DashboardController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService, $location, $firebaseArray, firebase) {


        $scope.$parent.toggle = true;

        var visualizationsRef = firebase.database().ref("visualizations").child($stateParams.runId);

        $scope.listwidgets = $firebaseArray(visualizationsRef);

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        }

        //$scope.listwidgets = $sce.trustAsResourceUrl("");

        //$scope.listwidgets = {}
        //
        //https://xapi-proxy.appspot.com/embed.html?queryId=5763059092029440&visualisationId=barchart
        //
        //
        //    https://xapi-proxy.appspot.com/embed.html?queryId=5643980486213632&visualisationId=barchart

        $scope.closeActivity = function() {
            $scope.$parent.toggle = false;
            $location.path('inquiry/'+$stateParams.runId);
        };

    }
);