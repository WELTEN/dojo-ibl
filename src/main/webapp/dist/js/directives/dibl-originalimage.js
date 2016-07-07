angular.module('DojoIBL')
    .directive('original', function() {
        return {
            restrict: 'A',
            scope: { hires: '@' },
            link: function(scope, element, attrs) {
                element.one('load', function() {
                    element.attr('ng-src', scope.src);
                });
            }
        };
    });