angular.module('DojoIBL')
    .directive('response', function ($compile, ResponseService, AccountService, $stateParams,
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
    })
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

;