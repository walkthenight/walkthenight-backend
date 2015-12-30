var wtnTicketingApp = angular.module('wtnTicketingApp');

wtnTicketingApp.controller('TicketAdminCtrl', ['$scope', '$facebook', 
	'promoterRepository', 
	'eventRepository',
	'usSpinnerService',
function($scope, $facebook, promoterRepository, eventRepository, usSpinnerService) {

	function refresh() {
		usSpinnerService.spin('spinner-1');
		
		$facebook.api("/me?fields=email,first_name,name").then(function(response) {
			$scope.loggedInName = response.first_name;
			$scope.isLoggedIn = true;
			var authResponse = $facebook.getAuthResponse();
			$scope.accessToken = authResponse.accessToken;
			console.log($scope.accessToken);
			
			promoterRepository.getPromoter($scope.accessToken).then(function(promoter) {
				
				eventRepository.getEvents(promoter.pageId).then(function(events) {
					$scope.events= events;
					usSpinnerService.stop('spinner-1');
				}, function(error) {
					usSpinnerService.stop('spinner-1');
					//:FIXME	
				});
			}, function(error) {
				usSpinnerService.stop('spinner-1');
			});
			
			$scope.maybeLoggedIn= false;
			
		}, function(err) {
			
			$scope.maybeLoggedIn= false;
			//:TODO
		});
	}

	$scope.maybeLoggedIn= true;
	$scope.isLoggedIn = false;
	$scope.login = function() {
		$facebook.login().then(function() {
			refresh();
		});
	};

	

	//ticketRepository.getTicketedEvents($scope.accessToken).then(function(result) {
	//	$scope.ticketedEvents = result;
	//}, function(error) {
	//	console.error(error);
	//	//:FIXME
	//});

	refresh();

}]);

