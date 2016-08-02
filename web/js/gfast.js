(function(){

    var app = angular.module('app', []);

    /* Directive */
    app.directive('fileModel', ['$parse', function($parse){
        return{
            restrict : 'A',
            link : function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                element.bind('change', function(){
                    scope.$apply(function(){
                        model.assign(scope, element[0].files[0]);
                    });
                });
            }
        };
    }]);

    /* Service */
    app.service('formService', ['$http', function($http){
        this.uploadFormDataToUrl = function(fd, url){
            return $http.post(url, fd, {
                transformRequest : angular.identity,
                headers : {'Content-type' : undefined}
            });
        };
    }]);

    /* Controller */
    app.controller('csvController',['$scope', 'formService', function($scope, formService){
        $scope.submitCSVData = function(){
            $scope.uploadSuccess = undefined;
            var file = $scope.csvFile;
            var name = $scope.name;
            console.log('File is ');
            console.dir(file);
            var url = "/rest/csv/uploadFile";
            var fd = new FormData();
            fd.append('file', file);
            fd.append('name', name);
            formService.uploadFormDataToUrl(fd, url).success(function(){
                $scope.uploadSuccess = true;
            }).error(function(){
                $scope.uploadSuccess = false;
            });

        }
    }]);
})();