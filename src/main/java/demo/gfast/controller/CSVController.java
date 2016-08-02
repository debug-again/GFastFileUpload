package demo.gfast.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/csv")
public class CSVController {

    private final String uploadDirectoryLocation = "C:\\Gfast\\UploadCSV\\";

    private void log(String message) {
        System.out.println(message);
    }

    private ResponseEntity<Map<String, String>> prepareResponse(String desc, HttpStatus status){
        Map<String, String> res = new HashMap<String, String>();
        res.put("description", desc);
        return new ResponseEntity<Map<String, String>>(res, status);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> uploadFileHandler(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, HttpServletRequest request) {

        log("Inside CSVController.uploadFileHandler");
        log("name = [" + name + "]");
        log(request.getSession().getServletContext().getRealPath("/"));

        File uploadDir = new File(uploadDirectoryLocation);
        File desFile = new File(uploadDirectoryLocation + file.getOriginalFilename());

        if(file.getSize() == 0){
            return prepareResponse("File Size should be more than 0KB !!", HttpStatus.LENGTH_REQUIRED);
        }

        try {
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }else if(desFile.exists()){
                desFile = new File(uploadDirectoryLocation + file.getOriginalFilename() + new Date().getTime());
            }
            file.transferTo(desFile);
        } catch (IOException e) {
            log("Unable to transfer object at " + desFile.getAbsolutePath());
            log("Exception received : " + e.getMessage());
            e.printStackTrace();
            return prepareResponse("Unable to transfer file to server !!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return prepareResponse("Unable to transfer file to server !!",HttpStatus.CREATED);
    }
}
