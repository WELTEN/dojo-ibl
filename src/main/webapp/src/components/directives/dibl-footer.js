angular.module('DojoIBL')

    .directive('diblFooter', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/templates/pages/footer.html',
            controller: 'FooterController'
        };
    }

);