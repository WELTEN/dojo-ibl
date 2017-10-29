angular.module('DojoIBL')

    .controller('InquiryController', function ($scope, $sce, $location, $stateParams, $state, Session, MessageService,
                                               ActivityService, AccountService, ChannelService, RunService, ActivityStatusService, toaster,
                                               LaService) {

        if(!Session.getAccessToken()){
            window.location.href='/#/login';
        }

        $scope.isOpenArray = []

        $scope.chat = true;
        $scope.visualization = true;
        $scope.state = $state.current.name;
        $scope.inqTitle = "";

        $scope.disableInquiryLoading = false;

        RunService.getRunById($stateParams.runId).then(function (data) {
            $scope.inqTitle = data.title;
            $scope.inqTempTitle = data.game.title;
            $scope.inqDescription = data.game.description;
            $scope.inqId = data.runId;
            $scope.phases = data.game.phases;
            $scope.code = data.code;
            $scope.serverCreationTime = data.serverCreationTime;
            $scope.disableInquiryLoading = true;
            $scope.gameId = data.game.gameId;

            angular.forEach(data.game.phases, function(value, key) {
                $scope.isOpenArray[key] = false;
                ActivityStatusService.getActivitiesServerStatus(data.game.gameId, $stateParams.runId, key);
            });

            $scope.isOpenArray[0] = true;
        });

        $scope.activities = ActivityStatusService.getActivitiesStatus();

        $scope.sortableOptions = {
            connectWith: ".connectList",
            scroll: false,
            receive: function(event, ui) {
                var item = ui.item.scope().activity;
                var group = event.target;

                //console.log(item.id, group.id, item.status.id, item)

                ActivityStatusService.changeActivityStatus($stateParams.runId, item.id, group.id, item.section, item.status.id).then(function (data) {
                        AccountService.myDetails().then(function(user) {
                            switch (data.status) {
                                case 0:
                                    toaster.success({
                                        title: 'Moved to ToDo list',
                                        body: 'The activity has been successfully moved to ToDo list.'
                                    });

                                    LaService.sendUpdateStatusStatement(
                                        "To Do",
                                        user,
                                        item.section,
                                        item.id,
                                        "Activity Name",
                                        "Activity Description",
                                        $stateParams.runId,
                                        "Group title",
                                        "Group description",
                                        $scope.gameId,
                                        "Project",
                                        "Project description");

                                    break;
                                case 1:
                                    toaster.success({
                                        title: 'Moved to In Progress list',
                                        body: 'The activity has been successfully moved to In Progress list.'
                                    });
                                    LaService.sendUpdateStatusStatement(
                                        "In Progress",
                                        user,
                                        item.section,
                                        item.id,
                                        "Activity Name",
                                        "Activity Description",
                                        $stateParams.runId,
                                        "Group title",
                                        "Group description",
                                        $scope.gameId,
                                        "Project",
                                        "Project description");
                                    break;
                                case 2:
                                    toaster.success({
                                        title: 'Moved to Completed list',
                                        body: 'The activity has been successfully moved to Completed list.'
                                    });
                                    LaService.sendUpdateStatusStatement(
                                        "Done",
                                        user,
                                        item.section,
                                        item.id,
                                        "Activity Name",
                                        "Activity Description",
                                        $stateParams.runId,
                                        "Group title",
                                        "Group description",
                                        $scope.gameId,
                                        "Project",
                                        "Project description");
                                    break;
                            }
                        });
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

        $scope.goToPhase = function(inqId, index) {
            $location.path('inquiry/'+inqId+'/phase/'+ index);
        };

        $scope.toggle = false;

        $scope.active = function(phaseClicked) {
            $scope.isOpenArray[$scope.isOpenArray.indexOf(true)] = false;
            $scope.isOpenArray[phaseClicked] = true;

        }

        $scope.goToActivity = function(inqId, index, activity) {
            $scope.toggle = true;
            $location.path('inquiry/'+inqId+'/phase/'+ index + '/activity/' +activity);
        };

        if($stateParams.activityId){
            $scope.toggle = true;
            $location.path('inquiry/'+$stateParams.runId+'/phase/'+ $stateParams.phase + '/activity/' +$stateParams.activityId);
        }

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

        $scope.gotoElement = function (eID){
            // set the location.hash to the id of
            // the element you wish to scroll to.
            $location.hash('structure-view');

        };

    }
);