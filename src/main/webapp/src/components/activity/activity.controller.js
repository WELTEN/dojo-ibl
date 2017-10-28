angular.module('DojoIBL')
    .controller('ActivityController', function ($scope, $sce, $stateParams, Session, ActivityService, UserService, AccountService,
                                                Response, ResponseService, RunService, ChannelService, Upload, config,
                                                ActivityStatusService, toaster, GameService, $firebaseArray, firebase, $location,
                                                LaService) {

        //$scope.topDistance = 120;

        $scope.$parent.toggle = true;

        $scope.account = AccountService.myDetailsCache();

        var responsesRef = firebase.database().ref("responses").child($stateParams.runId).child($stateParams.activityId);

        $scope.responses = $firebaseArray(responsesRef);

        $scope.sendComment = function() {

                AccountService.myDetails().then(function(data){

                    $scope.responses.$add({
                        "type": "org.celstec.arlearn2.beans.run.Response",
                        "runId": $stateParams.runId,
                        "deleted": false,
                        "generalItemId": $stateParams.activityId,
                        "generalItemName": $scope.activity.name,
                        "phase": $scope.activity.section,
                        "userAccountType": data.accountType,
                        "userLocalId": data.localId,
                        "userName": data.name,
                        "userProfile": (data.picture == undefined ? "" : data.picture ),
                        "multimedia": $scope.arrayMultimedia,
                        "responseValue": $scope.responseText,
                        "parentId": 0,
                        "likeCount": 0,
                        "revoked": false,
                        "lastModificationDate": firebase.database.ServerValue.TIMESTAMP
                    });

                    LaService.sendResponseStatement(
                        $scope.responseText,
                        data,
                        $scope.activity.section,
                        $stateParams.activityId,
                        $scope.activity.name,
                        $scope.activity.richText,
                        $stateParams.runId,
                        "Group title",
                        "Group description",
                        $scope.activity.gameId,
                        "Project",
                        "Project description");

                    $scope.responseText = '';
                    $scope.arrayMultimedia = [];
                });
        };

        var storageRef = firebase.storage().ref();

        //$scope.upload = function(file) {
        //
        //    console.log(file);
        //
        //    // Create the file metadata
        //    var metadata = {
        //        contentType: 'image/jpeg'
        //    };
        //
        //    // Upload file and metadata to the object 'images/mountains.jpg'
        //    var uploadTask = storageRef.child('images/' + file.name).put(file, metadata);
        //
        //    // Listen for state changes, errors, and completion of the upload.
        //    uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED, // or 'state_changed'
        //        function(snapshot) {
        //            // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
        //            var progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
        //            console.log('Upload is ' + progress + '% done');
        //            switch (snapshot.state) {
        //                case firebase.storage.TaskState.PAUSED: // or 'paused'
        //                    console.log('Upload is paused');
        //                    break;
        //                case firebase.storage.TaskState.RUNNING: // or 'running'
        //                    console.log('Upload is running');
        //                    break;
        //            }
        //        }, function(error) {
        //
        //            // A full list of error codes is available at
        //            // https://firebase.google.com/docs/storage/web/handle-errors
        //            switch (error.code) {
        //                case 'storage/unauthorized':
        //                    // User doesn't have permission to access the object
        //                    break;
        //
        //                case 'storage/canceled':
        //                    // User canceled the upload
        //                    break;
        //
        //                case 'storage/unknown':
        //                    // Unknown error occurred, inspect error.serverResponse
        //                    break;
        //            }
        //        }, function() {
        //            // Upload completed successfully, now we can get the download URL
        //            var downloadURL = uploadTask.snapshot.downloadURL;
        //        });
        //}

        $scope.responseText;

        $scope.closeActivity = function() {
            $scope.$parent.toggle = false;
            $location.path('inquiry/'+$stateParams.runId);
        };


        RunService.getRunById($stateParams.runId).then(function(data){

            ActivityStatusService.getActivityByIdRun($stateParams.activityId, data.game.gameId, $stateParams.runId).then(function (data) {
                $scope.activity = data;
                $scope.status =   data.status.status;
            });

            GameService.getGameAssets(data.game.gameId).then(function(data){
                $scope.assets = data;
            });
        });

        $scope.data = [{
            id: '-1',
            name: '--Select--'
        }, {
            id: '0',
            name: 'To Do'
        }, {
            id: '1',
            name: 'In progress'
        }, {
            id: '2',
            name: 'Done'
        }];

        $scope.changeStatus = function(){

            ActivityStatusService.changeActivityStatus($stateParams.runId, $scope.activity.id, $scope.status, $scope.activity.section, $scope.activity.status.id).then(function (data) {

                switch(data.status){
                    case 0:
                        toaster.success({
                            title: 'Moved to ToDo list',
                            body: 'The activity has been successfully moved to ToDo list.'
                        });
                        break;
                    case 1:
                        toaster.success({
                            title: 'Moved to In Progress list',
                            body: 'The activity has been successfully moved to In Progress list.'
                        });
                        break;
                    case 2:
                        toaster.success({
                            title: 'Moved to Completed list',
                            body: 'The activity has been successfully moved to Completed list.'
                        });
                        break;
                }

            });
        }



        AccountService.myDetails().then(function(data){
            $scope.myAccount = data;
        });


        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };


        $scope.arrayMultimedia = [];

        $scope.upload = function (file){
            // File or Blob named mountains.jpg

            $scope.picFile = file;

            $scope.picFile.name = (file.name).replace(/\s+/g, '_');

            // Create the file metadata
            var metadata = {
                contentType: file.type
            };

            // Upload file and metadata to the object 'images/mountains.jpg'
            var uploadTask = storageRef.child('images/' + file.name).put(file, metadata);

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED, // or 'state_changed'
                function(snapshot) {
                    // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
                    $scope.picFile.progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    console.log('Upload is ' + $scope.picFile.progress + '% done');
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
                    $scope.picFile.url = uploadTask.snapshot.downloadURL;

                    $scope.arrayMultimedia.push($scope.picFile);

                    $scope.picFile = null;
                    file = null;

                    //console.log(downloadURL)


                    //var starsRef = storageRef.child('images/2017-10-03-16-07-localhost-8182.png');
                    //
                    //// Get the download URL
                    //starsRef.getDownloadURL().then(function(url) {
                    //    // Insert url into an <img> tag to "download"
                    //
                    //    console.log(url)
                    //
                    //
                    //}).catch(function(error) {
                    //
                    //    // A full list of error codes is available at
                    //    // https://firebase.google.com/docs/storage/web/handle-errors
                    //    switch (error.code) {
                    //        case 'storage/object_not_found':
                    //            // File doesn't exist
                    //            break;
                    //
                    //        case 'storage/unauthorized':
                    //            // User doesn't have permission to access the object
                    //            break;
                    //
                    //        case 'storage/canceled':
                    //            // User canceled the upload
                    //            break;
                    //
                    //            case 'storage/unknown':
                    //            // Unknown error occurred, inspect the server response
                    //            break;
                    //    }
                    //});
                });
        }

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
)
;