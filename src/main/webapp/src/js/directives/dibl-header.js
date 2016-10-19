angular.module('DojoIBL')

    .directive('diblHeader', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/templates/pages/header.html',
            controller: 'HeaderController'
        };
    }

);