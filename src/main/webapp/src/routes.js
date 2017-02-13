angular.module('DojoIBL')
    .config(['$stateProvider','$routeProvider', function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $stateProvider
            .state('home', {
                url: '/home',
                templateUrl: '/src/components/home/home.template.html',
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
                        templateUrl: '/src/components/run/inquiry.structure.template.html'
                    }
                }
            })
            .state('inquiry.home', {
                url: '/:runId',
                templateUrl: '/src/components/run/inquiry.template.html',
                controller: 'InquiryController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.phases' | translate}} "
                },
                resolve: {
                    security: ['$q', '$stateParams', 'AccountService', 'UserService', 'toaster', '$location'
                        , function($q, $stateParams, AccountService, UserService, toaster,$location){

                            UserService.checkAccess();

                        //AccountService.myDetails().then(function(me){
                        //    console.log($stateParams.runId, me);
                        //    UserService.getUsersForRun($stateParams.runId).then(function(data){
                        //        console.log(arrayObjectIndexOf(data, me.localId, "localId"));
                        //        if(arrayObjectIndexOf(data, me.localId, "localId") == -1){
                        //            $location.path('home');
                        //            toaster.error({
                        //                title: 'No access ',
                        //                body: 'You do not have access to this inquiry.'
                        //            });
                        //        }
                        //    });
                        //});
                        //
                        //function arrayObjectIndexOf(myArray, searchTerm, property) {
                        //    for(var i = 0, len = myArray.length; i < len; i++) {
                        //        if (myArray[i][property] === searchTerm) return i;
                        //    }
                        //    return -1;
                        //}

                        //if(){
                        //    return $q.reject("Not Authorized");
                        //}
                    }]
                }
            })
            .state('inquiry.phase', {
                url: '/:runId/phase/:phase',
                templateUrl: '/src/components/activity/phase.template.html',
                controller: 'PhaseController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.phase' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('inquiry.activity', {
                url: '/:runId/phase/:phase/activity/:activityId',
                templateUrl: '/src/components/activity/activity.template.html',
                controller: 'ActivityController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.activity' | translate}}",
                    parent: 'inquiry.phase' // Override the parent state (only for the breadcrumb).

                }
            })
            .state('inquiry.timeline', {
                url: '/:runId/timeline',
                templateUrl: '/src/components/run/timeline.template.html',
                controller: 'TimelineController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.timeline' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('inquiry.calendar', {
                url: '/:gameId/:runId/calendar',
                templateUrl: '/src/components/run/calendar.template.html',
                controller: 'CalendarController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.calendar' | translate}}",
                    parent: 'inquiry.home' // Override the parent state (only for the breadcrumb).
                }
            })
            .state('editgame', {
                url: '/inquiry/:gameId/edit',
                templateUrl: '/src/components/home/game.edit.template.html',
                controller: 'InquiryEditGameController',
                ncyBreadcrumb: {
                    label: "{{'dibl.toolbar.editstructure' | translate}}"
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