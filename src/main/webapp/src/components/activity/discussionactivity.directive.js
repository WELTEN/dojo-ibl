angular.module('DojoIBL')

    .directive('discussionActivity', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/activity/discussionactivity.directive.html',
            controller: 'DiscussionActivityController',
            scope: {
                activity: '='
            }
        };
    }

);