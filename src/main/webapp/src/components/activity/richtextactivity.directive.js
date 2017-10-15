angular.module('DojoIBL')

    .directive('textActivity', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/activity/richtextactivity.directive.html',
            controller: 'TextActivityController',
            scope: {
                activity: '='
            }
        };
    }

);