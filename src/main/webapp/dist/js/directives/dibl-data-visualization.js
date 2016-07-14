angular.module('DojoIBL')
    .directive('data', function ($compile, $parse, ResponseService, AccountService, $stateParams, UserService, ActivityService,  $templateRequest, config) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/dist/templates/directives/data.html',
            link: function (scope, elem, attr) {

                AccountService.myDetails().then(
                    function(data){
                        scope.myAccount = data;
                    }
                );

                UserService.getUserByAccount(scope.response.runId, scope.response.userEmail.split(':')[1]).then(function(data){
                    scope.user = data;
                });

                scope.removeComment = function(data){
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

                        ResponseService.deleteResponse(data.responseId);

                    });
                };


                if(scope.response.responseValue.indexOf("{") == -1){
                    scope.extension = "text";
                    scope.resource = scope.response.responseValue;
                }else{
                    var json = JSON.parse(scope.response.responseValue);

                    if (json.pdfUrl){
                        scope.type = json.fileType;
                        scope.name = json.fileName;
                        scope.extension = "pdf"
                        scope.resource = json.pdfUrl;
                    }
                    if (json.videoUrl){
                        scope.type = json.fileType;
                        scope.name = json.fileName;
                        scope.extension = "video"
                        scope.resource = json.videoUrl;
                    }
                    if (json.imageUrl){
                        scope.type = json.fileType;
                        scope.name = json.fileName;
                        scope.extension = "image"
                        scope.resource = json.imageUrl;
                    }
                    if (json.audioUrl){
                        scope.type = json.fileType;
                        scope.name = json.fileName;
                        scope.extension = "audio"
                        scope.resource = json.audioUrl;
                    }
                }
            }
        };
    }
);