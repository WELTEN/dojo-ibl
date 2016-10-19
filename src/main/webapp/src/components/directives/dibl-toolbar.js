angular.module('DojoIBL')

    .directive('diblToolbar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/templates/pages/toolbar.html',
            controller: 'ToolbarController'
        };
    }

);