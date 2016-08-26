angular.module('DojoIBL')
    .config(['$stateProvider','$routeProvider', function ($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('landing', {
                url: '/landing',
                templateUrl: '/dist/templates/landing.html',
                controller: 'LandingController'
            })
            .state('home', {
                url: '/home',
                templateUrl: '/dist/templates/home.html',
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
                        templateUrl: '/dist/templates/inquiry-structure.html'
                    }
                }
            })
            .state('inquiry.home', {
                url: '/:runId',
                templateUrl: '/dist/templates/inquiry.html',
                controller: 'InquiryController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.phases' | translate}} "
                }
            })
            .state('inquiry.phase', {
                url: '/:runId/phase/:phase',
                templateUrl: '/dist/templates/phase.html',
                controller: 'PhaseController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.phase' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('inquiry.activity', {
                url: '/:runId/phase/:phase/activity/:activityId',
                templateUrl: '/dist/templates/activity.html',
                controller: 'ActivityController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.activity' | translate}}",
                    parent: 'inquiry.phase' // Override the parent state (only for the breadcrumb).

                }
            })
            .state('inquiry.timeline', {
                url: '/:runId/timeline',
                templateUrl: '/dist/templates/timeline.html',
                controller: 'TimelineController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.timeline' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('editgame', {
                url: '/inquiry/:gameId/edit',
                templateUrl: '/dist/templates/inquiry-edit-game.html',
                controller: 'InquiryEditGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('editrun', {
                url: '/inquiry/:gameId/run/:runId/edit',
                templateUrl: '/dist/templates/inquiry-edit-run.html',
                controller: 'InquiryEditRunController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editrun' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('news', {
                url: '/create/inquiry',
                templateUrl: '/dist/templates/inquiry-new.html',
                controller: 'InquiryNewGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.newinquiry' | translate}}"
                }
            })
            .state('profiles', {
                url: '/profiles',
                templateUrl: '/dist/templates/profiles.html',
                controller: 'ProfilesController',
                ncyBreadcrumb: {
                    label: 'Profiles'
                }
            })
            .state('profileAccount', {
                url: '/profile/:accountId',
                templateUrl: '/dist/templates/profile.html',
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
                templateUrl: '/dist/templates/error.html'
            })
            .state('channelmonitor', {
                url: '/channelmonitor',
                templateUrl: '/dist/templates/monitor.html',
                controller: 'ChannelMonitorController'
            })
        ;

        $urlRouterProvider.otherwise('/error');

    }]);