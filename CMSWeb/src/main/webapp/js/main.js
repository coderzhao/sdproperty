/* eslint no-alert: 0 */

'use strict';


var app = angular.module('CamViewModule', [
  'ngRoute',
  'ngCookies',
  'ngAnimate', 
  'ngSanitize', 
  'ui.bootstrap',
  'camApi'
]);

//index.html
app.controller('CameraCtrl', ['$rootScope','$scope','$location','api',function($rootScope, $scope,$location ,api) {
	$scope.cameras =[];
	$scope.onlineMac = [];
	$scope.viewers = [];
	var host = $location.host();
	var port = $location.port();
	
	var url = "ws://" +host+":" + port + "/sdproperty/liveView.do";
	var isTest = false;
	$scope.getCameras = function(){
		api.getAllCameras().then(function(resp){
			$scope.cameras = resp.data;
			if(isTest) $scope.cameras=[{"name":"cam1","mac":"dd"},{"name":"cam2","mac":"dd"},{"name":"cam3","mac":"dd"},{"name":"cam4","mac":"dd"}]
		});
	}
	
	$scope.isCameraOnline = function(camera){
		
		for(var i = 0 ; i < $scope.onlineMac.length ; i++){
			var mac = $scope.onlineMac[i];
			if(mac == camera.address) return true;
		}
		return false;
	}
	
	$scope.isViewing = function(camera){
		for(var i = 0 ; i < $scope.viewers.length ;i++){
			var viewer = $scope.viewers[i];
			if(viewer != undefined && camera.address === viewer.mac) {
				return true;
			}
		}
		return false;
	}
	
	$scope.getOnlineCamerasMac = function(){
		api.getOnlineCamerasMac().then(function(resp){
			$scope.onlineMac = resp.data;
		});
	}
	
	$scope.refresh = function(){
		$scope.getCameras();
		$scope.getOnlineCamerasMac();
	}
	
	
	
	$scope.startView = function(camera){
		var viewer = new CameraViewer(url ,camera.address, document.getElementById(camera.name));
		if($scope.isCameraOnline(camera)){
			$scope.viewers.push(viewer);
			viewer.start();
		}else{
			alert('Camera is offline');
		}
	}
	
	$scope.stopView = function(camera){
		var idx = -1;
		for(var i = 0 ; i < $scope.viewers.length ;i++){
			var viewer = $scope.viewers[i];
			if(viewer != undefined && camera.address === viewer.mac) {
				viewer.stop();
				delete $scope.viewers[i];
				break;
			}
		}
		
	}

	$scope.getCameras();
	$scope.getOnlineCamerasMac();
	setTimeout($scope.getOnlineCamerasMac,10000);
}]);



