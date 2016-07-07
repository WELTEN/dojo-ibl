angular.module('DojoIBL')

    .directive('chat', function($window) {
        return  {
            restrict: 'A',
            link: function (scope, element, attrs, controller) {
                scope.$watch(
                    function () {
                        return element[0].childNodes.length;
                    },
                    function (newValue, oldValue) {
                        if (newValue !== oldValue) {
                            element.scrollTop(element[0].scrollHeight);
                        }
                    });
            }
        };
    })
    .directive('ngEnter', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                element.bind("keydown keypress", function(event) {
                    if(event.which === 13) {
                        scope.$apply(function(){
                            scope.$eval(attrs.ngEnter);
                        });

                        event.preventDefault();
                    }
                });
            }
        };
    })
    .directive('message', function(UserService) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                message: '='
            },
            templateUrl: '/dist/templates/directives/chat.html',
            link: function (scope, elem, attr) {

                scope.userName = UserService.getUserFromCache(scope.message.senderId).name;
                scope.avatar = UserService.getUserFromCache(scope.message.senderId).picture;

                //if(scope.response.responseValue.indexOf("{") == -1){
                //    scope.extension = "text";
                //    scope.resource = scope.response.responseValue;
                //}else{
                //    var json = JSON.parse(scope.response.responseValue);
                //
                //    if (json.pdfUrl){
                //        scope.type = json.fileType;
                //        scope.name = json.fileName;
                //        scope.extension = "pdf"
                //        scope.resource = json.pdfUrl;
                //    }
                //    if (json.videoUrl){
                //        scope.type = json.fileType;
                //        scope.name = json.fileName;
                //        scope.extension = "video"
                //        scope.resource = json.videoUrl;
                //    }
                //    if (json.imageUrl){
                //        scope.type = json.fileType;
                //        scope.name = json.fileName;
                //        scope.extension = "image"
                //        scope.resource = json.imageUrl;
                //    }
                //    if (json.audioUrl){
                //        scope.type = json.fileType;
                //        scope.name = json.fileName;
                //        scope.extension = "audio"
                //        scope.resource = json.audioUrl;
                //    }
                //}
            }
        };
    })
;