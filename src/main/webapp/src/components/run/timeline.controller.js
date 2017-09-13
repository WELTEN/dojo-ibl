angular.module('DojoIBL')

    //.factory("$infiniteScroll", ["$firebaseArray", "$rootScope",
    //    function ($firebaseArray, $rootScope) {
    //        return function (ref, pageMax) {
    //            var main;
    //            var current = pageMax;
    //            console.log(ref.path.toString());
    //            console.log(ref.database.ref().child(ref.path.toString()));
    //            var result = $firebaseArray(ref.database.ref().child(ref.path.toString()).limitToFirst(pageMax));
    //            result.$scroll = function () {
    //                if (result.length < current) return false;
    //                if (main) {
    //                    main.off("child_added");
    //                    main.off("child_removed");
    //                    main.off("child_changed");
    //                    main.off("child_moved");
    //                }
    //                current += pageMax;
    //                console.log(current);
    //                main = ref.database.ref().child(ref.path.toString()).limitToFirst(current);
    //                main.on("child_added", function ($snapshot, $prev) { if (result.$$added($snapshot, $prev)) { result.$$process("child_added", result.$$added($snapshot, $prev), $prev); $rootScope.$digest(); } });
    //                main.on("child_removed", function ($snapshot, $prev) { if (result.$$removed($snapshot)) { result.$$process("child_removed", result.$getRecord($snapshot.key)); $rootScope.$digest(); } });
    //                main.on("child_changed", function ($snapshot, $prev) { if (result.$$updated($snapshot)) { result.$$process("child_changed", result.$getRecord($snapshot.key)); $rootScope.$digest(); } });
    //                main.on("child_moved", function ($snapshot, $prev) { if (result.$$moved($snapshot, $prev)) { result.$$process("child_moved", result.$getRecord($snapshot.key), $prev); $rootScope.$digest(); } });
    //            }
    //
    //            return result;
    //        }
    //    }
    //])
    .controller('TimelineController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService, $firebaseArray) {

        var ctrl = this;
        var responsesRef = firebase.database().ref("responses").child($stateParams.runId);

        ctrl.getResponses = function() {
            $scope.responses = $firebaseArray(responsesRef);

            angular.forEach($scope.responses, function(key, value){
                console.log(key, value)
            });

            //$scope.responses = $firebaseArray(responsesRef.limitToLast($scope.pageSize * $scope.page));
        };

        //$scope.pageItems = $pageArray(responseValidRed, 'number');

        ctrl.getResponses();

        //
        //$scope.games = {};
        //$scope.games.games = [];
        //
        //$scope.disableGameLoading = false;

        var old_item_id = 0;
        var old_side_left = true;

        //ChannelService.register('org.celstec.arlearn2.beans.run.Response', function (data) {
        //    if(data.runId == $stateParams.runId){
        //        var responses = [];
        //
        //        if(data.generalItemId != old_item_id){
        //            if(old_side_left){
        //                data.move_right = false;
        //            }else{
        //                data.move_right = true;
        //            }
        //        }
        //
        //        old_item_id = data.generalItemId;
        //        old_side_left = data.move_right;
        //
        //        RunService.getRunById($stateParams.runId).then(function (run) {
        //            ActivityService.getActivityById(data.generalItemId, run.gameId).then(function(act){
        //                data.activity = act;
        //            });
        //        });
        //
        //        //UserService.getUserByAccount($stateParams.runId, data.userEmail.split(':')[1]).then(function(user){
        //        //    data.user = user;
        //        //});
        //        data.user = UserService.getUser($stateParams.runId, data.userEmail);
        //
        //
        //        responses.push(data);
        //        $scope.games.games = responses.concat($scope.games.games)
        //        //$scope.responses.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);
        //        //if((user.localId != data.userEmail.split(':')[1]) && data.generalItemId == $stateParams.activityId && data.runId == $stateParams.runId)
        //        //    //ResponseService.addResponse(data, $stateParams.runId, $stateParams.activityId);
        //        //console.info("[Notification][Response]", data, $stateParams.runId, $stateParams.activityId);
        //    }
        //});


        //$scope.loadMoreGames = function () {
        //
        //    $scope.disableGameLoading = true;
        //
        //    Response.resume({resumptionToken: $scope.games.resumptionToken, runId: $stateParams.runId, from: 0 })
        //        .$promise.then(function (data) {
        //
        //            var responses = [];
        //
        //            angular.forEach(data.responses, function(resp){
        //
        //                // TODO check this is not working well
        //                if(resp.generalItemId != old_item_id){
        //                    if(old_side_left){
        //                        resp.move_right = false;
        //                    }else{
        //                        resp.move_right = true;
        //                    }
        //                }
        //
        //                old_item_id = resp.generalItemId;
        //                old_side_left = resp.move_right;
        //
        //                RunService.getRunById($stateParams.runId).then(function (run) {
        //                    ActivityService.getActivityById(resp.generalItemId, run.gameId).then(function(data){
        //                        resp.activity = data;
        //                    });
        //                });
        //
        //                //UserService.getUserByAccount($stateParams.runId, resp.userEmail.split(':')[1]).then(function(data){
        //                //    resp.user = data;
        //                //});
        //
        //                resp.user = UserService.getUser($stateParams.runId, resp.userEmail);
        //
        //                responses.push(resp);
        //            });
        //
        //            $scope.games.games = $scope.games.games.concat(responses);
        //            $scope.games.resumptionToken = data.resumptionToken;
        //            $scope.games.serverTime = data.serverTime;
        //
        //            if (data.resumptionToken) {
        //                $scope.disableGameLoading = false
        //            } else {
        //                $scope.disableGameLoading = true
        //            }
        //        });
        //};


        //UserService.getUsersForRun($stateParams.runId).then(function(data){
        //
        //    $scope.usersRun = data;
        //
        //
        //});

        $scope.filter = {};

        // Functions - Definitions
        $scope.filterByCategory = function(response) {
            return $scope.filter[response.name] || noFilter($scope.filter);
        };

        function noFilter(filterObj) {
            return Object.
                keys(filterObj).
                every(function (key) { return !filterObj[key]; });
        }

    }
);