angular.module('DojoIBL', ['ui.router', 'ui.comments', 'ngRoute', 'ngResource', 'angular-cache', 'ngDragDrop', 'localytics.directives',
    'summernote', 'ngSanitize', 'infinite-scroll', 'textAngular', 'pascalprecht.translate'])

    .config(function ($translateProvider) {

        $translateProvider.useStaticFilesLoader({
            files: [{
                prefix: '/dist/i18n/',
                suffix: '.json'
            }]
        });

        $translateProvider.preferredLanguage('en');
    })

    .run(function ($http) {
        //$http.defaults.headers.common['Authorization'] = 'GoogleLogin auth=5f108bd43fbb44457b2a8862ac2df65';
        $http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+localStorage.getItem('accessToken');
        // 22f99033db5925d66b524ff23346a5fd
        //console.log(localStorage.getItem('accessToken'));

    })
    .filter('unsafe', function($sce) {
        return $sce.trustAsHtml;
    });
;