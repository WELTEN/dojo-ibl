angular.module('DojoIBL')

    .directive('diblToolbar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/toolbar.directive.html',
            controller: 'ToolbarController'
        };
    }

);