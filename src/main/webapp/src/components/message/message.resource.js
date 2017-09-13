angular.module('DojoIBL')

    .factory('Message', function RunFactory($resource, $http, config) {
        return $resource(config.server+'/rest/messages/message', {}, {
            'resume': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/messages/runId/:runId/default?from=:from&resumptionToken=:resumptionToken'
            },
            'getMessageById': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/messages/messageId/:messageId'
            },
            'sendEmail': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/messages/send/email'
            },
            'notification':{
                method: 'POST',
                isArray: false,
                url: 'http://fcm.googleapis.com/fcm/send',
                headers: {
                    'Authorization': 'key=AAAAeNC1dA4:APA91bHYTmorx3Yd927llZwzFvmPEPYDFSX_a2R3QhrTNqhR4gMvFVbeAoz3YXwuEgoP-uLvmzZKEmqkCFCIEdolUW-5mOwWNEPoYV7nLeD4mCNapv1xtjFsWvAUugm1Y0xdSrhvJjos',
                    'Content-Type': 'application/json'
                }
            },
            'notificationTopic':{
                method: 'POST',
                isArray: false,
                url: 'http://fcm.googleapis.com/fcm/send',
                headers: {
                    'Authorization': 'key=AAAAeNC1dA4:APA91bHYTmorx3Yd927llZwzFvmPEPYDFSX_a2R3QhrTNqhR4gMvFVbeAoz3YXwuEgoP-uLvmzZKEmqkCFCIEdolUW-5mOwWNEPoYV7nLeD4mCNapv1xtjFsWvAUugm1Y0xdSrhvJjos',
                    'Content-Type': 'application/json'
                }
            }
        });
    }
);