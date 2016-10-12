angular.module('DojoIBL')

    .controller('InstantMessagingController', function ($window, $scope, $stateParams, Message, MessageService,
                                                        ChannelService, AccountService, UserService, ngAudio) {
        $scope.scroll = 0;
        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.bodyMessage;
        $scope.glued = true;

        $scope.sendMessage = function() {
            AccountService.myDetails().then(function(data){
                MessageService.newMessage({
                    runId: $stateParams.runId,
                    threadId: data.accountType + ":" + data.localId,
                    subject: "empty",
                    body: $scope.bodyMessage
                }).then(function(data){
                    $scope.bodyMessage = null;
                });
            });
        };

        $scope.messages = {};
        $scope.messages.messages = [];

        $scope.loadMoreButton = false;
        $scope.disableMessagesLoading = false;

        // Option 1
        $scope.messages.messages = MessageService.getMessages($stateParams.runId);

        // Option 2 - new
        //$scope.loadMoreMessages = function () {
        //
        //    $scope.disableMessagesLoading = true;
        //
        //    Message.resume({resumptionToken: $scope.messages.resumptionToken, runId: $stateParams.runId, from: 0 })
        //        .$promise.then(function (data) {
        //
        //            var messages = [];
        //
        //            angular.forEach(data.messages, function(message){
        //                messages.push(message);
        //            });
        //
        //            $scope.messages.messages = $scope.messages.messages.concat(messages);
        //            $scope.messages.resumptionToken = data.resumptionToken;
        //            $scope.messages.serverTime = data.serverTime;
        //
        //            if (data.resumptionToken) {
        //                $scope.disableMessagesLoading = false
        //            } else {
        //                $scope.disableMessagesLoading = true
        //            }
        //        });
        //};

        $scope.numberMessages = 0;

        ChannelService.register('org.celstec.arlearn2.beans.run.Message', function (notification) {
            console.info("[Notification][Message]", notification);
            if (notification.runId == $stateParams.runId) {
                $scope.numberMessages += 1;
                MessageService.getMessageById(notification.messageId).then(function (data) {
                    console.info("[Notification][Message]", data);
                    //if(me.localId != data.senderId){
                    //    $scope.sound = ngAudio.load("/dist/assets/beep.m4a");
                    //    $scope.sound.play();
                    //}
                });
            }
        });

        $scope.notifications = [];
        $scope.waitingForData = function () {
            $scope.notifications.length == 0;
        };

        UserService.getUsersForRun($stateParams.runId).then(function(data){
            $scope.usersRun = data;
        });
    }
);