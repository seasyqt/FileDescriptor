package main.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

  @Entity
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

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
    }

    public String getExt() {
      return ext;
    }

    public void setExt(String ext) {
      this.ext = ext;
    }

    public Long getSize() {
      return size;
    }

    public void setSize(Long size) {
      this.size = size;
    }

    public LocalDateTime getCreatedTs() {
      return createdTs;
    }

    public void setCreatedTs(LocalDateTime createdTs) {
      this.createdTs = createdTs;
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
