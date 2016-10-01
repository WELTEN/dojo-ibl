angular.module('DojoIBL')

    .controller('SidebarController', function ($scope, $rootScope, $modal, Session, $state, $stateParams, GameService, RunService, Account, AccountService) {

        $scope.$on("$stateChangeSuccess", function updatePage() {
            $scope.phaseNumber = $state.params.phase;
            $scope.statePhase = $state.current.name;
            console.log($state.current.name);
        });

        if ($stateParams.runId) {

            RunService.getRunById($stateParams.runId).then(function(data){
                $scope.run = data;
                AccountService.myDetails().then(
                    function(data){
                        $scope.myAccount = data;
                        //console.log(data)
                        //loadAccessRules();
                    }
                );

            });
        }else{
            AccountService.myDetails().then(
                function(data){
                    $scope.myAccount = data;
                    //console.log(data)
                    //loadAccessRules();
                }
            );
        }

        $scope.createNewInquiry = function () {

            var modalInstance = $modal.open({
                templateUrl: '/dist/templates/directives/modal_new_inquiry.html',
                controller: 'NewInqCtrl'
            });

            //modalInstance.result.then(function (result){
            //    angular.extend($scope.usersRun[$scope.runVar], result);
            //});
        };
    })
        .controller('NewInqCtrl', function ($scope, $modalInstance, GameService) {

            $scope.ok = function () {
                $scope.game.deleted = false;
                $scope.game.revoked = false;

                GameService.newGame($scope.game).then(function(data){
                    $scope.game = data;
                    $scope.game.config.roles = [];
                    $scope.phases = $scope.game.phases;
                });
                $modalInstance.close();
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        })
;