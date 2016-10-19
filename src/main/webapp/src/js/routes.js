angular.module('DojoIBL')
    .config(['$stateProvider','$routeProvider', function ($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('landing', {
                url: '/landing',
                templateUrl: '/src/templates/landing.html',
                controller: 'LandingController'
            })
            .state('home', {
                url: '/home',
                templateUrl: '/src/templates/home.html',
                controller: 'HomeController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.home' | translate}}"
                }
            })
            .state('inquiry', {
                url : '/inquiry',
                abstract: true,
                views: {
                    '': {
                        templateUrl: '/src/templates/inquiry-structure.html'
                    }
                }
            })
            .state('inquiry.home', {
                url: '/:runId',
                templateUrl: '/src/templates/inquiry.html',
                controller: 'InquiryController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.phases' | translate}} "
                }
            })
            .state('inquiry.phase', {
                url: '/:runId/phase/:phase',
                templateUrl: '/src/templates/phase.html',
                controller: 'PhaseController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.phase' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('inquiry.activity', {
                url: '/:runId/phase/:phase/activity/:activityId',
                templateUrl: '/src/templates/activity.html',
                controller: 'ActivityController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.activity' | translate}}",
                    parent: 'inquiry.phase' // Override the parent state (only for the breadcrumb).

                }
            })
            .state('inquiry.timeline', {
                url: '/:runId/timeline',
                templateUrl: '/src/templates/timeline.html',
                controller: 'TimelineController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.timeline' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('editgame', {
                url: '/inquiry/:gameId/edit',
                templateUrl: '/src/templates/inquiry-edit-game.html',
                controller: 'InquiryEditGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('editrun', {
                url: '/inquiry/:gameId/run/:runId/edit',
                templateUrl: '/src/templates/inquiry-edit-run.html',
                controller: 'InquiryEditRunController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editrun' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('news', {
                url: '/create/inquiry',
                templateUrl: '/src/templates/inquiry-new.html',
                controller: 'InquiryNewGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.newinquiry' | translate}}"
                }
            })
            .state('profiles', {
                url: '/profiles',
                templateUrl: '/src/templates/profiles.html',
                controller: 'ProfilesController',
                ncyBreadcrumb: {
                    label: 'Profiles'
                }
            })
            .state('profileAccount', {
                url: '/profile/:fullId',
                templateUrl: '/src/templates/profile.html',
                controller: 'ProfileController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.profile' | translate}}"
                }
            })
            .state('oauth', {
                url: '/oauth/:accessToken/:type/:exp',
                controller: 'OauthController'
            })
            .state('error', {
                url: '/error',
                templateUrl: '/src/templates/error.html'
            })
            .state('channelmonitor', {
                url: '/channelmonitor',
                templateUrl: '/src/templates/monitor.html',
                controller: 'ChannelMonitorController'
            })
        ;

        $urlRouterProvider.otherwise('/error');

    }]);