angular.module('DojoIBL')

    .controller('InquiryEditRunController', function ($scope, $sce, $stateParams, $state, Session, RunService, UserService, Contacts ) {

        console.log($stateParams.runId);

        $scope.roles = [];

        RunService.getRunById($stateParams.runId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.game = data.game;

            console.log($scope.game);

            if(!$scope.game.config.roles)
                $scope.game.config.roles = [];

            $scope.run = data;

            if (data.lat) {
                $scope.coords.latitude = data.lat;
                $scope.coords.longitude = data.lng;
                $scope.map.center.latitude = data.lat;
                $scope.map.center.longitude = data.lng;
                $scope.showMap = true;
            }
        });

        UserService.getUsersForRun($stateParams.runId).then(function(data){

            //$scope.usersRun[value.runId] = data;
            $scope.usersRun = data;
            console.log(data);

        });

        $scope.ok = function(){
            RunService.newRun($scope.run);
            //$window.history.back();
        };

        $scope.friends = [];

        Contacts.getContacts({date:0}).$promise.then(
            function(data){
                for(var i=0;i<data.accountList.length;i++){
                    $scope.friends.push(data.accountList[i]);
                }
            }
        );
    });