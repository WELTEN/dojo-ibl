angular.module('DojoIBL')

    .service('MessageService', function ($q, Message, CacheFactory) {

        CacheFactory('messagesCache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        return {
            newMessage: function(jsonMessage){
                var newMessage = new Message(jsonMessage);
                var dataCache = CacheFactory.get('messagesCache');
                return newMessage.$save();
            },
            //getMessagesDefaultByRun: function(runId){
            //    var service = this;
            //    var dataCache = CacheFactory.get('messagesCache');
            //    var deferred = $q.defer();
            //    Message.getMessagesDefaultByRun({ runId: runId }).$promise.then(function (data) {
            //            console.log(data);
            //            deferred.resolve(data);
            //        }
            //    );
            //
            //    return deferred.promise;
            //}
        }
    }
);