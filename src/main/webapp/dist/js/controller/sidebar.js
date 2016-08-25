angular.module('DojoIBL')

    .controller('SidebarController', function ($scope,$rootScope, Session, $state, $stateParams, RunService, Account, AccountService) {

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
    }
);