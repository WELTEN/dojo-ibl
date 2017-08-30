angular.module('DojoIBL', ['ui.router', 'ngRoute', 'ngResource', 'angular-cache', 'ngDragDrop', 'localytics.directives',
    'summernote', 'ui.select', 'ngSanitize',  'infinite-scroll', 'textAngular', 'pascalprecht.translate', 'ngFileUpload',
    'ncy-angular-breadcrumb', 'angular-table', 'luegg.directives', 'ngEmoticons', 'vButton', 'ui.sortable', 'ngAudio', 'ui.bootstrap',
    'ui.codemirror', 'ngLetterAvatar', 'toaster', 'ngAnimate', 'ui.footable', 'ui.calendar', 'datePicker', 'firebase'])

    .config(function ($translateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise('/login');

        $translateProvider.useStaticFilesLoader({
            files: [{
                prefix: '/src/assets/i18n/',
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
    .run(function ($http, $location, $translate, Session) {
        var absUrl = $location.absUrl();

        if(localStorage.getItem('i18')){
            $translate.use(localStorage.getItem('i18'));
        }else{
            if(angular.isUndefined(window.navigator)){
                $translate.use('en-US');
            }else{
                if(angular.isUndefined(window.navigator.languages)){
                    $translate.use('en-US');
                }else {
                    var lang = window.navigator.languages[0] || window.navigator.userLanguage;

                    if (lang === 'bg') {
                        $translate.use(lang);
                    }
                    if (lang === 'en-US') {
                        $translate.use(lang);
                    }
                    if (lang === 'es') {
                        $translate.use(lang);
                    }
                    if (lang === 'nl') {
                        $translate.use(lang);
                    }
                }
            }
        }

        //if(localStorage.getItem('accessToken')){
        //    //if(location.host == "localhost:8080"){
        //    //    //if(/absUrl/.test("8080(\/)?(index.html)?"))
        //    //    if(location.protocol+"//"+location.host+"/" == absUrl ||
        //    //        location.protocol+"//"+location.host+"/index.html" == absUrl)
        //    //        window.location = location.protocol+"//"+location.host+"/main.html#/home";
        //    //}else{
        //    //    if("http://dojo-ibl.appspot.com/" == absUrl || "http://dojo-ibl.appspot.com/index.html" == absUrl
        //    //    || "https://dojo-ibl.appspot.com/" == absUrl || "https://dojo-ibl.appspot.com/index.html" == absUrl)
        //    //        window.location = "https://dojo-ibl.appspot.com/main.html#/home";
        //    //}
        //
        //    if(location.protocol+"//"+location.host+"/" == absUrl || location.protocol+"//"+location.host+"/index.html" == absUrl)
        //        window.location = location.protocol+"//"+location.host+"/main.html#/home";
        //
        //}else{
        //
        //    if(absUrl.indexOf(location.protocol+"//"+location.host+"/main.html#/oauth/") == -1 && absUrl.indexOf(location.protocol+"//"+location.host+"/main.html#/") !== -1)
        //        window.location = location.protocol+"//"+location.host+"/";
        //
        //    //if(location.host == "localhost:8080"){
        //    //    if(absUrl.indexOf("localhost:8080/main.html#/oauth/") == -1 && absUrl.indexOf("localhost:8080/main.html#/") !== -1  )
        //    //        window.location = "http://localhost:8080";
        //    //}else{
        //    //    if(absUrl.indexOf("dojo-ibl.appspot.com/main.html#/oauth/") == -1 && absUrl.indexOf("dojo-ibl.appspot.com/main.html#/") !== -1)
        //    //        window.location = "http://dojo-ibl.appspot.com";
        //    //}
        //}

        //$http.defaults.headers.common['Authorization'] = 'GoogleLogin auth='+localStorage.getItem('accessToken');
        $http.defaults.headers.common['Authorization'] = localStorage.getItem('accessToken');
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
    .filter('isEmpty', function () {
        var bar;
        return function (obj) {
            for (bar in obj) {
                if (obj.hasOwnProperty(bar)) {
                    return false;
                }
            }
            return true;
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
    }).run(run);

    run.$inject = ['$rootScope', '$location', '$window'];
    function run($rootScope, $location, $window) {
        // initialise google analytics
        $window.ga('create', 'UA-75878329-2', 'auto');

        // track pageview on state change
        $rootScope.$on('$stateChangeSuccess', function (event) {
            $window.ga('send', 'pageview', $location.path());
        });
    }
;