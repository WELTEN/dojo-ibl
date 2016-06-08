angular.module('DojoIBL')

    .controller('ActivityController', function ($scope, $sce, $stateParams, Session, ActivityService, UserService, AccountService, Response, ResponseService, ChannelService) {
        $scope.activity = ActivityService.getItemFromCache($stateParams.activityId);

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.responses = {};
        $scope.responses.responses = [];

        $scope.loadMoreButton = false;

        $scope.loadResponses = function() {
            ResponseService.getResponses($stateParams.runId, $stateParams.activityId).then(function (data) {
                if (data.error) {
                    $scope.showNoAccess = true;
                } else {
                    $scope.show = true;
                    $scope.resum = data.resumptionToken;
                    if (data.resumptionToken) {
                        $scope.loadMoreButton = true;
                    }else{
                        $scope.loadMoreButton = false;
                    }
                }
                $scope.responses.responses = ResponseService.getResponsesByInquiryActivity($stateParams.runId, $stateParams.activityId);
            });
        };

        $scope.loadResponses();

        $scope.responses.responses = ResponseService.getResponsesByInquiryActivity($stateParams.runId, $stateParams.activityId);

        $scope.getUser = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).name;
        };
        $scope.getAvatar = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).picture;
        };

        $scope.sendComment = function(){
            AccountService.myDetails().then(function(data){
                ResponseService.newResponse({
                    "type": "org.celstec.arlearn2.beans.run.Response",
                    "runId": $stateParams.runId,
                    "deleted": false,
                    "generalItemId": $stateParams.activityId,
                    "userEmail": data.accountType+":"+data.localId,
                    "responseValue": $scope.response,
                    "parentId": 0,
                    "revoked": false,
                    "lastModificationDate": new Date().getTime()
                }).then(function(data){
                    $scope.response = null;
                });
            });
        };

        $scope.removeComment = function(data){
            ResponseService.deleteResponse(data.responseId);
        };

        $scope.responseChildren;

        $scope.sendChildComment = function(responseParent, responseChildren) {

            AccountService.myDetails().then(function(data){
                ResponseService.newResponse({
                    "type": "org.celstec.arlearn2.beans.run.Response",
                    "runId": $stateParams.runId,
                    "deleted": false,
                    "generalItemId": $stateParams.activityId,
                    "userEmail": data.accountType+":"+data.localId,
                    "responseValue": responseChildren,
                    "parentId": responseParent.responseId,
                    "revoked": false,
                    "lastModificationDate": new Date().getTime()
                }).then(function(childComment){
                    $scope.responseChildren = null;
                });
            });
        };

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        }

        var socket = new ChannelService.SocketHandler();
        socket.onMessage(function (data) {
            $scope.$apply(function () {
                console.log(data.type);
                switch (data.type) {

                    case 'org.celstec.arlearn2.beans.run.Response':

                        $scope.loadResponses();

                        break;
                }
            });
            //jQuery("time.timeago").timeago();
        });
    }
);