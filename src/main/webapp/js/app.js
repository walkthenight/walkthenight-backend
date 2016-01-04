$(document).foundation();

$('.iphone-mock-1-div').load('img/iphone-mockup-1.svg');
$('.iphone-mock-2-div').load('img/iphone-mockup-2.svg', function() {
	var s = Snap(".iphone-mock-2-div > svg");

	var card1= s.select('#card-1');
	card1.animate({transform:"r30 150 450"},500);
});

