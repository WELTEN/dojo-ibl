angular.module('DojoIBL')

    .service('ChannelService', function ($q, $http, CacheFactory) {

        CacheFactory('channelAPICache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        return {
            SocketHandler: function(user){

                var dataCache = CacheFactory.get('channelAPICache');

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

                    dataCache.put(user.accountType+":"+user.localId, channelData);

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


                if (!dataCache.get(user.accountType+":"+user.localId)) {
                    console.log("no existia")

                    $http({url: "/rest/channelAPI/token", method: 'GET'}).success(this.socketCreationCallback);
                }else{

                    var channelData = dataCache.get(user.accountType+":"+user.localId);
                    console.log("existia",channelData)

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
                        dataCache.remove(user.accountType+":"+user.localId);
                    };

                    context.channelSocket = socket;
                    //console.log("Channel info received");
                    //console.log(channelData.channelId);
                    context.channelSocket.onmessage = context.messageCallback;
                }
                //$.getJSON("/rest/channelAPI/token", this.socketCreationCallback);
            }
        }
    }
);