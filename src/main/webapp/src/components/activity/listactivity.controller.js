angular.module('DojoIBL')

    .controller('ListActivityController', function ($scope, $firebaseArray, AccountService) {

        var itemsRef = firebase.database().ref("items").child($scope.activity.id);

        $scope.items = $firebaseArray(itemsRef);

        $scope.addItem = function() {
            AccountService.myDetails().then(function(data){
                $scope.account = data;
                //console.log(data)
                if($scope.item){

                    $scope.items.$add({
                        localId: data.localId,
                        title: $scope.item,
                        checked: false,
                        date: firebase.database.ServerValue.TIMESTAMP
                    });

                    $scope.item = '';
                }
            });
        };

        $scope.changeStatus = function(item, activity) {

            var updates = {};

            updates['/items/' + activity.id + '/' + item.$id ] = {
                checked: !item.checked,
                date: firebase.database.ServerValue.TIMESTAMP,
                localId: item.localId,
                title: item.title
            };

            return firebase.database().ref().update(updates);

        };

        $scope.removeItem = function(item){

            var itemRef = firebase.database().ref("items").child($scope.activity.id);

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

                itemRef.child(item.$id).remove(function(error){
                    if (error) {
                        console.log("Error:", error);
                    } else {
                        console.log("Removed successfully!");
                    }
                });

            });
        };
    }
);