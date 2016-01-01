var wtnTicketingApp = angular.module('wtnTicketingApp');

wtnTicketingApp.factory('promoterRepository', ['$q', '$timeout', '$http', function($q, $timeout, $http) {
	return {
        getPromoter : function(accessToken) {
        	var d = $q.defer();
            $http.get('/api/promoters/me?access_token='+accessToken).success(function(promoter){
                d.resolve(promoter);
            });
            return d.promise;
        }
    }
}]);

wtnTicketingApp.factory('eventRepository', ['$q', '$timeout', '$http', function($q, $timeout, $http) {
	return {
        getEvents : function(pageId) {
        	var d = $q.defer();
            $http.get('/api/series/'+pageId+'/events?timeframe=future&cache=nocache').success(function(events){
                d.resolve(events);
            });
            return d.promise;
        }, 
	
		getEvent : function(eventId) {
	    	var d = $q.defer();
	        $http.get('/api/events/'+eventId+'?cache=nocache').success(function(event){
	            d.resolve(event);
	        });
	        return d.promise;
	    }
    }
}]);

wtnTicketingApp.factory('ticketedEventRepository', ['$q', '$timeout', '$http', function($q, $timeout, $http) {
	return {
        createTicketedEvent : function(ticketedEventSpec, accessToken) {
        	var d = $q.defer();
            $http.post('/api/ticketed-events?access_token='+accessToken, ticketedEventSpec).success(function(){
                d.resolve(true);
            });
            return d.promise;
        },
	
		getTicket : function(ticketId, accessToken) {
	    	var d = $q.defer();
	        $http.get('/api/tickets/'+ticketId+'?access_token='+accessToken).success(function(ticket){
	            d.resolve(ticket);
	        });
	        return d.promise;
	    }
    }
}]);

/*

wtnTicketingApp.factory('ticketRepository', ['$q', '$timeout', '$http', function($q, $timeout, $http) {

	function augmentEvent(event, accessToken) {
		$http.get('/api/ticketed-events/'+event.id+'?access_token='+accessToken).success(function(e){
            event.location= e.location;
            event.dateTime= e.dateTime;
            event.endDateTime= e.endDateTime;
            event.logoUrl= e.logoUrl;
        });
	}
	
    return {

        getTicketedEvents : function(accessToken) {
        	var d = $q.defer();
            $http.get('/api/ticketed-events?access_token='+accessToken).success(function(events){
                d.resolve(events);
                angular.forEach(events, function(event) {
            	  augmentEvent(event, accessToken);
            	});
            
            });
            return d.promise;
        }
    }

}]);*/



