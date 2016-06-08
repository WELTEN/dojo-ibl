angular.module('DojoIBL')

    .service('ChannelService', function ($q, $http) {
        return {
            SocketHandler: function(){
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
                        console.log("Channel error");
                    };
                    socket.onclose = function () {
                        console.log("Channel closed, reopening");
                        //We reopen the channel
                        context.messageCallback = context.channelSocket.onmessage;
                        context.channelSocket = undefined;
                        $.getJSON("chats/channel", context.socketCreationCallback);
                    };
                    context.channelSocket = socket;
                    //console.log("Channel info received");
                    //console.log(channelData.channelId);
                    context.channelSocket.onmessage = context.messageCallback;
                };

                $http({url: "/rest/channelAPI/token", method: 'GET'}).success(this.socketCreationCallback);
                //$.getJSON("/rest/channelAPI/token", this.socketCreationCallback);
            }
        }
    }
);