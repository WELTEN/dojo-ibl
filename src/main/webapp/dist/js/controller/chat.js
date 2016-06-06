angular.module('DojoIBL')

    .controller('InstantMessagingController', function ($scope, $stateParams, Message, MessageService, ChannelService, AccountService, UserService) {

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.bodyMessage;

        $scope.onKeydown = function(keycode){
            // do something with the keycode
            console.log(keycode)
        };

        $scope.sendMessage = function() {
            AccountService.myDetails().then(function(data){
                MessageService.newMessage({
                    runId: $stateParams.runId,
                    threadId: data.accountType + ":" + data.localId,
                    subject: "empty",
                    body: $scope.bodyMessage
                }).then(function(data){
                    //$scope.messages.messages.push(data);
                    $scope.bodyMessage = null;
                });
            });
        };

        $scope.messages = {};
        $scope.messages.messages = [];

        MessageService.getMessagesDefaultByRun($stateParams.runId).then(function (data) {

            $scope.messages.messages = $scope.messages.messages.concat(data.messages);

            if (data.resumptionToken) {
                $scope.showButtonMore = true;
            }else{
                $scope.showButtonMore = false;
            }
        });


        var socket = new ChannelService.SocketHandler();
        socket.onMessage(function (data) {
            $scope.$apply(function () {
                switch (data.type) {
                    case 'org.celstec.arlearn2.beans.notification.MessageNotification':

                        if(data.runId == $stateParams.runId){
                            MessageService.getMessageById(data.messageId).then(function (data) {
                                $scope.messages.messages = $scope.messages.messages.concat(data);
                            });
                        }

                        break;
                }
            });
            //jQuery("time.timeago").timeago();
        });

        $scope.notifications = [];
        $scope.waitingForData = function () {
            $scope.notifications.length == 0;
        };

        UserService.getUsersForRun($stateParams.runId).then(function(data){
            $scope.usersRun = data;
        });

        $scope.getUser = function (message){
            return UserService.getUserFromCache(message.senderId).name;
        };
        $scope.getAvatar = function (message){
            return UserService.getUserFromCache(message.senderId).picture;
        };
    }
);