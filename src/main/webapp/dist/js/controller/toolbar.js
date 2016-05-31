angular.module('DojoIBL')

    .controller('ToolbarController', function ($scope, $state, $stateParams,RunService, ActivityService, Session, GameService) {
        $scope.test = "foo";

        $scope.name = "your name";
        $scope.inquiryCode;

        $scope.state = $state;

        if ($stateParams.runId) {
            RunService.getRunById($stateParams.runId).then(function(data){
                $scope.run = data;

                if ($stateParams.phase) {


                    GameService.getGameById(data.gameId).then(function (data) {
                        $scope.namePhase = data.phases[$stateParams.phase];
                        //console.log(data.phases);
                    });

                    if ($stateParams.activityId) {

                        ActivityService.getActivityById($stateParams.activityId, data.gameId).then(function (data) {
                            $scope.activity = data;

                            //console.log(data);


                        });
                    }
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


        $scope.findAndJoin = function(){
            console.log($scope.inquiryCode);
            $scope.inquiryCode = null;
        }

    }
);