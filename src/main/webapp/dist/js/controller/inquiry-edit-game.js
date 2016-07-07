angular.module('DojoIBL')

    .controller('InquiryEditGameController', function ($scope, $sce, $stateParams, $state, Session, RunService, ActivityService,
                                                       AccountService, GameService, UserService ) {
        // Managing different tabs activities
        $scope.lists = [];

        GameService.getGameById($stateParams.gameId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.game = data;

            if(!$scope.game.config.roles)
                $scope.game.config.roles = [];
            else{
                //$scope.game.config.roles = new JSONObject(data.config.roles);
            }

            $scope.phases = $scope.game.phases;

            if (data.lat) {
                $scope.coords.latitude = data.lat;
                $scope.coords.longitude = data.lng;
                $scope.map.center.latitude = data.lat;
                $scope.map.center.longitude = data.lng;
                $scope.showMap = true;
            }

            // Original list of activities
            $scope.list_original = [
                //{'name': 'Google Resources', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-file-text'},
                {'name': 'Text activity', 'type': 'org.celstec.arlearn2.beans.generalItem.NarratorItem', 'icon': 'fa-file-text'},
                {'name': 'Youtube video', 'type': 'org.celstec.arlearn2.beans.generalItem.VideoObject', 'icon': 'fa-external-link'},
                //{'name': 'Concept map', 'type': 'org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest', 'icon': 'fa-sitemap'},
                //{'name': 'External widget', 'type': 'org.celstec.arlearn2.beans.generalItem.OpenBadge', 'icon': 'fa-link'},
                //{'name': 'Research question', 'type': 'org.celstec.arlearn2.beans.generalItem.ResearchQuestion', 'icon': 'fa-question'}
                {'name': 'Research question', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-question'},
                {'name': 'Data collection', 'type': 'org.celstec.arlearn2.beans.generalItem.ScanTag', 'icon': 'fa-picture-o'}
            ];

            angular.forEach($scope.game.phases, function(value, key) {
                $scope.lists[key] = [];
                ActivityService.getActivitiesForPhase($stateParams.gameId, key).then(function(data){
                    angular.forEach(data, function(i,a){
                        $scope.lists[key].push(i);
                    });
                });
            });
        });

        $scope.ok = function(){
            GameService.newGame($scope.game);
            //$window.history.back();
        };

        ////////////////////
        // Manage activities
        ////////////////////
        $scope.selected = false;
        $scope.activity = {};

        $scope.selectActivity = function(activity, combinationId){
            $scope.activity = activity;

            // Save roles into an array
            $scope.selection = $scope.activity.roles || [];

            $scope.selected = true;

            $scope.selectedCombination = combinationId;

            $scope.removeActivity = function(data){
                var position = $scope.lists[$(".select-activities > li.active").attr('data')].indexOf(data);
                $scope.lists[$(".select-activities > li.active").attr('data')].splice(position, 1);
                ActivityService.deleteActivity($scope.activity.gameId, $scope.activity.id);
                $scope.activity = null;
            };
        };

        $scope.addOne = function(a){

            var object = {
                type: a.type,
                section: $(".select-activities > li.active").attr('data'),
                gameId: $stateParams.gameId,
                deleted: true,
                name: a.name,
                description: "",
                autoLaunch: false,
                fileReferences: [],
                sortKey: 1,
                richText: ""
            };

            if(a.type == "org.celstec.arlearn2.beans.generalItem.VideoObject"){
                object.videoFeed = "example link";
            }else if(a.type == "org.celstec.arlearn2.beans.generalItem.AudioObject") {
                object.audioFeed = "example link";
            }

            if(angular.isUndefined($scope.lists[$(".select-activities > li.active").attr('data')])){
                $scope.lists[$(".select-activities > li.active").attr('data')] = []
            }

            ActivityService.newActivity(object).then(function(data){
                ActivityService.getActivityById(data.id, $stateParams.gameId).then(function(data){
                    $scope.lists[$(".select-activities > li.active").attr('data')].push(data);
                });
            });
        };



        $scope.toggleSelection = function toggleSelection(rol) {
            var idx = $scope.selection.indexOf(rol);

            // is currently selected
            if (idx > -1) {
                $scope.selection.splice(idx, 1);
            }

            // is newly selected
            else {
                $scope.selection.push(rol);
            }
        };

        $scope.saveActivity = function(){
            // Save array of roles into the activity
            $scope.activity.roles = $scope.selection;

            console.log($scope.activity);

            ActivityService.newActivity($scope.activity);
        };

        ///////////////
        // Manage roles
        ///////////////
        $scope.addRole = function () {
            $scope.game.config.roles.push({
                name: $scope.roleName
            });
            GameService.newGame($scope.game);
            $scope.roleName = "";
        };

        $scope.selectRole = function(index){
            $scope.removeRole = function(){
                $scope.game.config.roles.splice(index, 1);
                GameService.newGame($scope.game);
            };
        };

        ////////////////
        // Manage phases
        ////////////////
        $scope.addPhase = function(){

            $scope.phases.push({
                //phaseId: $scope.phases.length,
                title: $scope.phaseName,
                type: "org.celstec.arlearn2.beans.game.Phase"
            });

            //$scope.lists.push($scope.phases.length);
            //$scope.lists = [];

            $scope.phaseName = "";

            $scope.game.phases = $scope.phases;

            GameService.newGame($scope.game);
        };

        $scope.removePhase = function(index){
            $scope.phases.splice(index, 1);
            $scope.game.phases = $scope.phases;
            GameService.newGame($scope.game);
        };

        //////////////////////
        // Manage inquiry runs
        //////////////////////
        RunService.getParticipateRunsForGame($stateParams.gameId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.gameRuns = data;


            $scope.usersRun = [];

            angular.forEach($scope.gameRuns, function(value, key) {
                $scope.usersRun[value.runId] = [];
                UserService.getUsersForRun(value.runId).then(function(data){

                    $scope.usersRun[value.runId] = data;

                });
            });
        });

        $scope.createInquiryRun = function(){
            // Add the link between run and game
            $scope.run.gameId = $stateParams.gameId;
            RunService.newRun($scope.run).then(function(run){
                // Update run with data from the server
                $scope.run = run;

                console.log(run);

                if(angular.isUndefined($scope.gameRuns)){
                    $scope.gameRuns = []
                }

                //$scope.gameRuns.push(run);
                AccountService.myDetails().then(function(data){
                    // Grant me access to the run
                    RunService.giveAccess($scope.run.runId, data.accountType+":"+data.localId,1);
                    // Add me as a user to the run
                    RunService.addUserToRun({
                        runId: $scope.run.runId,
                        email: data.accountType+":"+data.localId,
                        accountType: data.accountType,
                        localId: data.localId,
                        gameId: $stateParams.gameId
                    });

                    $scope.gameRuns[$scope.run.runId] = $scope.run;

                    // Reset the run variable to create new ones
                    $scope.run = null;
                });
            });
        };

    }).controller('TabController', function ($scope, $stateParams, GameService, ActivityService) {
        this.tab = 0;

        GameService.getGameById($stateParams.gameId).then(function (data) {

            if (data.config.roles)
                $scope.roles = data.config.roles;

            $scope.game = data;

        });

        this.setTab = function (tabId) {
            this.tab = tabId;
            ActivityService.getActivitiesForPhase($scope.game.gameId, tabId).then(function (data) {
                $scope.activities = data;
            });
        };

        this.isSet = function (tabId) {
            return this.tab === tabId;
        };
    })
;