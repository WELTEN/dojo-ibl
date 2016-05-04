angular.module('DojoIBL', ['ngRoute', 'ngResource', 'infinite-scroll', 'angular-cache', 'pascalprecht.translate'])

    .config(function ($translateProvider) {

        $translateProvider.useStaticFilesLoader({
            files: [{
                prefix: '/ng/i18n/',
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