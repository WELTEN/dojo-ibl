angular.module('DojoIBL')

    .directive('diblChat', function() {
        return  {
            restrict: 'A',
            templateUrl: '/dist/templates/directives/chat.html'
        };
    }

);