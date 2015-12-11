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

        if(typeof $.cookie("arlearn.AccessToken") == 'undefined' || $.cookie("arlearn.AccessToken") == "" || $.cookie("arlearn.AccessToken") == "null"){
            if(window.location.hostname.toLowerCase().indexOf("localhost") >= 0){
                window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://localhost:8888/oauth/wespot&response_type=code&scope=profile+email";
            }else{
                window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://dojo-ibl.appspot.com/oauth/wespot&response_type=code&scope=profile+email";
            }
        }else {
            window.location = "main.html";
        }
    });

    var youTubeOptions = {
        // The list object property (or data attribute) with the YouTube video id:
        youTubeVideoIdProperty: 'youtube',
        // Optional object with parameters passed to the YouTube video player:
        // https://developers.google.com/youtube/player_parameters
        youTubePlayerVars: undefined,
        // Require a click on the native YouTube player for the initial playback:
        youTubeClickToPlay: false
    };

    blueimp.Gallery([
        {
            title: 'Master Class about IBL',
            type: 'text/html',
            youtube: '4PeQm6vaD0k',
            poster: 'https://www.ou.nl/images/Opener/CW/stamboomonderzoek/OU_Logo.JPG'
        },
        {
            title: 'Master Class about IBL',
            type: 'text/html',
            youtube: 'eQiajgwJrKU',
            poster: 'https://www.ou.nl/images/Opener/CW/stamboomonderzoek/OU_Logo.JPG'
        },
        {
            title: 'Google Glass for IBL',
            type: 'text/html',
            youtube: '4v7uf5khkC0',
            poster: 'https://www.ou.nl/images/Opener/CW/stamboomonderzoek/OU_Logo.JPG'
        },
        {
            title: 'IBL',
            type: 'text/html',
            youtube: 'u84ZsS6niPc',
            poster: 'https://www.ou.nl/images/Opener/CW/stamboomonderzoek/OU_Logo.JPG'
        },
        {
            title: 'Benefits of IBL',
            type: 'text/html',
            youtube: '2ylmVT5lkck',
            poster: 'https://www.ou.nl/images/Opener/CW/stamboomonderzoek/OU_Logo.JPG'
        },

        //{
        //    title: 'Create new inquiry',
        //    type: 'text/html',
        //    youtube: '6R8yxdih5t8'
        //},
        //{
        //    title: 'Create new inquiry',
        //    type: 'text/html',
        //    youtube: '6R8yxdih5t8'
        //}
        //{
        //    title: 'Big Buck Bunny',
        //    href: 'https://upload.wikimedia.org/wikipedia/commons/7/75/' +
        //    'Big_Buck_Bunny_Trailer_400p.ogg',
        //    type: 'video/ogg',
        //    poster: 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/' +
        //    'Big.Buck.Bunny.-.Opening.Screen.png/' +
        //    '800px-Big.Buck.Bunny.-.Opening.Screen.png'
        //},
        //{
        //    title: 'Elephants Dream',
        //    href: 'https://upload.wikimedia.org/wikipedia/commons/transcoded/8/83/' +
        //    'Elephants_Dream_%28high_quality%29.ogv/' +
        //    'Elephants_Dream_%28high_quality%29.ogv.360p.webm',
        //    type: 'video/webm',
        //    poster: 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/' +
        //    'Elephants_Dream_s1_proog.jpg/800px-Elephants_Dream_s1_proog.jpg'
        //},
        //{
        //    title: 'LES TWINS - An Industry Ahead',
        //    type: 'text/html',
        //    youtube: 'zi4CIXpx7Bg'
        //},
        //{
        //    title: 'KN1GHT - Last Moon',
        //    type: 'text/html',
        //    vimeo: '73686146',
        //    poster: 'https://secure-a.vimeocdn.com/ts/448/835/448835699_960.jpg'
        //}
    ], {
        container: '#blueimp-video-carousel',
        carousel: true
    });

    $(".convert-date-timeago").each(function(){
        $(this).html(jQuery.timeago(new Date($(this).html()).toISOString()));
    });
});

// Activate WOW.js plugin for animation on scrol
new WOW().init();