angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, ActivityService, config, Session, RunService, ChannelService,
                                            toaster, AccountService) {


        $scope.games = {};
        $scope.runs = {};

        $scope.games = GameService.getGames();

        function loadGames() {
            GameService.resumeLoadingGames().then(function (data) {
                if (data.error) {
                    $scope.showNoAccess = true;
                } else {
                    $scope.show = true;
                    if (data.resumptionToken) {
                        loadGames();
                    }
                }
            });
        }

        //if(isEmpty($scope.games)){
        loadGames();
        //}



        $scope.thumbnailUrl = function(gameId) {

            return config.server+'/game/'+gameId+'/gameThumbnail';
        };

        $scope.showRunsValue = true;

        $scope.showRuns = function (id) {
            $scope.gameSelected = id;
            RunService.getParticipateRunsForGame(id).then(function(data){
                $scope.runs[id] = {};
                $scope.runs[id] = data;

                if(Object.keys(data).length == 0){
                    toaster.warning({
                        title: 'No inquiry groups',
                        body: 'Inquiry without inquiry group. Go to Teacher View > step 5 to add one.'
                    });
                }

            });
        };

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.cloneInquiry = function(id){
            swal({
                title: "Clone inquiry",
                text: "Are you sure you want to clone the inquiry?",
                type: "input",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, clone it!",
                inputPlaceholder: "Title of the cloned inquiry",
                closeOnConfirm: false
            }, function (inputValue) {

                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                swal("Well done!", "You have cloned the inquiry", "success");

                GameService.cloneInquiry(id, inputValue);

            });
        };



        $scope.deleteInquiry = function (id) {

            swal({
                title: "Are you sure?",
                text: "You will not be able to recover this inquiry!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                closeOnConfirm: false
            }, function () {
                swal("Deleted!", "Your inquiry has been deleted.", "success");

                var idx = arrayObjectIndexOf($scope.games, GameService.getGameFromCache(id).gameId, "gameId");

                // is currently selected
                if (idx > -1) {
                    $scope.games.splice(idx, 1);
                }

                GameService.deleteGame(id);

            });

        };

        $scope.config = {
            itemsPerPage: 5,
            fillLastPage: true
        };


        //ChannelService.register('org.celstec.arlearn2.beans.game.Game', function (notification) {
        //    //console.info("[Notification][Game]", notification);
        //
        //    if(!angular.isUndefined(notification.gameId)){
        //        GameService.refreshGame(notification.gameId);
        //    }
        //});

        $scope.findAndJoin = function(){

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    //////
                    // AccessRight explanation
                    // 1: Editor
                    // 2: User

                    console.log(user);

                    GameService.giveAccess(run.game.gameId, user.accountType+":"+user.localId,2);

                    RunService.giveAccess(run.runId, user.accountType+":"+user.localId,2);

                    RunService.addUserToRun({
                        runId: run.runId,
                        email: user.accountType+":"+user.localId,
                        accountType: user.accountType,
                        localId: user.localId,
                        gameId: run.game.gameId });

                    window.location.href=config.server+'/#/inquiry/'+run.runId;
                });
            });

            $scope.inquiryCode = null;
        }


        // New home page
        $scope.gameSelected;

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    })
    .directive('fooRepeatDone', function() {
        return function($scope, element) {
            if ($scope.$last) { // all are rendered
                $('.table').trigger('footable_redraw');
            }
        }
    })
    .controller('GroupsController', function ($scope, $sce, Game, GameService, ActivityService, config, Session, RunService, ChannelService,
                                            toaster, AccountService) {


        $scope.runs = {};



        $scope.runs = RunService.getRuns();

        RunService.getParticipatedRuns();


        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.cloneInquiry = function(id){
            swal({
                title: "Clone inquiry",
                text: "Are you sure you want to clone the inquiry?",
                type: "input",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, clone it!",
                inputPlaceholder: "Title of the cloned inquiry",
                closeOnConfirm: false
            }, function (inputValue) {

                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                swal("Well done!", "You have cloned the inquiry", "success");

                GameService.cloneInquiry(id, inputValue);

            });
        };



        $scope.deleteInquiry = function (id) {

            swal({
                title: "Are you sure?",
                text: "You will not be able to recover this inquiry!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                closeOnConfirm: false
            }, function () {
                swal("Deleted!", "Your inquiry has been deleted.", "success");

                var idx = arrayObjectIndexOf($scope.games, GameService.getGameFromCache(id).gameId, "gameId");

                // is currently selected
                if (idx > -1) {
                    $scope.games.splice(idx, 1);
                }

                GameService.deleteGame(id);

            });

        };

        $scope.config = {
            itemsPerPage: 5,
            fillLastPage: true
        };


        //ChannelService.register('org.celstec.arlearn2.beans.game.Game', function (notification) {
        //    //console.info("[Notification][Game]", notification);
        //
        //    if(!angular.isUndefined(notification.gameId)){
        //        GameService.refreshGame(notification.gameId);
        //    }
        //});

        $scope.findAndJoin = function(){

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    //////
                    // AccessRight explanation
                    // 1: Editor
                    // 2: User

                    console.log(user);

                    GameService.giveAccess(run.game.gameId, user.accountType+":"+user.localId,2);

                    RunService.giveAccess(run.runId, user.accountType+":"+user.localId,2);

                    RunService.addUserToRun({
                        runId: run.runId,
                        email: user.accountType+":"+user.localId,
                        accountType: user.accountType,
                        localId: user.localId,
                        gameId: run.game.gameId });

                    window.location.href=config.server+'/#/inquiry/'+run.runId;
                });
            });

            $scope.inquiryCode = null;
        }


        // New home page
        $scope.gameSelected;

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    });