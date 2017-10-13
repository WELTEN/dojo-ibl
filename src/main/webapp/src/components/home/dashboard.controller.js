angular.module('DojoIBL')

    .controller('DashboardController', function ($scope, $rootScope, $modal, Session, $state, $stateParams, GameService, RunService, Account, AccountService) {
        $scope.editorOptions = {
            lineWrapping : true,
            lineNumbers: true,
            readOnly: 'nocursor',
            mode: 'json'
        };


    })
;