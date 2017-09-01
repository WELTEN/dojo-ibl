angular.module('DojoIBL')

    .service('ChannelService', function ($q, $http, CacheFactory, ChannelApi, $rootScope) {

        CacheFactory('channelAPICache', {
            maxAge: 24 * 60 * 60 * 1000, // Items added to this cache expire after 1 day
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive', // Items will be deleted from this cache when they expire
            storageMode: 'localStorage' // This cache will use `localStorage`.
        });

        return {
            saveDeviceRegistrationTokenMessaging : function(value){
                return localStorage.setItem('deviceRegistrationToken', value)
            },
            getDeviceRegistrationTokenMessaging : function(value){
                return localStorage.getItem('deviceRegistrationToken')
            }
        }
    }
);