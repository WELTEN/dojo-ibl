angular.module('DojoIBL')

    .directive('comment', function() {
        return  {
            restrict: 'EA',
            templateUrl: '/src/templates/directives/response.html'
        };
    })

;