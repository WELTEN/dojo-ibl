angular.module('DojoIBL')

    .controller('ProfileController', function ($scope, $sce, $stateParams, $state, Session, AccountService) {
        AccountService.myDetails().then(function(data){
            $scope.user = data;
            console.log(data);
        });
    }
);