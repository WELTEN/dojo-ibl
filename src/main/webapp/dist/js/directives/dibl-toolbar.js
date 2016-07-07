angular.module('DojoIBL')

    .directive('diblToolbar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/dist/templates/pages/toolbar.html',
            controller: 'ToolbarController'
        };
    }

);