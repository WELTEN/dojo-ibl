angular.module('DojoIBL')

    .controller('SidebarController', function ($scope, Session, $state, $stateParams, RunService, Account, AccountService) {

        //if (Session.getAccessToken() ) {
        //AccountService.myDetails().then(
        //    function(data){
        //        $scope.myAccount = data;
        //        console.log(data)
        //        //loadAccessRules();
        //    }
        //);
        //}

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