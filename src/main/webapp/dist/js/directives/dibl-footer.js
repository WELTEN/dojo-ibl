angular.module('DojoIBL')

    .directive('diblFooter', function() {
        return  {
            restrict: 'E',
            templateUrl: '/dist/templates/pages/footer.html',
            controller: 'FooterController'
        };
    }

);