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

        var config = {
            apiKey: "AIzaSyBqdNjX3HNZStyuBklf3FQRGxRLTAbb2hc",
            authDomain: "dojo-ibl.firebaseapp.com",
            databaseURL: "https://dojo-ibl.firebaseio.com",
            projectId: "dojo-ibl",
            storageBucket: "dojo-ibl.appspot.com",
            messagingSenderId: "518897628174"
        };

        if (!firebase.apps.length) {
            firebase.initializeApp(config);
        }

        const messaging = firebase.messaging();

        messaging.requestPermission()
            .then(function() {
                console.log('Notification permission granted.');
                // TODO(developer): Retrieve an Instance ID token for use with FCM.
                // ...
            })
            .catch(function(err) {
                console.log('Unable to get permission to notify.', err);
            });




        // Get Instance ID token. Initially this makes a network call, once retrieved
        // subsequent calls to getToken will return from cache.
        messaging.getToken()
            .then(function(currentToken) {

                if (currentToken) {

                    console.log(currentToken)

                    //ChannelService.saveDeviceRegistrationTokenMessaging(currentToken);
                    localStorage.setItem('deviceRegistrationToken', currentToken)

                    //sendTokenToServer(currentToken);
                    //updateUIForPushEnabled(currentToken);
                } else {
                    // Show permission request.
                    console.log('No Instance ID token available. Request permission to generate one.');
                    // Show permission UI.
                    //updateUIForPushPermissionRequired();
                    //setTokenSentToServer(false);
                }
            })
            .catch(function(err) {
                console.log('An error occurred while retrieving token. ', err);
                //showToken('Error retrieving Instance ID token. ', err);
                //setTokenSentToServer(false);
            });
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

    run.$inject = ['$rootScope', '$location', '$window', 'Session'];
    function run($rootScope, $location, $window, Session) {
        // initialise google analytics
        $window.ga('create', 'UA-75878329-2', 'auto');

        // track pageview on state change
        $rootScope.$on('$stateChangeSuccess', function (event) {
            $window.ga('send', 'pageview', $location.path());
        });

        $rootScope.$on("$stateChangeSuccess", function (event, next, current) {
            //if (next && next.$$route && next.$$route.secure) {
                //if (!authService.user.isAuthenticated) {
                //    authService.redirectToLogin();
                //}


                if(!Session.getAccessToken()){

                    if($location.path() != "/register")
                        window.location.href='/#/login';
                }
            //}
        });

    }
;