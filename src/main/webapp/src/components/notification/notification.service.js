angular.module('DojoIBL')
    .service('NotificationService', function ($q, Message, CacheFactory, UserService, ChannelService, $firebaseArray) {

        var notifications = firebase.database().ref("notifications");

        return {
            notify: function(notify_to, notification){
                $firebaseArray(notifications.child(notify_to)).$add(notification);
            },
            showNotifications: function(user){
                return $firebaseArray(notifications.child(user));
            },
            notificationRead: function(user, notification){
                notifications.child(user).child(notification.$id).remove(function(error){
                    if (error) {
                        console.log("Error:", error);
                    } else {
                        console.log("Removed successfully!");
                    }
                });
            }
        }

    }
);