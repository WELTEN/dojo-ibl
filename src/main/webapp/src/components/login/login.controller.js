angular.module('DojoIBL')

    .controller('LoginController', function ($scope, Session, Oauth, $http, firebase) {


        var config = {
            apiKey: "AIzaSyBqdNjX3HNZStyuBklf3FQRGxRLTAbb2hc",
            authDomain: "dojo-ibl.firebaseapp.com",
            databaseURL: "https://dojo-ibl.firebaseio.com",
            projectId: "dojo-ibl",
            storageBucket: "dojo-ibl.appspot.com",
            messagingSenderId: "518897628174"
        };

        if (!firebase.apps.length) {
            firebase.initializeApp(config);
        }

        $scope.google = function () {

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

    }
);
