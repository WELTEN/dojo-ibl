// INSPINIA Landing Page Custom scripts
$(document).ready(function () {

    // Highlight the top nav as scrolling
    $('body').scrollspy({
        target: '.navbar-fixed-top',
        offset: 80
    })

    // Page scrolling feature
    $('a.page-scroll').bind('click', function(event) {
        var link = $(this);
        $('html, body').stop().animate({
            scrollTop: $(link.attr('href')).offset().top - 70
        }, 500);
        event.preventDefault();
    });

    $('a.login-dojoibl').bind('click', function(event) {
        var link = $(this);

        event.preventDefault();

        console.log("click login");

        if($.cookie("arlearn.AccessToken")){
            var accessToken = $.cookie("arlearn.AccessToken");
            if(accessToken == "" || accessToken == "null"){
                if(window.location.hostname.toLowerCase().indexOf("localhost") >= 0){
                    window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://localhost:8888/oauth/wespot&response_type=code&scope=profile+email";
                }else{
                    window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://dojo-ibl.appspot.com/oauth/wespot&response_type=code&scope=profile+email";
                }
            }
        }
    });
});

// Activate WOW.js plugin for animation on scrol
new WOW().init();