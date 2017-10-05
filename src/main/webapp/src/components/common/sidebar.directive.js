angular.module('DojoIBL')

    .directive('diblSidebar', function($state) {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/sidebar.directive.html',
            controller: 'SidebarController'
        };
    }

);