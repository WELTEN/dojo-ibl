angular.module('DojoIBL')

    .directive('diblPinned', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/pinned.directive.html',
            controller: 'PinnedController'
        };
    }

);