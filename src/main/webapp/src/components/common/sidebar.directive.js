angular.module('DojoIBL')

    .directive('diblSidebar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/sidebar.directive.html',
            controller: 'SidebarController'
        };
    }

);