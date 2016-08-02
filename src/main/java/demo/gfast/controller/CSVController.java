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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/csv")
public class CSVController {

    private final String uploadDirectory = "C:\\Gfast\\UploadCSV\\";

    private void log(String message) {
        System.out.println(message);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> uploadFileHandler(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, HttpServletRequest request) {

        log("Inside CSVController.uploadFileHandler");
        log("name = [" + name + "]");
        log(request.getSession().getServletContext().getRealPath("/"));
        Map<String, String> res = new HashMap<String, String>();
        File desFile = new File(uploadDirectory + file.getOriginalFilename());

        try {
            file.transferTo(desFile);
            res.put("status", "success");
        } catch (IOException e) {
            log("Unable to transger object at " + desFile.getAbsolutePath());
            log("Exception received : " + e.getMessage());
            e.printStackTrace();
            res.put("status", "failure");
            return new ResponseEntity<Map<String, String>>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, String>>(res, HttpStatus.CREATED);
    }
}
