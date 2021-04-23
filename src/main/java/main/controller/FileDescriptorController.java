package main.controller;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import main.model.*;
import main.service.FileDescriptorService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileDescriptorController {

  private static final String SAVED_PATH = "files/";

  @Autowired
  private FileDescriptorRepository descriptorRepository;

  /**
   * Запрос POST , Сохраняем новый файл
   */
  @PostMapping("/files")
  public ResponseEntity saveFile(@RequestParam("file") MultipartFile file) {

    if (!file.isEmpty()) {
      try {
        int countSameFiles = countSameFile(file.getOriginalFilename());
        FileDescriptorService.createdOrUpdatedFile(file, countSameFiles);
        String nameFile = FileDescriptorService
            .uniqueNameFile(file.getOriginalFilename(), countSameFiles);
        FileDescriptor newFile = new FileDescriptor(nameFile, file.getSize());
        descriptorRepository.save(newFile);

        return ResponseEntity.ok(HttpStatus.OK);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity(e.getStackTrace(), HttpStatus.BAD_REQUEST);
      }

    }
    return new ResponseEntity(HttpStatus.NOT_FOUND);
  }

  /**
   * Запрос GET, Получаем список файлов
   */
  @GetMapping("/files")
  public ResponseEntity getFiles() {
    List<FileDescriptor> listFiles = new ArrayList<>();
    descriptorRepository
        .findAll()
        .forEach(listFiles::add);
    return new ResponseEntity(listFiles, HttpStatus.OK);
  }

  /**
   * Запрос GET , Получаем описание файла в формате JSON
   */
  @GetMapping(value = "/files/{id}/descriptor")
  public ResponseEntity descriptionFile(@PathVariable int id) {
    FileDescriptor fileDescriptor = descriptorRepository.findById(id).get();
    JSONObject jsonObject = FileDescriptorService.createJSONInfoFileDescriptor(fileDescriptor);
    return new ResponseEntity(jsonObject, HttpStatus.OK);
  }

  /**
   * Запрос PUT, Изменение файла
   */
  @PutMapping("/files/{id}")
  public ResponseEntity editFile(@PathVariable int id,
      @RequestParam("file") MultipartFile file) {
    Optional<FileDescriptor> fileDescriptor = descriptorRepository.findById(id);
    boolean isFindFile = fileDescriptor.isPresent();
    FileDescriptor updateFile = null;
    if (isFindFile) {
      try {
        int countSameFile = countSameFile(file.getOriginalFilename());
        FileDescriptorService.createdOrUpdatedFile(file, countSameFile);
        String nameFile = FileDescriptorService
            .uniqueNameFile(file.getOriginalFilename(), countSameFile);
        updateFile = new FileDescriptor(nameFile, file.getSize());
        updateFile.setId(id);
      } catch (Exception e) {
        e.getStackTrace();
      }
    }

    return isFindFile
        ? new ResponseEntity(updateFile, HttpStatus.OK)
        : new ResponseEntity(HttpStatus.NOT_FOUND);
  }

  /**
   * Запрос GET, Скачивание файла
   */
  @GetMapping("/files/{id}")
  public ResponseEntity<FileSystemResource> downloadFile(@PathVariable int id) {

    Optional<FileDescriptor> file = descriptorRepository.findById(id);
    boolean isFile = file.isPresent();
    File getFile = null;
    HttpHeaders httpHeaders = new HttpHeaders();
    if (isFile) {
      FileDescriptor fileDescriptor = file.get();
      String fullNameFile = SAVED_PATH + fileDescriptor.getFullNameFile();
      getFile = new File(fullNameFile);
      httpHeaders.setContentType(MediaType.valueOf("application/force-download"));
      httpHeaders.setContentLength(getFile.length());
      httpHeaders.setContentDispositionFormData("attachment", fileDescriptor.getFullNameFile());
    }

    return isFile ? new ResponseEntity<>(new FileSystemResource(getFile), httpHeaders,
        HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  /**
   * Запрос DELETE, Удаление файла
   */
  @DeleteMapping("/files/{id}")
  public ResponseEntity deleteFile(@PathVariable int id) {
    Optional<FileDescriptor> optional = descriptorRepository.findById(id);
    boolean isFindFile = optional.isPresent();

    if (isFindFile) {
      File file = new File(SAVED_PATH + optional.get().getFullNameFile());
      file.delete();
      descriptorRepository.deleteById(id);
    }
    return isFindFile ?
        new ResponseEntity(HttpStatus.OK) :
        new ResponseEntity(HttpStatus.NOT_FOUND);
  }

  private int countSameFile(String nameNewFile) {
    AtomicInteger count = new AtomicInteger();
    descriptorRepository.findAll().forEach(f -> {
      if (f.getFileName().contains(FileDescriptor.beforePoint(nameNewFile))
          && f.getExt().contains(FileDescriptor.afterPoint(nameNewFile))) {
        count.getAndIncrement();
      }
    });
    return count.get();
  }


}
