angular.module('DojoIBL')

    .controller('CalendarController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService) {


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
        //console.log($scope.events)

        /* message on eventClick */
        $scope.alertOnEventClick = function( event, allDay, jsEvent, view ){
            $scope.alertMessage = (event.title + ': Clicked ');
        };
        /* message on Drop */
        $scope.alertOnDrop = function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
            $scope.alertMessage = (event.title +': Droped to make dayDelta ' + dayDelta);
            console.log(event.title +': Droped to make dayDelta ' + dayDelta);
        };
        /* message on Resize */
        $scope.alertOnResize = function(event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
            $scope.alertMessage = (event.title +': Resized to make dayDelta ' + minuteDelta);
        };

        /* config object */
        $scope.uiConfig = {
            calendar:{
                height: 450,
                editable: false,
                header: {
                    left: 'prev,next,today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                eventClick: $scope.alertOnEventClick,
                eventDrop: $scope.alertOnDrop,
                eventResize: $scope.alertOnResize
            }
        };

        /* Event sources array */
        //$scope.eventSources = [$scope.events];
        //console.log($scope.eventSources)
    }
);