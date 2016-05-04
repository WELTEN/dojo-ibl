angular.module('DojoIBL')
    .config(['$routeProvider', function ($routeProvider) {

        $routeProvider
            .when('/home', {
                templateUrl: '/dist/templates/home.html',
                controller: 'HomeController'
            })
            .otherwise({redirectTo: '/home'});

    }]);