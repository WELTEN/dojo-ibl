angular.module('DojoIBL')

    .directive('chat', function() {
        return  {
            restrict: 'A',
            link: function (scope, element, attrs, controller) {
                scope.$watch(
                    function () { return element[0].childNodes.length; },
                    function (newValue, oldValue) {
                        if (newValue !== oldValue) {
                            element.scrollTop(element[0].scrollHeight);
                        }
                    });
            }
        };
    }

);