/*
 * Author: Angel Suarez
 * Date: 31 August 2015
 * Description:
 *      
 **/
"use strict";

$(function () {

	var base_url = "http://localhost:8888/rest/";

    /////////////////////////////////
    // Manage filter inquiries button
    /////////////////////////////////
    $("button.my-inquiries").click(function(){
		$("div.my-inquiries").show();
		$("div.other-inquiries").hide();
		$(this).addClass( "active" );
		$("button.other-inquiries").removeClass( "active" );
		$("button.all-inquiries").removeClass( "active" );
	});
	$("button.other-inquiries").click(function(){
		$("div.other-inquiries").show();
		$("div.my-inquiries").hide();
		$(this).addClass( "active" );
		$("button.all-inquiries").removeClass( "active" );
		$("button.my-inquiries").removeClass( "active" );
	});
	$("button.all-inquiries").click(function(){
		$("div.other-inquiries").show();
		$("div.my-inquiries").show();
		$(this).addClass( "active" );
		$("button.other-inquiries").removeClass( "active" );
		$("button.my-inquiries").removeClass( "active" );
	});

	jQuery.ajax({
		type: "GET",
		url: base_url+"myGames/gameAccess",
		headers:{ "Authorization" : "GoogleLogin auth=32ea7e66b9e1904b2786f531e415dda", "Accept" : "application/json" },
		contentType: "application/json",
		dataType: "json",
		success: function (data, status, jqXHR) {

			$.each(data.gamesAccess, function(i, item) {

				jQuery.ajax({
					type: "GET",
					url: base_url + "myGames/gameId/" +item.gameId,
					headers: {
						"Authorization": "GoogleLogin auth=32ea7e66b9e1904b2786f531e415dda",
						"Accept": "application/json"
					},
					contentType: "application/json",
					dataType: "json",
					success: function (data, status, jqXHR) {

						var markup =
							"<div class='info-box bg-yellow my-inquiries'>" +
							"<span class='info-box-icon'>" +
							"<i class='fa fa-comments-o'></i></span>" +
							"<div class='info-box-content'>" +
							"<span class='info-box-text'><a href='inquiry.html'>${title}</a></span>" +
							"<span class='info-box-number'>-</span>" +
							"<div class='progress'>" +
							"<div class='progress-bar' style='width: 70%'></div>" +
							"</div>" +
							"<span class='progress-description'>70% Increase in 30 Days</span>" +
							"</div>" +
							"</div>";

						// Compile the markup as a named template
						$.template( "movieTemplate", markup );

						// Render the template with the movies data and insert
						// the rendered HTML under the "movieList" element
						$.tmpl( "movieTemplate", data ).appendTo( "#inquiries > div > div.box-body" );
					},
					error: {
					}
				});
			});
		},

		error: function (jqXHR, status) {
			// error handler
		}
	});



});


