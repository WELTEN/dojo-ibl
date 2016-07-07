angular.module('DojoIBL')

    .directive('diblHeader', function() {
        return  {
            restrict: 'E',
            templateUrl: '/dist/templates/pages/header.html',
            controller: 'HeaderController'
        };
    }

);