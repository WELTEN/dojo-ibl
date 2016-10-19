angular.module("DojoIBL")
    .controller('dragCtrlr', ['$scope', function ($scope) {
        // variables
        var obj = {
            id: null,
            content: null,
            group: null
        };

        $scope.obj = angular.copy(obj);

        // listeners

        $scope.$on('drag.started', function (evt, data) {
            if (angular.isDefined(data.obj))
                $scope.obj = data.obj;
        });

        $scope.$on('drag.stopped', function (evt, data) {
            $scope.obj = angular.copy(obj); // reset controller's object
        });

        $scope.$on('data.clean', function (evt, data) {
            $scope.obj = angular.copy(obj); // reset controller's object
        });
    }]) // end controller(dragCtrlr)

    .directive('draggable', ['$compile', function ($compile) {
        return {
            restrict: 'AE',
            transclude: true,
            replace: true,
            scope: {},
            templateUrl: function (el, attrs) {
                return (angular.isDefined(attrs.template)) ? attrs.template : '/src/templates/directives/draggable-default.html';
                //return "";
            },
            link: function (scope, el, attrs, ctrlr, transFn) {
                // object properties, will be passed through jQuery UI events
                scope.obj = {
                    id: null,
                    content: '',
                    group: null
                };

                scope.placeholder = false;


                // get the content from the transclusion function
                transFn(scope, function (clone, innerScope) {
                    // need to compile the content to make sure we get any HTML that was transcluded
                    var dummy = angular.element('<div></div>');
                    dummy.append($compile(clone)(innerScope));
                    scope.obj.content = dummy.html();
                    dummy = null;

                    // remove ng-scope spans/classes & empty class attributes added by angular to get true content
                    scope.obj.content = scope.obj.content.replace(/<span class="ng\-scope">([^<]+)<\/span>/gi, "$1");
                    scope.obj.content = scope.obj.content.replace(/\s*ng\-scope\s*/gi, '');
                    scope.obj.content = scope.obj.content.replace(/\s*class\=\"\"\s*/gi, '');
                });

                // save the object's id if there is one
                if (angular.isDefined(attrs.id))
                    scope.obj.id = attrs.id;

                if (angular.isDefined(attrs.placeholder))
                    scope.placeholder = scope.$eval(attrs.placeholder);

                // setup the options object to pass to jQuery UI's draggable method
                var opts = (angular.isDefined(attrs.options)) ? scope.$eval(attrs.options) : {};

                // assign the object's group if any
                if (angular.isDefined(attrs.group)) {
                    scope.obj.group = attrs.group;
                    opts.stack = '.' + attrs.group;
                }

                var evts = {
                    start: function (evt, ui) {
                        if (scope.placeholder)
                            ui.helper.wrap('<div class="dragging"></div>');
                        scope.$apply(function () {
                            scope.$emit('drag.started', {obj: scope.obj});
                        });
                    },
                    drag: function (evt) {
                        scope.$apply(function () {
                            scope.$emit('drag.dragging', {obj: scope.obj});
                        });
                    },
                    stop: function (evt, ui) {
                        if (scope.placeholder)
                            ui.helper.unwrap();
                        scope.$apply(function () {
                            scope.$emit('drag.stopped', {obj: scope.obj});
                        });
                    }
                };

                // combine options passed through element attributes with events
                var options = angular.extend({}, opts, evts);
                el.draggable(options); // make element draggable
            } // end link
        }; // end return
    }]) // end directive(draggable)

    .directive('droppable', ['$compile', function ($compile) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {},
            templateUrl: function (el, attrs) {
                return (angular.isDefined(attrs.template)) ? attrs.template : '/src/templates/directives/droppable-default.html';
                //return "";
            },
            link: function (scope, el, attrs, ctrlr, transFn) {
                scope.obj = {
                    id: null,
                    dropped: []
                };

                // save the object's id if there is one
                if (angular.isDefined(attrs.id))
                    scope.obj.id = attrs.id;

                // setup the options object to pass to jQuery UI's draggable method
                var opts = (angular.isDefined(attrs.options)) ? scope.$eval(attrs.options) : {};

                var evts = {
                    drop: function (evt, ui) { // apply content
                        scope.$apply(function () {
                            scope.obj.dropped.push(angular.copy(scope.$parent.obj));
                            scope.$emit('data.clean');
                        });
                    }
                };

                var options = angular.extend({}, opts, evts);
                el.droppable(options);
            } // end link

        }; // end return
    }]); // end directive(droppable)