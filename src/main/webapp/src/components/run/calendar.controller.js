angular.module('DojoIBL')

    .controller('CalendarController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService) {


        RunService.getRunById($stateParams.runId).then(function (run) {
            var act = ActivityService.getActivitiesServer(run.gameId);

            console.log(act);


//            var fahrenheit = [0, 32, 45, 50, 75, 80, 99, 120];
//
//            var celcius = fahrenheit.map(function(elem) {
//                return Math.round((elem - 32) * 5 / 9);
//            });
//
//// ES6
//// fahrenheit.map(elem => Math.round((elem - 32) * 5 / 9));
//
//            celcius //  [-18, 0, 7, 10, 24, 27, 37, 49]

        });


        console.log(ActivityService.getActivities());

        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();


        // Events
        $scope.events = [
            //{title: 'All Day Event',start: new Date(y, m, 1)},
            //{title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
            //{id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
            //{id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
            //{title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
            //{title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
        ];

        for ( var run in ActivityService.getActivities() ) {
            for ( var section in ActivityService.getActivities()[run] ) {
                for ( var activity in ActivityService.getActivities()[run][section] ) {
                    var act = ActivityService.getActivities()[run][section][activity]
                    $scope.events.push({ title: act.name, start: new Date(act.timestamp) })
                }
            }
        }

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
                editable: true,
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
        $scope.eventSources = [$scope.events];


    }
);