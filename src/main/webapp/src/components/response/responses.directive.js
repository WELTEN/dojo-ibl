angular.module('DojoIBL')

    .directive('responses', function(AccountService, $firebaseArray, NotificationService, $location) {
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
                        "generalItemName": scope.response.generalItemName,
                        "phase": scope.response.phase,
                        "pinned": (scope.response.pinned == undefined ? "" : scope.response.pinned ),
                        "userAccountType": scope.response.userAccountType,
                        "userLocalId": scope.response.userLocalId,
                        "userName": scope.response.userName,
                        "multimedia": (scope.response.multimedia == undefined ? "" : scope.response.multimedia ),
                        "userProfile": (scope.response.userProfile == undefined ? "" : scope.response.userProfile ),
                        "responseValue": newValue,
                        "parentId": scope.response.parentId,
                        "revoked": scope.response.revoked,
                        "edited": true,
                        "likeCount":  scope.response.likeCount,
                        "likes": (scope.response.likes ? scope.response.likes : {} ),
                        "lastModificationDate": scope.response.lastModificationDate
                    };

                    //scope.newText = '';
                    scope.hiddenDiv = false;

                    return firebase.database().ref().update(updates);

                };

                scope.likedComment = function(response, uid){

                    var responsesRef = firebase.database().ref("responses").child(scope.response.runId).child(scope.response.generalItemId).child(scope.response.$id);

                    responsesRef.transaction(function(response) {
                        if (response) {
                            if (response.likes && response.likes[uid]) {
                                response.likeCount--;
                                response.likes[uid] = null;
                            } else {
                                response.likeCount++;
                                if (!response.likes) {
                                    response.likes = {};
                                }
                                response.likes[uid] = true;

                                NotificationService.notify(response.userLocalId, {
                                    description: scope.myAccount.name + " liked your comment.",
                                    message: "",
                                    type: "like_response",
                                    user_id: uid,
                                    groupId: scope.response.runId,
                                    activityId: scope.response.generalItemId,
                                    responseId: scope.response.$id,
                                    phaseId: scope.response.phase,
                                    timestamp: firebase.database.ServerValue.TIMESTAMP
                                });

                            }
                        }


                        return response;
                    });
                };

                scope.pinnedComment = function(user, uid){

                    var responsesRef = firebase.database().ref("responses").child(scope.response.runId).child(scope.response.generalItemId).child(scope.response.$id);

                    responsesRef.transaction(function(response) {
                        if (response) {
                            if (response.pinned) {
                                response.pinned = false;
                            } else {
                                response.pinned = true;
                            }
                        }
                        return response;
                    });



                    var responseRef = firebase.database().ref("responses_pinned").child(scope.response.runId).child(scope.response.generalItemId);

                    var id = scope.response.$id;

                    responseRef.transaction(function(response) {
                        if (response) {

                            console.log(response[id]);

                            if (response[id]) {
                                response[id] = false;
                            } else {
                                if (!response[id]) {
                                    response[id] = {};
                                }
                                response[id] = true;
                            }
                        }

                        return response;
                    });



                    //var messagesRef = firebase.database().ref("responses_pinned").child(scope.response.runId).child(scope.response.generalItemId);
                    //
                    //
                    //messages = $firebaseArray(messagesRef);









                    //responseRef.set(true);
                };


                scope.currentHash = function() {
                    return $location.hash();
                };

                //console.log(scope);

                //scope.sendComment = function(responseParent) {
                //
                //    AccountService.myDetails().then(function(data){
                //
                //
                //        response.replies[uid] = true;
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
                //        responsesRef = firebase.database().ref("responses").child(scope.response.runId).child(scope.response.generalItemId).child(scope.response.$id)
                //
                //    });
                //};

            }
        };
    })

;