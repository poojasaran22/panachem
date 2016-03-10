var panacheCont = angular.module("panache.controllers", ['ui.router']);

panacheCont.controller("mainController", ["$rootScope","$scope","httpService","$uibModal", "$log","$state",
	"$stateParams","dataService", function($rootScope,$scope,httpService,$uibModal, $log,$state,$stateParams,dataService) {
    console.log("Main Controller Module");
    httpService.doGet('model/list').then(function(data){
        console.log('inside den - '+data)
    });

	$rootScope.status2= sessionStorage.status;

	$scope.status= sessionStorage.status;
	$scope.showlogout= false;
	if(sessionStorage.status=='loggedin') {
		$scope.showlogout =true;
	}
	console.log($stateParams);

console.log("sessionStorage.status  "+$scope.status)
$scope.open = function(){
		var modalInstance = $uibModal.open({
			templateUrl: 'templates/modal.html',
			controller: 'ModalInstanceCtrl',
			backdrop: false,
			size: 'lg',
			resolve: {
				user: function () {
					return $scope.user;
				}
			}
		});

		modalInstance.result.then(function (user) {
			$scope.invalidLogin = false;
			if (user != undefined && user.type == "signin") {
				var JSONObj = new Object();
				JSONObj.username = user.username;
				JSONObj.password = user.password;
				httpService.doPost('model/login', JSONObj).then(function (data) {

					sessionStorage.status = 'loggedin';
					sessionStorage.user = data;
					if(data[0]['type']=='TALENT'){
						dataService.setId(data[0]['id']);
						$state.go('profile', {}, { reload: true });

					}else{
						$state.go('search', {}, { reload: true });
					}
					console.log(data);
				}, function (data) {

					$state.go('home', {}, {reload: true});

				});
			} else if (user != undefined && user.type == "signup") {

			}
		}, function () {
			$log.info('Modal dismissed at: ' + new Date());
		});
};

	$scope.action = function(){
console.log("inside action")
		if(sessionStorage.status=='loggedin'){
			sessionStorage.status = undefined;
			console.log("inside sessionStorage.status=='loggedin'")
		}else{
			console.log("inside $scope.open();")
			$scope.open();
		}

	}
	if($scope.status!='loggedin'){
		$scope.open();
	}

}]).controller("ModalInstanceCtrl", ["$scope","$uibModalInstance","user","$state", function($scope,$uibModalInstance,user,$state) {
	console.log(user+"HGGDs");
	$scope.user={};
	$scope.signin = function () {
		console.log($scope.user+"signin");
		$scope.user.type="signin";
	    $uibModalInstance.close($scope.user);
	  };

	  $scope.signup = function () {
		  $scope.user.type="signup";
		  console.log($scope.user+"signup");
		  $uibModalInstance.close($scope.user);
		  $state.go('signup', {}, { reload: true });
	  };

}]).controller("profileController", ["$scope","httpService","dataService", function($scope,httpService,dataService) {
	console.log(" 0 0 00 "+dataService.getId());
	httpService.doGet('model/profile/'+dataService.getId()).then(function(data){
		$scope.profile = data
		console.log('inside profile then - '+data)
	});

	console.log("profileController Controller Module");
    
}]).controller("contactController", ["$scope","httpService", function($scope,httpService) {
	console.log("Contact Controller Module");

}]).controller("logoutController", ["$scope","$state", function($scope,$state) {
	console.log("logoutController Controller Module");
	sessionStorage.status = undefined;
	$state.go('home', {}, { reload: true });

}]).controller("signupController", ["$scope","httpService","$state","dataService", function($scope,httpService,$state,dataService) {
   $scope.talentType=true;


   $scope.changeTalent = function (value) {
	   $scope.talentType=value;
	  };
	  
	  $scope.submit = function(){
		  var signupdata= {
				    username:$scope.talent.username,
				    password: $scope.talent.password,
				    talentType:  $scope.talentType?'TALENT':'',
				    name: $scope.talent.name,
				    bust: $scope.talent.bust,
				    country:$scope.talent.country,
				    experience: $scope.talent.experience,
				    extra_tag: $scope.talent.extra_tag,
				    eyes: $scope.talent.eyes,
				    field: "",
				    hair: $scope.talent.hair,
				    height: $scope.talent.height,
				    shoes: $scope.talent.shoes,
				    type: $scope.talent.type,
				    waist: $scope.talent.waist,
				    lookslike: $scope.talent.lookslike,
				    edu_q_map_list : [
				        {
				             line1: $scope.talent.educationdetails,
				             line2: $scope.talent.educationyear,
				             description: $scope.talent.educationdesc 
				    	 }, {
				             line1: $scope.talent.educationdetails2,
				             line2: $scope.talent.educationyear2,
				             description: $scope.talent.educationdesc2
				    	 }
				    ]

				}
		  
		  httpService.doPost('model/signup',signupdata).then(function(data){
        		console.log(data);
				if(!$scope.talentType){
					$state.go('search', {}, { reload: true });
				}else{
					dataService.setId(data)
					$state.go('profile', {}, { reload: true });
				}
            });
	  }
	

}]).controller("searchController", ["$scope","httpService","$state","dataService", function($scope,httpService,$state,dataService) {
    console.log("Search Controller Module");
    $scope.showPrev = false;
    $scope.type = 'Model';
    $scope.talentList =[];
    $scope.looksLikeList=[];
   
    var JSONObj = new Object();
    httpService.doGet('model/dropdown/looksLike').then(function(data){
    	$scope.looksLikeList = data;
    });

    $scope.searchDatabase = function(){
    	$scope.showPrev = false;
    	JSONObj.type=$scope.type;
    	JSONObj.country=$scope.country;
    	JSONObj.betweenHeight=$scope.betweenHeight;
    	JSONObj.eyes=$scope.eyes;
    	JSONObj.hair=$scope.hair;
    	JSONObj.experience=$scope.experience;
    	if($scope.selectedOption!=undefined || !($scope.selectedOption==null)){
    	JSONObj.looksLike=$scope.selectedOption.looksLike;
    	}
    	else{
    		$scope.selectedOption = undefined;
    		JSONObj.looksLike=undefined;
    	}
    	console.log($scope.selectedOption);
    	httpService.doPost('model/search',JSONObj).then(function(data){
    		$scope.showPrev = true;
    		$scope.talentList = data;
        });
    }

	$scope.showProfile= function(modelid){
		dataService.setId(modelid);
		$state.go('profile', {}, { reload: true });
	}
    
}]).service('httpService', function($http, $q){
    return {
        doGet : function(url){
            var deferred = $q.defer();
            $http.get(url)
                .success(function (data) {
                    deferred.resolve(data);
                })
                .error(function () {
                    deferred.reject();
                });

            return deferred.promise;
        },
        doPost : function(url, reqPayload){
            var deferred = $q.defer();
            $http.post(url, reqPayload)
                .success(function (data) {
                    deferred.resolve(data);
                })
                .error(function (data) {
					console.log('data - deferred.reject '+data)
                    deferred.reject(data);
                });

            return deferred.promise;
        }
    }
}).service('dataService', function(){
	var id;

	var setId = function(val){
		id = val;
	};

	var getId = function(){
		return id;
	};
	return {
		setId : setId,
		getId :getId
	}
});
