
function loadMentorships()
{
	var filter = getParameterByName("id");
	console.log(filter);
	$.ajax({
		type: 'POST',
		url: 'Data', 
		data: 
		{
			'action': 'getActiveMentorships',
			'filter': filter,
		},
		complete: function(data)
		{
			if(data.responseText == '') {returnToLogin(); return;} 

			if(data.responseJSON.length > 0)
				fillField(data.responseJSON);
			else
				$("#mentorshipList").append("<li id='contentless'><p class='title'>No Mentorships to Display!</p></li>");
			fillFocusDropdown();
		},
	});
}

function postMentorship()
{
	//var firstName = $("#firstName").val();
	//var lastName = $("#lastName").val();
	var mentorshipTitle = $("#mentorshipTitle").val();
	var location = $("#location").val();
	var company = $('#company').val();
	var description = $("#description").val();
	var contact = $("#contact").val();
	var interests = $("#interests").val();
	var company = $("#company").val();
	var focus = $("#focus").val();

	
	// Data Verification
	if(mentorshipTitle != "" && location != "" &&
			description != "" && contact != ""  && company != "")
		$.ajax({
			type: "POST",
			url: 'Data',
			data:
			{
				'action': 'postMentorship',
				
				//'firstName': firstName,
				//'lastName': lastName,
				'mentorshipTitle': mentorshipTitle,
				'location': location,
				'description': description,
				'contact': contact,
				'interests': interests,
				'company': company,
				'focus': focus,
			},
			complete: function(data)
			{	
				if(data.responseJSON == "username retrivel failed")
					alert("Post Failed!");
				else
					alert("Post Submitted!");
				
				$("#formDiv").addClass("hidden");
				$("#successDiv").removeClass("hidden");
			},
		});
	else
	{
		alert("Invalid Data");
		return;
	}	
}

function fillField(json)
{
	console.log(json);
	for(var i = 0; i < json.length; i++)
	{
		if(json[i].active)
		{
			var node = document.createElement("li");
			node.onclick = function(){expand(this)};
	
			$(node).append("<p class='title'>" + json[i].mentorshipTitle + "</p>");
			$(node).append("<p class='name hidden'>" + json[i].description + "</p>");
			$(node).append("<p class='name hidden'>Poster: " + json[i].owner.firstName + " " + 
										json[i].owner.lastName + " - " + json[i].contact + "</p>");
			$(node).append("<p class='name hidden'>Location: " + json[i].location + " at " +  json[i].company + "</p>");
			
			//$(node).append("<span style='float:right;margin-top: -100px'>Hello</span>");
			
			$("#mentorshipList").append(node);
		}
	}
}

function subscribe(mentorshipID)
{
	$.ajax({
		type: 'POST',
		url: 'Mentorships', 
		data: 
		{
			'action': 'subscribeToMentorship',
			'mentorshipID': mentorshipID
		},
		complete: function(data)
		{

		},
	});	
}


