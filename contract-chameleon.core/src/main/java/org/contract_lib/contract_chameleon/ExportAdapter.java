
package org.contract_lib.contract_chameleon;

import java.util.List;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.contract_lib.contract_chameleon.arguments.OutputPath;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.ChameleonMessageType;
import org.contract_lib.contract_chameleon.error.SimpleErrorMessage;

public abstract class ExportAdapter extends Adapter {

  public abstract List<TranslationResult> perform(
      List<Path> sourceFiles,
      ChameleonMessageManager messageManager) throws IOException;

  public abstract String adapterTitle();

  public abstract String defaultOutputDir();

  @Override
  public final void perform(
      ChameleonMessageManager messageManager,
      AdapterArgumentProvider adapterProvider,
      String[] args) {

    String adapterTitle = String.format("===== %s =====", adapterTitle());
    String frame = "=".repeat(adapterTitle.length());

    //TODO: Print as info message
    System.err.println(frame);
    System.err.println(adapterTitle);
    System.err.println(frame);

    //TODO: move to proper parameter
    if (args.length <= 1) {
      System.err.println("Expected path to files in command.");
      return;
    }
    String inputFileName = args[1];
    if (args.length > 2) {
      System.err.println("Only the first input file is handled at the moment.");
    }

    try {
      List<TranslationResult> results = this.perform(List.of(Paths.get(inputFileName)), messageManager);

      // Create adapter directory
      String outputDir = adapterProvider.getAdapterArgument(OutputPath.class).map(OutputPath::getPath)
          .orElse(defaultOutputDir());

      String fileDir = getDirForFile(inputFileName);
      Path directoryPath = Paths.get(".", outputDir, fileDir);

      if (Files.isDirectory(directoryPath)) {
        messageManager.report(new SimpleErrorMessage(
            String.format("Directory at %s does already exist.", directoryPath),
            ChameleonMessageType.INFO,
            null));
      } else {
        Files.createDirectories(directoryPath);
      }

      //Write results
      for (TranslationResult res : results) {
        String directoryName = res.directoryName();
        String fileName = res.fileName();
        String fileEnding = res.fileEnding();
        Path packagePath = Paths.get(".", outputDir, fileDir, directoryName);
        Path classPath = Paths.get(".", outputDir, fileDir, directoryName, fileName + fileEnding);

        if (Files.isDirectory(packagePath)) {
          messageManager.report(new SimpleErrorMessage(
              String.format("Directory at %s does already exist.", packagePath),
              ChameleonMessageType.INFO,
              null));
        } else {
          Files.createDirectories(packagePath);
        }

        if (Files.exists(classPath)) {
          messageManager.report(new SimpleErrorMessage(
              String.format("WARNING: File at %s does already exist, will be overridden!", classPath),
              ChameleonMessageType.INFO,
              null));
        }

        //TODO: Use better syntax, ensure closed stream
        BufferedWriter writer = Files.newBufferedWriter(classPath);
        res.write(writer);
        writer.close();
      }

    } catch (IOException e) {
      messageManager.report(new SimpleErrorMessage(e.toString()));
    }
  }

  //TODO: find better solution
  private String getDirForFile(String file) {
    return file.replaceAll("(\\.|/)", "_");
  }

  public interface TranslationResult {
    String directoryName();

    String fileName();

    String fileEnding();

    void write(Writer writer) throws IOException;
  }
}
