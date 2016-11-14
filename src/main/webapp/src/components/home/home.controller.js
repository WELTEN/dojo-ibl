angular.module('DojoIBL')

    .controller('HomeController', function ($scope, $sce, Game, GameService, ActivityService, config, Session, RunService, ChannelService, AccountService) {

        $scope.games = {};
        $scope.runs = {};

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

        $scope.games = GameService.getGames();

        $scope.thumbnailUrl = function(gameId) {

            return config.server+'/game/'+gameId+'/gameThumbnail';
        };

        $scope.showRunsValue = true;

        $scope.showRuns = function (id) {

            RunService.getParticipateRunsForGame(id).then(function(data){
                console.log(data);
                $scope.runs[id] = {};
                $scope.runs[id] = data;
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

        $scope.isLoggedIn = function () {
            if (Session.getAccessToken() ) return true;
            return false;
        };

        ChannelService.register('org.celstec.arlearn2.beans.game.Game', function (notification) {
            console.info("[Notification][Game]", notification);

            GameService.refreshGame(notification.gameId).then(function (data) {
            });
        });

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    }
);