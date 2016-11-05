angular.module('DojoIBL')

    .service('NotificationService', function ($q, $sce, Game, CacheFactory, RunService, ActivityService) {

        CacheFactory('userNotificationsCache', {
            maxAge: 3 * 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 3 * 24 * 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        var numberNotifications = 0;

        return {
            setNumberNotification: function(notifications){
                numberNotifications = notifications;
                var dataCache = CacheFactory.get('userNotificationsCache');
                dataCache.put("notifications", notifications);
            },
            getNumberNotification: function(){
                var dataCache = CacheFactory.get('userNotificationsCache');

                if (dataCache.get("notifications")) {
                    return dataCache.get("notifications");
                } else {
                    return 0;
                }
            }
        }
    }
);