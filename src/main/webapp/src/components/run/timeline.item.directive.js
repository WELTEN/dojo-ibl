angular.module('DojoIBL')
    .directive('item', function ($compile, $parse, ResponseService, AccountService, $stateParams, UserService, ActivityService,  $templateRequest, config) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/src/components/run/timeline.item.directive.html',
            link: function (scope, elem, attr) {



                if(scope.response.responseValue.indexOf("{") == -1){
                    scope.extension = "text"
                    scope.resource = scope.response.responseValue;
                }else
                {
                    var json = JSON.parse(scope.response.responseValue);

                    if (json.documentUrl){
                        scope.type = json.fileType;
                        scope.name = json.fileName;
                        scope.extension = "document"
                        scope.resource = json.documentUrl;
                    }
                    if (json.excelUrl){
                        scope.type = json.fileType;
                        scope.name = json.fileName;
                        scope.extension = "excel"
                        scope.resource = json.excelUrl;
                    }
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