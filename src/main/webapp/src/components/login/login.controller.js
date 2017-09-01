angular.module('DojoIBL')

    .controller('LoginController', function ($scope, Session, Oauth, $http, firebase, toaster) {

        $scope.googleProvider = function () {

            var provider = new firebase.auth.GoogleAuthProvider();

            firebase.auth().signInWithPopup(provider).then(function(result) {

                if (result.credential) {
                    // This gives you a Google Access Token. You can use it to access the Google API.
                    var token = result.credential.accessToken;
                    // The signed-in user info.
                    var user = result.user;

                    var email = user.email;
                    var emailVerified = user.emailVerified;
                    var photoURL = user.photoURL;
                    var uid = user.uid;
                    var phoneNumber = user.phoneNumber;
                    var providerData = user.providerData;

                    $http.defaults.headers.common['Authorization'] = result.user.De;

                    Session.authenticate().then(function(data){

                        Session.setAccessToken(data);
                        window.location.href='/#/home';
                    });

                }
            }).catch(function(error) {
                // Handle Errors here.
                var errorCode = error.code;
                var errorMessage = error.message;
                // The email of the user's account used.
                var email = error.email;
                // The firebase.auth.AuthCredential type that was used.
                var credential = error.credential;
                // ...
                console.log(4, errorCode);
            });

        }

        $scope.emailProvider = function () {

            firebase.auth().signInWithEmailAndPassword($scope.email, $scope.password).then(function(result) {


                $http.defaults.headers.common['Authorization'] = result.De;

                Session.authenticate().then(function(data){

                    Session.setAccessToken(data);
                    window.location.href='/#/home';
                });

            }).catch(function(error) {
                // Handle Errors here.
                var errorCode = error.code;
                var errorMessage = error.message;

                toaster.warning({
                    title: errorCode,
                    body: errorMessage
                });
            });



        }

    })
    .controller('RegisterController', function ($scope, Session, firebase, $http, toaster, AccountService){
        $scope.register = function() {
            firebase.auth().createUserWithEmailAndPassword($scope.email, $scope.password).then(function(result) {


                $http.defaults.headers.common['Authorization'] = result.De;

                $scope.uid = result.uid;

                Session.authenticate().then(function(data){
                    Session.setAccessToken(data);

                    AccountService.update({
                        "accountType": 7,
                        "localId": $scope.uid,
                        "email": $scope.email,
                        "name": $scope.name,
                        "given_name": $scope.name
                    })


                    window.location.href='/#/login';
                });

            }).catch(function(error) {
                // Handle Errors here.
                var errorCode = error.code;
                var errorMessage = error.message;

                console.log(errorCode)

                toaster.error({
                    title: errorCode,
                    body: errorMessage
                });
            });
        }
    })
;
