angular.module('DojoIBL')

    .controller('TextActivityController', function ($scope, AccountService, $firebaseArray) {
        var itemsRef = firebase.database().ref("richtexts").child($scope.activity.id);

        $scope.updateMode = false;

        $scope.richtexts = $firebaseArray(itemsRef);

        $scope.addText = function() {
            AccountService.myDetails().then(function(data){
                $scope.account = data;

                if($scope.richtext){

                    $scope.richtexts.$add({
                        localId: data.localId,
                        name: data.name,
                        text: $scope.richtext,
                        date: firebase.database.ServerValue.TIMESTAMP
                    });
                    $scope.richtext = ''
                }
            });
        };

        $scope.updateText = function(richtext, activity) {

           $scope.richtext = richtext.text;
           $scope.richtextToBeUpdated = richtext;
           $scope.updateMode = true;
        };



        $scope.saveText = function(richtext) {

            console.log(richtext)

            var updates = {};

            updates['/richtexts/' + $scope.activity.id + '/' + $scope.richtextToBeUpdated.$id ] = {
                date: firebase.database.ServerValue.TIMESTAMP,
                localId: $scope.richtextToBeUpdated.localId,
                name: $scope.richtextToBeUpdated.name,
                text: richtext
            };

            $scope.updateMode = false;
            $scope.richtextToBeUpdated = '';
            $scope.richtext = ''

            return firebase.database().ref().update(updates);


        };




        $scope.removeText = function(item){

            var itemRef = firebase.database().ref("richtexts").child($scope.activity.id);

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