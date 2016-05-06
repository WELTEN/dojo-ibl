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
        $http.defaults.headers.common['Authorization'] = 'GoogleLogin auth=5f108bd43fbb44457b2a8862ac2df65';
        //$http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+localStorage.getItem('accessToken');
    }
);