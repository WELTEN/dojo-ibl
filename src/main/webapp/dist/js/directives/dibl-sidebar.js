angular.module('DojoIBL')

    .directive('diblSidebar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/dist/templates/pages/sidebar.html',
            controller: 'SidebarController'
        };
    }

);