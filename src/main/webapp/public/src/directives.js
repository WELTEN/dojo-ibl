angular.module('DojoIBL')

    .directive('diblFooter', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/footer.directive.html',
            controller: 'FooterController'
        };
    }

);;angular.module('DojoIBL')

    .directive('diblHeader', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/header.directive.html',
            controller: 'HeaderController'
        };
    }

);;angular.module('DojoIBL')
    .directive('original', function() {
        return {
            restrict: 'A',
            scope: { hires: '@' },
            link: function(scope, element, attrs) {
                element.one('load', function() {
                    element.attr('ng-src', scope.src);
                });
            }
        };
    });;angular.module('DojoIBL')
    .directive("scroll", ["$window", function ($window) {
        return function (scope, element, attrs) {
            angular.element($window).bind("scroll", function () {
                if (this.pageYOffset >= 165) {
                    scope.boolChangeClass = true;
                    //console.log('Scrolled below header.');
                } else {
                    scope.boolChangeClass = false;
                    //console.log('Header is in view.');
                }
                scope.$apply();
            });

            /////
            // When scrolling move videos in activity view to the right side


        };
    }])
;;angular.module('DojoIBL')

    .directive('diblSidebar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/sidebar.directive.html',
            controller: 'SidebarController'
        };
    }

);;angular.module('DojoIBL')

    .directive('diblToolbar', function() {
        return  {
            restrict: 'E',
            templateUrl: '/src/components/common/toolbar.directive.html',
            controller: 'ToolbarController'
        };
    }

);;angular.module('DojoIBL')

    .directive('scrollPosition', ["$window", function($window) {
        return {
            scope: {
                scroll: '=scrollPosition'
            },
            link: function(scope, element, attrs) {
                var windowEl = angular.element($window);
                var handler = function() {
                    scope.scroll = windowEl.scrollTop();
                }
                windowEl.on('scroll', scope.$apply.bind(scope, handler));
                handler();
            }
        };
    }])

    .directive('chat', ["$window", function($window) {
        return  {
            restrict: 'A',
            link: function (scope, element, attrs, controller) {
                scope.$watch(
                    function () {
                        return element[0].childNodes.length;
                    },
                    function (newValue, oldValue) {
                        if (newValue !== oldValue) {
                            console.log("scroll")
                            element.scrollTop(element[0].scrollHeight);
                        }
                    });
            }
        };
    }])

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

    .directive('messageL', function() {
        return {
            restrict: "E",
            replace: true,
            scope: {
                message: '='
            },
            templateUrl: '/src/components/message/messagel.directive.template.html',
            link: function (scope, elem, attr) {
            }
        };
    })
    .directive('messageR', function() {
        return {
            restrict: "E",
            replace: true,
            scope: {
                message: '='
            },
            templateUrl: '/src/components/message/messager.directive.template.html',
            link: function (scope, elem, attr) {
            }
        };
    })

;;angular.module('DojoIBL')

    .directive('comment', function() {
        return  {
            restrict: 'EA',
            templateUrl: '/src/templates/directives/response.html'
        };
    })

;;angular.module('DojoIBL')
    .directive('data', ["$compile", "$parse", "ResponseService", "AccountService", "$stateParams", "UserService", "ActivityService", "$templateRequest", "config", function ($compile, $parse, ResponseService, AccountService, $stateParams, UserService, ActivityService,  $templateRequest, config) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/src/components/response/data.template.html',
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
    }]
);;angular.module('DojoIBL')
    .directive('response', ["$compile", "ResponseService", "AccountService", "$stateParams", "UserService", "ActivityService", "RunService", "$location", "$anchorScroll", "$sce", function ($compile, ResponseService, AccountService, $stateParams,
                                     UserService, ActivityService,RunService, $location, $anchorScroll, $sce) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/src/components/response/response.template.html',
            link: function (scope, element, attrs) {
                if(scope.response.parentId != 0){
                //
                    scope.response.parentValue = ResponseService.getResponsesClosureById(scope.response.parentId, scope.response.runId, $stateParams.activityId);
                //
                //    if(angular.isUndefined(parent)){
                //        scope.response.responseValueNew = scope.response.responseValue;
                //    }else{
                //        var html1 = $compile(parent.responseValue);
                //        var html = $sce.trustAsHtml(parent.responseValue)
                //        console.log(html1)
                //        console.log(html)
                //        console.log(parent.responseValue)
                //        scope.response.responseValueNew = '<a class="navigationalLink" href="main.html#/inquiry/'+scope.response.runId+'/phase/'+$stateParams.phase+'/activity/'+$stateParams.activityId+'#'+scope.response.parentId+'" title="'+html+'">#'+scope.response.parentId+'</a>'+" "+scope.response.responseValue;
                //    }
                }
                // else{
                    scope.response.phase = $stateParams.phase;
                    scope.response.responseValueNew = scope.response.responseValue;
                //}

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

                scope.share = function(data){

                    RunService.getRunById(data.runId).then(function (run) {
                        ActivityService.getActivityById(data.generalItemId, run.gameId).then(function(act){
                            var controllerElement = document.querySelector('[ng-controller=InstantMessagingController]');
                            var controllerScope = angular.element(controllerElement).scope();
                            controllerScope.bodyMessage = act.name+"@"+UserService.getUserFromCache(data.userEmail.split(':')[1]).name+": "+data.responseValue;
                        });
                    });
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
                            scope.hiddenDiv = false;
                        });
                    });
                };

                scope.gotoAnchor = function(x) {
                    var newHash = x;
                    if ($location.hash() !== newHash) {
                        // set the $location.hash to `newHash` and
                        // $anchorScroll will automatically scroll to it
                        $location.hash(x);
                    } else {
                        // call $anchorScroll() explicitly,
                        // since $location.hash hasn't changed
                        $anchorScroll();
                    }
                };

                //console.log(scope.response);

                //$("div[data-item='"+scope.response.parentId+"']").parent().append(element);
            }
        };
    }])
    //.directive('pane', function(){
    //    return {
    //        restrict: 'A',
    //        transclude: true,
    //        scope: { parentId:'@', parentResponseValue:'@' },
    //        template: '<a class="navigationalLink" title="{{ parentResponseValue }}" href="#{{ parentId }}">Link</a>'
    //    };
    //})
