angular.module('DojoIBL')

    .controller('PhaseController', function ($scope, $sce, $location, $stateParams, $state, toaster, Session, ActivityStatusService, RunService, ChannelService) {
        $scope.runId = $stateParams.runId;

        ChannelService.register('org.celstec.arlearn2.beans.run.GeneralItemsStatus', function (notification) {
            //
            //console.log(notification)
            //
            ActivityStatusService.refreshActivityStatus(notification.generalItemId, $scope.gameId, $stateParams.runId, $stateParams.phase);
            toaster.success({
                title: 'Activity modified',
                body: 'The structure of the activity has been modified.'
            });
        });

        $scope.phase = "holaaaaaaa";


        RunService.getRunById($stateParams.runId).then(function(data){
            $scope.phase = data.game.phases[$stateParams.phase];
            $scope.phase.num = $stateParams.phase;
            $scope.gameId = data.game.gameId;
            ActivityStatusService.getActivitiesServerStatus(data.game.gameId, $stateParams.runId, $stateParams.phase);
        });

        $scope.activities = ActivityStatusService.getActivitiesStatus();

        $scope.sortableOptions = {
            connectWith: ".connectList",
            scroll: false,
            receive: function(event, ui) {
                var item = ui.item.scope().activity;

                console.log(item.status);


                var group = event.target;
                ActivityStatusService.changeActivityStatus($stateParams.runId, item.id,group.id, $stateParams.phase).then(function (data) {

                    switch(data.status){
                        case 0:
                            toaster.success({
                                title: 'Moved to ToDo list',
                                body: 'The activity has been successfully moved to the ToDo list.'
                            });
                            break;
                        case 1:
                            toaster.success({
                                title: 'Moved to In Progress list',
                                body: 'The activity has been successfully moved to the In Progress list.'
                            });
                            break;
                        case 2:
                            toaster.success({
                                title: 'Moved to Completed list',
                                body: 'The activity has been successfully moved to the Completed list.'
                            });
                            break;
                    }

                });
            },
            'ui-floating': 'auto',
            'start': function (event, ui) {
                if($scope.sortableFirst){
                    $scope.wscrolltop = $(window).scrollTop();
                }
                $scope.sortableFirst = true;
            },
            'sort': function (event, ui) {
                ui.helper.css({'top': ui.position.top + $scope.wscrolltop + 'px'});
            }
        };

        $scope.goToActivity = function(runId, section, id) {
            $location.path('inquiry/'+runId+'/phase/'+section+'/activity/'+id);
        };

        $scope.getRoleName = function(roles){
            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(roles[0])) {
                        return roles[0].name;
                    }
                }catch(e){
                    return "-";
                }
            }
            return "-";
        };


        $scope.getRoleColor = function(roles){

            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(roles[0])) {
                        return {
                            "border-left": "3px solid "+roles[0].color
                        };
                    }
                }catch(e){
                    return {
                        "border-left": "3px solid #2f4050"
                    };
                }
            }
            return {
                "border-left": "3px solid #2f4050"
            };
        };


        function isEmpty(obj) {
            for(var prop in obj) {
                if(obj.hasOwnProperty(prop))
                    return false;
            }
            return true;
        }
    }
);