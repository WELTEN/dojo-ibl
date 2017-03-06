angular.module('DojoIBL')

    .controller('ActivityController', function ($scope, $sce, $stateParams, Session, ActivityService, UserService, AccountService,
                                                Response, ResponseService, RunService, ChannelService, Upload, config, toaster, GameService) {

        ChannelService.register('org.celstec.arlearn2.beans.run.Response', function (data) {

            console.log(data);

            if($stateParams.activityId == data.generalItemId && $stateParams.runId == data.runId){
                ResponseService.refreshResponse(data, $stateParams.runId, $stateParams.activityId);
            }
            if($stateParams.runId == data.runId){
                //console.log(data.userEmail, AccountService.myDetailsCache().fullId);
                //
                //if(data.userEmail == AccountService.myDetailsCache().fullId){
                //    toaster.success({
                //        title: 'You added a response',
                //        body: 'You have contributed to an activity.'
                //    });
                //}else{

                    toaster.success({
                        title: UserService.getUser($stateParams.runId, data.userEmail).name+' added a response',
                        body: UserService.getUser($stateParams.runId, data.userEmail).name+' has contributed to an activity.'
                    });
                //}

            }
        });

        ChannelService.register('org.celstec.arlearn2.beans.notification.GeneralItemModification', function (notification) {
            ActivityService.refreshActivity(notification.itemId, notification.gameId).then(function (data) {
                $scope.activity = data;
            });
            toaster.success({
                title: 'Activity modified',
                body: 'The structure of the activity has been modified.'
            });
        });

        $scope.responses = {};

        $scope.loadMoreButton = false;

        function loadResponses() {
            ResponseService.resumeLoadingResponses($stateParams.runId, $stateParams.activityId).then(function (data) {
                if (data.error) {
                    $scope.showNoAccess = true;
                } else {
                    $scope.show = true;
                    if (data.resumptionToken) {
                        loadResponses();
                    }
                }
            });
        }

        loadResponses();

        $scope.responses = ResponseService.getResponsesByInquiryActivity($stateParams.runId, $stateParams.activityId);
        //console.log($scope.responses);
        //$scope.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);

        RunService.getRunById($stateParams.runId).then(function(data){

            ActivityService.getActivityById($stateParams.activityId, data.game.gameId).then(function (data) {
                $scope.activity = data;
            });

            GameService.getGameAssets(data.game.gameId).then(function(data){
                $scope.assets = data;
            });
        });

        AccountService.myDetails().then(function(data){
            $scope.myAccount = data;
        });

        $scope.sendComment = function(){
            if($scope.response != null && $scope.response.length > 0){

                if(angular.isUndefined($scope.response.id)){
                    ResponseService.removeCachedResponse($stateParams.activityId);
                }

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
            }
        };

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

        $scope.removeComment = function(data){

            swal({
                title: "Are you sure?",
                text: "You will not be able to recover it in the future!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, remove it!",
                closeOnConfirm: false
            }, function () {
                swal("Removed!", "Your contribution has been removed from the inquiry", "success");

                ResponseService.deleteResponse(data.responseId);

            });
        };

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };

        ////////////////////////////
        // Response cache management
        ////////////////////////////

        if(ResponseService.getResponseInCached($stateParams.activityId)){
            $scope.response = ResponseService.getResponseInCached($stateParams.activityId);
        }

        $scope.saveResponse = function (){
            if($scope.response != null && $scope.response.length > 0){
                if(angular.isUndefined($scope.response.id)){
                    ResponseService.saveResponseInCache($scope.response, $stateParams.activityId);
                }else{
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
                }
            }
        };

        // upload on file select or drop
        $scope.upload = function (file) {
            $scope.progressPercentage = 0;
            if(file){

                file.name = (file.name).replace(/\s+/g, '_');

                ResponseService.uploadUrl($stateParams.runId, $scope.myAccount.accountType+":"+$scope.myAccount.localId, file.name.replace(/\s+/g, '_')).$promise.then(function(url){
                    console.log(url, url.uploadUrl);
                    Upload.rename(file, file.name.replace(/\s+/g, '_'));
                    Upload.upload({
                        url: url.uploadUrl,
                        data: {file: file, 'username': $scope.myAccount.accountType+":"+$scope.myAccount.localId}
                    }).then(function (resp) {

                        switch (true) {
                            case /video/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "videoUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /image/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "imageUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /pdf/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "pdfUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /audio/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "audioUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /wordprocessingml|msword/.test(resp.config.data.file.type):
                                console.log(resp.config.data.file.type);
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "documentUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /vnd.ms-excel|spreadsheetml/.test(resp.config.data.file.type):
                                console.log(resp.config.data.file.type);
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "excelUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            default:
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": file.name,
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;

                        }

                        console.log(resp)
                        console.log('Success ' + resp.config.data.file.name + ' uploaded by: ' + resp.config.data.username);

                    }, function (resp) {
                        console.log(resp)
                        console.log('Error ' + resp.config.data.file.name + ' from: ' + resp.config.data.username);
                        console.log('Error status: ' + resp.status);
                    }, function (evt) {
                        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        $scope.progressPercentage = progressPercentage;
                        console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                    });
                });

            }
        };

        $scope.getUser = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).name;
        };
        $scope.getAvatar = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).picture;
        };

        $scope.getRoleColor = function(roles){
            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(angular.fromJson(roles[0]))) {
                        if (!angular.isObject(roles[0])) {
                            return {
                                "color": angular.fromJson(roles[0]).color
                            };
                        }
                    }
                }catch(e){
                    return {
                        "color": "#f8ac59"
                    };
                }
            }
            return {
                "color": "#f8ac59"
            };
        };

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };

    }
);