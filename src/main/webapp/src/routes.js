angular.module('DojoIBL')
    .config(['$stateProvider','$routeProvider', function ($stateProvider, $urlRouterProvider, $locationProvider, Session) {


        $stateProvider
            .state('home', {
                url: '/home',
                templateUrl: '/src/components/home/home.template.html',
                controller: 'HomeController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.home' | translate}}"
                }
            })
            .state('groups', {
                url: '/groups',
                templateUrl: '/src/components/home/groups.template.html',
                controller: 'GroupsController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.home' | translate}}"
                }
            })
            .state('authentication', {
                url: '/authentication',
                controller: 'AuthenticationController'
            })
            .state('login', {
                url: '/login',
                controller: 'LoginController',
                templateUrl: '/src/components/login/login.template.html'
            })
            .state('register', {
                url: '/register',
                controller: 'RegisterController',
                templateUrl: '/src/components/login/register.template.html'
            })
            .state('landing', {
                url: '/landing',
                controller: 'LoginController',
                templateUrl: '/src/components/landing/landing.template.html'
            })
            .state('inquiry', {
                url : '/inquiry/:runId',
                controller: 'InquiryController',
                templateUrl: '/src/components/run/inquiry.structure.template.html'
                //resolve: {
                //    security: ['$q', '$stateParams', 'UserService'
                //        , function($q, $stateParams, UserService){
                //
                //            UserService.checkAccess($stateParams.runId);
                //        }]
                //}
            })
            //.state('inquiry.home', {
            //    url: '/:runId',
            //    templateUrl: '/src/components/run/inquiry.template.html',
            //    controller: 'InquiryController',
            //    ncyBreadcrumb: {
            //        label: "{{'dibl.toolbar.phases' | translate}} "
            //    },
            //    resolve: {
            //        security: ['$q', '$stateParams', 'UserService'
            //            , function($q, $stateParams, UserService){
            //
            //                UserService.checkAccess($stateParams.runId);
            //        }]
            //    }
            //})
            //.state('inquiry.phase', {
            //    url: '/:runId/phase/:phase',
            //    templateUrl: '/src/components/activity/phase.template.html',
            //    controller: 'PhaseController',
            //    ncyBreadcrumb: {
            //        label: "{{'dibl.toolbar.phase' | translate}}",
            //        parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
            //    }
            //})
            .state('inquiry.activity', {
                url: '/phase/:phase/activity/:activityId',
                templateUrl: '/src/components/activity/activity.template.html',
                controller: 'ActivityController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.activity' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).

                }
            })
            .state('inquiry.timeline', {
                url: '/timeline',
                templateUrl: '/src/components/run/timeline.template.html',
                controller: 'TimelineController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.timeline' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('inquiry.calendar', {
                url: '/:gameId/calendar',
                templateUrl: '/src/components/run/calendar.template.html',
                controller: 'CalendarController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.calendar' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('inquiry.dashboard', {
                url: '/dashboard',
                templateUrl: '/src/components/run/dashboard.template.html',
                controller: 'DashboardController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.dashboard' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('editgame', {
                url: '/project/:gameId/edit',
                abstract: true,
                templateUrl: '/src/components/home/game.edit.template.html',
                controller: 'InquiryEditGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('editgame.metadata', {
                url: '/metadata',
                templateUrl: '/src/components/home/game.edit.metadata.template.html',
                controller: 'InquiryEditGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('editgame.structure', {
                url: '/structure',
                templateUrl: '/src/components/home/game.edit.structure.template.html',
                controller: 'InquiryEditGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('editgame.groups', {
                url: '/groups',
                templateUrl: '/src/components/home/game.edit.groups.template.html',
                controller: 'InquiryEditGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('editgame.calendar', {
                url: '/calendar',
                controller: 'InquiryEditGameController',
                templateUrl: '/src/components/home/game.edit.calendar.template.html',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('editgame.editors', {
                url: '/editors',
                controller: 'InquiryEditGameController',
                templateUrl: '/src/components/home/game.edit.editors.template.html',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
                }
            })
            .state('gamecatalogue', {
                url: '/catalogue/inquiry',
                templateUrl: '/src/components/home/game.new.template.html',
                controller: 'InquiryNewGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.selectstructure' | translate}}"
                }
            })
            .state('editrun', {
                url: '/inquiry/:gameId/run/:runId/edit',
                templateUrl: '/src/components/run/inquiry.edit.template.html',
                controller: 'InquiryEditRunController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editrun' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('profiles', {
                url: '/profiles',
                templateUrl: '/src/components/account/profiles.template.html',
                controller: 'ProfilesController',
                ncyBreadcrumb: {
                    label: 'Profiles'
                }
            })
            .state('profileAccount', {
                url: '/profile/:fullId',
                templateUrl: '/src/components/account/profile.template.html',
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
                templateUrl: '/src/components/common/error.template.html'
            });
    }]);