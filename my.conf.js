// Karma configuration
// Generated on Fri Oct 14 2016 12:24:48 GMT+0200 (CEST)

exports.config = {
  framework: 'jasmine',
  seleniumAddress: 'http://localhost:4444/wd/hub',
  specs: [
    'src/main/webapp/src/test/test.js'
  ],
  onPrepare: function() {
    browser.driver.get('https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://localhost:8080/oauth/wespot&response_type=code&scope=profile+email');

    browser.driver.findElement(by.id('username')).sendKeys('weTitogelo');
    browser.driver.findElement(by.id('password')).sendKeys('gelillo');
    browser.driver.findElement(by.name('Login')).click();

    // Login takes some time, so wait until it's done.
    // For the test app's login, we know it's done when it redirects to
    // index.html.
    return browser.driver.wait(function() {
      return browser.driver.getCurrentUrl().then(function(url) {
        return /home/.test(url);
      });
    }, 10000);
  }

};

//
//module.exports = function(config) {
//  config.set({
//
//    // base path that will be used to resolve all patterns (eg. files, exclude)
//    basePath: '',
//
//
//    // frameworks to use
//    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
//    frameworks: ['jasmine'],
//
//
//    // list of files / patterns to load in the browser
//    files: [
//      'src/main/webapp/dist/js/**/*.js',
//      'src/main/webapp/dist/js/test/**/*.js',
//      'src/main/webapp/dist/js/test/**/*.js',
//      'src/main/webapp/dist/js/test/**/*.js'
//    ],
//
//
//    // list of files to exclude
//    exclude: [
//    ],
//
//
//    // preprocess matching files before serving them to the browser
//    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
//    preprocessors: {
//    },
//
//
//    // test results reporter to use
//    // possible values: 'dots', 'progress'
//    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
//    reporters: ['progress'],
//
//
//    // web server port
//    port: 9876,
//
//
//    // enable / disable colors in the output (reporters and logs)
//    colors: true,
//
//
//    // level of logging
//    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
//    logLevel: config.LOG_INFO,
//
//
//    // enable / disable watching file and executing tests whenever any file changes
//    autoWatch: true,
//
//
//    // start these browsers
//    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
//    browsers: ['Chrome', 'Firefox', 'Safari'],
//
//
//    // Continuous Integration mode
//    // if true, Karma captures browsers, runs the tests and exits
//    singleRun: false,
//
//    // Concurrency level
//    // how many browser should be started simultaneous
//    concurrency: Infinity
//  })
//}
