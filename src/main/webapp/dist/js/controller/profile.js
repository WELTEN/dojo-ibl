angular.module('DojoIBL')

    .controller('ProfileController', function ($scope, $sce, $stateParams, $state, Session, AccountService, Upload) {
        //AccountService.myDetails().then(function(data){
        //    $scope.user = data;
        //});

        AccountService.accountDetailsById($stateParams.accountId).then(function(data){
            console.log(data, $stateParams.accountId)
            $scope.user = data;
        });

        $scope.upload = function (file) {
            $scope.progressPercentage = 0;

            AccountService.uploadUrl($scope.myAccount.accountType+":"+$scope.myAccount.localId, file.name).$promise.then(function(url){
                console.log(url);
                Upload.upload({
                    url: url.uploadUrl,
                    data: {file: file, 'username': $scope.username}
                }).then(function (resp) {

                    //ResponseService.newResponse({
                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                    //    "runId": $stateParams.runId,
                    //    "deleted": false,
                    //    "generalItemId": $stateParams.activityId,
                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                    //    "responseValue": {
                    //        "videoUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name,
                    //        "fileName": file.name,
                    //        "fileType": resp.config.data.file.type,
                    //        "width": 3264,
                    //        "height": 1840
                    //    },
                    //    "parentId": 0,
                    //    "revoked": false,
                    //    "lastModificationDate": new Date().getTime()
                    //}).then(function(data){
                    //
                    //});

                    //TODO modify the account

                    console.log(resp)
                    console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);

                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                }, function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    $scope.progressPercentage = progressPercentage;
                    console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                });
            });
        };

    }
);