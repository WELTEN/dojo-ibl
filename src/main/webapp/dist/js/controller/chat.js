angular.module('DojoIBL')

    .controller('InstantMessagingController', function ($scope, $stateParams, Message, MessageService, ChannelService, AccountService) {

        $scope.bodyMessage;

        $scope.sendMessage = function() {
            AccountService.myDetails().then(function(data){
                MessageService.newMessage({
                    runId: $stateParams.runId,
                    threadId: data.accountType + ":" + data.localId,
                    subject: "empty",
                    body: $scope.bodyMessage
                });
                $scope.bodyMessage = null;
            });
        };

        $scope.messages = {};
        $scope.messages.messages = [];

        $scope.loadMoreMessages = function () {

            $scope.disableMessagesLoading = true;

            Message.resume({resumptionToken: $scope.messages.resumptionToken, runId: $stateParams.runId, from: 0 }).$promise.then(function (data) {
                    $scope.messages.messages = $scope.messages.messages.concat(data.messages);
                    //for (i = 0; i < data.responses.length; i++) {
                    //    //GameService.storeInCache(data.games[i]);
                    //    data.responses[i].description = $sce.trustAsHtml(data.games[i].description);
                    //
                    //}
                    //$scope.games = GameService.getGames();
                    $scope.messages.resumptionToken = data.resumptionToken;
                    $scope.messages.serverTime = data.serverTime;

                    console.log(data);

                    if (data.resumptionToken) {
                        $scope.disableMessagesLoading = false
                    } else {
                        $scope.disableMessagesLoading = true
                    }
                });
        };

        //
        //$scope.loadMoreMessages = function () {
        //
        //    $scope.disableMessageLoading = true;
        //
        //    MessageService.getMessagesDefaultByRun($stateParams.runId).then(function(data){
        //        if (data.error) {
        //            $scope.showNoAccess = true;
        //        } else {
        //            $scope.show = true;
        //        }
        //
        //        $scope.messages = data;
        //        console.log(data);
        //
        //    });
        //
        //    //Response.resume({resumptionToken: $scope.games.resumptionToken, runId: $stateParams.runId, from: 0 })
        //    //    .$promise.then(function (data) {
        //    //
        //    //
        //    //        $scope.games.games = $scope.games.games.concat(data.responses);
        //    //        //for (i = 0; i < data.responses.length; i++) {
        //    //        //    //GameService.storeInCache(data.games[i]);
        //    //        //    data.responses[i].description = $sce.trustAsHtml(data.games[i].description);
        //    //        //
        //    //        //}
        //    //        //$scope.games = GameService.getGames();
        //    //        $scope.games.resumptionToken = data.resumptionToken;
        //    //        $scope.games.serverTime = data.serverTime;
        //    //
        //    //        if (data.resumptionToken) {
        //    //            $scope.disableMessageLoading = false
        //    //        } else {
        //    //            $scope.disableMessageLoading = true
        //    //        }
        //    //
        //    //    });
        //
        //};




        //var socket = new ChannelService.SocketHandler();
        //socket.onMessage(function (data) {
        //    $scope.$apply(function () {
        //        console.log(data);
        //        console.log(data.type);
        //        //$scope.notifications.push({
        //        //    sort: new Date(),
        //        //    time: new Date().toISOString(),
        //        //    json: JSON.stringify(data, undefined, 2)
        //        //});
        //        switch (data.type) {
        //            case 'org.celstec.arlearn2.beans.notification.GeneralItemModification':
        //                //GeneralItemService.handleNotification(data);
        //                console.log("Received ")
        //                break;
        //        }
        //
        //    });
        //    //jQuery("time.timeago").timeago();
        //});
        //
        //$scope.notifications = [];
        //$scope.waitingForData = function () {
        //    $scope.notifications.length == 0;
        //}
    }
);