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

                UserService.getUserByAccount(scope.message.runId, scope.message.senderId).then(function(data){
                    scope.user = data;
                });
            }
        };
    })
;