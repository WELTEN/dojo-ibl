angular.module('DojoIBL')

    .directive('diblFooter', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/footer.directive.html',
            controller: 'FooterController'
        };
    }

);