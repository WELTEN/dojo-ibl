var gulp = require('gulp');
var concat = require('gulp-concat');
var ngAnnotate = require('gulp-ng-annotate');
var plumber = require('gulp-plumber');

var uglify = require('gulp-uglify');
var bytediff = require('gulp-bytediff');
var rename = require('gulp-rename');

//'src/main/webapp/src/app.js',
//'src/main/webapp/src/routes.js',
//'src/main/webapp/src/components/home/*.js',
//'src/main/webapp/src/components/account/*.js',
//'src/main/webapp/src/components/activity/*.js',
//'src/main/webapp/src/components/common/*.controller.js',
//'src/main/webapp/src/components/common/*.directive.js',
//'src/main/webapp/src/components/landing/*.js',
//'src/main/webapp/src/components/message/*.js',
//'src/main/webapp/src/components/oauth/*.js',
//'src/main/webapp/src/components/response/*.js',
//'src/main/webapp/src/components/run/*.js',
//'src/main/webapp/src/components/user/*.js',
//'src/main/webapp/src/components/channel/*.js'

// Minify JS
gulp.task('app', function() {
    return gulp.src([
        'src/main/webapp/src/app.js',
        'src/main/webapp/src/routes.js',
        'src/main/webapp/src/components/*/*.directive.js',
        'src/main/webapp/src/components/*/*.service.js',
        'src/main/webapp/src/components/*/*.resources.js',
        'src/main/webapp/src/components/*/*.controller.js'
    ])
        .pipe(plumber())
        .pipe(concat('app.js', {newLine: ';'}))
        .pipe(ngAnnotate({add: true}))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

// Minify JS
gulp.task('directives', function() {
    return gulp.src([
        'src/main/webapp/src/components/*/*.directive.js'
    ])
        .pipe(plumber())
        .pipe(concat('directives.js', {newLine: ';'}))
        .pipe(ngAnnotate({add: true}))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

gulp.task('services', function() {
    return gulp.src([
        'src/main/webapp/src/components/*/*.service.js'
    ])
        .pipe(plumber())
        .pipe(concat('services.js', {newLine: ';'}))
        .pipe(ngAnnotate({add: true}))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

gulp.task('resources', function() {
    return gulp.src([
        'src/main/webapp/src/components/*/*.resource.js'
    ])
        .pipe(plumber())
        .pipe(concat('resources.js', {newLine: ';'}))
        .pipe(ngAnnotate({add: true}))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

gulp.task('controllers', function() {
    return gulp.src([
        'src/main/webapp/src/components/*/*.controller.js'
    ])
        .pipe(plumber())
        .pipe(concat('controllers.js', {newLine: ';'}))
        .pipe(ngAnnotate({add: true}))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

gulp.task('vendor', function() {
    return gulp.src([
        'src/main/webapp/src/assets/vendor/jquery.min.js',
        'src/main/webapp/src/assets/vendor/jquery-ui.min.js',
        'src/main/webapp/src/assets/vendor/bootstrap.js',
        'src/main/webapp/src/assets/vendor/summernote.min.js',
        'src/main/webapp/src/assets/vendor/angular.js',
        'src/main/webapp/src/assets/vendor/ui-bootstrap-tpls-0.12.0.min.js',
        'src/main/webapp/src/assets/vendor/angular-route.js',
        'src/main/webapp/src/assets/vendor/angular-ui-router.js',
        'src/main/webapp/src/assets/vendor/angular-animate/angular-animate.min.js',
        'src/main/webapp/src/assets/vendor/angular-resource.js',
        'src/main/webapp/src/assets/vendor/angular-cookies.js',
        'src/main/webapp/src/assets/vendor/angular-translate.js',
        'src/main/webapp/src/assets/vendor/angular-cache.js',
        'src/main/webapp/src/assets/vendor/angular-summernote.js',
        'src/main/webapp/src/assets/vendor/angular-sanitize.js',
        'src/main/webapp/src/assets/vendor/angular-chosen.min.js',
        'src/main/webapp/src/assets/vendor/angular-dragdrop.min.js',
        'src/main/webapp/src/assets/vendor/angular-translate-loader-static-files.js',
        'src/main/webapp/src/assets/vendor/scrollglue.js',
        'src/main/webapp/src/assets/vendor/ng-emoticons.min.js',
        'src/main/webapp/src/assets/vendor/sortable.js',
        'src/main/webapp/src/assets/vendor/angular.audio.js',
        'src/main/webapp/src/assets/vendor/ui-codemirror.min.js',
        'src/main/webapp/src/assets/vendor/codemirror.js',
        'src/main/webapp/src/assets/vendor/javascript.js',
        'src/main/webapp/src/assets/vendor/ngletteravatar.js',
        'src/main/webapp/src/assets/vendor/sweetalert/sweetalert.min.js',
        'src/main/webapp/src/assets/vendor/toastr/toaster.min.js',
        'src/main/webapp/src/assets/vendor/textAngular-sanitize.min.js',
        'src/main/webapp/src/assets/vendor/textAngular-rangy.min.js',
        'src/main/webapp/src/assets/vendor/textAngularSetup.js',
        'src/main/webapp/src/assets/vendor/textAngular.min.js',
        'src/main/webapp/src/assets/vendor/ng-file-upload-shim.js',
        'src/main/webapp/src/assets/vendor/ng-file-upload.js',
        'src/main/webapp/src/assets/vendor/jquery.blueimp-gallery.min.js',
        'src/main/webapp/src/assets/vendor/angular-breadcrumb.min.js',
        'src/main/webapp/src/assets/vendor/angular-table.min.js',
        'src/main/webapp/src/assets/vendor/moment.min.js',
        'src/main/webapp/src/assets/vendor/v-button.min.js',
        'src/main/webapp/src/assets/vendor/select2/select.js',
        'src/main/webapp/src/assets/vendor/inspinia.js',
        'src/main/webapp/src/assets/vendor/pace.min.js',
        'src/main/webapp/src/assets/vendor/metisMenu/jquery.metisMenu.js',
        'src/main/webapp/src/assets/vendor/ng-infinite-scroll.min.js',
        'src/main/webapp/src/assets/vendor/chosen.jquery.js',
        'src/main/webapp/src/assets/vendor/slimscroll/jquery.slimscroll.min.js',
        'src/main/webapp/src/assets/vendor/angulartics/angulartics.min.js',
        'src/main/webapp/src/assets/vendor/angulartics/angulartics-ga-cordova.min.js',
        'src/main/webapp/src/assets/vendor/footable/footable.all.min.js',
        'src/main/webapp/src/assets/vendor/footable/angular-footable.js',
        'src/main/webapp/src/assets/vendor/fullcalendar/fullcalendar.min.js',
        'src/main/webapp/src/assets/vendor/fullcalendar/gcal.js',
        'src/main/webapp/src/assets/vendor/fullcalendar/calendar.js',
        'src/main/webapp/src/assets/vendor/angular-datapicker/angular-datepicker.js',
    ])
        .pipe(plumber())
        .pipe(concat('vendor.js', {newLine: ';'}))
        .pipe(ngAnnotate({add: true}))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

gulp.task('prod', ['app', 'directives', 'services', 'resources', 'controllers'], function() {
    return gulp.src([
        'src/main/webapp/public/src/app.js',
        //'src/main/webapp/public/src/directives.js',
        //'src/main/webapp/public/src/services.js',
        //'src/main/webapp/public/src/resources.js',
        //'src/main/webapp/public/src/controllers.js'

    ])
        .pipe(plumber())
            .pipe(bytediff.start())
                .pipe(uglify({mangle: true}))
            .pipe(bytediff.stop())
            .pipe(rename('app.min.js'))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

gulp.task('prod-vendor', ['vendor'], function() {
    return gulp.src('src/main/webapp/public/src/vendor.js')
        .pipe(plumber())
            .pipe(bytediff.start())
                .pipe(uglify({mangle: true}))
            .pipe(bytediff.stop())
            .pipe(rename('vendor.min.js'))
        .pipe(plumber.stop())
        .pipe(gulp.dest('src/main/webapp/public/src/'));
});

gulp.task('watch', ['prod', 'prod-vendor'], function () {
    return gulp.watch([
        'src/main/webapp/src/app.js',
        'src/main/webapp/src/routes.js',
        'src/main/webapp/src/components/*/*.directive.js',
        'src/main/webapp/src/components/*/*.service.js',
        'src/main/webapp/src/components/*/*.resources.js',
        'src/main/webapp/src/components/*/*.controller.js'
    ], ['prod']);
});

gulp.task('default', [ 'watch', 'vendor', 'app', 'directives', 'services', 'resources', 'controllers']);