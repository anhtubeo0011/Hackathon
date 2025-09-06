package com.knowledgehub.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DocumentReaderService {

  /**
   * Đọc nội dung từ file dựa trên đường dẫn
   */
  public String readDocumentFromPath(String filePath) throws IOException {
    // Kiểm tra file có tồn tại không
    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      throw new IllegalArgumentException("File không tồn tại: " + filePath);
    }

    // Xác định loại file dựa trên extension
    String fileExtension = getFileExtension(filePath).toLowerCase();

    switch (fileExtension) {
      case "pdf":
        return readPdfFromPath(filePath);

      case "doc":
        return readDocFromPath(filePath);

      case "docx":
        return readDocxFromPath(filePath);

      default:
        throw new IllegalArgumentException("Loại file không được hỗ trợ: " + fileExtension);
    }
  }

  /**
   * Đọc nội dung file PDF từ đường dẫn
   */
  private String readPdfFromPath(String filePath) throws IOException {
    File file = new File(filePath);
    try (PDDocument document = PDDocument.load(file)) {
      PDFTextStripper stripper = new PDFTextStripper();
      return stripper.getText(document);
    }
  }

  /**
   * Đọc nội dung file Word cũ (.doc) từ đường dẫn
   */
  private String readDocFromPath(String filePath) throws IOException {
    try (FileInputStream fis = new FileInputStream(filePath);
         HWPFDocument document = new HWPFDocument(fis);
         WordExtractor extractor = new WordExtractor(document)) {

      return extractor.getText();
    }
  }

  /**
   * Đọc nội dung file Word mới (.docx) từ đường dẫn
   */
  private String readDocxFromPath(String filePath) throws IOException {
    try (FileInputStream fis = new FileInputStream(filePath);
         XWPFDocument document = new XWPFDocument(fis);
         XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

      return extractor.getText();
    }
  }

  /**
   * Lấy extension của file
   */
  private String getFileExtension(String filePath) {
    int lastDotIndex = filePath.lastIndexOf('.');
    if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) {
      return "";
    }
    return filePath.substring(lastDotIndex + 1);
  }

  /**
   * Kiểm tra xem file có được hỗ trợ hay không
   */
  public boolean isSupportedFile(String filePath) {
    String extension = getFileExtension(filePath).toLowerCase();
    return extension.equals("pdf") || extension.equals("doc") || extension.equals("docx");
  }

  /**
   * Lấy thông tin về file
   */
  public FileInfo getFileInfo(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      throw new IllegalArgumentException("File không tồn tại: " + filePath);
    }

    File file = new File(filePath);
    String extension = getFileExtension(filePath);

    return new FileInfo(
        file.getName(),
        extension,
        file.length(),
        Files.getLastModifiedTime(path).toMillis(),
        isSupportedFile(filePath)
    );
  }

  /**
   * Đọc nội dung và trả về thông tin đầy đủ
   */
  public DocumentContent readDocumentWithInfo(String filePath) throws IOException {
    FileInfo fileInfo = getFileInfo(filePath);

    if (!fileInfo.isSupported()) {
      throw new IllegalArgumentException("Loại file không được hỗ trợ: " + fileInfo.getExtension());
    }

    String content = readDocumentFromPath(filePath);

    return new DocumentContent(fileInfo, content);
  }

  /**
   * Class chứa thông tin file
   */
  public static class FileInfo {
    private String fileName;
    private String extension;
    private long fileSize;
    private long lastModified;
    private boolean supported;

    public FileInfo(String fileName, String extension, long fileSize, long lastModified, boolean supported) {
      this.fileName = fileName;
      this.extension = extension;
      this.fileSize = fileSize;
      this.lastModified = lastModified;
      this.supported = supported;
    }

    // Getters
    public String getFileName() { return fileName; }
    public String getExtension() { return extension; }
    public long getFileSize() { return fileSize; }
    public long getLastModified() { return lastModified; }
    public boolean isSupported() { return supported; }

    public String getFileType() {
      switch (extension.toLowerCase()) {
        case "pdf": return "PDF";
        case "doc": return "Word 97-2003 (.doc)";
        case "docx": return "Word (.docx)";
        default: return "Không hỗ trợ";
      }
    }
  }

  /**
   * Class chứa nội dung document và thông tin file
   */
  public static class DocumentContent {
    private FileInfo fileInfo;
    private String content;
    private int contentLength;

    public DocumentContent(FileInfo fileInfo, String content) {
      this.fileInfo = fileInfo;
      this.content = content;
      this.contentLength = content != null ? content.length() : 0;
    }

    // Getters
    public FileInfo getFileInfo() { return fileInfo; }
    public String getContent() { return content; }
    public int getContentLength() { return contentLength; }
  }
}