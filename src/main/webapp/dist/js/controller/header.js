angular.module('DojoIBL')

    .controller('HeaderController', function ($scope, Session) {
        $scope.test = "foo";

        $scope.name = "your name";

        if (Session.getAccessToken() ) {
            //Account.accountDetails().$promise.then(
            //    function(data){
            //        $scope.pictureUrl = data.picture;
            //        $scope.name = data.name;
            //    }
            //);
        }
    }
);