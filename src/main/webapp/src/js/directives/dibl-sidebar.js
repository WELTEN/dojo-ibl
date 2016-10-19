angular.module('DojoIBL')

    .directive('diblSidebar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/templates/pages/sidebar.html',
            controller: 'SidebarController'
        };
    }

);