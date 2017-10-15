angular.module('DojoIBL')

    .directive('actMultiActivity', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/activity/actmultiactivity.directive.html',
            controller: 'ActivityMultiactivityController',
            scope: {
                list: '='
            }
        };
    }

);