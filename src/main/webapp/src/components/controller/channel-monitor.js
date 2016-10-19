angular.module('DojoIBL')

    .controller('ChannelMonitorController', function ($scope, $http, $rootScope) {

        var SocketHandler = function () {
            this.messageCallback = function () {
            };

            this.onMessage = function (callback) {
                var theCallback = function (message) {
                    callback(JSON.parse(message.data));
                }

                if (this.channelSocket == undefined) {
                    this.messageCallback = theCallback;
                } else {
                    this.channelSocket.onmessage = theCallback;
                }
            }

            var context = this;
            this.socketCreationCallback = function (channelData) {
                var channel = new goog.appengine.Channel(channelData.token);
                context.channelId = channelData.channelId;
                var socket = channel.open();
                socket.onerror = function () {
                    //console.log("Channel error");
                };
                socket.onclose = function () {
                    //console.log("Channel closed, reopening");
                    //We reopen the channel
                    context.messageCallback = context.channelSocket.onmessage;
                    context.channelSocket = undefined;
                    $.getJSON("chats/channel", context.socketCreationCallback);
                };
                context.channelSocket = socket;
                //console.info("Channel info received");
                context.channelSocket.onmessage = context.messageCallback;
            };

            $http({url: "/rest/channelAPI/token", method: 'GET'}).success(this.socketCreationCallback);
            //$.getJSON("/rest/channelAPI/token", this.socketCreationCallback);
        };

        //var socket = new SocketHandler();
        //socket.onMessage(function (data) {
        //    console.log(data);
        //
        //    $rootScope.$apply(function () {
        //        //$scope.notifications.push({
        //        //    sort: new Date(),
        //        //    time: new Date().toISOString(),
        //        //    json: JSON.stringify(data, undefined, 2)
        //        //});
        //        ////switch (data.type) {
        //        ////    //case 'org.celstec.arlearn2.beans.notification.GeneralItemModification':
        //        ////    //    //GeneralItemService.handleNotification(data);
        //        ////    //    console.log("Received ")
        //        ////    //    break;
        //        ////}
        //
        //    });
        //    jQuery("time.timeago").timeago();
        //});

        $scope.notifications = [];
        $scope.waitingForData = function () {
            $scope.notifications.length == 0;
        }

    });