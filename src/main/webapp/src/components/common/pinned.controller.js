angular.module('DojoIBL')

    .controller('PinnedController', function ($scope, firebase, $firebaseArray, $stateParams, $location) {


        $scope.$on('inquiry-run', function(event, args) {

            $scope.pinned_messages = {};

            loadPinnedContent(args.runId);
        });


        $scope.isActive = false;
        $scope.activeButton = function() {
            $scope.isActive = !$scope.isActive;
        }

        function loadPinnedContent(runId) {
            var rootRef = firebase.database().ref();
            var regionMessagesRef = rootRef.child("responses/" + runId);
            $scope.pinned_messages = {}; // Here I reset the scope variable
            regionMessagesRef.on('child_added', function (rmSnap) {
                var responsesRef = firebase.database().ref("responses").child(runId).child(rmSnap.key);
                responsesRef.once("value", function (snapshot) {

                    snapshot.forEach(function (child) {
                        var responsesRef = firebase.database().ref("responses").child(runId).child(child.val().generalItemId);
                        responsesRef.on('child_removed', function (rmSnap) {
                            delete $scope.pinned_messages[rmSnap.key];
                        });
                        if (child.val().pinned)
                            $scope.pinned_messages[child.key] = child.val()
                        else
                            delete $scope.pinned_messages[child.key]
                    });
                });
            });
            regionMessagesRef.on('child_changed', function (rmSnap) {
                var responsesRef = firebase.database().ref("responses").child(runId).child(rmSnap.key);
                responsesRef.once("value", function (snapshot) {
                    snapshot.forEach(function (child) {
                        if (child.val().pinned)
                            $scope.pinned_messages[child.key] = child.val()
                        else
                            delete $scope.pinned_messages[child.key]
                    });
                })
            });
        }

        loadPinnedContent($stateParams.runId);

        $scope.goToActivity = function(inqId, index, activity) {
            $scope.toggle = true;
            $location.path('inquiry/'+inqId+'/phase/'+ index + '/activity/' +activity);
        };

    }
);