var panacheApp = angular.module("panacheApp", ["ui.router", "panache.controllers",'ui.bootstrap']);


panacheApp.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state("home", {
            url: "/home",
            controller : "mainController",
            templateUrl: "templates/main-content.html"
        }) .state("about", {
            url: "/about",
            templateUrl: "templates/about.html"
        }) .state("search", {
            url: "/search",
            controller : "searchController",
            templateUrl: "templates/search.html"
        }).state("contact", {
            url: "/contact",
            controller : "contactController",
            templateUrl: "templates/contact.html"
        }).state("signup", {
            url: "/signup",
            controller : "signupController",
            templateUrl: "templates/signup.html"
        }).state("profile", {
            url: "/profile",
            controller : "profileController",
            templateUrl: "templates/single-model-ng.html"
        })
        .state("logout", {
            url: "/logout",
            controller : "logoutController",
            templateUrl: "templates/search.html"
        })

        $urlRouterProvider.otherwise("/home");

}])