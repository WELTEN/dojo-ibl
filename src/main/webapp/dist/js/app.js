angular.module('DojoIBL', ['ngRoute', 'ngResource', 'angular-cache', 'pascalprecht.translate'])

    .config(function ($translateProvider) {

        $translateProvider.useStaticFilesLoader({
            files: [{
                prefix: '/dist/i18n/',
                suffix: '.json'
            }]
        });

        $translateProvider
            .preferredLanguage('en');
    })

    .run(function ($http) {
        $http.defaults.headers.common['Authorization'] = 'GoogleLogin auth=63f5b75a3fc7540cc561b4a2aa234fd';
        //$http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+localStorage.getItem('accessToken');
    }
);