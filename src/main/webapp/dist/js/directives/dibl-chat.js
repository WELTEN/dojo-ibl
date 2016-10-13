angular.module('DojoIBL')

    .directive('scrollPosition', function($window) {
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
    })

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
                            console.log("scroll")
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
                    console.log("key")
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

                //UserService.getUserByAccount(scope.message.runId, scope.message.senderId).then(function(data){
                //    scope.user = data;
                //});
            }
        };
    })

;