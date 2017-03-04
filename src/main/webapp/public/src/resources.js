angular.module('DojoIBL')

    .factory('Account', ["$resource", "$http", "config", function AccountFactory($resource, $http, config) {
        return $resource(config.server+'/rest/account/createAccount', {}, {
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
            },
            'update': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/account/createAccount'
            }
        });
    }])
    .factory('Contacts', ["$resource", "$http", "config", function GameFactory($resource, $http, config) {
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
    }]
);;angular.module('DojoIBL')

    .factory('ChannelApi', ["$resource", "$http", "config", function ChannelFactory($resource, $http, config) {
        return $resource(config.server+'/rest/channelAPI', {}, {
            'getToken': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/channelAPI/token'
            }
        });
    }]);;angular.module('DojoIBL')

    .factory('Activity', ["$resource", "$http", "config", function ActivityFactory($resource, $http, config) {
        return $resource(config.server+'/rest/generalItems', {}, {
            getActivities: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/generalItems/gameId/:gameId'
            },
            getActivitiesForPhase: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/generalItems/gameId/:gameId/section/:phase'
            },
            'create': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/generalItems'
            },
            'delete':{
                params: { gameId: '@gameId', itemId: '@itemId' },
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/generalItems/gameId/:gameId/generalItem/:itemId'
            },
            'getActivity':{
                params: { gameId: '@gameId', itemId: '@itemId' },
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/generalItems/gameId/:gameId/generalItem/:itemId'
            },
            changeActivityStatus: {
                params: { runId: '@runId', generalItemId: '@generalItemId', status: '@status' },
                method: 'POST',
                isArray: false,
                url: config.server+'/rest/myRuns/runId/:runId/generalItem/:generalItemId/status/:status'
            },
            getActivityStatus: {
                params: { runId: '@runId', generalItemId: '@generalItemId' },
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/runId/:runId/generalItem/:generalItemId/status'
            },
            'addRole': {
                params: { generalItemId: '@generalItemId' },
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/generalItems/generalItem/:generalItemId/role'
            },
            'uploadUrl': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/generalItems/pictureUrl/gameId/:gameId/generalItem/:itemId/:key'
            }
        });
    }]
);;angular.module('DojoIBL')

    .factory('Game', ["$resource", "$http", "config", function GameFactory($resource, $http, config) {
        return $resource(config.server+'/rest/myGames/:id', {}, {
            access: {
                params: {id: 'gameAccess'},
                method: 'GET',
                isArray: false
            },
            'query': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames'
            },
            'resume': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/myGames?resumptionToken=:resumptionToken&from=:from'
            },
            'getGameById': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames/gameId/:id'
            },
            'create': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames'
            },
            'addRole': {
                params: { gameId: '@gameId' },
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames/config/gameId/:gameId/role'
            },
            'removeRole': {
                params: { gameId: '@gameId' },
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames/config/gameId/:gameId/role/remove'
            },
            'editRole': {
                params: { gameId: '@gameId', index: '@index' },
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myGames/config/gameId/:gameId/role/:index/edit'
            },
            giveAccess: {
                params: { gameId: '@gameId', accountId: '@accountId', accessRight: '@accessRight' },
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames/access/gameId/:gameId/account/:accountId/accessRight/:accessRight'
            },
            getGameAccesses: {
                params: { gameId: '@gameId'},
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myGames/access/gameId/:gameId'
            },
            deleteGame: {
                params: { gameId: '@gameId' },
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/myGames/gameId/:gameId'
            },
            'gameAssets': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/myGames/gameContent/gameId/:gameId'
            }
        });
    }]
);;angular.module('DojoIBL')

    .factory('Message', ["$resource", "$http", "config", function RunFactory($resource, $http, config) {
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
            }
        });
    }]
);;angular.module('DojoIBL')

    .factory('Oauth', ["$resource", "$http", "config", function GameFactory($resource, $http, config) {
        return $resource(config.server+'/rest/oauth/', {}, {
            'info': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/oauth/getOauthInfo/'
            }
        });
    }]

);
;angular.module('DojoIBL')

    .factory('Response', ["$resource", "$http", "config", function ResponseFactory($resource, $http, config) {
        return $resource(config.server + '/rest/response', {}, {
            'getResponse': {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/response/responseId/:id'
            },
            getResponsesForActivity: {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/response/runId/:runId/itemId/:itemId?from=:from&resumptionToken=:resumptionToken&orderByLastModificationDate=true'
            },
            'getResponsesInquiryActivity': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/response/runId/:runId/itemId/:itemId?resumptionToken=:resumptionToken&from=:from&orderByLastModificationDate=true'
            },
            getResponsesForInquiry: {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/response/runId/:runId'
            },
            'resume': {
                method: 'GET',
                isArray: false,
                url: config.server + '/rest/response/runId/:runId?from=:from&resumptionToken=:resumptionToken&orderByLastModificationDate=true'
            },
            deleteResponse: {
                params: {responseId: '@responseId'},
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/response/responseId/:responseId'
            },
            'uploadUrl': {
                url: config.server + '/rest/response/url/runId/:runId/account/:account/:key'
            }
        });
    }]
);;angular.module('DojoIBL')

    .factory('User', ["$resource", "$http", "config", function RunFactory($resource, $http, config) {
        return $resource(config.server+'/rest/users', {}, {
            getUsersRun: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/users/runId/:id'
            },
            getUserByAccount: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/users/runId/:runId/account/:accountId'
            }
        });
    }]
);;angular.module('DojoIBL')

    .factory('Run', ["$resource", "$http", "config", function RunFactory($resource, $http, config) {
        return $resource(config.server+'/rest/myRuns', {}, {
            getRun: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/runId/:id'
            },
            getRunByCode: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/code/:code'
            },
            getParticipateRunsForGame: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/participate/gameId/:id'
            },
            getOwnedRunsForGame: {
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/runAccess/gameId/:id'
            },
            'create': {
                method: 'POST',
                isArray: false,
                url: config.server + '/rest/myRuns'
            },
            'update': {
                params: { runId: '@runId' },
                method: 'PUT',
                isArray: false,
                url: config.server + '/rest/myRuns/runId/:runId'
            },
            'delete': {
                method: 'DELETE',
                isArray: false,
                url: config.server + '/rest/myRuns/runId/:id'
            },
            giveAccess: {
                params: { runId: '@runId', accountId: '@accountId', accessRight: '@accessRight' },
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/access/runId/:runId/account/:accountId/accessRight/:accessRight'
            },
            removeAccess: {
                params: { runId: '@runId', accountId: '@accountId' },
                method: 'GET',
                isArray: false,
                url: config.server+'/rest/myRuns/removeAccess/runId/:runId/account/:accountId'
            },
            addUserToRun: {
                method: 'POST',
                isArray: false,
                url: config.server+'/rest/users'
            },
            removeUserRun: {
                method: 'DELETE',
                params: { runId: '@runId', email: '@email' },
                isArray: false,
                url: config.server+'/rest/users/runId/:runId/email/:email'
            }
        });
    }]
);