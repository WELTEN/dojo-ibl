angular.module('DojoIBL')

    .controller('ProfileController', function ($scope, $sce, $stateParams, $state, Session, RunService) {

        $scope.chat = true;
        $scope.visualization = true;
        $scope.state = $state.current.name;

        $scope.disableInquiryLoading = false;

        RunService.getRunById($stateParams.runId).then(function (data) {
            $scope.inqTitle = data.title;
            $scope.inqId = data.runId;
            $scope.phases = data.game.phases;
            console.log(data);
            $scope.disableInquiryLoading = true;
        });

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

    }
);