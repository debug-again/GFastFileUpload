(function(){

    var app = angular.module('app', []);

    /* Directive */
    app.directive('fileModel', ['$parse', function($parse){
        return{
            restrict : 'A',
            link : function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                element.bind('change', function(){

                    if(element[0].files[0].size == 0){
                        scope.uploadSuccess = false;
                        scope.failureReason = "File Cannot be uploaded. Size should be more than 0KB";
                        $('#uploadButton').prop("disabled", true);
                    }else{
                        $('#uploadButton').prop("disabled", false);
                        scope.uploadSuccess = undefined;
                    }
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
            $scope.failureReason = "";
            $scope.uploadingInProgress = true;

            var file = $scope.csvFile;
            var name = $scope.name;
            var url = "/rest/csv/uploadFile";

            var fd = new FormData();
            fd.append('file', file);
            fd.append('name', name);

            formService.uploadFormDataToUrl(fd, url)
                .success(function(){
                    $scope.uploadSuccess = true;
                    $scope.uploadingInProgress = false;
                })
                .error(function(result){
                    $scope.uploadSuccess = false;
                    $scope.uploadingInProgress = false;
                    if(result && result.description) {
                        $scope.failureReason = "File Not Uploaded. Reason : " + result.description;
                    }
                }
            );
        }
    }]);
})();