angular.module('DojoIBL')

    .controller('InquiryEditGameController', function ($scope, $sce, $stateParams, $state, $modal, Session, RunService, ActivityService,
                                                       AccountService, GameService, UserService, toaster ) {
        // Managing different tabs activities
        $scope.lists = [];
        $scope.selection = [];

        GameService.getGameById($stateParams.gameId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.game = data;
            console.log(data);

            if(!$scope.game.config.roles)
                $scope.game.config.roles = [];
            else{
                //$scope.game.config.roles = new JSONObject(data.config.roles);
            }

            if(!$scope.game.config.roles2)
                $scope.game.config.roles2 = [];
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
                {'name': 'Add text activity', 'type': 'org.celstec.arlearn2.beans.generalItem.NarratorItem', 'icon': 'fa-file-text'},
                {'name': 'Add external resource', 'type': 'org.celstec.arlearn2.beans.generalItem.VideoObject', 'icon': 'fa-external-link'},
                //{'name': 'Concept map', 'type': 'org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest', 'icon': 'fa-sitemap'},
                //{'name': 'External widget', 'type': 'org.celstec.arlearn2.beans.generalItem.OpenBadge', 'icon': 'fa-link'},
                //{'name': 'Research question', 'type': 'org.celstec.arlearn2.beans.generalItem.ResearchQuestion', 'icon': 'fa-question'}
                {'name': 'Add list activity', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-tasks'},
                {'name': 'Add data collection', 'type': 'org.celstec.arlearn2.beans.generalItem.ScanTag', 'icon': 'fa-picture-o'}
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

            toaster.success({
                title: 'Inquiry template modified',
                body: 'The inquiry "'+$scope.game.title+'" has been modified.'
            });
        };

        ////////////////////
        // Manage activities
        ////////////////////
        $scope.selected = false;
        $scope.activity = {};
        $scope.data = {
            model: null
        };
        $scope.data_old = {
            model: null
        };

        $scope.compare = function() {
            var bool_activity = !angular.equals($scope.activity_old, $scope.activity);
            var bool_roles = !angular.equals($scope.data_old, $scope.data);

            return (bool_roles) || (bool_activity);
        };

        $scope.saveActivity = function(){

            // Save array of roles into the activity
            $scope.activity_old = angular.copy($scope.activity);
            $scope.data_old.model = angular.copy($scope.data.model);

            $scope.activity.roles = [];
            $scope.activity.roles.push($scope.data.model);

            ActivityService.newActivity($scope.activity);
            $scope.game.lastModificationDate = new Date();
        };

        $scope.selectActivity = function(activity, combinationId){
            if(!angular.isUndefined($scope.activity_old)){
                if(!angular.equals($scope.activity_old, $scope.activity) || !angular.equals($scope.data_old.model, $scope.data.model)){
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

            if(!angular.isUndefined($scope.activity.roles)){

                $scope.data.model = $scope.activity.roles[0];
            }

            $scope.data_old.model = angular.copy($scope.data.model);

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
                //if(!angular.equals($scope.activity_old, $scope.activity)){
                if(!angular.equals($scope.activity_old, $scope.activity) || !angular.equals($scope.data_old.model, $scope.data.model)){
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

                        //$scope.activity = activity;
                        //
                        //$scope.activity_old = angular.copy(activity);
                        //
                        //// Save roles into an array
                        //$scope.selection = $scope.activity.roles || [];
                        //
                        //$scope.selected = true;
                        //
                        //$scope.selectedCombination = combinationId;

                    });
                    return;
                }
            }
        };

        $scope.isChecked = function(id){

            var match = false;
            for(var i=0 ; i < $scope.selection.length; i++) {
                if($scope.selection[i].name == id){
                    match = true;
                }
            }
            return match;
        };

        $scope.sync = function(bool, item){
            if(bool){
                // add item
                $scope.selection.push(item);
            } else {
                // remove item
                for(var i=0 ; i < $scope.selection.length; i++) {
                    if($scope.selection[i].name == item.name){
                        $scope.selection.splice(i,1);
                    }
                }
            }
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

            toaster.success({
                title: 'Activity added',
                body: 'The activity "'+a.name+'" has been added to the phase.'
            });
        };

        $scope.toggleSelection = function toggleSelection(rol) {
            var idx = $scope.selection.indexOf(rol.name);

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
            $scope.game.config.roles.push($scope.role);
            GameService.addRole($scope.game.gameId, $scope.role).then(function(data){
                console.log(data);
                $scope.game = data;
            });

            toaster.success({
                title: 'Role added to inquiry',
                body: 'The role "'+$scope.role.name+'" has been added.'
            });

            $scope.role = null;
        };

        $scope.editRole = function (index) {

            GameService.editRole($scope.game.gameId, $scope.role, $scope.index).then(function(data){
                $scope.game = data;
            });

            toaster.success({
                title: 'Role modified',
                body: 'The role "'+$scope.role.name+'" has been edited.'
            });

            $scope.role = null;
        };

        $scope.clearRole = function (index) {
            $scope.role = null;
        };

        $scope.selectRole = function(index){
            $scope.index = index;
            $scope.role = $scope.game.config.roles2[index];

            $scope.removeRole = function(){
                GameService.removeRole($scope.game.gameId, $scope.role).then(function(data){
                    $scope.game = data;
                });

                toaster.warning({
                    title: 'Role deleted',
                    body: 'The role "'+$scope.role.name+'" has been removed.'
                });

                $scope.role = null;
            };
        };

        ////////////////
        // Manage phases
        ////////////////
        $scope.currentPhase = 0;

        $scope.addPhase = function(){

            $scope.phases.push({
                //phaseId: $scope.phases.length,
                title: $scope.phaseName,
                type: "org.celstec.arlearn2.beans.game.Phase"
            });

            //$scope.lists.push($scope.phases.length);
            //$scope.lists = [];

            toaster.success({
                title: 'Phase added',
                body: 'The phase "'+$scope.phaseName+'" has been added to the inquiry template.'
            });

            $scope.phaseName = "";

            $scope.game.phases = $scope.phases;

            GameService.newGame($scope.game).then(function(updatedGame){

                angular.forEach($scope.gameRuns, function(value, key) {

                    value.game = updatedGame;

                    RunService.storeInCache(value);
                });
            });
        };

        //$scope.removePhase = function(index){
        //    swal({
        //        title: "Are you sure you want to delete the phase?",
        //        text: "You will not be able to recover this phase!",
        //        type: "warning",
        //        showCancelButton: true,
        //        confirmButtonColor: "#DD6B55",
        //        confirmButtonText: "Yes, delete the phase!",
        //        closeOnConfirm: false
        //    }, function () {
        //        swal("Phase deleted!", "The phase has been removed from the inquiry structure.", "success");
        //
        //        GameService.removePhase($scope.game, index);
        //
        //        toaster.warning({
        //            title: 'Phase deleted',
        //            body: 'The phase has been removed from the inquiry template.'
        //        });
        //    });
        //};

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

                toaster.warning({
                    title: 'Phase removed',
                    body: 'The phase "'+$scope.phases[index].title+'" has been removed from the inquiry template.'
                });

                $scope.phases.splice(index, 1);
                $scope.game.phases = $scope.phases;
                GameService.newGame($scope.game).then(function(updatedGame){
                    angular.forEach($scope.gameRuns, function(value, key) {
                        value.game = updatedGame;
                        RunService.storeInCache(value);
                    });
                });

                ActivityService.getActivitiesForPhase($scope.game.gameId, index).then(function(data){
                    angular.forEach(data, function(i, a){
                        ActivityService.deleteActivity(i.gameId, i.id);
                    });
                });
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

                angular.forEach($scope.lists[x], function(i, a){
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

                GameService.newGame($scope.game).then(function(updatedGame){

                    angular.forEach($scope.gameRuns, function(value, key) {
                        console.log(value, key);

                        value.game = updatedGame;

                        RunService.storeInCache(value);
                    });
                });

                toaster.success({
                    title: 'Phase moved',
                    body: 'The phase has been moved successfully.'
                });
            });
        };

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

                GameService.newGame($scope.game).then(function(updatedGame){

                    angular.forEach($scope.gameRuns, function(value, key) {
                        value.game = updatedGame;

                        RunService.storeInCache(value);
                    });
                });

                toaster.success({
                    title: 'Phase renamed',
                    body: 'The phase has been renamed successfully.'
                });

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

            swal({
                title: "Are you sure you want to delete the inquiry run?",
                text: "You will not be able to recover this inquiry run!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete the run!",
                closeOnConfirm: false
            }, function () {
                swal("Inquiry run deleted!", "The Inquiry Run has been removed from the list of inquiry runs.", "success");

                var idx = arrayObjectIndexOf($scope.gameRuns, run.runId, "runId");

                // is currently selected
                if (idx > -1) {
                    $scope.gameRuns.splice(idx, 1);
                }

                var updatedRun = RunService.deleteRun(run.runId);
            });
        };

        $scope.createInquiryRun = function(){
            // Add the link between run and game
            $scope.run.gameId = $stateParams.gameId;
            $scope.run.game = $scope.game;

            RunService.newRun($scope.run).then(function(run){
                // Update run with data from the server
                $scope.run = run;

                if(angular.isUndefined($scope.gameRuns)){
                    $scope.gameRuns = []
                }
                if(angular.isUndefined($scope.usersRun)){
                    $scope.usersRun = []
                }

                console.log($scope.accountsAccesGame);

                angular.forEach($scope.accountsAccesGame, function (gameAccess) {
                    if(gameAccess.accessRights == 1){
                        RunService.giveAccess(run.runId, gameAccess.account, 2);

                        RunService.addUserToRun({
                            runId: run.runId,
                            email: gameAccess.account,
                            accountType: gameAccess.accountType,
                            localId: gameAccess.localId,
                            gameId: run.game.gameId });
                    }
                });

                //$scope.gameRuns.push(run);
                AccountService.myDetails().then(function(data){

                    $scope.me = data;

                    // Grant me access to the run
                    RunService.giveAccess($scope.run.runId, data.accountType+":"+data.localId,1);

                    if(angular.isUndefined($scope.usersRun[$scope.run.runId])){
                        $scope.usersRun[$scope.run.runId] = []
                    }

                    var newUsers = [];

                    // Add me as a user to the run
                    var user = RunService.addUserToRun({
                        runId: $scope.run.runId,
                        email: data.accountType+":"+data.localId,
                        accountType: data.accountType,
                        localId: data.localId,
                        gameId: $stateParams.gameId
                    });

                    newUsers.push(user);

                    $scope.gameRuns[$scope.run.runId] = $scope.run;
                    angular.extend($scope.usersRun[$scope.run.runId], newUsers);

                    // Reset the run variable to create new ones
                    $scope.run = null;
                });
            });
        };

        $scope.addUsersToRun = function (run, game) {

            $scope.runVar = run;
            $scope.gameVar = game;

            var modalInstance = $modal.open({
                templateUrl: '/src/components/home/add.user.modal.html',
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
                angular.extend($scope.usersRun[$scope.runVar], result);

            });

        };

        ///////////////////////
        // Manage access rights
        ///////////////////////
        AccountService.myDetails().then(function(data){
           $scope.me = data;
        });

        GameService.getGameAccesses($stateParams.gameId).then(function(data){
            $scope.accountsAccesGame = {};
            angular.forEach(data.gamesAccess, function (gameAccess) {
                AccountService.accountDetailsById(gameAccess.account).then(function(data){
                    var data_extended = angular.extend({}, data, gameAccess);
                    $scope.accountsAccesGame[gameAccess.account] = data_extended;
                });
            });
        });

        $scope.grantEditorAccess = function (game, user){
            GameService.giveAccess(game.gameId, user.accountType+":"+user.localId,1);
            $scope.accountsAccesGame[user.account].accessRights = 1;

            angular.forEach($scope.gameRuns, function(run){
                RunService.giveAccess(run.runId, user.account, 2);

                RunService.addUserToRun({
                    runId: run.runId,
                    email: user.account,
                    accountType: user.accountType,
                    localId: user.localId,
                    gameId: run.game.gameId });
            });
        };

        $scope.revokeEditorAccess = function (game, user){
            GameService.giveAccess(game.gameId, user.accountType+":"+user.localId,2);
            $scope.accountsAccesGame[user.account].accessRights = 2;
        };

        //////////////////
        // Extra functions
        //////////////////
        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
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