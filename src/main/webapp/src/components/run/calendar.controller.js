angular.module('DojoIBL')

    .controller('CalendarController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService, $location) {

        $scope.$parent.toggle = true;

        $scope.closeActivity = function() {
            $scope.$parent.toggle = false;
            $location.path('inquiry/'+$stateParams.runId);
        };

        $scope.events = [];

        RunService.getRunById($stateParams.runId).then(function (run) {
            ActivityService.getActivitiesServer(run.gameId);
        });

        $scope.events = ActivityService.getCalendarActivities()[$stateParams.gameId]
        $scope.eventSources = [$scope.events];


        /* message on eventClick */
        $scope.alertOnEventClick = function( event, allDay, jsEvent, view ){
            $scope.goToActivity($stateParams.runId, event.activity.section, event.activity.id)
        };

        /* config object */
        $scope.uiConfig = {
            calendar:{
                firstDay:1,
                height: 450,
                editable: true,
                header: {
                    left: 'prev,next,today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                eventClick: $scope.alertOnEventClick,
                disableDragging :true,
                views: {
                    basic: {
                        // options apply to basicWeek and basicDay views
                    },
                    agenda: {
                        // options apply to agendaWeek and agendaDay views
                    },
                    week: {
                        // options apply to basicWeek and agendaWeek views
                    },
                    day: {
                        // options apply to basicDay and agendaDay views
                    }
                }
            }
        };

        $scope.goToActivity = function(inqId, index, activity) {
            $location.path('inquiry/'+inqId+'/phase/'+ index + '/activity/' +activity);
        };
    }
);