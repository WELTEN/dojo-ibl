angular.module('DojoIBL')

    .controller('InquiryController', function ($scope, $sce, $stateParams, $state, Session, MessageService, AccountService, ChannelService, RunService) {

        $scope.chat = true;
        $scope.visualization = true;
        $scope.state = $state.current.name;

        $scope.disableInquiryLoading = false;



        RunService.getRunById($stateParams.runId).then(function (data) {
            $scope.inqTitle = data.title;
            $scope.inqTempTitle = data.game.title;
            $scope.inqDescription = data.game.description;
            $scope.inqId = data.runId;
            $scope.phases = data.game.phases;
            $scope.code = data.code;
            $scope.serverCreationTime = data.serverCreationTime;
            console.log(data);
            $scope.disableInquiryLoading = true;
        });

        //ChannelService.register('org.celstec.arlearn2.beans.run.Message', function (notification) {
        //    console.info("[Notification][Message]", notification);
        //    if (notification.runId == $stateParams.runId) {
        //        $scope.numberMessages += 1;
        //        MessageService.getMessageById(notification.messageId).then(function (data) {
        //            console.info("[Notification][Message]", data);
        //            //if(me.localId != data.senderId){
        //            //    $scope.sound = ngAudio.load("/src/assets/beep.m4a");
        //            //    $scope.sound.play();
        //            //}
        //        });
        //    }
        //});

        var fields = $(this.el).find('#circlemenu li'),
            container = $('#circlemenu'),
            width = container.width(),
            height = 200,
            angle = 300,
            radius = 100;


        //$scope.getTotal = function(){
        //    var total = 0;
        //    for(var i = 0; i < $scope.cart.products.length; i++){
        //        var product = $scope.cart.products[i];
        //        total += (product.price * product.quantity);
        //    }
        //    return total;
        //}

        $scope.calculateLeft = function(i) {

            angle += (2*Math.PI) / $scope.phases.length;

            angle *= i;
            //console.log(angle, width, height)


            return Math.round(width/2 + radius * Math.cos(angle) - width/2);
        };

        $scope.calculateTop = function(phases, i) {
            angle += (2*Math.PI) / phases.length;

            angle *= i;

            //console.log(angle, height)

            return Math.round(height/2 + radius * Math.sin(angle) - height/2);
        };

        //
        //$scope.arrangeInCircle = function(index){
        //    var radius = 170;
        //    var fields = $(this.el).find('#circlemenu li'),
        //        container = $(this.el).find('#circlemenu'),
        //        width = container.width(),
        //        height = container.height(),
        //        angle = 300,
        //        step = (2*Math.PI) / fields.length,
        //        x_initial = -4,
        //        y_initial = ;
        //
        //
        //
        //    //console.log(fields, container);
        //
        //    fields.each(function() {
        //
        //        //console.log(width, $(this).width());
        //
        //        var x = Math.round(width/2 + radius * Math.cos(angle) - $(this).width()/2);
        //        var y = Math.round(height/2 + radius * Math.sin(angle) - $(this).height()/2);
        //        //console.log(x,y);
        //        if(window.console) {
        //            //console.log($(this).text(), x, y);
        //        }
        //        $(this).css({
        //            left: x + 'px',
        //            top: y + 'px'
        //        });
        //        angle += step;
        //    });
        //};

        //

    }
);