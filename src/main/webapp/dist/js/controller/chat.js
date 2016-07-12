angular.module('DojoIBL')

    .controller('InstantMessagingController', function ($window, $scope, $stateParams, Message, MessageService, ChannelService, AccountService, UserService) {

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.bodyMessage;

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

        $scope.messages.messages = MessageService.getMessages($stateParams.runId);

        $scope.numberMessages = 0;

        AccountService.myDetails().then(function(data) {
            var socket = new ChannelService.SocketHandler(data);
            socket.onMessage(function (data) {
                $scope.$apply(function () {
                    switch (data.type) {
                        case 'org.celstec.arlearn2.beans.notification.MessageNotification':

                            if (data.runId == $stateParams.runId) {
                                $scope.numberMessages += 1;
                                MessageService.getMessageById(data.messageId).then(function (data) {
                                    console.info("message received");
                                });
                            }

                            break;
                    }
                });
                //jQuery("time.timeago").timeago();
            });
        });

        $scope.notifications = [];
        $scope.waitingForData = function () {
            $scope.notifications.length == 0;
        };

        UserService.getUsersForRun($stateParams.runId).then(function(data){
            $scope.usersRun = data;
        });

        $scope.getUser = function (message){
            return UserService.getUserByAccount($stateParams.runId, message.senderId).name;
        };
        $scope.getAvatar = function (message){
            return UserService.getUserFromCache(message.senderId).picture;
        };
    }
);