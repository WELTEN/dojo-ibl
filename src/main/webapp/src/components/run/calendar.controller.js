angular.module('DojoIBL')

    .controller('CalendarController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService, $location) {


        ChannelService.register('org.celstec.arlearn2.beans.notification.GeneralItemModification', function (notification) {
            ActivityService.refreshActivity(notification.itemId, notification.gameId);
            toaster.success({
                title: 'Activity modified',
                body: 'The structure of the activity has been modified.'
            });
        });

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