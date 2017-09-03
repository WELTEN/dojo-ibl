angular.module('DojoIBL')

    .directive('responses', function(AccountService) {
        return  {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/src/components/response/response.template.html',
            link: function (scope, element, attrs) {

                AccountService.myDetails().then(function(data){
                    scope.myAccount = data;
                });


                scope.removeComment = function(data){

                    var responsesRef = firebase.database().ref("responses").child(data.runId).child(data.generalItemId);

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

                scope.newText = scope.response.responseValue;

                    scope.saveComment = function(newValue) {

                    var updates = {};

                        updates['/responses/' + scope.response.runId + '/' + scope.response.generalItemId + '/' + scope.response.$id ] = {
                            "type": "org.celstec.arlearn2.beans.run.Response",
                            "runId": scope.response.runId,
                            "deleted": scope.response.deleted,
                            "generalItemId": scope.response.generalItemId,
                            "userAccountType": scope.response.userAccountType,
                            "userLocalId": scope.response.userLocalId,
                            "userName": scope.response.userName,
                            "userProfile": scope.response.userProfile,
                            "responseValue": newValue,
                            "parentId": scope.response.parentId,
                            "revoked": scope.response.revoked,
                            "lastModificationDate": firebase.database.ServerValue.TIMESTAMP
                        };

                        scope.newText = '';
                        scope.hiddenDiv = false;

                        return firebase.database().ref().update(updates);

                };


                //console.log(scope);

                //scope.sendComment = function(responseParent, responseChildren) {
                //
                //    AccountService.myDetails().then(function(data){
                //
                //        scope.responses.$add({
                //            "type": "org.celstec.arlearn2.beans.run.Response",
                //            "runId": scope.responseChildren.runId,
                //            "deleted": false,
                //            "generalItemId": scope.generalItemId.activityId,
                //            "userAccountType": data.accountType,
                //            "userLocalId": data.localId,
                //            "userName": data.name,
                //            "userProfile": data.picture,
                //            "responseValue": $scope.responseText,
                //            "parentId": scope.responseParent.$id,
                //            "revoked": false,
                //            "lastModificationDate": firebase.database.ServerValue.TIMESTAMP
                //        });
                //
                //    });
                //};

                //if (angular.isArray(scope.response.children)) {
                //    element.append("<collection collection='member.children'></collection>");
                //    $compile(element.contents())(scope)
                //}
            }
        };
    })

;