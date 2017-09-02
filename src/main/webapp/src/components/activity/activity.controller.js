angular.module('DojoIBL')

    //.factory('$pageArray', function($firebaseArray) {
    //    return function(ref, field) {
    //        // create a Paginate reference
    //        var pageRef = new Firebase.util.Paginate(ref, field, {maxCacheSize: 250});
    //        // generate a synchronized array using the special page ref
    //        var list = $firebaseArray(pageRef);
    //        // store the "page" scope on the synchronized array for easy access
    //        list.page = pageRef.page;
    //
    //        // when the page count loads, update local scope vars
    //        pageRef.page.onPageCount(function(currentPageCount, couldHaveMore) {
    //            list.pageCount = currentPageCount;
    //            list.couldHaveMore = couldHaveMore;
    //        });
    //
    //        // when the current page is changed, update local scope vars
    //        pageRef.page.onPageChange(function(currentPageNumber) {
    //            list.currentPageNumber = currentPageNumber;
    //        });
    //
    //        // load the first page
    //        pageRef.page.next();
    //
    //        return list;
    //    }
    //})

    .controller('ActivityController', function ($scope, $sce, $stateParams, Session, ActivityService, UserService, AccountService,
                                                Response, ResponseService, RunService, ChannelService, Upload, config,
                                                ActivityStatusService, toaster, GameService, $firebaseArray, firebase) {


        var ctrl = this;
        $scope.account = AccountService.myDetailsCache();


        $scope.pageSize = 25;
        $scope.page = 1;

        //var responseValidRed = Firebase("https://dojo-ibl.firebaseio.com/responses");
        var responsesRef = firebase.database().ref("responses").child($stateParams.runId).child($stateParams.activityId);

        ctrl.getResponses = function() {
            $scope.responses = $firebaseArray(responsesRef.limitToLast($scope.pageSize * $scope.page));
        };

        //$scope.pageItems = $pageArray(responseValidRed, 'number');

        ctrl.getResponses();

        $scope.sendComment = function() {

                AccountService.myDetails().then(function(data){

                    $scope.responses.$add({
                        "type": "org.celstec.arlearn2.beans.run.Response",
                        "runId": $stateParams.runId,
                        "deleted": false,
                        "generalItemId": $stateParams.activityId,
                        "userLocalId": data.localId,
                        "userName": data.name,
                        "userProfile": data.picture,
                        "responseValue": $scope.responseText,
                        "parentId": 0,
                        "revoked": false,
                        "lastModificationDate": firebase.database.ServerValue.TIMESTAMP
                    });

                    $scope.responseText = '';
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

                responsesRef.child(data.$id).remove(function(error){
                    if (error) {
                        console.log("Error:", error);
                    } else {
                        console.log("Removed successfully!");
                    }
                });

            });
        };

        var storageRef = firebase.storage().ref();

        $scope.upload = function(file) {

            // Create the file metadata
            var metadata = {
                contentType: 'image/jpeg'
            };

            // Upload file and metadata to the object 'images/mountains.jpg'
            var uploadTask = storageRef.child('images/' + file.name).put(file, metadata);

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED, // or 'state_changed'
                function(snapshot) {
                    // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
                    var progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    console.log('Upload is ' + progress + '% done');
                    switch (snapshot.state) {
                        case firebase.storage.TaskState.PAUSED: // or 'paused'
                            console.log('Upload is paused');
                            break;
                        case firebase.storage.TaskState.RUNNING: // or 'running'
                            console.log('Upload is running');
                            break;
                    }
                }, function(error) {

                    // A full list of error codes is available at
                    // https://firebase.google.com/docs/storage/web/handle-errors
                    switch (error.code) {
                        case 'storage/unauthorized':
                            // User doesn't have permission to access the object
                            break;

                        case 'storage/canceled':
                            // User canceled the upload
                            break;

                        case 'storage/unknown':
                            // Unknown error occurred, inspect error.serverResponse
                            break;
                    }
                }, function() {
                    // Upload completed successfully, now we can get the download URL
                    var downloadURL = uploadTask.snapshot.downloadURL;
                });
        }

        $scope.responseText;

        // ===============

        //$scope.responses = {};
        //
        //$scope.loadMoreButton = false;
        //
        //function loadResponses() {
        //    ResponseService.resumeLoadingResponses($stateParams.runId, $stateParams.activityId).then(function (data) {
        //        if (data.error) {
        //            $scope.showNoAccess = true;
        //        } else {
        //            $scope.show = true;
        //            if (data.resumptionToken) {
        //                loadResponses();
        //            }
        //        }
        //    });
        //}
        //
        //loadResponses();
        //
        //$scope.responses = ResponseService.getResponsesByInquiryActivity($stateParams.runId, $stateParams.activityId);

        RunService.getRunById($stateParams.runId).then(function(data){

            ActivityStatusService.getActivityByIdRun($stateParams.activityId, data.game.gameId, $stateParams.runId).then(function (data) {
                $scope.activity = data;
            });

            GameService.getGameAssets(data.game.gameId).then(function(data){
                $scope.assets = data;
            });
        });

        AccountService.myDetails().then(function(data){
            $scope.myAccount = data;
        });

        //$scope.sendComment = function(){
        //    if($scope.response != null && $scope.response.length > 0){
        //
        //        if(angular.isUndefined($scope.response.id)){
        //            ResponseService.removeCachedResponse($stateParams.activityId);
        //        }
        //
        //        AccountService.myDetails().then(function(data){
        //            ResponseService.newResponse({
        //                "type": "org.celstec.arlearn2.beans.run.Response",
        //                "runId": $stateParams.runId,
        //                "deleted": false,
        //                "generalItemId": $stateParams.activityId,
        //                "userEmail": data.accountType+":"+data.localId,
        //                "responseValue": $scope.response,
        //                "parentId": 0,
        //                "revoked": false,
        //                "lastModificationDate": new Date().getTime()
        //            }).then(function(data){
        //                $scope.response = null;
        //            });
        //        });
        //    }
        //};
        //
        //$scope.sendChildComment = function(responseParent, responseChildren) {
        //
        //    AccountService.myDetails().then(function(data){
        //        ResponseService.newResponse({
        //            "type": "org.celstec.arlearn2.beans.run.Response",
        //            "runId": $stateParams.runId,
        //            "deleted": false,
        //            "generalItemId": $stateParams.activityId,
        //            "userEmail": data.accountType+":"+data.localId,
        //            "responseValue": responseChildren,
        //            "parentId": responseParent.responseId,
        //            "revoked": false,
        //            "lastModificationDate": new Date().getTime()
        //        }).then(function(childComment){
        //            $scope.responseChildren = null;
        //        });
        //    });
        //};



        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };

        ////////////////////////////
        // Response cache management
        ////////////////////////////

        //if(ResponseService.getResponseInCached($stateParams.activityId)){
        //    $scope.response = ResponseService.getResponseInCached($stateParams.activityId);
        //}
        //
        //$scope.saveResponse = function (){
        //    if($scope.response != null && $scope.response.length > 0){
        //        if(angular.isUndefined($scope.response.id)){
        //            ResponseService.saveResponseInCache($scope.response, $stateParams.activityId);
        //        }else{
        //            ResponseService.newResponse({
        //                "type": "org.celstec.arlearn2.beans.run.Response",
        //                "runId": $stateParams.runId,
        //                "deleted": false,
        //                "generalItemId": $stateParams.activityId,
        //                "userEmail": data.accountType+":"+data.localId,
        //                "responseValue": $scope.response,
        //                "parentId": 0,
        //                "revoked": false,
        //                "lastModificationDate": new Date().getTime()
        //            }).then(function(data){
        //                $scope.response = null;
        //            });
        //        }
        //    }
        //};
        //
        //// upload on file select or drop
        //$scope.upload = function (file) {
        //    $scope.progressPercentage = 0;
        //    if(file){
        //
        //        file.name = (file.name).replace(/\s+/g, '_');
        //
        //        ResponseService.uploadUrl($stateParams.runId, $scope.myAccount.accountType+":"+$scope.myAccount.localId, file.name.replace(/\s+/g, '_')).$promise.then(function(url){
        //            console.log(url, url.uploadUrl);
        //            Upload.rename(file, file.name.replace(/\s+/g, '_'));
        //            Upload.upload({
        //                url: url.uploadUrl,
        //                data: {file: file, 'username': $scope.myAccount.accountType+":"+$scope.myAccount.localId}
        //            }).then(function (resp) {
        //
        //                switch (true) {
        //                    case /video/.test(resp.config.data.file.type):
        //                        ResponseService.newResponse({
        //                            "type": "org.celstec.arlearn2.beans.run.Response",
        //                            "runId": $stateParams.runId,
        //                            "deleted": false,
        //                            "generalItemId": $stateParams.activityId,
        //                            "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
        //                            "responseValue": {
        //                                "videoUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
        //                                "fileName": file.name,
        //                                "fileType": resp.config.data.file.type,
        //                                "width": 3264,
        //                                "height": 1840
        //                            },
        //                            "parentId": 0,
        //                            "revoked": false,
        //                            "lastModificationDate": new Date().getTime()
        //                        }).then(function(data){
        //
        //                        });
        //                        break;
        //                    case /image/.test(resp.config.data.file.type):
        //                        ResponseService.newResponse({
        //                            "type": "org.celstec.arlearn2.beans.run.Response",
        //                            "runId": $stateParams.runId,
        //                            "deleted": false,
        //                            "generalItemId": $stateParams.activityId,
        //                            "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
        //                            "responseValue": {
        //                                "imageUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
        //                                "fileName": file.name,
        //                                "fileType": resp.config.data.file.type,
        //                                "width": 3264,
        //                                "height": 1840
        //                            },
        //                            "parentId": 0,
        //                            "revoked": false,
        //                            "lastModificationDate": new Date().getTime()
        //                        }).then(function(data){
        //
        //                        });
        //                        break;
        //                    case /pdf/.test(resp.config.data.file.type):
        //                        ResponseService.newResponse({
        //                            "type": "org.celstec.arlearn2.beans.run.Response",
        //                            "runId": $stateParams.runId,
        //                            "deleted": false,
        //                            "generalItemId": $stateParams.activityId,
        //                            "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
        //                            "responseValue": {
        //                                "pdfUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
        //                                "fileName": file.name,
        //                                "fileType": resp.config.data.file.type,
        //                                "width": 3264,
        //                                "height": 1840
        //                            },
        //                            "parentId": 0,
        //                            "revoked": false,
        //                            "lastModificationDate": new Date().getTime()
        //                        }).then(function(data){
        //
        //                        });
        //                        break;
        //                    case /audio/.test(resp.config.data.file.type):
        //                        ResponseService.newResponse({
        //                            "type": "org.celstec.arlearn2.beans.run.Response",
        //                            "runId": $stateParams.runId,
        //                            "deleted": false,
        //                            "generalItemId": $stateParams.activityId,
        //                            "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
        //                            "responseValue": {
        //                                "audioUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
        //                                "fileName": file.name,
        //                                "fileType": resp.config.data.file.type,
        //                                "width": 3264,
        //                                "height": 1840
        //                            },
        //                            "parentId": 0,
        //                            "revoked": false,
        //                            "lastModificationDate": new Date().getTime()
        //                        }).then(function(data){
        //
        //                        });
        //                        break;
        //                    case /wordprocessingml|msword/.test(resp.config.data.file.type):
        //                        console.log(resp.config.data.file.type);
        //                        ResponseService.newResponse({
        //                            "type": "org.celstec.arlearn2.beans.run.Response",
        //                            "runId": $stateParams.runId,
        //                            "deleted": false,
        //                            "generalItemId": $stateParams.activityId,
        //                            "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
        //                            "responseValue": {
        //                                "documentUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
        //                                "fileName": file.name,
        //                                "fileType": resp.config.data.file.type,
        //                                "width": 3264,
        //                                "height": 1840
        //                            },
        //                            "parentId": 0,
        //                            "revoked": false,
        //                            "lastModificationDate": new Date().getTime()
        //                        }).then(function(data){
        //
        //                        });
        //                        break;
        //                    case /vnd.ms-excel|spreadsheetml/.test(resp.config.data.file.type):
        //                        console.log(resp.config.data.file.type);
        //                        ResponseService.newResponse({
        //                            "type": "org.celstec.arlearn2.beans.run.Response",
        //                            "runId": $stateParams.runId,
        //                            "deleted": false,
        //                            "generalItemId": $stateParams.activityId,
        //                            "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
        //                            "responseValue": {
        //                                "excelUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
        //                                "fileName": file.name,
        //                                "fileType": resp.config.data.file.type,
        //                                "width": 3264,
        //                                "height": 1840
        //                            },
        //                            "parentId": 0,
        //                            "revoked": false,
        //                            "lastModificationDate": new Date().getTime()
        //                        }).then(function(data){
        //
        //                        });
        //                        break;
        //                    default:
        //                        ResponseService.newResponse({
        //                            "type": "org.celstec.arlearn2.beans.run.Response",
        //                            "runId": $stateParams.runId,
        //                            "deleted": false,
        //                            "generalItemId": $stateParams.activityId,
        //                            "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
        //                            "responseValue": file.name,
        //                            "parentId": 0,
        //                            "revoked": false,
        //                            "lastModificationDate": new Date().getTime()
        //                        }).then(function(data){
        //
        //                        });
        //                        break;
        //
        //                }
        //
        //                console.log(resp)
        //                console.log('Success ' + resp.config.data.file.name + ' uploaded by: ' + resp.config.data.username);
        //
        //            }, function (resp) {
        //                console.log(resp)
        //                console.log('Error ' + resp.config.data.file.name + ' from: ' + resp.config.data.username);
        //                console.log('Error status: ' + resp.status);
        //            }, function (evt) {
        //                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
        //                $scope.progressPercentage = progressPercentage;
        //                console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
        //            });
        //        });
        //
        //    }
        //};

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