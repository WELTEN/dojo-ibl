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
                controller: 'HomeController'
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
                controller: 'InquiryController'
                //controller: function ($scope, $stateParams, RunService) {
                //    // If we got here from a url of /contacts/42
                //    //expect($stateParams).toBe({contactId: "42"});
                //    if($stateParams)
                //        console.log($stateParams.runId);
                //        RunService.getRunById($stateParams.runId).then(function(data){
                //            $scope.inqTitle = data.title;
                //            $scope.inqDescription = data.game.description;
                //            $scope.inqId = data.runId;
                //            $scope.phases = data.game.phases;
                //            console.log(data);
                //            //AccountService.myDetails().then(
                //            //    function(data){
                //            //        $scope.myAccount = data;
                //            //        loadAccessRules();
                //            //    }
                //            //);
                //
                //        });
                //}
            })
            .state('inquiry.phase', {
                url: '/:runId/phase/:phase',
                templateUrl: '/dist/templates/phase.html',
                controller: 'PhaseController'
            })
            .state('inquiry.activity', {
                url: '/:runId/phase/:phase/activity/:activityId',
                templateUrl: '/dist/templates/activity.html',
                controller: 'ActivityController'
            })
            .state('inquiry.timeline', {
                url: '/:runId/timeline',
                templateUrl: '/dist/templates/timeline.html',
                controller: 'TimelineController'
            })
            .state('editgame', {
                url: '/inquiry/:gameId/edit',
                templateUrl: '/dist/templates/inquiry-edit-game.html',
                controller: 'InquiryEditGameController'
            })
            .state('editrun', {
                url: '/inquiry/:gameId/run/:runId/edit',
                templateUrl: '/dist/templates/inquiry-edit-run.html',
                controller: 'InquiryEditRunController'
            })
            .state('news', {
                url: '/create/inquiry',
                templateUrl: '/dist/templates/inquiry-new.html',
                controller: 'InquiryNewGameController'
            })
            .state('profiles', {
                url: '/profiles',
                templateUrl: '/dist/templates/profiles.html',
                controller: 'ProfilesController'
            })
            .state('profileAccount', {
                url: '/profile/:accountId',
                templateUrl: '/dist/templates/profile.html',
                controller: 'ProfileController'
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

        $urlRouterProvider.otherwise({redirectTo: '/error'});

    }]);