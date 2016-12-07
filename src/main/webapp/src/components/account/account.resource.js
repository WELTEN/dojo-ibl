angular.module('DojoIBL')

    .factory('Account', function AccountFactory($resource, $http, config) {
        return $resource(config.server+'/rest/account', {}, {
            'accountDetails': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/account/myAccountDetails'
            },
            'accountDetailsById': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/account/accountDetails/:fullId'
            },
            'uploadUrl': {
                url: config.server + '/rest/account/url/account/:account/:key'
            },
            'search': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/account/search'
            },
            'reminder': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/notifications/reminder/:account'
            }
        });
    })
    .factory('Contacts', function GameFactory($resource, $http, config) {
        return $resource(config.server+'/rest/collaboration', {}, {
            'getContacts': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/collaboration/getContacts?from=:date'
            },
            'addContact': {
                method: 'POST',
                isArray: false,
                url: config.server+'/rest/collaboration/addContact'
            },'getContactToken': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/collaboration/getContact/addContactToken/:token'
            },'confirmAddContact': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/collaboration/confirmAddContact/:token'
            },
            'pendingInvitations': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/collaboration/pendingInvitations'
            },
            'revokeInvitation': {
                method: 'DELETE',
                isArray: false,
                url: config.server+'/rest/collaboration/invitation/:id'
            },
            'removeContact': {
                method: 'DELETE',
                isArray: false,
                url: config.server+'/rest/collaboration/:accountType/:localId'
            }
        });
    }
);