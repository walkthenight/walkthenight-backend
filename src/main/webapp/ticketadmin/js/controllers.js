var wtnTicketingApp = angular.module('wtnTicketingApp');

wtnTicketingApp.controller('TicketAdminCtrl', ['$scope', '$facebook', 
	'promoterRepository', 
	'eventRepository',
	'ticketedEventRepository',
	'usSpinnerService',
function($scope, $facebook, promoterRepository, eventRepository, ticketedEventRepository, usSpinnerService) {

	function refresh() {
		usSpinnerService.spin('spinner-1');
		
		$facebook.api("/me?fields=email,first_name,name").then(function(response) {
			$scope.loggedInName = response.first_name;
			$scope.isLoggedIn = true;
			var authResponse = $facebook.getAuthResponse();
			$scope.accessToken = authResponse.accessToken;
			loadPromoter();
			
			$scope.maybeLoggedIn= false;
			
		}, function(err) {
			
			$scope.maybeLoggedIn= false;
			//:TODO
		});
	}
	
	function loadPromoter() {
		promoterRepository.getPromoter($scope.accessToken).then(function(promoter) {
				
			$scope.hasPageToAdminister= true;
			$scope.pageName= promoter.pageName;
			
			downloadEvents(promoter);
			
		}, function(error) {
			usSpinnerService.stop('spinner-1');
			//:FIXME
		});
	}
	
	function downloadEvents(promoter) {
		eventRepository.getEvents(promoter.pageId).then(function(events) {
			$scope.events= events;
			usSpinnerService.stop('spinner-1');
			downloadTickets(events);
		}, function(error) {
			usSpinnerService.stop('spinner-1');
			//:FIXME	
		});
	}
	
	function downloadTickets(events) {
		angular.forEach(events, function(event) {
			if (event.tickeraPrimaryTicketId) {
        		augmentEventWithTicketDetails(event);
        	}
    	});
	}
	
	function augmentEventWithTicketDetails(event) {
		ticketedEventRepository.getTicket(event.tickeraPrimaryTicketId, $scope.accessToken).then(function(ticket) {
			event.ticketsSold= ticket.ticketsSold;
			event.ticketPrice= ticket.pricePerTicket;
			event.ticketsAvailable= ticket.quantityAvailable;
			
			console.log(event);
		});
	};
	
	function updateEvent(eventId) {
		eventRepository.getEvent(eventId).then(function(updatedEvent) {
			angular.forEach($scope.events, function(event) {
				if (event.id == updatedEvent.id) {
					event.tickeraPrimaryTicketId= updatedEvent.tickeraPrimaryTicketId;
					event.wtnManagedEventUrlName= updatedEvent.wtnManagedEventUrlName;
					event.ticketsSold= 0;
					event.ticketPrice= $scope.ticketedEventSpec.price;
					event.ticketsAvailable= $scope.ticketedEventSpec.quantity;
				}
			});
		});
	};
	
	$scope.maybeLoggedIn= true;
	$scope.isLoggedIn = false;
	$scope.hasPageToAdminister= false;
	$scope.login = function() {
		$facebook.login().then(function() {
			refresh();
		});
	};
	
	$scope.addingTicket = function (eventId, eventName) {
		$scope.ticketedEventName= eventName;
		$scope.ticketedEventSpec= {'facebookEventId':eventId};
	};
	
	$scope.addTicket = function() {
		$scope.isAddingTicket= true;
		ticketedEventRepository.createTicketedEvent($scope.ticketedEventSpec).then(function() {
			$scope.isAddingTicket= false;
			$scope.dismiss();
			updateEvent($scope.ticketedEventSpec.facebookEventId);
		}, function(error) {
			$scope.isAddingTicket= false;
			$scope.dismiss();
			alert("Couldn't create event and ticket. Please try again.");
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

