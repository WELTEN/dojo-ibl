angular.module('DojoIBL')
    .directive('data', function ($compile, ResponseService, AccountService, $stateParams, UserService, ActivityService,  $templateRequest, config) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/dist/templates/directives/data.html',
            link: function (scope, elem, attr) {

                var json = JSON.parse(scope.response.responseValue);

                scope.getResponseString= function(response) {
                    var json = JSON.parse(response.responseValue);
                    if (json.answer) return json.answer;
                    if (json.text) return json.text;
                    if (json.videoUrl) return "watch video";
                    if (json.imageUrl)
                        return json.imageUrl;
                    if (json.audioUrl) return "watch audio"
                };

                scope.getResponse= function(response) {
                    var json = JSON.parse(response.responseValue);
                    if (json.answer) return json.answer;
                    if (json.text) return json.text;
                    if (json.videoUrl)
                        return "video";
                    if (json.imageUrl)
                        return "image";
                    if (json.pdfUrl)
                        return "pdf";
                    if (json.audioUrl) return "watch audio"
                };
            }
        };
    }
);