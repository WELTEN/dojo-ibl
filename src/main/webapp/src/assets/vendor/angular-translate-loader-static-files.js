/*!
 * angular-translate - v2.11.0 - 2016-03-20
 *
 * Copyright (c) 2016 The angular-translate team, Pascal Precht; Licensed MIT
 */
!function(a,b){"function"==typeof define&&define.amd?
// AMD. Register as an anonymous module unless amdModuleId is set
    define([],function(){return b()}):"object"==typeof exports?
// Node. Does not work with strict CommonJS, but
// only CommonJS-like environments that support module.exports,
// like Node.
    module.exports=b():b()}(this,function(){function a(a,b){"use strict";return function(c){if(!(c&&(angular.isArray(c.files)||angular.isString(c.prefix)&&angular.isString(c.suffix))))throw new Error("Couldn't load static files, no files and prefix or suffix specified!");c.files||(c.files=[{prefix:c.prefix,suffix:c.suffix}]);for(var d=function(d){if(!d||!angular.isString(d.prefix)||!angular.isString(d.suffix))throw new Error("Couldn't load static file, no prefix or suffix specified!");return b(angular.extend({url:[d.prefix,c.key,d.suffix].join(""),method:"GET",params:""},c.$http)).then(function(a){return a.data},function(){return a.reject(c.key)})},e=[],f=c.files.length,g=0;f>g;g++)e.push(d({prefix:c.files[g].prefix,key:c.key,suffix:c.files[g].suffix}));return a.all(e).then(function(a){for(var b=a.length,c={},d=0;b>d;d++)for(var e in a[d])c[e]=a[d][e];return c})}}return a.$inject=["$q","$http"],angular.module("pascalprecht.translate").factory("$translateStaticFilesLoader",a),a.displayName="$translateStaticFilesLoader","pascalprecht.translate"});