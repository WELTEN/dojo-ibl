angular.module('DojoIBL')

    .service('MessageService', function ($q, Message, CacheFactory, UserService) {

        CacheFactory('messagesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var messages = {};
        var dataCache = CacheFactory.get('messagesCache');
        var messagesId = dataCache.keys();
        for (var i=0; i < messagesId.length; i++) {
            var message = dataCache.get(messagesId[i]);
            messages[message.runId] = messages[message.runId] || {};
            messages[message.runId][message.messageId] = message;
        }

        var resumptionToken;
        var serverTime= 0;
        var serverTimeFirstInvocation;

        return {
            getMessagesDefaultByRun: function(runId){
                //console.log(messages[runId]);
                return messages[runId];
            },
            newMessage: function(jsonMessage){
                var newMessage = new Message(jsonMessage);
                var dataCache = CacheFactory.get('messagesCache');
                return newMessage.$save();
            },
            refreshMessage: function(runId, messageId){
                var dataCache = CacheFactory.get('messagesCache');
                if(dataCache.get(messageId)){
                    delete messages[runId][messageId];
                    dataCache.remove(messageId);
                }
                return this.getMessageById(messageId);
            },
            getMessages: function (runId, from, resumptionToken) {
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('messagesCache');

                messages[runId] = messages[runId] || {};
                if (!from) {
                    from = 0;
                }

                var service = this;

                if(resumptionToken){
                    Message.resume({ runId:runId, resumptionToken: resumptionToken, from:from }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (i = 0; i < data.messages.length; i++) {
                                if (!data.messages[i].deleted) {
                                    dataCache.put(data.messages[i].messageId, data.messages[i]);
                                    messages[runId][data.messages[i].messageId] = data.messages[i];

                                    var message = data.messages[i];

                                    messages[runId][message.messageId].user = UserService.getUser(data.messages[i].senderProviderId+":"+data.messages[i].senderId);
                                }else{
                                    delete messages[runId][data.messages[i].messageId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getMessages(runId, from, data.resumptionToken);
                            }
                        }
                    });
                }else{
                    Message.resume({ runId:runId, from:0 }).$promise.then(function (data) {
                        if (data.error) {
                            deferred.resolve(data);
                        } else {

                            for (var i = 0; i < data.messages.length; i++) {
                                if (!data.messages[i].deleted) {
                                    dataCache.put(data.messages[i].messageId, data.messages[i]);
                                    messages[runId][data.messages[i].messageId] = data.messages[i];

                                    var message = data.messages[i];

                                    messages[runId][message.messageId].user = UserService.getUser(data.messages[i].senderProviderId+":"+data.messages[i].senderId);
                                }else{
                                    delete messages[runId][data.messages[i].messageId];
                                }
                            }

                            if(data.resumptionToken){
                                service.getMessages(runId, from, data.resumptionToken);
                            }
                        }
                    });
                }

                return messages[runId];
            },
            getMessageById: function(messageId){
                var deferred = $q.defer();
                var dataCache = CacheFactory.get('messagesCache');
                if (dataCache.get(messageId)) {
                    deferred.resolve(dataCache.get(messageId));
                } else {

                    Message.getMessageById({ messageId:messageId }).$promise.then(
                        function(data){
                            dataCache.put(messageId, data);
                            messages[data.runId][data.messageId] = data;
                            messages[data.runId][data.messageId].user = UserService.getUser(data.senderProviderId+":"+data.senderId);
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            }
        }
    }
);