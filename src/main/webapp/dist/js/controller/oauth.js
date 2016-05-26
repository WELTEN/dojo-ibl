angular.module('DojoIBL')

    .controller('OauthController', function ($scope, $stateParams, Session, $location) {
        $scope.token = $stateParams.accessToken;
        $scope.type = $stateParams.type;
        $scope.expires = $stateParams.expires;
        Session.setAccessToken($stateParams.accessToken);

        $location.path('/home');

    }
);