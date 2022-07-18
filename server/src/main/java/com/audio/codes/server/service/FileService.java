package com.audio.codes.server.service;

import com.audio.codes.server.repository.FileRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class.getName());
    private final FileRepository fileRepository;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    private final String FILE_TYPE;
    private final Set<String> selectedFileCache;

    @Autowired
    public FileService(FileRepository fileRepository, @Value("${type.file}") String file_type) {
        this.fileRepository = fileRepository;
        FILE_TYPE = file_type;
        selectedFileCache = new HashSet<>();
    }

    public List<com.audio.codes.server.model.File> saveAllMPThreeFiles(String path) {
        logger.info("Save all files in DB.");
        try {
            List<com.audio.codes.server.model.File> listOfFiles = fetchMP3Items(path);
            deleteAllFiles();
            writeLock.lock();
            try {
                return fileRepository.saveAll(listOfFiles);
            } finally {
                logger.info("All MP3 files saved successfully.");
                writeLock.unlock();
            }
        } catch (Exception exception) {
            logger.error("Can't save to DB!!!\n" + exception.getCause());
            return null;
        }
    }

    public void deleteAllFiles() {
        logger.info("Delete all files in DB.");
        writeLock.lock();
        try {
            fileRepository.deleteAll();
        } finally {
            logger.info("All MP3 files deleted successfully.");
            writeLock.unlock();
        }
    }

    private List<com.audio.codes.server.model.File> fetchMP3Items(String path) throws Exception {
        logger.info("Fetch only MP3 files from " + path);
        List<com.audio.codes.server.model.File> fileList = new ArrayList<>();
        final DecimalFormat df = new DecimalFormat("0.00");
        int id = 1;
        File folder = new File(path);
        File[] allFiles = folder.listFiles();
        for (int i = 0; i < Objects.requireNonNull(allFiles).length; i++) {
            File fileFromFolder = allFiles[i];
            if (fileFromFolder.getName().endsWith(FILE_TYPE)) {
                com.audio.codes.server.model.File file = new com.audio.codes.server.model.File(id++, fileFromFolder.getName());
                file.setSize(df.format(file.getFileSize(fileFromFolder)));
                String length = file.getFileLength(fileFromFolder);
                if (length != null) {
                    file.setLength(length);
                    fileList.add(file);
                } else {
                    throw new Exception("Can't calculate length of file: " + file.getFileName() + " !!!");
                }
            }
        }

        return fileList;
    }

    public boolean selectItem(String name) {
        AtomicBoolean isOk = new AtomicBoolean(true);
        isItemSelected(name);
        selectedFileCache.forEach(item -> {
            Optional<com.audio.codes.server.model.File> file = findByFileName(item);
            if (file.isPresent()) {
                if (file.get().getValid() == null) {
                    file.get().setValid(true);
                    saveFile(file.get());
                }
            } else {
                isOk.set(false);
                logger.info("Can't find song name :" + name);
            }
        });

        return isOk.get();
    }

    public void saveFile(com.audio.codes.server.model.File file) {
        logger.info("Save file name " + file.getFileName() + "to DB");
        writeLock.lock();
        try {
            fileRepository.save(file);
        } finally {
            logger.info("File name " + file.getFileName() + " saved to DB successfully");
            writeLock.unlock();
        }
    }

    public Optional<com.audio.codes.server.model.File> findByFileName(String str) {
        logger.info("Find file name " + str + "in DB");
        Optional<com.audio.codes.server.model.File> file;
        readLock.lock();
        try {
            file = fileRepository.findByFileName(str);
        } finally {
            logger.info("File name " + str + "found in DB.");
            readLock.unlock();
        }
        return file;
    }

    private void isItemSelected(String name) {
        Gson gson = new Gson();
        List<String> newItems = gson.fromJson(name, List.class);
        selectedFileCache.addAll(newItems);
        List<String> itemToRelease = new ArrayList<>();
        selectedFileCache.forEach(item -> {
            if (!newItems.contains(item)) {
                itemToRelease.add(item);
                Optional<com.audio.codes.server.model.File> file = findByFileName(item);
                if (file.isPresent()) {
                    if (file.get().getValid()) {
                        file.get().setValid(null);
                        saveFile(file.get());
                    }
                } else {
                    logger.error("Can't find song name :" + name);
                }
            }
        });

        itemToRelease.forEach(selectedFileCache::remove);
    }
}
