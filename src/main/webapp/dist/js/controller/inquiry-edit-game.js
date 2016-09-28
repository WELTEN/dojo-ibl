angular.module('DojoIBL')

    .controller('InquiryEditGameController', function ($scope, $sce, $stateParams, $state, $modal, Session, RunService, ActivityService,
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

        $scope.compare = function() {
            return !angular.equals($scope.activity_old, $scope.activity);
        };

        $scope.saveActivity = function(){
            console.log($scope.activity)
            // Save array of roles into the activity
            $scope.activity_old = angular.copy($scope.activity);
            $scope.activity.roles = $scope.selection;
            ActivityService.newActivity($scope.activity);
        };

        $scope.selectActivity = function(activity, combinationId){
            if(!angular.isUndefined($scope.activity_old)){
                if(!angular.equals($scope.activity_old, $scope.activity)){
                    swal({
                        title: "Save you changes",
                        text: "Do you want to save before going on?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Yes, save it!",
                        closeOnConfirm: false
                    }, function () {
                        $scope.saveActivity();
                        swal("Saved!", "The activity has been saved successfully", "success");

                        $scope.activity = activity;

                        $scope.activity_old = angular.copy(activity);

                        // Save roles into an array
                        $scope.selection = $scope.activity.roles || [];

                        $scope.selected = true;

                        $scope.selectedCombination = combinationId;

                    });
                    return;
                }
            }

            $scope.activity = activity;

            $scope.activity_old = angular.copy(activity);

            // Save roles into an array
            $scope.selection = $scope.activity.roles || [];
            //
            //
            //$scope.selection = [];
            //
            //angular.forEach($scope.activity.roles, function(value, key) {
            //    this.push(jQuery.parseJSON(value));
            //}, $scope.selection);
            //console.log($scope.selection);

            $scope.selected = true;

            $scope.selectedCombination = combinationId;

            $scope.removeActivity = function(data){

                swal({
                    title: "Are you sure?",
                    text: "You will not be able to recover this activity!",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, delete it!",
                    closeOnConfirm: false
                }, function () {
                    swal("Deleted!", "The activity has been removed from the inquiry structure.", "success");

                    var position = $scope.lists[$(".select-activities > li.active").attr('data')].indexOf(data);
                    $scope.lists[$(".select-activities > li.active").attr('data')].splice(position, 1);
                    ActivityService.deleteActivity($scope.activity.gameId, $scope.activity.id);
                    $scope.activity = $scope.activity_old;
                    $scope.selectedCombination = 0;
                    $scope.selected = false;
                });
            };
        };

        $scope.changeTab = function(index){

            $scope.selectedCombination = 0;
            $scope.selected = false;

            if(!angular.isUndefined($scope.activity_old)){
                if(!angular.equals($scope.activity_old, $scope.activity)){
                    swal({
                        title: "Save you changes",
                        text: "Make sure you save your changes before going on",
                        type: "warning",
                        //showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Save changes now!",
                        closeOnConfirm: false
                    }, function () {
                        $scope.saveActivity();
                        swal("Saved!", "The activity has been saved successfully", "success");

                        $scope.activity = activity;

                        $scope.activity_old = angular.copy(activity);

                        // Save roles into an array
                        $scope.selection = $scope.activity.roles || [];

                        $scope.selected = true;

                        $scope.selectedCombination = combinationId;

                    });
                    return;
                }
            }

        };

        $scope.currentPhase = 0;

        $scope.renamePhase = function(tab, index){
            swal({
                title: "Rename phase "+index+" '"+$scope.phases[index].title+"'?",
                text: "Are you sure you want to rename the phase?",
                type: "input",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, rename it!",
                inputPlaceholder: "New phase name",
                closeOnConfirm: false
            }, function (inputValue) {

                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                swal("Well done!", "You renamed the phase, now it's called: " + inputValue, "success");

                $scope.phases[index].title = inputValue;

                $scope.game.phases = $scope.phases;

                GameService.newGame($scope.game);

            });
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
                object.videoFeed = "";
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

        ///////////////
        // Manage roles
        ///////////////
        $scope.addRole = function () {
            $scope.game.config.roles.push({
                name: $scope.roleName,
                color: $scope.roleColor

            });
            GameService.newGame($scope.game);
            $scope.roleName = "";
            $scope.roleColor = "";
        };

        $scope.selectRole = function(index){
            $scope.roleName = $scope.game.config.roles[index].name;
            $scope.roleColor = $scope.game.config.roles[index].color;

            $scope.removeRole = function(){
                $scope.game.config.roles.splice(index, 1);
                GameService.newGame($scope.game);
                $scope.roleName = "";
                $scope.roleColor = "";
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

            swal({
                title: "Are you sure you want to delete the phase?",
                text: "You will not be able to recover this phase!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete the phase!",
                closeOnConfirm: false
            }, function () {
                swal("Phase deleted!", "The phase has been removed from the inquiry structure.", "success");

                $scope.phases.splice(index, 1);
                $scope.game.phases = $scope.phases;
                GameService.newGame($scope.game);
            });

        };

        $scope.movePhase = function(x, y){

            swal({
                title: "Moving phase "+x+" to phase "+y,
                text: "You will switch phase positions. Current phase "+x+" will be "+y+" and phase "+y+" will be phase "+x,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, move them!",
                closeOnConfirm: false
            }, function () {
                swal("Phase moved!", "Phases have been reordered", "success");

                var b = $scope.phases[y];
                var acts = $scope.lists[y];

                $scope.phases[y] = $scope.phases[x];
                $scope.lists[y] = $scope.lists[x];

                $scope.phases[x] = b;
                $scope.lists[x] = acts;

                angular.forEach($scope.lists[x], function(i,a){
                    i.section = x;
                    ActivityService.newActivity(i).then(function(data){
                        console.log(data);
                    });
                });

                angular.forEach($scope.lists[y], function(i,a){
                    i.section = y;
                    ActivityService.newActivity(i).then(function(data){
                        console.log(data);
                    });
                });

                $scope.game.phases = $scope.phases;

                GameService.newGame($scope.game);
            });

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

        $scope.removeRun = function(run){
            //console.log(run);
            //$scope.phases.splice(index, 1);
            //$scope.game.phases = $scope.phases;
            RunService.deleteRun(run.runId);
        };

        $scope.createInquiryRun = function(){
            // Add the link between run and game
            $scope.run.gameId = $stateParams.gameId;
            RunService.newRun($scope.run).then(function(run){
                // Update run with data from the server
                $scope.run = run;

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

        $scope.addUsersToRun = function (run, game) {

            $scope.runVar = run;
            $scope.gameVar = game;

            var modalInstance = $modal.open({
                templateUrl: '/dist/templates/directives/modal_add_user.html',
                controller: 'AddUserCtrl',
                resolve: {
                    run: function () {
                        return $scope.runVar;
                    },
                    game: function () {
                        return $scope.gameVar;
                    }
                }
            });

            modalInstance.result.then(function (result){
                console.log($scope.usersRun[$scope.runVar]);
                angular.extend($scope.usersRun[$scope.runVar], result);
                console.log($scope.usersRun[$scope.runVar]);
            });

        };
    })

    .controller('AddUserCtrl', function ($scope, $modalInstance, AccountService, RunService, run, game) {

        $scope.refreshAccounts = function(query) {

            AccountService.searchAccount(query).then(function(data){
                $scope.availableColors = data.accountList;
            });
        };

        $scope.multipleDemo = {};
        $scope.multipleDemo.colors = [];

        $scope.ok = function () {

            var newUsers = [];

            angular.forEach($scope.multipleDemo.colors, function(value, key) {

                // Grant me access to the run
                RunService.giveAccess(run, value.accountType+":"+value.localId,1);
                // Add me as a user to the run
                var a = RunService.addUserToRun({
                    runId: run,
                    email: value.accountType+":"+value.localId,
                    accountType: value.accountType,
                    localId: value.localId,
                    gameId: game
                });
               newUsers.push(a);
            });

            $modalInstance.close(newUsers);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    })

    .controller('TabController', function ($scope, $stateParams, GameService, ActivityService) {
        this.tab = 0;

        GameService.getGameById($stateParams.gameId).then(function (data) {

            if (data.config.roles)
                $scope.roles = data.config.roles;

            $scope.game = data;

        });

        this.setTab = function (tabId) {

            this.tab = tabId;
            //ActivityService.getActivitiesForPhase($scope.game.gameId, tabId).then(function (data) {
            //    $scope.activities = data;
            //});

        };

        this.isSet = function (tabId) {
            return this.tab === tabId;
        };
    })
;