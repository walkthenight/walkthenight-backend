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
            $http.get('/api/series/'+pageId+'/events?timeframe=future').success(function(events){
                d.resolve(events);
                
                
                
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



