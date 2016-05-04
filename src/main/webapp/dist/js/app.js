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
        $http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+localStorage.getItem('accessToken');
    }
);