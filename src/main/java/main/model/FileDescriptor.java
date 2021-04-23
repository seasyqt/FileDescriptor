package main.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
public class FileDescriptor {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String fileName;

  private String ext;

  private Long size;


  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime createdTs;


  public FileDescriptor() {
  }

  public FileDescriptor(String fullFileName, Long size) {
    this.fileName = beforePoint(fullFileName);
    this.ext = afterPoint(fullFileName);
    this.size = size;
    this.createdTs = LocalDateTime.now();
  }


  public String getFullNameFile() {
    StringBuilder builder = new StringBuilder(this.getFileName()).append(".")
        .append(this.getExt());
    return builder.toString();
  }

  public static String afterPoint(String fullNameFile) {
    StringBuilder stringBuilder = new StringBuilder(fullNameFile);
    return stringBuilder.toString()
        .substring(fullNameFile.lastIndexOf(".") + 1, fullNameFile.length());
  }

  public static String beforePoint(String fullNameFile) {
    StringBuilder stringBuilder = new StringBuilder(fullNameFile);
    return stringBuilder.toString().substring(0, fullNameFile.indexOf("."));
  }


}
