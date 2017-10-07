angular.module('DojoIBL')

    .directive('listActivity', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/activity/listactivity.directive.html',
            controller: 'ListActivityController',
            scope: {
                activity: '='
            }
        };
    }

);