angular.module('DojoIBL')

    .controller('ActivityController', function ($scope, $sce, $stateParams, Session, ActivityService, UserService, AccountService, Response, ResponseService) {
        $scope.activity = ActivityService.getItemFromCache($stateParams.activityId);

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.games = {};
        $scope.games.games = [];

        $scope.responses = [];
        $scope.showButtonMore;

        function loadResponses() {

            var resp_cache = ResponseService.getResponsesFromCache($stateParams.activityId);
            if(resp_cache) {
                $scope.responses = $scope.responses.concat(resp_cache.responses);

                console.log(resp_cache)

                if (resp_cache.resumptionToken) {
                    console.log(resp_cache.resumptionToken);

                    $scope.showButtonMore = true;
                }else{
                    $scope.showButtonMore = false;
                }
            }

            if(!resp_cache){
                ResponseService.getResponsesByInquiryActivity($stateParams.runId, $stateParams.activityId).then(function (data) {

                    $scope.responses = $scope.responses.concat(data.responses);

                    if (data.resumptionToken) {
                        console.log(data.resumptionToken);
                        //loadResponses();
                        $scope.showButtonMore = true;
                    }else{
                        $scope.showButtonMore = false;
                    }
                });
            }
        }

        $scope.loadMoreResponses = function(){
            ResponseService.getResponsesByInquiryActivity($stateParams.runId, $stateParams.activityId).then(function (data) {

                $scope.responses = $scope.responses.concat(data.responses);

                if (data.resumptionToken) {
                    console.log(data.resumptionToken);
                    //loadResponses();
                    $scope.showButtonMore = true;
                }else{
                    $scope.showButtonMore = false;
                }
            });
        };



        //console.log("load responses",$scope.games.games);
        loadResponses();

        $scope.getUser = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).name;
        };
        $scope.getAvatar = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).picture;
        };

        //$scope.getUser = function(response) {
        //
        //    UserService.getUserByAccount($stateParams.runId, response.userEmail).then(function (data) {
        //        console.log(data);
        //        return data;
        //    });
        //};

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
                    //console.log(data);

                    $scope.responses.push(data);

                    $scope.response = null;
                });
            });
        };

        $scope.removeComment = function(data){
            console.log(data);
            var idx = $scope.responses.indexOf(data);

            // is currently selected
            if (idx > -1) {
                $scope.responses.splice(idx, 1);
            }
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

                    //console.log(response);
                    console.log(childComment);

                    //if(!$scope.response.children) {
                    //    $scope.response.children = [];
                    //}
                    //$scope.response.children.push(childComment);

                    $scope.responses.push(data);

                    $scope.responseChildren = null;
                });
            });


        };

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        }
    }
);