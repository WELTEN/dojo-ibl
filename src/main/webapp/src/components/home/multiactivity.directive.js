angular.module('DojoIBL')

    .directive('multiActivity', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/home/multiactivity.directive.html',
            controller: 'MultiactivityController',
            scope: {
                activity: '='
            }
        };
    }

);