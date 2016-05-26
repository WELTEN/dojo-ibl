angular.module('DojoIBL')

    .controller('InquiryEditGameController', function ($scope, $sce, $stateParams, $state, Session, RunService, GameService ) {

        console.log($stateParams.runId);

        $scope.roles = [];

        RunService.getRunById($stateParams.runId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.game = data.game;

            console.log($scope.game);

            if(!$scope.game.config.roles)
                $scope.game.config.roles = [];

            $scope.run = data;

            if (data.lat) {
                $scope.coords.latitude = data.lat;
                $scope.coords.longitude = data.lng;
                $scope.map.center.latitude = data.lat;
                $scope.map.center.longitude = data.lng;
                $scope.showMap = true;
            }
        });

        $scope.addRole = function () {

            $scope.game.config.roles.push({
                name: $scope.roleName
            });

            GameService.newGame($scope.game);
            RunService.newRun($scope.run);

            $scope.roleName = "";

        };

        $scope.selectRole = function(index){

            $scope.removeRole = function(){
                $scope.game.config.roles.splice(index, 1);
                GameService.newGame($scope.game);
                RunService.newRun($scope.run);
            };

        };

        $scope.addPhase = function(){

            swal({
                title: "New phase",
                text: "Provide a new for the phase",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: true,
                animation: "slide-from-top",
                inputPlaceholder: "Example: Data collection"
            }, function (inputValue) {
                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                $scope.run.game.phases.push({
                    phaseId: 1,
                    title: inputValue,
                    type: "org.celstec.arlearn2.beans.game.Phase"
                });

                GameService.newGame($scope.run.game);
                RunService.newRun($scope.run);

            });
        };

        $scope.removePhase = function(index){

            $scope.run.game.phases.splice(index, 1);

            GameService.newGame($scope.run.game);
            RunService.newRun($scope.run);
        };

        $scope.ok = function(){
            RunService.newRun($scope.run);
            //$window.history.back();
        };

        //$scope.chat = true;
        //$scope.visualization = true;
        //$scope.state = $state.current.name;
        //
        //RunService.getRunById($stateParams.runId).then(function (data) {
        //    $scope.inqTitle = data.title;
        //    $scope.inqId = data.runId;
        //    $scope.phases = data.game.phases;
        //    console.log(data);
        //});

        //var radius = 170;
        //var fields = $(this.el).find('#circlemenu li'),
        //    container = $(this.el).find('#circlemenu'),
        //    width = container.width(),
        //    height = container.height(),
        //    angle = 300,
        //    step = (2*Math.PI) / fields.length;
        //
        ////console.log(fields, container);
        //
        //fields.each(function() {
        //
        //    //console.log(width, $(this).width());
        //
        //    var x = Math.round(width/2 + radius * Math.cos(angle) - $(this).width()/2);
        //    var y = Math.round(height/2 + radius * Math.sin(angle) - $(this).height()/2);
        //    //console.log(x,y);
        //    if(window.console) {
        //        //console.log($(this).text(), x, y);
        //    }
        //    $(this).css({
        //        left: x + 'px',
        //        top: y + 'px'
        //    });
        //    angle += step;
        //});

    }).controller('TabController', function ($scope, $stateParams, RunService, ActivityService) {
        this.tab = 0;

        RunService.getRunById($stateParams.runId).then(function (data) {

            console.log(data.game);

            if (data.game.config.roles)
                $scope.roles = data.game.config.roles;

            $scope.run = data;

        });

        this.setTab = function (tabId) {
            this.tab = tabId;
            console.log("set" + tabId);
            ActivityService.getActivitiesForPhase($scope.run.game.gameId, tabId).then(function (data) {
                console.log(data);
                $scope.activities = data;
            });
        };

        this.isSet = function (tabId) {
            return this.tab === tabId;
        };
    })
;