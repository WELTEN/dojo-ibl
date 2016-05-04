angular.module('DojoIBL')
    .config(['$routeProvider', function ($routeProvider) {

        $routeProvider
            .when('/landing', {
                //templateUrl: '/dist/templates/landing.html',
                controller: 'LandingController'
            })
            .when('/home', {
                templateUrl: '/dist/templates/home.html',
                controller: 'HomeController'
            })
            .otherwise({redirectTo: '/home'});

    }]);