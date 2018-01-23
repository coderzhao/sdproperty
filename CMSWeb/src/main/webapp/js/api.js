/*global angular*/
/*jslint todo:true,  browser: true */

var app = angular.module('camApi',[]);

app.factory('api', ['$http',  '$q', '$window', function ($http, $q, $window) {
        'use strict';

        var urlBase = '/sdproperty';
        	
        var api = {};

        // ==============================================================================

        api.getAllCameras = function(){
        	
        	return $http.get(urlBase +"/PreApiIpc/getAllIpcByUser.do");
        		$http.get(urlBase +"/getAllIpcByUser.do");
        }
        
        api.getOnlineCamerasMac = function(){
        		return $http.get(urlBase + "/quadrant/getOnlineCamera.do");
        }
        
//        api.listInsuranceProduct = function(){
//        	return $http.get(urlBase +"/ean/product/list");
//        }
//        
//        api.createInsuranceOrder = function(data){
//        	return $http({
//                method: 'POST',
//                url: urlBase + '/ean/order/create',
//                cache: false,
//                data: data
//            });
//        };
//        
//        api.listInsuranceOrderHistory = function(){
//        	return $http.get(urlBase + '/ean/history/succeed');
//        }
//        
//        api.getProvince = function(){
//        	return $http.get(context + '/content/js/json-province.js');
//        }
//        
//        api.getCity = function(){
//        	return $http.get(context + '/content/js/json-city.js');
//        }
//        
//        api.getDistrict = function(){
//        	return $http.get(context + '/content/js/json-district.js');
//        }

        return api;
    }]);
