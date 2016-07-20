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
                };

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

                        console.warn("Channel closed, reopening");
                        //We reopen the channel
                        context.messageCallback = context.channelSocket.onmessage;
                        context.channelSocket = undefined;
                        $.getJSON("chats/channel", context.socketCreationCallback);
                        dataCache.remove(user.accountType+":"+user.localId);
                        console.warn('Channel closed.');

                        swal({
                            title: "Timeout!",
                            text: "The session has timed out. Refresh!",
                            type: "warning",
                            showCancelButton: false,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "Refresh!!",
                            closeOnConfirm: false
                        }, function () {
                            //window.location.replace(window.location.href);
                            location.reload();
                            //swal("Timeout!", "Refresh this page to continue.", "success");
                        });
                    };
                    context.channelSocket = socket;
                    //console.info("Channel info received");
                    context.channelSocket.onmessage = context.messageCallback;
                };

                $http({url: "/rest/channelAPI/token", method: 'GET'}).success(this.socketCreationCallback);

                //var context = this;
                //this.socketCreationCallback = function (channelData) {
                //
                //    var channel = new goog.appengine.Channel(channelData.token);
                //
                //    dataCache.put(user.accountType+":"+user.localId, channelData);
                //
                //    context.channelId = channelData.channelId;
                //
                //    var socket = channel.open();
                //
                //    socket.onerror = function () {
                //        console.log("Channel error");
                //    };
                //
                //    socket.onclose = function () {
                //        console.log("Channel closed, reopening");
                //        //We reopen the channel
                //        context.messageCallback = context.channelSocket.onmessage;
                //        context.channelSocket = undefined;
                //        $.getJSON("chats/channel", context.socketCreationCallback);
                //    };
                //
                //    context.channelSocket = socket;
                //    //console.log("Channel info received");
                //    //console.log(channelData.channelId);
                //    context.channelSocket.onmessage = context.messageCallback;
                //};
                //
                //
                //if (!dataCache.get(user.accountType+":"+user.localId)) {
                //    $http({url: "/rest/channelAPI/token", method: 'GET'}).success(this.socketCreationCallback);
                //}else{
                //    var channelData = dataCache.get(user.accountType+":"+user.localId);
                //
                //    var channel = new goog.appengine.Channel(channelData.token);
                //    context.channelId = channelData.channelId;
                //
                //
                //    var socket = channel.open();
                //
                //    socket.onopen = function(){
                //        console.warn("Channel opened");
                //
                //    };
                //
                //    socket.onmessage = function(){
                //        console.warn("Channel opened");
                //
                //    };
                //
                //    socket.onerror = function () {
                //        console.warn("Channel error");
                //    };
                //
                //    socket.onclose = function () {
                //        console.warn("Channel closed, reopening");
                //        //We reopen the channel
                //        context.messageCallback = context.channelSocket.onmessage;
                //        context.channelSocket = undefined;
                //        $.getJSON("chats/channel", context.socketCreationCallback);
                //        dataCache.remove(user.accountType+":"+user.localId);
                //        console.warn('Channel closed.');
                //
                //        swal({
                //            title: "Timeout!",
                //            text: "The session has timed out. Refresh!",
                //            type: "warning",
                //            showCancelButton: false,
                //            confirmButtonColor: "#DD6B55",
                //            confirmButtonText: "Refresh!!",
                //            closeOnConfirm: false
                //        }, function () {
                //            //window.location.replace(window.location.href);
                //            location.reload();
                //            //swal("Timeout!", "Refresh this page to continue.", "success");
                //        });
                //    };
                //
                //    context.channelSocket = socket;
                //    context.channelSocket.onmessage = context.messageCallback;
                //}
            }
        }
    }
);