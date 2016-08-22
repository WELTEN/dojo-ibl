angular.module('DojoIBL')

    .controller('InstantMessagingController', function ($window, $scope, $stateParams, Message, MessageService, ResponseService, ChannelService, AccountService, UserService) {

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

        AccountService.myDetails().then(function(me) {
            var socket = new ChannelService.SocketHandler(me);

            socket.onMessage(function (data) {
                $scope.$apply(function () {
                    switch (data.type) {
                        case 'org.celstec.arlearn2.beans.notification.MessageNotification':
                            if (data.runId == $stateParams.runId) {
                                $scope.numberMessages += 1;
                                MessageService.getMessageById(data.messageId).then(function (data) {
                                    console.info("[Notification][Message]");
                                });
                            }
                            break;
                        case 'org.celstec.arlearn2.beans.run.Response':
                            //$scope.responses.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);
                            if(me.localId != data.userEmail.split(':')[1])
                                ResponseService.addResponse(data, $stateParams.runId, $stateParams.activityId);
                            console.info("[Notification][Response]", data, $stateParams.runId, $stateParams.activityId);
                            break;
                    }
                });
            });
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