angular.module('DojoIBL')

    .controller('PinnedController', function ($scope, firebase, $firebaseArray, $stateParams, ActivityService) {


        var rootRef = firebase.database().ref();
        var regionMessagesRef = rootRef.child("responses/"+ $stateParams.runId);
        $scope.pinned_messages = {}; // Here I reset the scope variable
        regionMessagesRef.on('child_added', function(rmSnap) {

            var responsesRef = firebase.database().ref("responses").child($stateParams.runId).child(rmSnap.key);

            responsesRef.once("value", function(snapshot) {

                console.log(snapshot.val(), snapshot.key)

                snapshot.forEach(function(child) {

                    var responsesRef = firebase.database().ref("responses").child($stateParams.runId).child(child.val().generalItemId);

                    responsesRef.on('child_removed', function(rmSnap) {
                          delete $scope.pinned_messages[rmSnap.key];
                    });

                    if(child.val().pinned)
                        $scope.pinned_messages[child.key] = child.val()
                    else
                        delete $scope.pinned_messages[child.key]
                });
            });
        });

        regionMessagesRef.on('child_changed', function(rmSnap) {

            var responsesRef = firebase.database().ref("responses").child($stateParams.runId).child(rmSnap.key);
            responsesRef.once("value", function(snapshot) {

                snapshot.forEach(function(child) {
                    if(child.val().pinned)
                        $scope.pinned_messages[child.key] = child.val()
                    else
                        delete $scope.pinned_messages[child.key]
                });
            })
        });


        regionMessagesRef.on('child_removed', function(rmSnap) {

            console.log(rmSnap.key, rmSnap.val());

            var responsesRef = firebase.database().ref("responses").child($stateParams.runId).child(rmSnap.key);
            responsesRef.once("value", function(snapshot) {

                console.log(3, snapshot.val())

                snapshot.forEach(function(child) {
                    delete $scope.pinned_messages[child.key]
                });
            })
        });

    }
);