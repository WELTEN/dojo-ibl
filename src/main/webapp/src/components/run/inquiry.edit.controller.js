angular.module('DojoIBL')

    .controller('InquiryEditRunController', function ($scope, $sce,
                                                      $stateParams, $state, Session, RunService, UserService, Contacts,
                                                      $firebaseArray, firebase) {

        $scope.roles = [];

        RunService.getRunById($stateParams.runId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.game = data.game;

            if(!$scope.game.config.roles)
                $scope.game.config.roles = [];

            $scope.run = data;

            if (data.lat) {
                $scope.coords.latitude = data.lat;
                $scope.coords.longitude = data.lng;
                $scope.map.center.latitude = data.lat;
                $scope.map.center.longitude = data.lng;
                $scope.showMap = true;
            }
        });

        //UserService.getUsersForRun(parseInt($stateParams.runId)).then(function(data){
        //    $scope.usersRun = data;
        //});

        $scope.ok = function(){
            if($scope.run.runId){
                RunService.updateRun($scope.run);
            }else{
                RunService.newRun($scope.run);
            }
        };

        $scope.removeAccess = function(runId, account){
            swal({
                title: "Remove user from inquiry",
                text: "Are you sure you want to remove this student from the inquiry?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, remove the student!",
                closeOnConfirm: false
            }, function () {
                swal("Student removed!", "The student has been removed from the list of participants in this inquiry.", "success");

                console.log($scope.usersRun, runId, account.email);

                var idx = arrayObjectIndexOf($scope.usersRun, account.email, "email");

                if (idx > -1) {
                    $scope.usersRun.splice(idx, 1);
                }

                RunService.removeAccessToInquiryRun(runId, account)

            });
        };

        $scope.friends = [];

        Contacts.getContacts({date:0}).$promise.then(
            function(data){
                for(var i=0;i<data.accountList.length;i++){
                    $scope.friends.push(data.accountList[i]);
                }
            }
        );

        //////
        // Visualizations
        /////

        var visualizationsRef = firebase.database().ref("visualizations").child($stateParams.runId);

        $scope.visualizations = $firebaseArray(visualizationsRef);

        $scope.addVisualization = function() {
            if($scope.visualization){
                $scope.visualizations.$add({
                    url: $scope.visualization.url,
                    title: $scope.visualization.title,
                    description: $scope.visualization.description,
                    date: firebase.database.ServerValue.TIMESTAMP
                });

                $scope.visualization = {};
            }
        };

        //////////////////
        // Extra functions
        //////////////////
        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    });