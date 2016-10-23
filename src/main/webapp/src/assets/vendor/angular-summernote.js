/*  angular-summernote v0.8.1 | (c) 2016 JeongHoon Byun | MIT license */
angular.module("summernote",[]).controller("SummernoteController",["$scope","$attrs","$timeout",function($scope,$attrs,$timeout){"use strict";var currentElement,summernoteConfig=angular.copy($scope.summernoteConfig)||{};if(angular.isDefined($attrs.height)&&(summernoteConfig.height=+$attrs.height),angular.isDefined($attrs.minHeight)&&(summernoteConfig.minHeight=+$attrs.minHeight),angular.isDefined($attrs.maxHeight)&&(summernoteConfig.maxHeight=+$attrs.maxHeight),angular.isDefined($attrs.placeholder)&&(summernoteConfig.placeholder=$attrs.placeholder),angular.isDefined($attrs.focus)&&(summernoteConfig.focus=!0),angular.isDefined($attrs.airmode)&&(summernoteConfig.airMode=!0),angular.isDefined($attrs.dialogsinbody)&&(summernoteConfig.dialogsInBody=!0),angular.isDefined($attrs.lang)){if(!angular.isDefined($.summernote.lang[$attrs.lang]))throw new Error('"'+$attrs.lang+'" lang file must be exist.');summernoteConfig.lang=$attrs.lang}summernoteConfig.callbacks=summernoteConfig.callbacks||{},angular.isDefined($attrs.onInit)&&(summernoteConfig.callbacks.onInit=function(evt){$scope.init({evt:evt})}),angular.isDefined($attrs.onEnter)&&(summernoteConfig.callbacks.onEnter=function(evt){$scope.enter({evt:evt})}),angular.isDefined($attrs.onFocus)&&(summernoteConfig.callbacks.onFocus=function(evt){$scope.focus({evt:evt})}),angular.isDefined($attrs.onPaste)&&(summernoteConfig.callbacks.onPaste=function(evt){$scope.paste({evt:evt})}),angular.isDefined($attrs.onKeyup)&&(summernoteConfig.callbacks.onKeyup=function(evt){$scope.keyup({evt:evt})}),angular.isDefined($attrs.onKeydown)&&(summernoteConfig.callbacks.onKeydown=function(evt){$scope.keydown({evt:evt})}),angular.isDefined($attrs.onImageUpload)&&(summernoteConfig.callbacks.onImageUpload=function(files){$scope.imageUpload({files:files,editable:$scope.editable})}),angular.isDefined($attrs.onMediaDelete)&&(summernoteConfig.callbacks.onMediaDelete=function(target){var removedMedia={attrs:{}};removedMedia.tagName=target[0].tagName,angular.forEach(target[0].attributes,function(attr){removedMedia.attrs[attr.name]=attr.value}),$scope.mediaDelete({target:removedMedia})}),this.activate=function(scope,element,ngModel){var updateNgModel=function(){var newValue=element.summernote("code");element.summernote("isEmpty")&&(newValue=""),ngModel&&ngModel.$viewValue!==newValue&&$timeout(function(){ngModel.$setViewValue(newValue)},0)},originalOnChange=summernoteConfig.callbacks.onChange;summernoteConfig.callbacks.onChange=function(contents){$timeout(function(){element.summernote("isEmpty")&&(contents=""),updateNgModel()},0),angular.isDefined($attrs.onChange)?$scope.change({contents:contents,editable:$scope.editable}):angular.isFunction(originalOnChange)&&originalOnChange.apply(this,arguments)},angular.isDefined($attrs.onBlur)&&(summernoteConfig.callbacks.onBlur=function(evt){!summernoteConfig.airMode&&element.blur(),$scope.blur({evt:evt})}),element.summernote(summernoteConfig);var unwatchNgModel,editor$=element.next(".note-editor");editor$.find(".note-toolbar").click(function(){updateNgModel(),editor$.hasClass("codeview")?(editor$.on("keyup",updateNgModel),ngModel&&(unwatchNgModel=scope.$watch(function(){return ngModel.$modelValue},function(newValue){editor$.find(".note-codable").val(newValue)}))):(editor$.off("keyup",updateNgModel),angular.isFunction(unwatchNgModel)&&unwatchNgModel())}),ngModel&&(ngModel.$render=function(){ngModel.$viewValue?element.summernote("code",ngModel.$viewValue):element.summernote("empty")}),angular.isDefined($attrs.editable)&&($scope.editable=editor$.find(".note-editable")),angular.isDefined($attrs.editor)&&($scope.editor=element),currentElement=element,element.on("$destroy",function(){element.summernote("destroy"),$scope.summernoteDestroyed=!0})},$scope.$on("$destroy",function(){$scope.summernoteDestroyed||currentElement.summernote("destroy")})}]).directive("summernote",[function(){"use strict";return{restrict:"EA",transclude:"element",replace:!0,require:["summernote","?ngModel"],controller:"SummernoteController",scope:{summernoteConfig:"=config",editable:"=",editor:"=",init:"&onInit",enter:"&onEnter",focus:"&onFocus",blur:"&onBlur",paste:"&onPaste",keyup:"&onKeyup",keydown:"&onKeydown",change:"&onChange",imageUpload:"&onImageUpload",mediaDelete:"&onMediaDelete"},template:'<div class="summernote"></div>',link:function(scope,element,attrs,ctrls,transclude){var summernoteController=ctrls[0],ngModel=ctrls[1];if(ngModel)var clearWatch=scope.$watch(function(){return ngModel.$viewValue},function(value){clearWatch(),element.append(value),summernoteController.activate(scope,element,ngModel)},!0);else transclude(scope,function(clone,scope){element.append(clone.html())}),summernoteController.activate(scope,element,ngModel)}}}]);