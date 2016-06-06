angular.module('DojoIBL')

    .controller('ProfilesController', function ($scope, $sce, $stateParams, $state, Session, AccountService) {
        AccountService.myDetails().then(function(data){
            $scope.user = data;
        });
    }
);