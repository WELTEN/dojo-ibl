angular.module('DojoIBL')

    .controller('InstantMessagingController', function ($window, $scope, $stateParams, Message, MessageService,
                                                        ChannelService, AccountService, UserService, ngAudio, firebase, $firebaseArray) {

        var ctrl = this;

        $scope.account = AccountService.myDetailsCache();


        $scope.pageSize = 25;
        $scope.page = 1;

        var messagesRef = firebase.database().ref("messages").child($stateParams.runId);

        ctrl.getMessages = function() {
            $scope.messages = $firebaseArray(messagesRef.limitToLast($scope.pageSize * $scope.page));
        };

        ctrl.getMessages();

        $scope.sendMessage = function() {
            AccountService.myDetails().then(function(data){
                $scope.account = data;
                //console.log(data)
                if($scope.bodyMessage){

                    $scope.messages.$add({
                        localId: data.localId,
                        name: data.name,
                        body: $scope.bodyMessage,
                        picture: data.picture,
                        date: firebase.database.ServerValue.TIMESTAMP
                    });

                    $scope.bodyMessage = '';

                    //MessageService.sendNotification();


                    //MessageService.newMessage({
                    //    runId: $stateParams.runId,
                    //    threadId: data.accountType + ":" + data.localId,
                    //    subject: "empty",
                    //    body: m
                    //}).then(function(data){
                    //});
                    //
                    //MessageService.sendNotification();

                }
            });

        };


        $scope.bodyMessage;
        $scope.glued = true;


        //var detachOnAuth = Auth.authObj.$onAuth(function(authData) {
        //    if (!authData) {
        //        return;
        //    }
        //    roomUserRef = roomUsersRef.child(authData.uid);
        //    roomUserRef.set(true);
        //    roomUserRef.onDisconnect().remove(); // User disconnected
        //});


        //const messaging = firebase.messaging();
        //
        //// Handle incoming messages. Called when:
        //// - a message is received while the app has focus
        //// - the user clicks on an app notification created by a sevice worker
        ////   `messaging.setBackgroundMessageHandler` handler.
        //messaging.onMessage(function(payload) {
        //    console.log("Message received. ", payload);
        //});


        //var loadMessages = function(anyThing){
        //    UserService.getUsersForRun(anyThing).then(function(data){
        //        $scope.usersRun = data;
        //    }).then(function(){
        //        $scope.messages = {};
        //        $scope.messages.messages = [];
        //
        //        $scope.loadMoreButton = false;
        //        $scope.disableMessagesLoading = false;
        //
        //        // Option 1
        //        $scope.messages.messages = MessageService.getMessages($stateParams.runId);
        //
        //        // Option 2 - new
        //        //$scope.loadMoreMessages = function () {
        //        //
        //        //    $scope.disableMessagesLoading = true;
        //        //
        //        //    Message.resume({resumptionToken: $scope.messages.resumptionToken, runId: $stateParams.runId, from: 0 })
        //        //        .$promise.then(function (data) {
        //        //
        //        //            var messages = [];
        //        //
        //        //            angular.forEach(data.messages, function(message){
        //        //                messages.push(message);
        //        //            });
        //        //
        //        //            $scope.messages.messages = $scope.messages.messages.concat(messages);
        //        //            $scope.messages.resumptionToken = data.resumptionToken;
        //        //            $scope.messages.serverTime = data.serverTime;
        //        //
        //        //            if (data.resumptionToken) {
        //        //                $scope.disableMessagesLoading = false
        //        //            } else {
        //        //                $scope.disableMessagesLoading = true
        //        //            }
        //        //        });
        //        //};
        //    });
        //}
        //$scope.scroll = 0;
        //AccountService.myDetails().then(
        //    function(data){
        //        $scope.myAccount = data;
        //    }
        //);



        //$scope.$on('inquiry-run', function(event, args) {
        //    $scope.disableMessagesLoading = true;
        //    loadMessages(args.runId)
        //});
        //
        //
        //loadMessages($stateParams.runId);
        //
        //
        //




        //$scope.numberMessages = 0;



        //ChannelService.register('org.celstec.arlearn2.beans.run.Message', function (notification) {
        //    if (notification.runId == $stateParams.runId) {
        //        $scope.numberMessages += 1;
        //
        //        MessageService.getMessageById(notification.messageId).then(function (data) {
        //            if($scope.account.localId != data.senderId){
        //                console.info("[Notification][Message]", notification, $scope.account.localId, data.senderId);
        //
        //                notifyMe(notification);
        //                //$scope.sound = ngAudio.load("/src/assets/beep.m4a");
        //                //$scope.sound.play();
        //            }
        //
        //
        //        });
        //
        //
        //
        //    }
        //});

        //$scope.notifications = [];
        //$scope.waitingForData = function () {
        //    $scope.notifications.length == 0;
        //};
        //
        //
        //$scope.people = [
        //    { label: 'Joe'},
        //    { label: 'Mike'},
        //    { label: 'Diane'}
        //]
        //
        //function notifyMe(message) {
        //
        //    var not = {};
        //    not.body = message.body;
        //
        //    AccountService.accountDetailsById(message.senderProviderId+":"+message.senderId).then(function(account){
        //        not.icon = account.picture;
        //        not.title = account.name;
        //        console.log(not)
        //
        //
        //
        //        // Let's check if the browser supports notifications
        //        if (!("Notification" in window)) {
        //            alert("This browser does not support desktop notification");
        //        }
        //
        //        // Let's check whether notification permissions have already been granted
        //        else if (Notification.permission === "granted") {
        //            // If it's okay let's create a notification
        //            var notification = new Notification(not.title, {
        //                body: not.body,
        //                icon:not.icon
        //            });
        //        }
        //
        //        // Otherwise, we need to ask the user for permission
        //        else if (Notification.permission !== 'denied') {
        //            Notification.requestPermission(function (permission) {
        //                // If the user accepts, let's create a notification
        //                if (permission === "granted") {
        //                    var notification = new Notification(not.title, {
        //                        body: not.body,
        //                        icon:not.icon
        //                    });
        //                }
        //            });
        //        }
        //
        //
        //    });
        //

            // At last, if the user has denied notifications, and you
            // want to be respectful there is no need to bother them any more.
        //}
    }
);