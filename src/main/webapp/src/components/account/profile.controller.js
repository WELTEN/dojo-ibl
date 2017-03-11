angular.module('DojoIBL')

    .controller('ProfileController', function ($scope, $sce, $stateParams, $state, Session,
                                               MessageService,
                                               AccountService, Upload, config) {
        AccountService.myDetails().then(function(data){
            $scope.myAccount = data;
            $scope.nameValue = $scope.myAccount.familyName+" "+$scope.myAccount.givenName;
        });

        AccountService.accountDetailsById($stateParams.fullId).then(function(data){
            $scope.user = data;
        });

        $scope.ok = function(){
            console.log($scope.user)
            AccountService.update({
                "type": "org.celstec.arlearn2.beans.account.Account",
                "accountType": $scope.myAccount.accountType,
                "localId": $scope.myAccount.localId,
                "email": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                "picture": $scope.myAccount.picture,
                "name": $scope.user.name
            });
        };

        $scope.send = function(){
            console.log("J")
            MessageService.sendEmail()
        };

        $scope.upload = function (file) {
            $scope.progressPercentage = 0;

            AccountService.uploadUrl($scope.myAccount.accountType+":"+$scope.myAccount.localId, file.name).$promise.then(function(url){
                console.log(url);
                Upload.upload({
                    url: url.uploadUrl,
                    data: {file: file, 'username': $scope.username}
                }).then(function (resp) {
                    AccountService.update({
                        "type": "org.celstec.arlearn2.beans.account.Account",
                        "accountType": $scope.myAccount.accountType,
                        "localId": $scope.myAccount.localId,
                        "email": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                        "name": $scope.myAccount.familyName+" "+$scope.myAccount.givenName,
                        "picture": config.server+"/uploadUserContent/"+resp.config.data.file.name+"?account="+$scope.myAccount.accountType+":"+$scope.myAccount.localId
                    });

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