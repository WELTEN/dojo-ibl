angular.module('DojoIBL')

    .controller('InquiryEditRunController', function ($scope, $sce, $stateParams, $state, Session, RunService, UserService ) {

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

        UserService.getUsersForRun($stateParams.runId).then(function(data){

            //$scope.usersRun[value.runId] = data;
            $scope.usersRun = data;
            console.log(data);

        });

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

    });