angular.module('DojoIBL')

    .directive('diblHeader', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/header.directive.html',
            controller: 'HeaderController'
        };
    }

);