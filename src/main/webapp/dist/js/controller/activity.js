angular.module('DojoIBL')

    .controller('ActivityController', function ($scope, $sce, $stateParams, Session, ActivityService, UserService, AccountService,
                                                Response, ResponseService, RunService, ChannelService, Upload, config) {

        RunService.getRunById($stateParams.runId).then(function(data){
            ActivityService.getActivityById($stateParams.activityId, data.game.gameId).then(function (data) {
                $scope.activity = data;
            });
        });

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.responses = {};
        $scope.responses.responses = [];

        $scope.loadMoreButton = false;

        $scope.responses.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);

        $scope.getUser = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).name;
        };
        $scope.getAvatar = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).picture;
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

        $scope.sendComment = function(){
            if($scope.response != null && $scope.response.length > 0){
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
        };

        AccountService.myDetails().then(function(user) {

            var socket = new ChannelService.SocketHandler(user);
            socket.onMessage(function (data) {
                $scope.$apply(function () {
                    switch (data.type) {

                        case 'org.celstec.arlearn2.beans.run.Response':
                            //$scope.responses.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);
                            if(user.localId != data.userEmail.split(':')[1])
                                ResponseService.addResponse(data, $stateParams.runId, $stateParams.activityId);
                            console.info("[Notification][Response]");
                            break;
                    }
                });
                //jQuery("time.timeago").timeago();
            });
        });

        // upload on file select or drop
        $scope.upload = function (file) {
            $scope.progressPercentage = 0;
            console.log($scope.myAccount, file);
            if(file){


                file.name = (file.name).replace(/\s+/g, '_');
                console.log()

                ResponseService.uploadUrl($stateParams.runId, $scope.myAccount.accountType+":"+$scope.myAccount.localId, file.name.replace(/\s+/g, '_')).$promise.then(function(url){
                    console.log(url, url.uploadUrl);
                    Upload.rename(file, file.name.replace(/\s+/g, '_'))
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

    }
);