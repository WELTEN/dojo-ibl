angular.module('DojoIBL')

    .directive('responseQuestion', function ($compile, ResponseService, AccountService, $stateParams, UserService, $templateRequest) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/dist/templates/directives/responseQuestion.html',
            link: function (scope, element, attrs) {

                AccountService.myDetails().then(
                    function(data){
                        scope.myAccount = data;
                    }
                );

                scope.getUser = function (response){
                    return UserService.getUserFromCache(response.userEmail.split(':')[1]).name;
                };
                scope.getAvatar = function (response){
                    return UserService.getUserFromCache(response.userEmail.split(':')[1]).picture;
                };

                scope.removeComment = function(data){
                    ResponseService.deleteResponse(data.responseId);
                };

                scope.sendChildComment = function(responseParent, responseChildren) {

                    AccountService.myDetails().then(function(data){
                        ResponseService.newResponse({
                            "type": "org.celstec.arlearn2.beans.run.Response",
                            "runId": $stateParams.runId,
                            "deleted": false,
                            "generalItemId": $stateParams.activityId,
                            "userEmail": data.accountType+":"+data.localId,
                            "responseValue": responseChildren,
                            "parentId": responseParent.responseId,
                            "revoked": false,
                            "lastModificationDate": new Date().getTime()
                        }).then(function(childComment){
                            scope.responseChildren = null;
                        });
                    });
                };

                //var isField = (scope.response.parentId != 0);
                //
                //console.log(isField);
                //
                //if(!isField) {
                //    // Load the html through $templateRequest
                //    $templateRequest('/dist/templates/directives/response.html').then(function(html){
                //        // Convert the html to an actual DOM node
                //        var template = angular.element(html);
                //        // Append it to the directive element
                //        $("div[data-item='"+scope.response.parentId+"']").append(template);
                //        // And let Angular $compile it
                //        $compile(template)(scope);
                //
                //    });
                //}

                //$templateRequest("/dist/templates/directives/response.html").then(function(html){
                //    var template = angular.element(html);
                //    element.append(template);
                //    $compile(template)(scope);
                    $("div[data-item='"+scope.response.parentId+"']").append(element);
                //});


            }
        };
    }
);