angular.module('DojoIBL')

    .directive('comments', function() {
        return  {
            restrict: 'EA',
            templateUrl: '/dist/templates/directives/comments.html'
        };
    })

;