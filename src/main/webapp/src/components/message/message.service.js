angular.module('DojoIBL')

    .service('MessageService', function ($q, Message, CacheFactory, UserService, ChannelService) {

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
            sendNotification: function(){
              Message.notification( {
                  "notification": {
                      "title": "Portugal vs. Denmark",
                      "body": "5 to 1",
                      "icon": "firebase-logo.png",
                      "click_action": "http://localhost:8081"
                  },
                  "to": 'cgm4qWkp5qY:APA91bGxIK9jbV9VaY6VhiQ6feFsnSneEyFYcQex5sLrDED9wvXKjI2qa6RSh0LGqzMFdZTdZy5oeiL_Psf9nckJ0YYaqhG25w5dVufOqXkEQpi4W3Te8U0sKRfoWzmegg5tyYyI4Omm'
              });
            },
            sendNotificationTopic: function(topic){
              Message.notificationTopic( {
                  "notification": {
                      "title": "Portugal vs. Denmark",
                      "body": "5 to 1",
                      "icon": "firebase-logo.png",
                      "click_action": "http://localhost:8081"
                  },
                  "to": '/topics/'+topic
              });
            },
            getMessagesDefaultByRun: function(runId){
                //console.log(messages[runId]);
                return messages[runId];
            },
            newMessage: function(jsonMessage){
                var newMessage = new Message(jsonMessage);
                var dataCache = CacheFactory.get('messagesCache');
                return newMessage.$save();
            },
            sendEmail: function(jsonMessage){
                Message.sendEmail();
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

                                    messages[runId][message.messageId].user = UserService.getUser(runId, data.messages[i].senderProviderId+":"+data.messages[i].senderId);
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

                                    messages[runId][message.messageId].user = UserService.getUser(runId, data.messages[i].senderProviderId+":"+data.messages[i].senderId);
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
                            messages[data.runId][data.messageId].user = UserService.getUser(data.runId, data.senderProviderId+":"+data.senderId);
                            deferred.resolve(data);
                        }
                    );
                }
                return deferred.promise;
            },
            emptyMessagesCache: function(){
                var dataCache = CacheFactory.get('messagesCache');
                if(dataCache) dataCache.removeAll();
                messages = {};
            }
        }
    }
);