// a directive to auto-collapse long text
// in elements with the "dd-text-collapse" attribute

    .directive('ddTextCollapse', ['$compile', function($compile) {

    return {
        restrict: 'A',
        scope: true,
        link: function(scope, element, attrs) {

            // start collapsed
            scope.collapsed = false;

            // create the function to toggle the collapse
            scope.toggle = function() {
                scope.collapsed = !scope.collapsed;
            };

            // wait for changes on the text
            attrs.$observe('ddTextCollapseText', function(text) {

                // get the length from the attributes
                var maxLength = scope.$eval(attrs.ddTextCollapseMaxLength);

                if (text.length > maxLength) {
                    // split the text in two parts, the first always showing
                    var firstPart = String(text).substring(0, maxLength);
                    var secondPart = String(text).substring(maxLength, text.length);

                    // create some new html elements to hold the separate info
                    var firstSpan = $compile('<span>' + firstPart + '</span>')(scope);
                    var secondSpan = $compile('<span ng-if="collapsed">' + secondPart + '</span>')(scope);
                    var moreIndicatorSpan = $compile('<span ng-if="!collapsed">... </span>')(scope);
                    var lineBreak = $compile('<br ng-if="collapsed">')(scope);
                    var toggleButton = $compile('<span class="collapse-text-toggle" ng-click="toggle()">{{collapsed ? "less" : "more"}}</span>')(scope);

                    // remove the current contents of the element
                    // and add the new ones we created
                    element.empty();
                    element.append(firstSpan);
                    element.append(secondSpan);
                    element.append(moreIndicatorSpan);
                    element.append(lineBreak);
                    element.append(toggleButton);
                }
                else {
                    element.empty();
                    element.append(text);
                }
            });
        }
    };
}])

;;angular.module('DojoIBL')
    .directive('responseQuestion', ["$compile", "ResponseService", "AccountService", "$stateParams", "UserService", "ActivityService", "RunService", function ($compile, ResponseService, AccountService, $stateParams, UserService, ActivityService,RunService) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/src/components/response/response.question.template.html',
            link: function (scope, element, attrs) {

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

                scope.share = function(data){

                    RunService.getRunById(data.runId).then(function (run) {
                        ActivityService.getActivityById(data.generalItemId, run.gameId).then(function(act){
                            var controllerElement = document.querySelector('[ng-controller=InstantMessagingController]');
                            var controllerScope = angular.element(controllerElement).scope();
                            controllerScope.bodyMessage = act.name+"@"+UserService.getUserFromCache(data.userEmail.split(':')[1]).name+": "+data.responseValue;
                        });
                    });
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

                $("div[data-item='"+scope.response.parentId+"']").parent().append(element);
            }
        };
    }]
);;angular.module('DojoIBL')

    .directive('responses', function() {
        return  {
            restrict: "E",
            replace: true,
            scope: {
                responses: '='
            },
            template: "<response ng-repeat='response in responses | orderByDayNumber:\"lastModificationDate\"' response='response'></response>"
        };
    })

;;angular.module('DojoIBL')
    .directive('item', ["$compile", "$parse", "ResponseService", "AccountService", "$stateParams", "UserService", "ActivityService", "$templateRequest", "config", function ($compile, $parse, ResponseService, AccountService, $stateParams, UserService, ActivityService,  $templateRequest, config) {
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
                }else{
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
    }]
);