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

                var json = JSON.parse(scope.response.responseValue);

                if (json.answer){

                }
                if (json.pdfUrl){
                    scope.name = json.fileName;
                    scope.type = "pdf"
                    scope.resource = json.pdfUrl;
                }
                if (json.videoUrl){
                    scope.name = json.fileName;
                    scope.type = "video"
                    scope.resource = json.videoUrl;
                }
                if (json.imageUrl){
                    scope.name = json.fileName;
                    scope.type = "image"
                    scope.resource = json.imageUrl;
                }
                if (json.audioUrl){
                    scope.name = json.fileName;
                    scope.type = "audio"
                    scope.resource = json.audioUrl;
                }

            }
        };
    }
);