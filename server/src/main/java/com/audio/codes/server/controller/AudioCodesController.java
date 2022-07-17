package com.audio.codes.server.controller;

import com.audio.codes.server.model.File;
import com.audio.codes.server.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */

@RestController
@RequestMapping("audio/codes")
public class AudioCodesController {

    private static final Logger logger = LoggerFactory.getLogger(AudioCodesController.class.getName());
    private final FileService fileService;

    @Autowired
    public AudioCodesController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<List<File>> saveAllFiles(@RequestBody String path) {
        try {
            logger.info("Save all MP3 files from path: " + path + " to DB.");
            return ResponseEntity.ok(fileService.saveAllMPThreeFiles(path));
        } catch (Exception exception) {
            logger.error("Can't save files form " + path + " to DB!!! " + exception.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(value = "select", method = RequestMethod.POST)
    public ResponseEntity<Void> selectItem(Principal principal, @RequestBody String name) {
        try {
            logger.info("song: " + name + " was selected");
            if (fileService.selectItem(name)) {
                return ResponseEntity.ok().build();
            } else {
                throw new Exception("The file: " + name + " does not exist!!!");
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.noContent().build();
        }
    }
}
