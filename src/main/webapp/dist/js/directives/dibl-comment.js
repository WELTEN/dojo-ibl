angular.module('DojoIBL')

    .directive('comment', function() {
        return  {
            restrict: 'EA',
            templateUrl: '/dist/templates/directives/comment.html'
        };
    })

;