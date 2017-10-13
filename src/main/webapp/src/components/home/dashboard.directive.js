angular.module('DojoIBL')

    .directive('queryBuilder', ['$compile', function ($compile) {
        return {
            restrict: 'E',
            scope: {
                fields: '=',
                result: '=',
                conditions: '=?',
                query: '=',
                queryJson: '=?'
            },
            controller: 'DashboardController',
            templateUrl: '/src/components/home/dashboard.directive.html',
            compile: function (element, attrs) {
                var content, directive;
                content = element.contents().remove();
                return function (scope, element, attrs) {
                    scope.conditions = [
                        { name: '=' },
                        { name: '<>' },
                        { name: '<' },
                        { name: '<=' },
                        { name: '>' },
                        { name: '>=' },
                        { name: 'Contains'}
                    ];

                    scope.timestamp_functions = [
                        { name: 'Date format', fun: 'DATE'},
                        { name: 'Time format', fun: 'TIME'}
                    ];

                    scope.operators = [
                        { name: 'AND' },
                        { name: 'OR' }
                    ];
                    scope.group = scope.queryJson ? scope.queryJson : {
                        "operator": "AND",
                        "rules": []
                    };


                    scope.query = '';
                    scope.conditions = scope.conditions || DEFAULT_CONDITIONS;

                    scope.addSelectCondition = function () {
                        if(!scope.fields || !scope.fields.length) {
                            return;
                        }

                        scope.group.select.push({
                            type: scope.field.type,
                            field: scope.field.field,
                            asName: scope.asName,
                            function: (scope.function ? scope.function.fun : '' )
                        });

                        scope.field = ''
                        scope.function = ''
                        scope.asName = ''
                    };

                    scope.removeSelectStatement = function (index) {
                        scope.group.select.splice(index, 1);
                    };

                    scope.addWhereRule = function () {
                        if(!scope.fields || !scope.fields.length) {
                            return;
                        }

                        scope.group.where.rules.push({
                            condition: scope.whereCondition.name,
                            field: scope.whereField.name,
                            data: scope.fieldValue,
                            operator: (scope.whereOpe ? scope.whereOpe.name : '' )
                        });

                        scope.fieldValue = ''
                        scope.whereCondition = ''
                        scope.whereField = ''
                    };

                    scope.removeWhereRule = function (index) {
                        scope.group.where.rules.splice(index, 1);
                    };

                    scope.addWhereGroup = function () {
                        scope.group.rules.push({
                            group: {
                                operator: 'AND',
                                rules: []
                            }
                        });
                    };

                    scope.removeGroup = function () {
                        "group" in scope.$parent && scope.$parent.group.rules.splice(scope.$parent.$index, 1);
                    };

                    function escape(str) {
                        return (str + '').replace(/[\\"']/g, '\\$&').replace(/\u0000/g, '\\0');
                    }

                    function computed(group) {
                        if (!group) return "";
                        if (!group.select) return "";
                        for (var str_select = "SELECT ", i = 0; i < group.select.length; i++) {

                            if(group.select[i].field == 'timestamp'){
                                str_select += group.select[i].function + "(" + group.select[i].field+") as " + group.select[i].asName;
                            }else if(group.select[i].field == 'function'){
                                str_select += group.select[i].function + " as " + group.select[i].asName;
                            }else{
                                str_select += group.select[i].field + " as " + group.select[i].asName;
                            }

                            if(i < group.select.length - 1){
                                str_select += ", "
                            }
                        }
                        str_select += "\nWHERE "

                        if (!group.where) return "";
                        for (i = 0; i < group.where.rules.length; i++) {

                            if(i > 0) {
                                str_select +=  group.where.rules[i].operator + " "
                            }
                            str_select +=  group.where.rules[i].field + " "+ group.where.rules[i].condition + " " +  group.where.rules[i].data + " ";


                        }

                        return str_select;

                        //if (!group.where) return "";
                        //for (var str = "(", i = 0; i < group.where.rules.length; i++) {
                        //    i > 0 && (str += ' '+group.where.operator+' ');
                        //
                        //    if(group.where.rules[i].group) {
                        //        str += computed(group.where.rules[i].group)
                        //    }
                        //    else {
                        //        if(group.where.rules[i].condition === 'Contains') {
                        //            str += group.where.rules[i].field + " LIKE \"%" + escape(group.where.rules[i].data) + "%\"";
                        //        }
                        //        else {
                        //            str += group.where.rules[i].field + " " + group.where.rules[i].condition + " \"" + escape(group.where.rules[i].data) + "\"";
                        //        }
                        //    }
                        //}
                        //
                        //return str + ")";
                    }



                    scope.$watch(function() {return JSON.stringify(scope.group)}, function (newValue) {
                        scope.query = computed(scope.group);
                        scope.queryJson = angular.copy(scope.group);
                    }, true);

                    directive || (directive = $compile(content));

                    element.append(directive(scope, function ($compile) {
                        return $compile;
                    }));
                };
            }
        }
    }]);