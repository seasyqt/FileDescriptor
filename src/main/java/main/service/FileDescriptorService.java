package main.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import main.model.FileDescriptor;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public class FileDescriptorService {

  private final static String SAVED_PATH = "files/";

  /**
   * Сохранение файла в разделе /files
   * @throws IOException
   */
  public static void createdOrUpdatedFile(MultipartFile file, int countSameFiles)
      throws IOException {
    StringBuilder pathName;
    if (countSameFiles != 0) {
      String nameFile = file.getOriginalFilename();
      String beforePoint = FileDescriptor.beforePoint(nameFile);
      String afterPoint = FileDescriptor.afterPoint(nameFile);
      pathName = new StringBuilder(SAVED_PATH).append(beforePoint).append("(")
          .append(countSameFiles).append(")")
          .append(".").append(afterPoint);
    } else {
      pathName = new StringBuilder(SAVED_PATH).append(file.getOriginalFilename());
    }
    byte[] fileBytes = file.getBytes();
    BufferedOutputStream stream =
        new BufferedOutputStream(
            new FileOutputStream(new File(pathName.toString())));
    stream.write(fileBytes);
    stream.close();
  }

  /**
   * Информация по файлу из DB в JSON формате
   * @return JSONObject
   */
  public static JSONObject createJSONInfoFileDescriptor(FileDescriptor fileDescriptor) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", fileDescriptor.getId());
    jsonObject.put("fileName", fileDescriptor.getFileName());
    jsonObject.put("ext", fileDescriptor.getExt());
    jsonObject.put("size", fileDescriptor.getSize());
    jsonObject.put("createdTime", fileDescriptor.getCreatedTs());
    return jsonObject;
  }

  /**
   * Формирование названия файла с учетом уникальности файлов
   */
  public static String uniqueNameFile(String fullNameNewFile, int countSameFiles) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(FileDescriptor.beforePoint(fullNameNewFile));
    if (countSameFiles != 0) {
      stringBuilder.append("(").append(countSameFiles).append(")");
    }
    stringBuilder.append(".").append(FileDescriptor.afterPoint(fullNameNewFile));
    return stringBuilder.toString();
  }

}
