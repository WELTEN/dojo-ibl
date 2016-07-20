angular.module('DojoIBL', ['ui.router', 'ngRoute', 'ngResource', 'angular-cache', 'ngDragDrop', 'localytics.directives',
    'summernote', 'ngSanitize', 'ui.select', 'infinite-scroll', 'textAngular', 'pascalprecht.translate', 'ngFileUpload',
    'ncy-angular-breadcrumb', 'angular-table', 'luegg.directives', 'ngEmoticons', 'vButton', 'ui.sortable'])

    .config(function ($translateProvider) {

        $translateProvider.useStaticFilesLoader({
            files: [{
                prefix: '/dist/i18n/',
                suffix: '.json'
            }]
        });

        $translateProvider.preferredLanguage('en');
    })
    .config(function($breadcrumbProvider) {
        $breadcrumbProvider.setOptions({
            prefixStateName: 'home',
            template: 'bootstrap2'
        });
    })
    .run(function ($http) {
        //$http.defaults.headers.common['Authorization'] = 'GoogleLogin auth=5f108bd43fbb44457b2a8862ac2df65';
        $http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+localStorage.getItem('accessToken');
        // 22f99033db5925d66b524ff23346a5fd
        //console.log(localStorage.getItem('accessToken'));

    })
    .filter('unsafe', function($sce) {
        return $sce.trustAsHtml;
    })
    .filter('orderByDayNumber', function() {
        return function(items, field, reverse) {
            var filtered = [];
            angular.forEach(items, function(item) {
                filtered.push(item);
            });
            filtered.sort(function (a, b) {
                return (a[field] > b[field] ? 1 : -1);
            });
            if(reverse) filtered.reverse();
            return filtered;
        };
    })
    .filter('timeago', function(){
        return function(date){
            return moment(date).fromNow();
        };
    }).directive('timeago', function() {
        return {
            restrict:'A',
            link: function(scope, element, attrs){
                attrs.$observe("timeago", function(){
                    element.text(moment(attrs.timeago).fromNow());
                });
            }
        };
    });
;